package masterthesis.base;

import org.logicng.datastructures.Tristate;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.util.ArrayList;

public class LogicNGModel extends Model {

    public LogicNGModel(ApplicationContext ac) {
        super(ac);
    }

    @Override
    public void generate() {
        final FormulaFactory factory = new FormulaFactory();
        final SATSolver solver = MiniSat.miniSat(factory);
        solver.reset();

        final ArrayList<Literal> literals = new ArrayList<>();
        ac.getCNFContent().stream().forEach(line -> {
            literals.clear();
            line.stream().forEach(l -> {
                String var = "v" + Math.abs(l);
                literals.add(l > 0 ? factory.literal(var, true) : factory.literal(var, false));
            });
            if (!literals.isEmpty()) {
                solver.add(factory.or(literals));
            }
        });
        if (solver.sat() == Tristate.TRUE) {
            solver.model().literals().forEach(l -> {
                String value = l.name().substring(1, l.name().length());
                boolean polarity = l.phase();
                this.addLiteral(ac.getLiteral(Integer.parseInt(value), polarity));
            });
        } else {
            System.err.println("Could not find a model");
        }
    }
}
