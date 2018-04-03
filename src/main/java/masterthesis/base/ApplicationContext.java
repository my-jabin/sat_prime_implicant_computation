package masterthesis.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private static ApplicationContext mInstance = null;

    private Map<Integer, Literal> variables;
    private Map<Integer, Literal> negLiterals;
    private ArrayList<ArrayList<Integer>> cnfContent;

    private ApplicationContext() {
        this.negLiterals = new HashMap<>();
        this.variables = new HashMap<>();
        reset();
    }

    public static ApplicationContext getInstance() {
        if (mInstance == null) {
            mInstance = new ApplicationContext();
        }
        return mInstance;
    }

    public void reset() {
        cnfContent = new ArrayList<>();
    }

    public void addCNFContent(final ArrayList<Integer> line) {
        if (line == null && !line.isEmpty()) return;
        cnfContent.add(line);
    }

    private Literal getPosLiteral(final int value) {
        if (value < 0) return this.getNegLiteral(value);
        Literal l = variables.get(value);
        if (l == null) {
            l = new Literal(value, true);
            variables.put(value, l);
        }
        return l;
    }

    private Literal getNegLiteral(final int value) {
        int key = Math.abs(value);
        Literal l = negLiterals.get(key);
        if (l == null) {
            l = new Literal(key, false);
            negLiterals.put(key, l);
            Literal v = new Literal(key,true);
            variables.put(key,v);
        }
        return l;
    }

    public Literal getLiteral(final int value) {
        return value >= 0 ? getPosLiteral(value) : getNegLiteral(value);
    }

    public Literal getLiteral(final int value, final boolean polarity) {
        if (polarity) return getPosLiteral(Math.abs(value));
        return getNegLiteral(value);
    }

    public Literal getLiteral(Literal l) {
        return getLiteral(l.getValue(), l.getPolarity());
    }

    public ArrayList<ArrayList<Integer>> getCNFContent() {
        return this.cnfContent;
    }

    public int getVariablesCount(){
        return this.variables.size();
    }
}
