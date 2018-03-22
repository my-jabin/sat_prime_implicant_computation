package masterthesis.base;
import masterthesis.primebywatches.Watcher;
import masterthesis.utils.Debug;
import masterthesis.utils.DimacsFormatReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Solver {
    private final ApplicationContext ac;
    private final ClauseSet clauseSet;
    private Model model = null;
    private SolverEngine engine = SolverEngine.LOGICNG;
    private Implicant primeImplicant;
    private Map<Literal, ArrayList<Watcher>> watchedList;

    public Solver(String fileName) {
        DimacsFormatReader.read(fileName);
        // application context and clause set won't be changed.
        this.ac = ApplicationContext.getInstance();
        this.clauseSet = new ClauseSet();
        clauseSet.init();
        reset();
    }

    public void reset(){
        this.watchedList = new HashMap<>();
        this.primeImplicant = new Implicant(ac);
        this.model = null;
        this.watchedList = new HashMap<>();
    }

    public void setEngine(SolverEngine engine) {
        if(this.engine == engine) {
            return;
        }
        this.engine = engine;
        reset();
    }

    private void initWatchesAndSubset() throws CloneNotSupportedException {
        if (model.isEmpty()) {
            System.out.println("Model doesn't exist, watched List cannot be initialized");
            return;
        }
        clauseSet.getClauses().stream().forEach(c -> {
            final Watcher watcher = new Watcher(c);
            //watcher.initWatchedIndex(primeImplicant, this, ac);
            watcher.initWatchedIndex(primeImplicant, watchedList, ac);
        });

        // foreach l in PI do
        //  foreach w in w(l) do
        //      set watcher status to be success
        primeImplicant.getLiterals().stream().forEach(l -> {
            watchedList.computeIfAbsent(l, k -> new ArrayList<>()).stream()
                    .forEach(w -> {
                        w.setStatus(Watcher.Status.SUCCESS);
                    });
        });

        // prime Unit propagation
        Debug.println(false, "First of all, add unit literals to PI", primeImplicant);
        primeUnitPropagation();
    }

    private void primeUnitPropagation() throws CloneNotSupportedException {
        Implicant uppi = (Implicant) primeImplicant.clone();
        do {
            uppi.getLiterals().forEach(l -> watchedList.getOrDefault(l, new ArrayList<>()).clear());
            primeImplicant.addLiterals(uppi.getLiterals());
            Implicant tempPI = (Implicant) uppi.clone();
            uppi.clear();
            tempPI.getLiterals().forEach(l -> {
                //this.ac.unwatchAndPropagate(l.getComplementary(this.ac),clauseSet,uppi,model,this);
                this.unwatchAndPropagate(l.getComplementary(), uppi,model);
            });
        } while (!uppi.isEmpty());
    }

    private void primeByWatches() throws CloneNotSupportedException {
        Debug.println(false, primeImplicant);
        if (model.isEmpty()) {
            System.err.println("Model does't exist, Please check your formula");
        }
        model.getLiterals().stream()
                .filter(l -> !primeImplicant.contains(l))
                .collect(Collectors.toList())
                .forEach(l -> {
                    Debug.println(false, l);
                    //remove -l from watched list in all clauses
                    unwatchAndPropagate(l.getComplementary(), primeImplicant,model);
                });
        // after first
        Debug.println(false, watchedList);
        Debug.println(false, "After first UWAP", primeImplicant);

        // 现在所有状态为DEFAULT（既非success）的clause的watch index都指向satisfiable literal( could also points to an prime literal)
        // 这时候可以进行第二次UWAP，既：随机剔除一个literal
        Model m = (Model) model.clone();
        while (true) {
            // randomly pick a literal up, here I pick the first literal up.
            Optional<Literal> o = m.getLiterals().stream().filter(l -> !primeImplicant.contains(l)).findFirst();
            Literal pickup = o.orElse(null);
            if (pickup == null) break;
            m.removeLiteral(pickup); // remove the pickup literal from model
            unwatchAndPropagate(pickup, primeImplicant,m);
        }

        Debug.println(false, watchedList);
        Debug.println(false, "After second UWAP", primeImplicant);
    }

    private void unwatchAndPropagate(final Literal l, final Implicant pi, final Model m) {
        Debug.println(false, l);
        watchedList.computeIfAbsent(l, k -> new ArrayList<>()).stream()
                .filter(w -> w.getStatus() != Watcher.Status.SUCCESS)
                .forEach(w -> {
                    Literal satisfied = w.getNextSatisfiedLiteral(m);
                    if (satisfied == null) {
                        // add the other literal into Pi;
                        final Literal other = w.getOtherWatchedLiteral(l);
                        pi.addLiteral(other);
                        watchedList.get(other).remove(w);
                        watchedList.get(other).forEach(watcher -> watcher.setStatus(Watcher.Status.SUCCESS));
                    } else {
                        // move watched index down
                        w.moveWatchedIndex(l, satisfied);
                        watchedList.computeIfAbsent(satisfied, k -> new ArrayList<>()).add(w);
                       // watchedList.get(satisfied).add(w);
                        // wl.get(l).remove(w);
                    }
                });
        // after unwatching, all watchers of literal l should be clean
        watchedList.computeIfAbsent(l, k -> new ArrayList<>()).clear();
    }


    public boolean sat() {
        return !getModel().isEmpty();
    }

    public Implicant getPrimeImplicant() throws CloneNotSupportedException {
        if (primeImplicant.isEmpty()) {
            initWatchesAndSubset();
            primeByWatches();
        }
        return this.primeImplicant;
    }


    public Model getModel() {
        if(this.model == null){
            this.model = ModelFactory.getModel(this.engine,ac);
        }
        return this.model;
    }

    public ClauseSet getClauseSet() {
        return this.clauseSet;
    }

}
