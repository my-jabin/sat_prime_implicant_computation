package masterthesis.base;

import javafx.beans.binding.StringBinding;

import java.util.ArrayList;

public class Clause {

    private ArrayList<Literal> literals;

    public Clause() {
        this.literals = new ArrayList<>();
    }

    public void addLiteral(final Literal l){
        this.literals.add(l);
    }

    // if found, return index, else return -1;
    public int getNextSatLiteralIndex(Model model){
        return -1;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.literals.forEach( l ->{
            sb.append(l).append(" ");
        });
        sb.deleteCharAt(sb.lastIndexOf(" "));
        return sb.toString();
    }
}
