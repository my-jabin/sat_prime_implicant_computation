package masterthesis.base;

import java.util.*;

public class ApplicationContext {

    private static ApplicationContext mInstance = null;

    private Map<Integer, Literal> posLiterals;
    private Map<Integer, Literal> negLiterals;
    private ArrayList<ArrayList<Integer>> cnfContent;

    private ApplicationContext() {
        this.negLiterals = new HashMap<>();
        this.posLiterals = new HashMap<>();
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
        Literal l = posLiterals.get(value);
        if (l == null) {
            l = new Literal(value, true);
            posLiterals.put(value, l);
        }
        return l;
    }

    private Literal getNegLiteral(final int value) {
        int key = Math.abs(value);
        Literal l = negLiterals.get(key);
        if (l == null) {
            l = new Literal(key, false);
            negLiterals.put(key, l);
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

//
//    public void primeByWatches(final ClauseSet cs, final Implicant pi, final Model model, final WatchedList wl) throws CloneNotSupportedException {
//        Debug.println(false, pi);
//        if (model.isEmpty()) {
//            System.err.println("Model does't exist, Please check your formula");
//        }
//        model.getLiterals().stream()
//                .filter(l -> !pi.contains(l))
//                .collect(Collectors.toList())
//                .forEach(l -> {
//                    Debug.println(false, l);
//                    //remove -l from watched list in all clauses
//                    unwatchAndPropagate(l.getComplementary(), cs, pi, model, wl);
//                });
//        // after first
//        Debug.println(false, wl);
//        Debug.println(false, "After first UWAP", pi);
//
//        // 现在所有状态为DEFAULT（既非success）的clause的watch index都指向satisfiable literal( could also points to an prime literal)
//        // 这时候可以进行第二次UWAP，既：随机剔除一个literal
//        Model m = (Model) model.clone();
//        while (true) {
//            // randomly pick a literal up, here I pick the first literal up.
//            Optional<Literal> o = m.getLiterals().stream().filter(l -> !pi.contains(l)).findFirst();
//            Literal pickup = o.orElse(null);
//            if (pickup == null) break;
//            m.removeLiteral(pickup); // remove the pickup literal from model
//            unwatchAndPropagate(pickup, cs, pi, m, wl);
//        }
//
//        Debug.println(false, wl);
//        Debug.println(false, "After second UWAP", pi);
//    }
//
//    public void unwatchAndPropagate(final Literal l, final ClauseSet cs, final Implicant pi, final Model model, final WatchedList wl) {
//        Debug.println(false, l);
//        wl.get(l).stream()
//                .filter(w -> w.getStatus() != Watcher.Status.SUCCESS)
//                .forEach(w -> {
//                    Literal satisfied = w.getNextSatisfiedLiteral(model);
//                    if (satisfied == null) {
//                        // add the other literal into Pi;
//                        final Literal other = w.getOtherWatchedLiteral(l);
//                        pi.addLiteral(other);
//                        wl.get(other).remove(w);
//                        wl.get(other).forEach(watcher -> watcher.setStatus(Watcher.Status.SUCCESS));
//                    } else {
//                        // move watched index down
//                        w.moveWatchedIndex(l, satisfied);
//                        wl.get(satisfied).add(w);
//                        // wl.get(l).remove(w);
//                    }
//                });
//        // after unwatching, all watchers of literal l should be clean
//        wl.get(l).clear();
//    }
}
