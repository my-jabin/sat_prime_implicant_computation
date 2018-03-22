package masterthesis.base;

import javafx.beans.binding.StringBinding;

import java.util.ArrayList;

public class Clause {

    private ArrayList<Literal> literals;

    //if the clause contains one pi literal, then its state is PRIME
    public enum STATE {PRIME, DEFAULT};

    private STATE state;

    public Clause() {
        this.literals = new ArrayList<>();
        state = STATE.DEFAULT;
    }

    public void addLiteral(final Literal l){
        this.literals.add(l);
    }

    public ArrayList<Literal> getLiterals() {
        return literals;
    }

    public boolean isEmpyt(){
        return this.literals.isEmpty();
    }

    public int size(){
        return this.literals.size();
    }

    public Literal get(int index){
        return this.literals.get(index);
    }

    public boolean contains(Literal l ){
        return this.literals.contains(l);
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.literals.forEach( l ->{
            sb.append(l).append(" ");
        });
        sb.append(this.state);
        return sb.toString();
    }
}
