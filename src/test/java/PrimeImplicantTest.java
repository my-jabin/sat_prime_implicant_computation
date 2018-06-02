import masterthesis.base.*;
import masterthesis.utils.Debug;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PrimeImplicantTest {

    private static final String SATPATH = "src/main/resources/sat/";
    private static final String UNSATPATH = "src/main/resources/unsat/";
    private ApplicationContext ac;

    @Before
    public void init() {
        this.ac = ApplicationContext.getInstance();
    }

    @Test
    public void testCase1() {
        this.ac.reset();
        final String fileNameTest = UNSATPATH + "formula01.cnf";
        Solver solver = new Solver(fileNameTest);
        Assert.assertTrue(!solver.sat());
    }

    @Test
    public void testCase2() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula02.cnf";
        Solver solver = new Solver(fileNameTest);
        final Model model = solver.getModel();

        Assert.assertTrue(!model.isEmpty());
        Assert.assertTrue(model.contains(ac.getLiteral(1)));
        Assert.assertTrue(model.contains(ac.getLiteral(-2)));
        Assert.assertTrue(model.contains(ac.getLiteral(-3)));
        Assert.assertTrue(model.contains(ac.getLiteral(-4)));
        Assert.assertTrue(model.contains(ac.getLiteral(5)));

        Assert.assertTrue(solver.sat());
        Implicant primeImplicant = solver.getPrimeImplicant();
        Assert.assertEquals(5, primeImplicant.size());
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(1)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-2)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(5)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-4)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-3)));
    }

    @Test
    public void testCase3() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula03.cnf";
        Solver solver = new Solver(fileNameTest);
        final Model model = solver.getModel();

        Assert.assertEquals(4, model.size());
        Assert.assertTrue(model.contains(ac.getLiteral(1)));
        Assert.assertTrue(model.contains(ac.getLiteral(2)));
        Assert.assertTrue(model.contains(ac.getLiteral(-3)));
        Assert.assertTrue(model.contains(ac.getLiteral(4)));


        Assert.assertTrue(solver.sat());
        Implicant primeImplicant = solver.getPrimeImplicant();
        Assert.assertEquals(3, primeImplicant.size());
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(1)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(2)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(4)));
    }

    @Test
    public void testCase4() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula04.cnf";
        Solver solver = new Solver(fileNameTest);
        final Model model = solver.getModel();

        Assert.assertEquals(9, model.size());
        Assert.assertTrue(model.contains(ac.getLiteral(-1)));
        Assert.assertTrue(model.contains(ac.getLiteral(-2)));
        Assert.assertTrue(model.contains(ac.getLiteral(-3)));
        Assert.assertTrue(model.contains(ac.getLiteral(-4)));
        Assert.assertTrue(model.contains(ac.getLiteral(-5)));
        Assert.assertTrue(model.contains(ac.getLiteral(-6)));
        Assert.assertTrue(model.contains(ac.getLiteral(-7)));
        Assert.assertTrue(model.contains(ac.getLiteral(-8)));
        Assert.assertTrue(model.contains(ac.getLiteral(-9)));

        Assert.assertTrue(solver.sat());
        Implicant primeImplicant = solver.getPrimeImplicant();
        Assert.assertEquals(7, primeImplicant.size());
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-6)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-3)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-1)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-2)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-5)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-9)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-6)));
    }

    @Test
    public void testCase5() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula05.cnf";
        Solver solver = new Solver(fileNameTest);
        final Model model = solver.getModel();
        Assert.assertEquals(11, model.size());
        Assert.assertTrue(model.contains(ac.getLiteral(1)));
        Assert.assertTrue(model.contains(ac.getLiteral(2)));
        Assert.assertTrue(model.contains(ac.getLiteral(3)));
        Assert.assertTrue(model.contains(ac.getLiteral(4)));
        Assert.assertTrue(model.contains(ac.getLiteral(-5)));
        Assert.assertTrue(model.contains(ac.getLiteral(-6)));
        Assert.assertTrue(model.contains(ac.getLiteral(-7)));
        Assert.assertTrue(model.contains(ac.getLiteral(-8)));
        Assert.assertTrue(model.contains(ac.getLiteral(9)));
        Assert.assertTrue(model.contains(ac.getLiteral(-10)));
        Assert.assertTrue(model.contains(ac.getLiteral(-11)));

        Assert.assertTrue(solver.sat());
        Implicant primeImplicant = solver.getPrimeImplicant();
        Assert.assertEquals(6, primeImplicant.size());
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(1)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(3)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(2)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(4)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(9)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-8)));
    }

    @Test
    public void testCase6() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula06.cnf";
        Solver solver = new Solver(fileNameTest);
        final Model model = solver.getModel();
        Assert.assertEquals(20, model.size());
        Assert.assertTrue(model.contains(ac.getLiteral(-1)));
        Assert.assertTrue(model.contains(ac.getLiteral(-10)));
        Assert.assertTrue(model.contains(ac.getLiteral(-11)));
        Assert.assertTrue(model.contains(ac.getLiteral(-12)));
        Assert.assertTrue(model.contains(ac.getLiteral(-13)));
        Assert.assertTrue(model.contains(ac.getLiteral(-14)));
        Assert.assertTrue(model.contains(ac.getLiteral(-15)));
        Assert.assertTrue(model.contains(ac.getLiteral(-16)));
