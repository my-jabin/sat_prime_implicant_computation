package masterthesis.base;

import masterthesis.utils.Debug;

import java.util.ArrayList;

public class Problem {

    public static void primeImplicantCover(String fileName) {
        Solver solver = new Solver(fileName);
        if (!solver.sat()) return;
        ClauseSet originalCS = solver.getClauseSet();

        ArrayList<Implicant> piList = new ArrayList<>();
        Implicant pi;
        ClauseSet currentCS;
        while (solver.sat()) {
            pi = solver.getPrimeImplicant();
            currentCS = solver.getClauseSet();
            Debug.println(true, pi.toPrettyString());
            ClauseSet newCS = new ClauseSet(currentCS);
            newCS.addClause(pi.toClause().reverse());
            solver.setClauseSet(newCS);

            // TODO： How to remove rotatable literals from pi?
            piList.add(pi);
        }

        Debug.println(true,solver.getClauseSet());



    }
}
