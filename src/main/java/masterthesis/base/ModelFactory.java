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
        return getModel(engine,null);
    }

    public static Model getModel(SolverEngine engine, ClauseSet cs) {
        switch (engine) {
            case EMPTY:
                return new CustomModel();
            case SAT4J:
                if(cs == null || cs.isEmpty())
                    throw new IllegalArgumentException("Clause set must not empty");
                return new Sat4jModel(cs);
            case LOGICNG:
                if(cs == null || cs.isEmpty())
                    throw new IllegalArgumentException("Clause set must not empty");
                return new LogicNGModel(cs);
            default:
                throw new IllegalArgumentException("Must specify model engine");
        }
    }

    public static List<Model> getAllModels(SolverEngine engine, ClauseSet cs){
        switch (engine) {
            case SAT4J:
                return getAllSat4jModels(cs);
            default:
                return null;
        }
    }

    private static List<Model> getAllSat4jModels(ClauseSet cs){
        int MAXVAR = 1000000;
        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        solver.newVar(MAXVAR);
        List<Model> models = new ArrayList<>();
        ISolver mi = new ModelIterator(solver);
        final ApplicationContext ac = ApplicationContext.getInstance();
        try {
            for (Clause clause : cs.getClauses()) {
                int[] literals = new int[clause.size()];
                for (int i = 0; i < clause.size(); i++) {
                    literals[i] = clause.get(i).getValue();
                }
                solver.addClause(new VecInt(literals));
            }

            Model model = null;
            while (mi.isSatisfiable()) {
                model = new Sat4jModel(cs);
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
