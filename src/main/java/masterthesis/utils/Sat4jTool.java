package masterthesis.utils;

import masterthesis.base.*;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.util.ArrayList;
import java.util.Arrays;

public class Sat4jTool {

    private static final ISolver solver = SolverFactory.newDefault();
    private static final int TIMEOUT = 3600; // 1 hour timeout
    private static final int MAXVAR = 1000000;

    static {
        solver.setTimeout(TIMEOUT);
        solver.newVar(MAXVAR);
    }

    public static int[] model(ClauseSet cs) {
        int[] model = null;
        solver.reset();
        try {
            for (Clause clause : cs.getClauses()) {
                int[] literals = new int[clause.size()];
                for (int i = 0; i < clause.size(); i++) {
                    literals[i] = clause.get(i).toInteger();
                }
                solver.addClause(new VecInt(literals));
            }
            IProblem problem = solver;
            if (problem.isSatisfiable()) {
                model = problem.model();
            }
        } catch (ContradictionException | TimeoutException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static Implicant primeImplicant(ClauseSet cs){
        Implicant implicant = null ;
        solver.reset();
        try {
            for (Clause clause : cs.getClauses()) {
                int[] literals = new int[clause.size()];
                for (int i = 0; i < clause.size(); i++) {
                    literals[i] = clause.get(i).toInteger();
                }
                solver.addClause(new VecInt(literals));
            }
            IProblem problem = solver;
            if (problem.isSatisfiable()) {
                implicant =  new Implicant(problem.primeImplicant());
                implicant.removeLiteral(0);
            }
        } catch (ContradictionException | TimeoutException e) {
            e.printStackTrace();
        }
        return implicant;
    }
}
