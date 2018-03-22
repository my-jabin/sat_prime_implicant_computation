package masterthesis.base;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.util.ArrayList;
import java.util.Arrays;

public class ImplicantFactory {
    private static final int MAXVAR = 1000000;

    public static Implicant getPrimeImplicant(ApplicationContext ac){
        ISolver solver  = SolverFactory.newDefault();
        Implicant implicant = new Implicant(ac) ;
        solver.reset();
        solver.newVar(MAXVAR);
        try {
            for (ArrayList<Integer> line : ac.getCNFContent()) {
                int[] literals = new int[line.size()];
                for (int i = 0; i < line.size(); i++) {
                    literals[i] = line.get(i);
                }
                solver.addClause(new VecInt(literals));
            }
            IProblem problem = solver;
            if (problem.isSatisfiable()) {
                Arrays.stream(problem.primeImplicant()).forEach(i-> implicant.addLiteral(ac.getLiteral(i)));
                implicant.removeLiteral(ac.getLiteral(0));
            }
        } catch (ContradictionException | TimeoutException e) {
            e.printStackTrace();
        }
        return implicant;
    }
}
