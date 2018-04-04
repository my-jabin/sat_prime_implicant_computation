package masterthesis.base;

import masterthesis.utils.Debug;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Problem {

    public static Set<Implicant> primeImplicantCover(String fileName) {
        Solver solver = new Solver(fileName);
        if (!solver.sat()) return null;
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
            Debug.println(true, pi.toPrettyString());
            ClauseSet newCS = new ClauseSet(currentCS);
            //add blocking clause
            newCS.addClause(pi.toClause().reverse());
            Debug.println(false, "New added blocking clause:", pi.toClause().reverse().toPrettyString());
            solver.setClauseSet(newCS);
            blockingPI.add(pi);
        }

        HashSet<Implicant> piCovers = new HashSet<>();
        solver.setClauseSet(originalCS);
        for (Implicant implicant : blockingPI) {
            // remove rotatable literals in the implicant to get a new prime implicant.
            piCovers.add(solver.getPrimeImplicant(implicant));
        }
        return piCovers;
    }
}
