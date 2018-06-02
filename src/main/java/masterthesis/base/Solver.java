package masterthesis.base;

import masterthesis.utils.Debug;
import masterthesis.utils.DimacsFormatReader;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Solver class is in charge of solving a SAT problem based on CNF data or a given clause set.
 * Like providing a model, prime implicant(s), checking if the formula is SAT or NOT SAT.
 */
public class Solver {
    private final ApplicationContext ac;
    private ClauseSet clauseSet;
    private Model defaultModel = null;
    private SolverEngine engine = SolverEngine.LOGICNG;
    //    private SolverEngine engine = SolverEngine.SAT4J;
    private Implicant defaultPrimeImplicant;
    private Map<Literal, ArrayList<Watcher>> watchedList;

    public Solver(String fileName) {
        DimacsFormatReader.read(fileName);
        // application context and clause set won't be changed.
        this.ac = ApplicationContext.getInstance();
        this.clauseSet = new ClauseSet();
        this.clauseSet.init();
//        this.clauseSet = ac.getClauseSet();
        reset();
    }

    public void setClauseSet(ClauseSet cs) {
        this.clauseSet = cs;
        reset();
        // todo: any better way to reset the clause set?
    }

    public void addClause(Clause clause) {
        this.clauseSet.addClause(clause);
        // todo: add LogicNGTool to application context, to be singleton
    }