//        Assert.assertTrue(model.contains(ac.getLiteral(17)));
        Assert.assertTrue(model.contains(ac.getLiteral(-17)));
//        Assert.assertTrue(model.contains(ac.getLiteral(18)));
        Assert.assertTrue(model.contains(ac.getLiteral(-18)));
        Assert.assertTrue(model.contains(ac.getLiteral(-19)));
        Assert.assertTrue(model.contains(ac.getLiteral(-2)));
        Assert.assertTrue(model.contains(ac.getLiteral(-20)));
        Assert.assertTrue(model.contains(ac.getLiteral(-3)));
        Assert.assertTrue(model.contains(ac.getLiteral(-4)));
        Assert.assertTrue(model.contains(ac.getLiteral(-5)));
//        Assert.assertTrue(model.contains(ac.getLiteral(-6)));
        Assert.assertTrue(model.contains(ac.getLiteral(6)));
        Assert.assertTrue(model.contains(ac.getLiteral(-7)));
//        Assert.assertTrue(model.contains(ac.getLiteral(-8)));
        Assert.assertTrue(model.contains(ac.getLiteral(8)));
//        Assert.assertTrue(model.contains(ac.getLiteral(-9)));
        Assert.assertTrue(model.contains(ac.getLiteral(9)));

        Assert.assertTrue(solver.sat());
        Implicant primeImplicant = solver.getPrimeImplicant();
        Assert.assertEquals(11, primeImplicant.size());
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-3)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(6)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(8)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(9)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-10)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-12)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-14)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-16)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-18)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-19)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-20)));
    }

    @Test
    public void testCase7() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula07.cnf";
        int[] literals = new int[]{1, 2, 4, 5};
        Solver solver = new Solver(fileNameTest);
        solver.setEngine(SolverEngine.EMPTY);

        final Model model = solver.getModel();
        model.addLiterals(literals);
        Assert.assertEquals(4, model.size());
        Assert.assertTrue(model.contains(ac.getLiteral(1)));
        Assert.assertTrue(model.contains(ac.getLiteral(2)));
        Assert.assertTrue(model.contains(ac.getLiteral(4)));
        Assert.assertTrue(model.contains(ac.getLiteral(5)));

        Assert.assertTrue(solver.sat());
        Implicant primeImplicant = solver.getPrimeImplicant();
        Assert.assertEquals(2, primeImplicant.size());
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(2)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(5)));
    }

    @Test
    public void testCase7And1() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula07.cnf";

        Solver solver = new Solver(fileNameTest);
        Assert.assertTrue(solver.sat());
        Implicant primeImplicant = solver.getPrimeImplicant();
        Assert.assertEquals(2, primeImplicant.size());
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(2)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(4)));
    }

    @Test
    public void testCase8() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "D1119_M23.cnf";
        Solver solver = new Solver(fileNameTest);
        Assert.assertTrue(solver.sat());
        Implicant primeImplicant = solver.getPrimeImplicant();
//        ClauseSet clauseSet = solver.getClauseSet();
//        Assert.assertTrue(testRemoveOneLiteralFromPI(clauseSet, primeImplicant));
        Debug.printPrettyString(true, primeImplicant.getLiterals());
        // time taken 1s
        // size = 1387
    }


    @Test
    public void testCase9() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "dp02s02.shuffled.cnf";
        Solver solver = new Solver(fileNameTest);
        Assert.assertTrue(solver.sat());
        Implicant primeImplicant = solver.getPrimeImplicant();
//        ClauseSet clauseSet = solver.getClauseSet();
//        Assert.assertTrue(testRemoveOneLiteralFromPI(clauseSet, primeImplicant));
        Debug.printPrettyString(true, primeImplicant.getLiterals());
    }


    /**
     * Test if the given implicant is an prime implicant. We iteratively remove one literal from the given
     * implicant, test if each clause contains at least one pi literal. If it returns true, then the implicant is not a
     * prime implicant, otherwise the implicant is a prime implicant
     *
     * @param cs Clause set
     * @param pi Implicant
     * @return
     */
    private boolean testRemoveOneLiteralFromPI(ClauseSet cs, Implicant pi) {
        Implicant clone;
        for (Literal literal : pi.getLiterals()) {
            clone = new Implicant(pi);
            clone.removeLiteral(literal);
            if (containAtLeastOnePiLiteral(cs, clone)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Test if each clause contains at least one pi literal.
     *
     * @param cs Clause set
     * @param pi prime implicant
     * @return True if it contains, otherwise false
     */
    private boolean containAtLeastOnePiLiteral(ClauseSet cs, Implicant pi) {
        if (cs.isEmpty() || pi.isEmpty()) return false;

//        for (int j = 0; j < cs.getClauses().size(); j++) {
        for (Clause clause : cs.getClauses()) {
//            Clause clause = cs.get(j);
            // does each clause contains at least one pi literal.
            boolean r = false;
            for (int i = 0; i < clause.getLiterals().size(); i++) {
                if (pi.contains(clause.get(i))) {
                    r = true;
                    break;
                }
            }
            if (!r) {
                return false;
            }
        }
        return true;
    }
}
