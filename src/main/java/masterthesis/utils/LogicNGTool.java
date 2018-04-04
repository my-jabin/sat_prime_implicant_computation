package masterthesis.utils;

import masterthesis.base.ClauseSet;
import org.logicng.datastructures.Tristate;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

public class LogicNGTool {

    private static final FormulaFactory factory = new FormulaFactory();
    private static final SATSolver solver = MiniSat.miniSat(factory);
    private static final String PREFIX = "v";

    public static Literal literal(int i){
        String var = PREFIX + Math.abs(i);
        return factory.literal(var, i > 0);
    }

    private static void initSolver(ClauseSet cs) {
        final ArrayList<Literal> literals = new ArrayList<>();
        cs.getClauses().forEach(clause -> {
            literals.clear();
            clause.getLiterals().forEach(literal -> {
                String var = PREFIX + Math.abs(literal.getValue());
                literals.add(literal.getPolarity() ? factory.literal(var, true) : factory.literal(var, false));
            });
            if (!literals.isEmpty()) {
                solver.add(factory.or(literals));
            }
        });
    }

    public static int[] model(ClauseSet cs) {
        int[] model = null;
        solver.reset();
        initSolver(cs);
        if (solver.sat() == Tristate.TRUE) {
            SortedSet<Literal> literalSortedSet = solver.model().literals();
            model = new int[literalSortedSet.size()];
            int index = 0;
            Iterator<Literal> iterator = literalSortedSet.iterator();
            while (iterator.hasNext()) {
                Literal l = iterator.next();
                int value = Integer.parseInt(l.name().substring(1, l.name().length()));
                boolean polarity = l.phase();
                model[index++] = polarity ? value : -value;
            }
        }
        return model;
    }

    public static boolean sat(ClauseSet cs, List<Literal> literals) {
        solver.reset();
        initSolver(cs);
        List<Literal> assumption = new ArrayList<>();
        for (Literal l : literals) {
            assumption.add(factory.literal(l.name(), l.phase()));
        }
        Tristate result = solver.sat(assumption);
        return result == Tristate.TRUE;
    }
}
