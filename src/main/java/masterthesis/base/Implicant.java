package masterthesis.base;


import java.util.*;
import java.util.stream.Collectors;

public class Implicant{

    protected LinkedHashSet<Literal> literals;
    private ApplicationContext ac = ApplicationContext.getInstance();

    public Implicant() {
        this.literals =  new LinkedHashSet<>();
    }

    public Implicant(Implicant implicant){
        this.literals = new LinkedHashSet<>(implicant.getLiterals());
    }

    public Implicant(int[] literals){
        this.literals = new LinkedHashSet<>();
        for(int i : literals){
            this.addLiteral(ac.getLiteral(i));
        }
    }

    public void removeLiteral(Literal literal){
        if(literals != null && !literals.isEmpty()){
            literals.remove(literal);
        }
    }

    public void removeLiteral(int l){
        removeLiteral(ac.getLiteral(l));
    }

    public boolean contains(Literal l){
        return this.literals.contains(l);
    }

    public boolean containsVariable(int var) {
        return contains(ac.getLiteral(var)) || contains(ac.getLiteral(-var));
    }

    public void addLiteral(Literal l){
        this.literals.add(l);
    }

    public void addLiterals(Collection<Literal> list){
        this.literals.addAll(list);
    }

    public void addLiterals(int[] literals) {
        addLiterals(Arrays.stream(literals).mapToObj(ac::getLiteral).collect(Collectors.toList()));
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
        if (isEmpty()) {
            return getClass().getSimpleName() + ":NULL";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("(").append(this.literals.size()).append("): (");
        this.literals.forEach( l -> sb.append(l.toString()).append(" "));
        sb.deleteCharAt(sb.lastIndexOf(" "));
        sb.append(")");
        return sb.toString();
    }

    public String toPrettyString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("(").append(this.literals.size()).append("): (");
        for (Literal literal : this.literals.stream().sorted().collect(Collectors.toList())) {
            sb.append(literal.toString()).append(" ");
        }
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
