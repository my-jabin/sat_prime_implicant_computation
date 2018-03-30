package masterthesis.base;


import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Implicant implements Cloneable{

    private LinkedHashSet<Literal> literals;

    public Implicant() {
        this.literals =  new LinkedHashSet<>();
    }

    public void removeLiteral(Literal literal){
        if(literals != null && !literals.isEmpty()){
            literals.remove(literal);
        }
    }

    public boolean contains(Literal l){
        return this.literals.contains(l);
    }

    public void addLiteral(Literal l){
        this.literals.add(l);
    }

    public void addLiterals(Collection<Literal> list){
        this.literals.addAll(list);
    }


    public void clear(){
        this.literals.clear();
    }
    public Set<Literal> getLiterals() {
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
        i.literals = (LinkedHashSet<Literal>) literals.clone();
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
        return i.getLiterals().containsAll(this.getLiterals());
    }

    public boolean containsAll(Collection list){
        return this.getLiterals().containsAll(list);
    }

    public Clause toClause(){
        Clause clause = new Clause();
        clause.addLiterals(this.getLiterals());
        return clause;
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
