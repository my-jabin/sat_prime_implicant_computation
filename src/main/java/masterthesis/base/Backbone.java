package masterthesis.base;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Backbone {

    protected HashSet<Literal> literals;

    public Backbone(Collection<Literal> collection) {
        this.literals = new HashSet<>(collection);
    }

    public Backbone() {
        this.literals = new HashSet<>();
    }

    public void addLiteral(Literal l) {
        this.literals.add(l);
    }

    public void addLiterals(Collection<Literal> list) {
        this.literals.addAll(list);
    }

    public Set<Literal> getLiterals() {
        return literals;
    }

    public boolean contains(Literal l) {
        return this.literals.contains(l);
    }

    public boolean isEmpty() {
        return this.literals.isEmpty();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return getClass().getSimpleName() + ":NULL";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("(").append(this.literals.size()).append("): (");
        this.literals.forEach(l -> sb.append(l.toString()).append(" "));
        sb.deleteCharAt(sb.lastIndexOf(" "));
        sb.append(")");
        return sb.toString();
    }

    public String toPrettyString() {
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
        Backbone implicant = (Backbone) o;
        if (implicant.getLiterals() == null
                || implicant.getLiterals().size() == 0
                || this.getLiterals().size() != implicant.getLiterals().size()) return false;
        for (Literal l : this.getLiterals()) {
            if (!implicant.contains(l))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(literals);
    }
}
