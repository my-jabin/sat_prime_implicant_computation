package masterthesis.base;

import masterthesis.utils.Debug;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Problem {

    public static Set<Implicant> primeImplicantCover(String fileName) {
        Solver solver = new Solver(fileName);
        if (!solver.sat()) {
            throw new IllegalStateException("The formula is UNSAT");
        }
        ClauseSet originalCS = solver.getClauseSet();
        Debug.println(false, "Original clause set:", originalCS);
        // new generated PI from new clause set that contains blocking clause.
        ArrayList<Implicant> blockingPI = new ArrayList<>();
        Implicant pi;
        ClauseSet currentCS;
        while (solver.sat()) {
            // those pi contains normally rotatable literals except the pi from original clause set.
            pi = solver.getPrimeImplicant();
            currentCS = solver.getClauseSet();
            Debug.println(false, pi.toPrettyString());
            ClauseSet newCS = new ClauseSet(currentCS);
            //add blocking clause
            newCS.addClause(pi.toClause().reverse());
            Debug.println(false, "New added blocking clause:", pi.toClause().reverse().toPrettyString());
            solver.setClauseSet(newCS);

            // solver.addClause(pi.toClause().reverse());

            Debug.println(false, "new clause set size =" + newCS.size());
            blockingPI.add(pi);
        }
        Debug.println(false, "Being with computing all prime implicants");

        HashSet<Implicant> piCovers = new HashSet<>();
        solver.setClauseSet(originalCS);
        for (Implicant implicant : blockingPI) {
            // remove rotatable literals in the implicant to get a new prime implicant.
            piCovers.add(solver.getPrimeImplicant(implicant));
        }
        return piCovers;
    }

    /**
     * Compute backbone by intersecting prime implicant cover
     *
     * @param allPI
     * @return
     */
    public static ArrayList<Literal> backbones(Set<Implicant> allPI) {
        ArrayList<Implicant> all = new ArrayList<>(allPI);
        ArrayList<Literal> backbone = new ArrayList<>();
        for (Literal l : all.get(0).getLiterals()) {
            backbone.add(l);
        }
        for (Implicant pi : all) {
            backbone.retainAll(pi.getLiterals());
        }
        Debug.println(false, backbone);
        return backbone;
    }

    /**
     * Compute backbone by removing rotatable literal from a model to build a superset of backbone.
     * Iteratively refine the superset until the backbone is empty or formula is UNSAT
     *
     * @param fileName
     * @return
     */
    public static Set<Literal> backbonePrimeInter(String fileName) {
        Solver solver = new Solver(fileName);
        if (!solver.sat()) {
            throw new IllegalStateException("The formula is UNSAT");
        }
        // init back is the model
        Set<Literal> backbone = solver.getModel().getLiterals();
        ClauseSet originalCS = solver.getClauseSet();
        ClauseSet currentCS = null;
        HashSet<Clause> potentials = new HashSet<>();
        while (solver.sat() && !backbone.isEmpty()) {
            Set<Literal> potentialBackbone = solver.getBackboneCandidate();
            currentCS = solver.getClauseSet();
            ClauseSet newCS = new ClauseSet(currentCS);
            Clause c = new Clause(potentialBackbone).reverse();
            Debug.printPrettyString(false, potentialBackbone);
            newCS.addClause(c);
            solver.setClauseSet(newCS);
            Debug.println(false, newCS.size());
            backbone.retainAll(potentialBackbone);
            Debug.printPrettyString(true, backbone);
        }
        return backbone;
    }

    public static Set<Literal> backbonePrimeInterImprov(String fileName) {
        Solver solver = new Solver(fileName);
        if (!solver.sat()) {
            throw new IllegalStateException("The formula is UNSAT");
        }
        Set<Literal> backbone = solver.getGlobalUnitPropagatedLiterals();
        Debug.printPrettyString(true, backbone);
        Set<Literal> backboneSuperSet = solver.getBackboneCandidate();
        Debug.printPrettyString(true, backboneSuperSet);
        HashSet<Literal> candidate = new HashSet<>(backboneSuperSet);
        candidate.removeAll(backbone);
        ClauseSet originalCS = solver.getClauseSet();
        for (Literal l : candidate) {
            ClauseSet newCS = new ClauseSet(originalCS);
            newCS.addClause(new Clause(l.getComplementary()));
            solver.setClauseSet(newCS);
            if (!solver.sat()) {
                backbone.add(l);
            }
        }
        return backbone;
    }

    public static Set<Literal> backboneWithChunks(String fileName, int chunkSize) {
        Solver solver = new Solver(fileName);
        if (!solver.sat()) {
            throw new IllegalStateException("The formula is UNSAT");
        }
        Set<Literal> backbone = solver.getGlobalUnitPropagatedLiterals();
        Debug.printPrettyString(false, backbone);
        Set<Literal> backboneSuperSet = solver.getBackboneCandidate();
        Debug.printPrettyString(false, backboneSuperSet);

        ArrayList<Literal> candidate = new ArrayList<>(backboneSuperSet);
        candidate.removeAll(backbone);
        ClauseSet originalCS = solver.getClauseSet();

        while (!candidate.isEmpty()) {
            // chunk size = 1
            List<Literal> chunks = pickLiteralsWithChunk(candidate, chunkSize);

            ClauseSet newCS = new ClauseSet(originalCS);
            newCS.addClause(new Clause(chunks).reverse());
            solver.setClauseSet(newCS);
            if (!solver.sat()) {
                backbone.addAll(chunks);
                candidate.removeAll(chunks);
                // todo: add backbone literal to clause set
            } else {
                candidate.retainAll(solver.getModel().getLiterals());
            }

        }
        return backbone;
    }

    private static List<Literal> pickLiteralsWithChunk(List<Literal> collection, int chunkSize) {
        return collection.subList(0, Math.min(collection.size(), chunkSize));
    }

    public static Set<Literal> backboneWithKaiserKuechlin(String fileName) {
        Solver solver = new Solver(fileName);
        if (!solver.sat()) {
            throw new IllegalStateException("The formula is UNSAT");
        }
        Set<Literal> backbone = new HashSet<>();
        Model model = solver.getModel();
        ClauseSet originCS = solver.getClauseSet();

        for (Literal l : model.getLiterals()) {
            ClauseSet newCs = new ClauseSet(originCS);
            newCs.addClause(new Clause(l));
            solver.setClauseSet(newCs);
            if (!solver.sat()) {
                backbone.add(l);
            }
        }

        model.getLiterals().stream()
                .filter(l -> !backbone.contains(l))
                .forEach(literal -> {
                    ClauseSet newCs = new ClauseSet(originCS);
                    newCs.addClause(new Clause(literal).reverse());
                    solver.setClauseSet(newCs);
                    if (!solver.sat()) {
                        backbone.add(literal);
                    }
                });

        return backbone;
    }
}
