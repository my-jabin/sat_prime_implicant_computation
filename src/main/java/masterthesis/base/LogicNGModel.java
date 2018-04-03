package masterthesis.base;

import masterthesis.utils.LogicNGTool;
import org.logicng.datastructures.Tristate;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.util.ArrayList;

public class LogicNGModel extends Model {

    public LogicNGModel(ClauseSet cs) {
        super(cs);
    }

    /**
     * Model generation should be based on the formula(Clause set)
     */
    @Override
    public void generate() {
        int[] model = LogicNGTool.model(cs);
        if(model != null && model.length > 0){
            this.addLiterals(model);
        }
    }
}