    private void reset() {
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

    /**
     * initializing the watched literal on the first two literals who's variable appears in the implicant
     *
     * @param primeImplicant The prime implicant
     * @param implicant      The implicant, specially case is a model
     */
    private void initWatchesAndSubset(final Implicant primeImplicant, final Implicant implicant) {
        if (implicant.isEmpty()) {
            throw new IllegalArgumentException("Must specify non-empty implicant");
        }
        clauseSet.getClauses().forEach(c -> {
            final Watcher watcher = new Watcher(c);
            watcher.initWatchedIndex(primeImplicant, watchedList, implicant);
        });
        primeImplicant.getLiterals().forEach(l -> {
            watchedList.computeIfAbsent(l, k -> new ArrayList<>())
                    .forEach(w -> {
                        w.setStatus(Watcher.Status.SUCCESS);
                    });
        });

        // prime Unit propagation
        Debug.println(false, "First of all, add unit literals to PI", primeImplicant);
        primeUnitPropagation(primeImplicant, implicant);
    }

    private void primeUnitPropagation(final Implicant primeImplicant, final Implicant implicant) {
        Implicant uppi = new Implicant(primeImplicant);
        do {
            uppi.getLiterals().forEach(l -> watchedList.getOrDefault(l, new ArrayList<>()).clear());
            primeImplicant.addLiterals(uppi.getLiterals());
            Implicant tempPI = new Implicant(uppi);
            uppi.clear();
            tempPI.getLiterals().forEach(l -> this.unwatchAndPropagate(l.getComplementary(), uppi, implicant));
        } while (!uppi.isEmpty());
    }


    private void primeByWatches(final Implicant primeImplicant, final Implicant implicant) {
        Debug.println(false, primeImplicant);
        if (implicant.isEmpty()) {
            throw new IllegalArgumentException("Must specify a non-empty implicant");
        }
        // 1 UWAP
        implicant.getLiterals().stream()
                .filter(l -> !primeImplicant.contains(l))
                .collect(Collectors.toList())
                .forEach(l -> {
                    //remove -l from watched list in all clauses
                    unwatchAndPropagate(l.getComplementary(), primeImplicant, implicant);
                });
        Debug.println(false, watchedList);
        Debug.println(false, "After first UWAP", primeImplicant);

        // 现在所有状态为DEFAULT（既非success）的clause的watch index都指向satisfiable literal( could also points to an prime literal)
        // 这时候可以进行第二次UWAP，既：随机剔除一个literal
        try {
            //Model m = (Model) implicant.clone();
            Implicant m = new Implicant(implicant);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void unwatchAndPropagate(final Literal l, final Implicant pi, final Implicant implicant) {
        Debug.println(false, l);
        watchedList.computeIfAbsent(l, k -> new ArrayList<>()).stream()
                .filter(w -> w.getStatus() != Watcher.Status.SUCCESS)
                .forEach(w -> {
                    Literal satisfied = w.getNextSatisfiedLiteral(implicant);
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
//        if(this.engine == SolverEngine.LOGICNG){
//            return LogicNGTool.sat(this.clauseSet);
//        }else{
//            return !getModel().isEmpty();
//        }
        return !getModel().isEmpty();
    }

    /**
     * Compute a prime implicant under the default model
     *
     * @return Prime implicant
     */
    public Implicant getPrimeImplicant() {
        if (this.defaultPrimeImplicant.isEmpty()) {
            computePI(this.defaultPrimeImplicant, getModel());
        }
        return this.defaultPrimeImplicant;
    }

    /**
     * Compute a prime implicant under a specified implicant
     *
     * @param pi        Prime implicant reference
     * @param implicant Specified implicant
     */
    private void computePI(Implicant pi, Implicant implicant) {
        watchedList.clear();
        initWatchesAndSubset(pi, implicant);
        primeByWatches(pi, implicant);
    }

    /**
     * Compute a prime implicant under the specified implicant
     *
     * @param implicant specified implicant
     * @return Prime implicant
     */
    public Implicant getPrimeImplicant(Implicant implicant) {
        Implicant pi = new Implicant();
        computePI(pi, implicant);
        return pi;
    }

    /**
     * compute all prime implicants under the default defaultModel.
     *
     * @return A list of prime implicants
     */
    public List<Implicant> getAllPrimeImplicants() {
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
     * @param model Model
     * @return A list of prime implicants
     */
    public List<Implicant> getAllPrimeImplicants(Model model) {
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
        Debug.println(false, "Trees size = " + trees.size());
        Debug.println(false, "Weights size = " + weight.size());

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


    public Set<Literal> getBackboneCandidate() {
        this.watchedList.clear();
        Implicant backbone = new Implicant();
        Model model = getModel();
        initWatchesAndSubset(backbone, model);
        model.getLiterals().stream()
                .filter(l -> !backbone.contains(l))
                .collect(Collectors.toList())
                .forEach(l -> {
                    //remove -l from watched list in all clauses
                    unwatchAndPropagate(l.getComplementary(), backbone, model);
                });
        Debug.println(false, "After first UWAP", backbone);
        return backbone.getLiterals();
    }


    public Set<Literal> getGlobalUnitPropagatedLiterals() {
        if (!sat()) {
            throw new IllegalStateException("The formula is not satisfiable");
        }
        this.watchedList.clear();
        // subset of backbone. is empty at first.
        HashSet<Literal> backbone = new HashSet<>();
        clauseSet.getClauses().forEach(c -> {
            final Watcher watcher = new Watcher(c);
            watcher.initGlobalWatcher(backbone, watchedList);
        });
        backbone.forEach(l -> {
            watchedList.computeIfAbsent(l, k -> new ArrayList<>())
                    .forEach(w -> {
                        w.setStatus(Watcher.Status.SUCCESS);
                    });
        });
        // global Unit propagation
        globalUnitPropagation(backbone);
        this.watchedList.clear();
        return backbone;
    }


    private void globalUnitPropagation(final Set<Literal> backbone) {
        HashSet<Literal> unitPropagatedLiterals = new HashSet<>(backbone);
        do {
            backbone.addAll(unitPropagatedLiterals);
            HashSet<Literal> temp = new HashSet<>(unitPropagatedLiterals);
            unitPropagatedLiterals.clear();
            temp.stream()
                    .map(Literal::getComplementary)
                    .forEach(l -> {
                        this.watchedList.computeIfAbsent(l, k -> new ArrayList<>())
                                .stream()
                                .filter(watcher -> watcher.getStatus() != Watcher.Status.SUCCESS)
                                .forEach(watcher -> {
                                    // check if there is any unassigned literal
                                    Integer unAssignedIndex = watcher.hasNextUnAssignedLiteral(backbone, unitPropagatedLiterals);
                                    if (unAssignedIndex == null) {
                                        // not found unassigned literal, then the other literal must be unit
                                        Literal other = watcher.getOtherWatchedLiteral(l);
                                        unitPropagatedLiterals.add(other);
                                        this.watchedList.computeIfAbsent(other, k -> new ArrayList<>()).forEach(w -> w.setStatus(Watcher.Status.SUCCESS));
                                    } else if (unAssignedIndex == -1) {
                                        watcher.setStatus(Watcher.Status.SUCCESS);
                                    } else {
                                        watcher.moveWatchedIndex(l, unAssignedIndex);
                                        watchedList.computeIfAbsent(watcher.getLiteral(unAssignedIndex), k -> new ArrayList<>()).add(watcher);
                                    }
                                });
                    });
        } while (!unitPropagatedLiterals.isEmpty());
    }


    public Set<Literal> getUnitLiterals(Model model, Set<Literal> unitPropagatedLitrals) {
        Implicant implicant = new Implicant(unitPropagatedLitrals);
        model.getLiterals().stream()
                .filter(l -> !unitPropagatedLitrals.contains(l))
                .collect(Collectors.toList())
                .forEach(l -> {
                    //remove -l from watched list in all clauses
                    unwatchAndPropagate(l.getComplementary(), implicant, model);
                });
        return implicant.getLiterals();
    }

    public Set<Literal> getUnitLiterals(Set<Literal> unitPropagatedLitrals) {
        Implicant implicant = new Implicant(unitPropagatedLitrals);
        getModel().getLiterals().stream()
                .filter(l -> !unitPropagatedLitrals.contains(l))
                .collect(Collectors.toList())
                .forEach(l -> {
                    //remove -l from watched list in all clauses
                    unwatchAndPropagate(l.getComplementary(), implicant, getModel());
                });
        implicant.getLiterals().removeAll(unitPropagatedLitrals);
        return implicant.getLiterals();
    }

//    public void getBackboneSuperSet(Set<Literal> upLiterals, Set<Literal> unitLiterals) {
//        this.watchedList.clear();
//        Implicant backbone = new Implicant();
//        Model model = getModel();
//        initWatchesAndSubset(backbone, model);
//        upLiterals = new HashSet<>(backbone.getLiterals());
//
//        model.getLiterals().stream()
//                .filter(l -> !backbone.contains(l))
//                .collect(Collectors.toList())
//                .forEach(l -> {
//                    //remove -l from watched list in all clauses
//                    unwatchAndPropagate(l.getComplementary(), backbone, model);
//                });
//        Debug.println(false, "After first UWAP", backbone);
//        unitLiterals = new HashSet<>(backbone.getLiterals());
//        unitLiterals.removeAll(upLiterals);
//    }


    /**
     * Deeply Search current node, if the node has children, recursively search to the leaf,
     * alone the path to leaf, put the node's value into the temporary implicant.
     * Finally, if the implicant is minimal, add the subsets of prime implicant.
     *
     * @param node        Current node.
     * @param subsets     A list contains all subsets of prime implicant.
     * @param tImplicants A temporary implicant object.
     */
    private void dfs(Node node, List<Implicant> subsets, Implicant tImplicants) {
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
//            Implicant instance = (Implicant) tImplicants.clone();
            Implicant instance = new Implicant(tImplicants);
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
            this.defaultModel = ModelFactory.getModel(this.engine, this.clauseSet);
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
}
