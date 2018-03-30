package masterthesis.base;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.util.ArrayList;
import java.util.Arrays;

public class Sat4jModel extends Model {

    private static final int MAXVAR = 1000000;

    public Sat4jModel() {
        super();
    }

    @Override
    public void generate() {
        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
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
                Arrays.stream(problem.model()).forEach(i-> this.addLiteral(ac.getLiteral(i)));
            }
        } catch (ContradictionException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
