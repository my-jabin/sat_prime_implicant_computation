package masterthesis.base;

import masterthesis.utils.Debug;
import masterthesis.utils.DimacsFormatReader;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Solver class is in charge of solving a SAT problem based on CNF data.
 * Like providing a model, prime implicant(s), checking if the formula is SAT or NOT SAT.
 */
public class Solver {
    private final ApplicationContext ac;
    private final ClauseSet clauseSet;
    private Model defaultModel = null;
    private SolverEngine engine = SolverEngine.LOGICNG;
    private Implicant defaultPrimeImplicant;
    private Map<Literal, ArrayList<Watcher>> watchedList;

    public Solver(String fileName) {
        DimacsFormatReader.read(fileName);
        // application context and clause set won't be changed.
        this.ac = ApplicationContext.getInstance();
        this.clauseSet = new ClauseSet();
        clauseSet.init();
        reset();
    }

    public void reset() {
        this.watchedList = new HashMap<>();
        this.defaultPrimeImplicant = new Implicant();
        this.defaultModel = null;
    }

    public void setEngine(SolverEngine engine) {
        if (this.engine == engine) {
            return;
        }
        this.engine = engine;
        reset();
    }

    private void initWatchesAndSubset(final Implicant primeImplicant, final Model model) throws CloneNotSupportedException {
        if (model.isEmpty()) {
            throw new IllegalArgumentException("Must specify non empty defaultModel");
        }
        clauseSet.getClauses().stream().forEach(c -> {
            final Watcher watcher = new Watcher(c);
            watcher.initWatchedIndex(primeImplicant, watchedList);
        });
        primeImplicant.getLiterals().stream().forEach(l -> {
            watchedList.computeIfAbsent(l, k -> new ArrayList<>()).stream()
                    .forEach(w -> {
                        w.setStatus(Watcher.Status.SUCCESS);
                    });
        });

        // prime Unit propagation
        Debug.println(false, "First of all, add unit literals to PI", primeImplicant);
        primeUnitPropagation(primeImplicant, model);
    }

    private void primeUnitPropagation(final Implicant primeImplicant, final Model model) throws CloneNotSupportedException {
        Implicant uppi = (Implicant) primeImplicant.clone();
        do {
            uppi.getLiterals().forEach(l -> watchedList.getOrDefault(l, new ArrayList<>()).clear());
            primeImplicant.addLiterals(uppi.getLiterals());
            Implicant tempPI = (Implicant) uppi.clone();
            uppi.clear();
            tempPI.getLiterals().forEach(l -> {
                this.unwatchAndPropagate(l.getComplementary(), uppi, model);
            });
        } while (!uppi.isEmpty());
    }

