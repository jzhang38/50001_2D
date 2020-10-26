package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        return solve(formula.getClauses(), new Environment());
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        if (clauses.isEmpty()) return env;
        Clause smallest = clauses.first();
        for (Clause c : clauses) {
            if (c.isEmpty()) return null;
            if (c.size() < smallest.size()) smallest = c;
        }
        if (smallest.isUnit()) {
            Literal l = smallest.chooseLiteral();
            clauses = substitute(clauses, l);
            if (l instanceof PosLiteral) {
                env = env.putTrue(l.getVariable());
            } else {
                env = env.putFalse(l.getVariable());
            }
            return solve(clauses, env);
        } else {
            Literal l = smallest.chooseLiteral(); //chosen literal


            ImList<Clause> clausesCase1 = substitute(clauses, l);
            Environment envCase1 = l instanceof PosLiteral ? env.putTrue(l.getVariable()) : env.putFalse(l.getVariable());
            Environment case1Result = solve(clausesCase1, envCase1);
            if (case1Result != null) return case1Result;

            ImList<Clause> clausesCase2 = substitute(clauses, l.getNegation());
            Environment envCase2 = l instanceof PosLiteral ? env.putFalse(l.getVariable()) : env.putTrue(l.getVariable());
            return solve(clausesCase2, envCase2);
        }

    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        ImList<Clause> res = new EmptyImList<Clause>();
        for (Clause c: clauses){
            c = c.reduce(l);
            if (c != null)  res = res.add(c);
        }
        return res;
    }

}
