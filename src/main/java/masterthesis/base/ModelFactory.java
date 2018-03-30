package masterthesis.base;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

import java.util.ArrayList;
import java.util.List;

public class ModelFactory {

    public static Model getModel(SolverEngine engine) {
        switch (engine) {
            case EMPTY:
                return new CustomModel();
            case SAT4J:
                return new Sat4jModel();
            case LOGICNG:
                return new LogicNGModel();
            default:
                throw new IllegalArgumentException("Must specify model engine");
        }
    }

    public static List<Model> getAllModels(SolverEngine engine){
        List<Model> models = new ArrayList<>();
        switch (engine) {
            case SAT4J:
                return getAllSat4jModels();
            default:
                return null;
        }
    }

    private static List<Model> getAllSat4jModels(){
        int MAXVAR = 1000000;
        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        solver.newVar(MAXVAR);
        List<Model> models = new ArrayList<>();
        ISolver mi = new ModelIterator(solver);
        final ApplicationContext ac = ApplicationContext.getInstance();
        try {
            for (ArrayList<Integer> line : ac.getCNFContent()) {
                int[] literals = new int[line.size()];
                for (int i = 0; i < line.size(); i++) {
                    literals[i] = line.get(i);
                }
                solver.addClause(new VecInt(literals));
            }

            Model model = null;
            while (mi.isSatisfiable()) {
                model = new Sat4jModel();
                for (int i : mi.model()) {
                    model.addLiteral(ac.getLiteral(i));
                }
                models.add(model);
            }
        } catch (ContradictionException | TimeoutException e) {
            e.printStackTrace();
        }
        return models;
    }
}
