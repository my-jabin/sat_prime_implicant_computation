package masterthesis.base;

import java.util.Objects;

public abstract class Model extends Implicant implements Cloneable {

    protected ClauseSet cs;

    public Model(ClauseSet cs) {
        super();
        this.cs = cs;
        generate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        if(model.getLiterals() == null
                || model.getLiterals().size() ==0
                || this.getLiterals().size() != model.getLiterals().size()) return false;
        for(Literal l : this.getLiterals()){
            if(!model.contains(l))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(literals);
    }

    public abstract void generate();
}