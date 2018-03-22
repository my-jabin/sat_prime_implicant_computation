package masterthesis.base;

import java.util.ArrayList;

public abstract class Model implements Cloneable {

    private ArrayList<Literal> literals;
    final ApplicationContext ac;

    public Model(ApplicationContext ac) {
        this.ac = ac;
        this.literals =  new ArrayList<>();
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
