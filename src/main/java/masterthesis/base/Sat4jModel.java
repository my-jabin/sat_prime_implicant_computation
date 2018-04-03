package masterthesis.base;
import masterthesis.utils.Sat4jTool;

public class Sat4jModel extends Model {

    public Sat4jModel(ClauseSet cs) {
        super(cs);
    }

    @Override
    public void generate() {
        int[] model = Sat4jTool.model(cs);
        if (model != null && model.length > 0)
            this.addLiterals(model);
    }
}
