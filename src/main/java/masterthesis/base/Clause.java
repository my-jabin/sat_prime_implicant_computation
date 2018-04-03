package masterthesis.base;

import com.sun.jdi.event.ClassUnloadEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

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

    public void addLiterals(Collection<Literal> c){
        this.literals.addAll(c);
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

    public String toPrettyString() {
        StringBuilder sb = new StringBuilder();
        this.literals.stream().sorted().collect(Collectors.toList()).forEach(l ->{
            sb.append(l).append(" ");
        });
        sb.append(this.state);
        return sb.toString();
    }

    public Clause reverse(){
        Clause clause = new Clause();
        for(Literal l : this.getLiterals()){
            clause.addLiteral( l.getComplementary());
        }
        return clause;
    }
}