    private void primeByWatches(final Implicant primeImplicant, final Model model) throws CloneNotSupportedException {
        Debug.println(false, primeImplicant);
        if (model.isEmpty()) {
            throw new IllegalArgumentException("Must specify a non empty defaultModel");
        }
        model.getLiterals().stream()
                .filter(l -> !primeImplicant.contains(l))
                .collect(Collectors.toList())
                .forEach(l -> {
                    Debug.println(false, l);
                    //remove -l from watched list in all clauses
                    unwatchAndPropagate(l.getComplementary(), primeImplicant, model);
                });
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
            m.removeLiteral(pickup); // remove the pickup literal from defaultModel
            unwatchAndPropagate(pickup, primeImplicant, m);
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
                        // state of the clause that contains other literal must set to be PRIME
                        clauseSet.getClauses().stream()
                                .filter(clause -> clause.getState() != Clause.STATE.PRIME)
                                .filter(clause -> clause.contains(other))
                                .forEach(clause -> clause.setState(Clause.STATE.PRIME));

                        watchedList.get(other).remove(w);
                        watchedList.get(other).forEach(watcher -> watcher.setStatus(Watcher.Status.SUCCESS));
                    } else {
                        // move watched index down
                        w.moveWatchedIndex(l, satisfied);
                        watchedList.computeIfAbsent(satisfied, k -> new ArrayList<>()).add(w);
                    }
                });
        // after unwatching, all watchers of literal l should be clean
        watchedList.computeIfAbsent(l, k -> new ArrayList<>()).clear();
    }


    public boolean sat() {
        return !getModel().isEmpty();
    }

    /**
     * Compute a prime implicant under the default defaultModel
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public Implicant getPrimeImplicant() throws CloneNotSupportedException {
        if (this.defaultPrimeImplicant.isEmpty()) {
            computePI(this.defaultPrimeImplicant, getModel());
        }
        return this.defaultPrimeImplicant;
    }

    private void computePI(Implicant implicant, Model model) throws CloneNotSupportedException {
        watchedList.clear();
        initWatchesAndSubset(implicant, model);
        primeByWatches(implicant, model);
    }

    /**
     * Compute a prime implicant under the specified defaultModel
     *
     * @param model
     * @return
     * @throws CloneNotSupportedException
     */
    public Implicant getPrimeImplicant(Model model) throws CloneNotSupportedException {
        Implicant pi = new Implicant();
        computePI(pi, model);
        return pi;
    }

    /**
     * compute all prime implicants under the default defaultModel.
     *
     * @return
     */
    public List<Implicant> getAllPrimeImplicants() throws CloneNotSupportedException {
        return getAllPrimeImplicants(getModel());
    }

    /**
     * Compute all prime implicants under the specified defaultModel.
     * <p>
     * Step 1: initialize watched literals and subset of prime implicant
     * Step 2: Perform UnwatchAndPropagate(), now watched literals in the NOT SUCCESS watcher are all
     * satisfied literal.
     * Step 3: reform the clause set so that each clause in the clause set contains only satisfied literal.
     * Step 4: Build trees with the reformed clause set. Each literal in the reformed clause set is a root for a tree.
     * Step 5: Deep First Search each tree, generate the subsets of prime implicant
     *
     * @param model
     * @return
     */
    public List<Implicant> getAllPrimeImplicants(Model model) throws CloneNotSupportedException {
        if (model == null || model.isEmpty()) return null;
        watchedList.clear();
        List<Implicant> allPI = new ArrayList<>();
        Implicant baseImplicant = new Implicant();

        // step 1:
        initWatchesAndSubset(baseImplicant, model);
        Debug.println(false, baseImplicant);
        // step 2:
        model.getLiterals().stream()
                .filter(l -> !baseImplicant.contains(l))
                .collect(Collectors.toList())
                .forEach(l -> {
                    Debug.println(false, l);
                    //remove -l from watched list in all clauses
                    unwatchAndPropagate(l.getComplementary(), baseImplicant, model);
                });

        // Step 3:
        ClauseSet reformedCS = reformClauseSet(this.clauseSet, model);
        Debug.println(true, reformedCS.size());

        // if the reformed clause set is empty, it means that the base implicant covers all clause set.
        // No need to do more. The base implicant is the only one prime implicant.
        if (reformedCS.size() == 0) {
            allPI.add(baseImplicant);
            return allPI;
        }

        // Step 4
        LinkedHashMap<Literal, Integer> weight = weighting(reformedCS);

        ArrayList<Node> trees = new ArrayList<>();
        for (Map.Entry<Literal, Integer> entry : weight.entrySet()) {
            Literal l = ac.getLiteral(entry.getKey());
            // root is the entry point of a tree
            Node root = new Node(l);
            buildTree(reformedCS, root, l);
            trees.add(root);
        }

        // Step 5: Deeply search the tree, generate the subset of prime implicant
        Debug.println(true, "Trees size = " + trees.size());
        Debug.println(true, "Weights size = " + weight.size());

        // each implicant is an subset of a prime implicant
        List<Implicant> subsets = new ArrayList<>();
        Implicant tempImplicant = new Implicant();
        for (Node root : trees) {
            dfs(root, subsets, tempImplicant);
        }

        // Step 6: combine the base implicant with the subset to generate all prime implicants


        subsets.forEach(implicant -> {
            Implicant i = new Implicant();
            i.addLiterals(baseImplicant.getLiterals());
            i.addLiterals(implicant.getLiterals());
            allPI.add(i);
        });

        return allPI;
    }

    /**
     * Deeply Search current node, if the node has children, recursively search to the leaf,
     * alone the path to leaf, put the node's value into the temporary implicant.
     * Finally, if the implicant is minimal, add the subsets of prime implicant.
     *
     * @param node        Current node.
     * @param subsets     A list contains all subsets of prime implicant.
     * @param tImplicants A temporary implicant object.
     * @throws CloneNotSupportedException
     */
    private void dfs(Node node, List<Implicant> subsets, Implicant tImplicants) throws CloneNotSupportedException {
        List<Node> children = node.getChildren();
        if (children != null && children.size() != 0) {
            tImplicants.addLiteral(node.getValue());
            for (Node child : children) {
                dfs(child, subsets, tImplicants);
            }
            tImplicants.removeLiteral(node.getValue());
        } else {
            // reach the end node(the leaf). construct the implicant instance
            // if the instance is not the minimal(subset of the instance is in the list)
            // do not add the instance to the list.
            Implicant instance = (Implicant) tImplicants.clone();
            instance.addLiteral(node.getValue());
            for (Implicant implicant : subsets) {
                if (implicant.isSubset(instance)) {
                    return;
                }
            }
            subsets.add(instance);
        }
    }


    /**
     * Reform the clause set so that the returned clause set contains NON-PRIME clause,
     * and each clause contains only satisfied literals.
     *
     * @param clauseSet The original clause set to be reformed
     * @param model     The defaultModel of the original clause set
     * @return Reformed ClauseSet
     */
    private ClauseSet reformClauseSet(ClauseSet clauseSet, Model model) {
        ClauseSet reformed = new ClauseSet();
        Debug.println(false, watchedList);
        clauseSet.getClauses().stream()
                .filter(clause -> clause.getState() != Clause.STATE.PRIME)
                .forEach(clause -> {
                    Clause c = new Clause();
                    clause.getLiterals().stream()
                            .filter(l -> model.contains(l))
                            .forEach(c::addLiteral);
                    reformed.addClause(c);
                });
        return reformed;
    }

    public Model getModel() {
        if (this.defaultModel == null) {
            this.defaultModel = ModelFactory.getModel(this.engine);
        }
        return this.defaultModel;
    }

    public ClauseSet getClauseSet() {
        return this.clauseSet;
    }

    /**
     * Weighting a clause set in order descending. The heaviest one is the first entry.
     *
     * @param cs Clause set
     * @return LinkedHashMap
     */
    public LinkedHashMap<Literal, Integer> weighting(ClauseSet cs) {
        HashMap<Literal, Integer> weightMap = weight(cs);
        if (weightMap == null) return null;

        List<Map.Entry<Literal, Integer>> sortedList = weightMap.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toList());

        LinkedHashMap<Literal, Integer> result = new LinkedHashMap<>();

        for (Map.Entry<Literal, Integer> entry : sortedList) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * Weighting a clause set. Each literal has a weight represents the occurrence in the clause set.
     *
     * @param cs Clause set
     * @return HashMap
     */
    private HashMap<Literal, Integer> weight(ClauseSet cs) {
        if (cs == null) return null;
        HashMap<Literal, Integer> weightMap = new HashMap<>();
        cs.getClauses().forEach(clause -> {
            clause.getLiterals().forEach(literal -> {
                if (weightMap.containsKey(literal)) {
                    weightMap.put(literal, weightMap.get(literal) + 1);
                } else {
                    weightMap.put(literal, 1);
                }
            });
        });
        return weightMap;
    }

    private List<Literal> getHighestWeightLiterals(ClauseSet cs) {
        HashMap<Literal, Integer> weightMap = weight(cs);
        if (weightMap == null) return null;

        List<Map.Entry<Literal, Integer>> sortedList = weightMap.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toList());

        int maxWeight = sortedList.get(0).getValue();

        List<Literal> result = new ArrayList<>();

        for (Map.Entry<Literal, Integer> entry : sortedList) {
            if (entry.getValue() == maxWeight) {
                result.add(entry.getKey());
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Create a (sub)tree with the parent node.
     * The process of building the tree is:
     * Clause set removes the clause that contains literal l.
     * Calculate again the weight of each literal,
     * then add the literals with highest weight to the child of parent node.
     * Repetitively build (sub)tree base on the child node.
     * <p>
     * We could access the whole tree by access the parent node
     *
     * @param cs     Clause set
     * @param parent Parent node is the root of this (sub)tree
     * @param l      Literal, the value of the parent
     */
    public void buildTree(ClauseSet cs, Node parent, Literal l) {
        ClauseSet reducedCS = new ClauseSet();
        for (Clause clause : cs.getClauses()) {
            if (!clause.contains(l))
                reducedCS.addClause(clause);
        }
        if (reducedCS.isEmpty()) {
            return;
        }
        List<Literal> highestWeightLiterals = getHighestWeightLiterals(reducedCS);
        if (highestWeightLiterals == null)
            return;
        for (Literal li : highestWeightLiterals) {
            Node child = new Node(li);
            parent.addChild(child);
            buildTree(reducedCS, child, li);
        }
    }

    public void primeImplicantCover() throws CloneNotSupportedException {
        Implicant pi = this.getPrimeImplicant();
        Clause clause = pi.toClause();
        ClauseSet secondCS = new ClauseSet();
        secondCS.init();
        secondCS.addClause(clause);

    }

}
