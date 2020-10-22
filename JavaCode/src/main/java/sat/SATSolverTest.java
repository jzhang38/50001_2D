package sat;
/*
import static org.junit.Assert.*;

import org.junit.Test;
*/
import java.util.ArrayList;
import sat.env.*;
import sat.formula.*;
import java.io.*;
import java.util.Scanner;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();



	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static void main(String[] args) throws Exception{
        File file = new File(args[0]);
        Scanner sc = new Scanner(file);
        while (sc.next().charAt(0) == 'c') sc.nextLine();
        sc.next();
        int n_v = sc.nextInt(); //number of variables
        int n_c = sc.nextInt();//number of clauses
        Formula f = new Formula();
        for (int i = 0; i < n_c; i++){
            int literalNumber = sc.nextInt();
            Clause c = new Clause();
            while (literalNumber != 0){
                if (literalNumber > 0) c = c.add(PosLiteral.make(Integer.toString(literalNumber)));
                else c = c.add(NegLiteral.make(Integer.toString(-literalNumber)));
                literalNumber = sc.nextInt();
            }
            f = f.addClause(c);
        }
        sc.close();
        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        Environment env = SATSolver.solve(f);
        long time = System.nanoTime();
        long timeTaken = time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");
        System.out.println(env == null ? "unsatisfiable" : "satisfiable");
        if (env != null) {
            File result = new File("BoolAssignment.txt");
            file.createNewFile();
            FileWriter writer = new FileWriter("BoolAssignment.txt");
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= n_v; i++){
                sb.append(i);
                sb.append(":");
                Bool b = env.get(new Variable(Integer.toString(i)));
                sb.append(b == Bool.FALSE ? "FALSE" : "TRUE");
                sb.append('\n');
            }
            writer.write(sb.toString());
            writer.close();
        }
    }


    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
    	assert
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable());
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));

    	assert Bool.FALSE == e.get(na.getVariable());

    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}