package masterthesis.base;

import javafx.collections.transformation.SortedList;
import masterthesis.utils.Debug;

import java.util.*;
import java.util.stream.Collectors;

public class Implicant implements Cloneable{

    private ArrayList<Literal> literals;

    public Implicant() {
        this.literals =  new ArrayList<>();
    }

    public void removeLiteral(Literal literal){
        if(literals != null && !literals.isEmpty()){
            literals.remove(literal);
        }
    }

    public boolean contains(Literal l){
        Debug.println(false,"====contains literal? " + l +  " "+this.literals.contains(l));
        return this.literals.contains(l);
    }

    public void addLiteral(Literal l){
        if(!this.literals.contains(l))
            this.literals.add(l);
    }

    public void addLiterals(List<Literal> list){
        list.forEach(l -> {
            if(!this.literals.contains(l))
                this.literals.add(l);
        });
    }

    /**
     * Inserts a collection of literals, starting at the specified position.
     * @param index
     * @param list
     */
    public void addLiterals(int index, List<Literal> list){
        this.literals.addAll(index,list);
    }

    public Literal get(int index){
        return this.literals.get(index);
    }

    public void clear(){
        this.literals.clear();
    }
    public ArrayList<Literal> getLiterals() {
        return literals;
    }

    public boolean isEmpty() {
        return  this.literals.isEmpty();
    }

    public int size(){
        return this.literals.size();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Implicant i = (Implicant) super.clone();
        i.literals = (ArrayList<Literal>) literals.clone();
        return i;
    }

    /**
     * If the instance is a subset of implicant i, then it returns true, otherwise false.
     * If the instance contains no literal, returns also false.
     * For example, {-8} is a subset of {-8.-10}
     * @param i
     * @return
     */
    public boolean isSubset(Implicant i){
        if(this.isEmpty() || this.size() > i.size()) return false;
        for(Literal l: this.literals){
            if(!i.contains(l)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Implicant(").append(this.literals.size()).append("): (");
        this.literals.forEach( l -> sb.append(l.toString()).append(" "));
        sb.deleteCharAt(sb.lastIndexOf(" "));
        sb.append(")");
        return sb.toString();
    }


    public String toStringPretty() {
        StringBuilder sb = new StringBuilder();
        Collections.sort(this.literals);
        List<Literal> newList =  this.literals.stream()
                .sorted((o1, o2) -> o1.compareTo(o2))
                .collect(Collectors.toList());

        sb.append("Implicant(").append(newList.size()).append("): (");
        newList.forEach( l -> sb.append(l.toString()).append(" "));
        sb.deleteCharAt(sb.lastIndexOf(" "));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Implicant implicant = (Implicant) o;
        if(implicant.getLiterals() == null
                || implicant.getLiterals().size() ==0
                || this.getLiterals().size() != implicant.getLiterals().size()) return false;
        for(Literal l : this.getLiterals()){
            if(!implicant.contains(l))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(literals);
    }
}
