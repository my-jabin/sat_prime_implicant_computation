package masterthesis.base;

import java.util.ArrayList;
import java.util.Objects;

public class Literal implements Comparable {

    // true: positive, false: negative
    private boolean polarity;

    private int value;

    public Literal(int value, boolean polarity) {
        this.polarity = polarity;
        this.value = value;
    }

    public Literal(int value) {
        this(value, true);
    }

    public Literal getComplementary() {
        ApplicationContext ac = ApplicationContext.getInstance();
        return ac.getLiteral(value, !this.polarity);
    }


    public boolean getPolarity() {
        return this.polarity;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return polarity ? String.valueOf(value) : "-" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return value == literal.value && polarity == literal.polarity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(polarity, value);
    }

    @Override
    public int compareTo(Object o) {
        if (this.value == ((Literal) o).value)
            return 0;
        return this.value > ((Literal) o).value ? 1 : -1;
    }
}
