package masterthesis.base;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Model implements Cloneable {

    private ArrayList<Literal> literals;
    protected ClauseSet cs;
    final ApplicationContext ac = ApplicationContext.getInstance();

    public Model(ClauseSet cs) {
        this.literals =  new ArrayList<>();
        this.cs = cs;
        generate();
    }

    public abstract void generate();

    public void removeLiteral(Literal literal){
        if(literals != null && !literals.isEmpty()){
            literals.remove(literal);
        }
    }

    public void addLiterals(int[] literals){
        for(int i: literals){
            this.addLiteral(ac.getLiteral(i));
        }
    }

    public void addLiteral(Literal l){
        this.literals.add(ac.getLiteral(l));
    }

    public Literal get(int index){
        return this.literals.get(index);
    }

    public boolean contains(Object o){
        return this.literals.contains(o);
    }

    public void clear(){
        this.literals.clear();
    }
    public ArrayList<Literal> getLiterals() {
        return literals;
    }

    public int size(){
        return this.literals.size();
    }

    public boolean isEmpty() {
        return  this.literals.isEmpty();
    }

    public Object clone() throws CloneNotSupportedException {
        Model m = (Model)super.clone();
        m.literals = (ArrayList<Literal>) literals.clone();
        return m;
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

    @Override
    public String toString() {
        if(isEmpty()){
            return "Model: NULL";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Model(").append(this.literals.size()).append("): (");
        this.literals.forEach( l -> sb.append(l).append(","));
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(")");
        return sb.toString();
    }
}
