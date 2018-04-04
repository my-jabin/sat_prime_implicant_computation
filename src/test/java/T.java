
import masterthesis.base.*;
import masterthesis.utils.Debug;
import masterthesis.utils.Sat4jTool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T {
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
        Assert.assertTrue(model.contains(ac.getLiteral(17)));
        Assert.assertTrue(model.contains(ac.getLiteral(18)));
        Assert.assertTrue(model.contains(ac.getLiteral(-19)));
        Assert.assertTrue(model.contains(ac.getLiteral(-2)));
        Assert.assertTrue(model.contains(ac.getLiteral(-20)));
        Assert.assertTrue(model.contains(ac.getLiteral(-3)));
        Assert.assertTrue(model.contains(ac.getLiteral(-4)));
        Assert.assertTrue(model.contains(ac.getLiteral(-5)));
        Assert.assertTrue(model.contains(ac.getLiteral(-6)));
        Assert.assertTrue(model.contains(ac.getLiteral(-7)));
        Assert.assertTrue(model.contains(ac.getLiteral(-8)));
        Assert.assertTrue(model.contains(ac.getLiteral(-9)));

        Assert.assertTrue(solver.sat());
        Implicant primeImplicant = solver.getPrimeImplicant();
        Assert.assertEquals(11, primeImplicant.size());
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-16)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-3)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-10)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-20)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(18)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-9)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-6)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(17)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-19)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-8)));
        Assert.assertTrue(primeImplicant.contains(ac.getLiteral(-15)));
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
        ClauseSet clauseSet = solver.getClauseSet();
        Assert.assertTrue(testRemoveOneLiteralFromPI(clauseSet, primeImplicant));
    }

    @Test
    /*
      Test if the prime implicant computed by my watched-literals program
      is equals to the prime implicant computed by sat4j library
     */
    public void testPiEquality() throws IOException {
        List<String> fileNames;
        Path start = Paths.get(SATPATH);
        int maxDepth = 2;
        try (Stream<Path> stream = Files.walk(start, maxDepth)) {
            fileNames = stream
                    .map(String::valueOf)
                    .filter(path -> path.endsWith(".cnf"))
                    .collect(Collectors.toList());
        }

        for (String name : fileNames) {
            this.ac.reset();
            Solver solver = new Solver(name);
            solver.setEngine(SolverEngine.SAT4J);

            Assert.assertTrue(solver.sat());
            Implicant primeImplicant = solver.getPrimeImplicant();
            Implicant piFromSat4j = Sat4jTool.primeImplicant(solver.getClauseSet());
            Debug.println(true, name, primeImplicant, piFromSat4j);
            Assert.assertEquals(piFromSat4j, primeImplicant);
        }

    }

    @Test
    public void testPrimeImplicantUnderModel() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula07.cnf";
        Solver solver = new Solver(fileNameTest);
        Model original = solver.getModel();
        Model custom = ModelFactory.getModel(SolverEngine.EMPTY);
        custom.addLiterals(new int[]{1, 2, 3, 4, 5});
        Assert.assertNotEquals(original, custom);
        Implicant originalPI = solver.getPrimeImplicant();
        Implicant customPI = solver.getPrimeImplicant(custom);
        Debug.println(false, originalPI);
        Debug.println(false, customPI);

        Assert.assertNotEquals(originalPI, customPI);
    }

    @Test
    public void testAllPrimeImplicantUnderImplicant() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula05.cnf";
        Solver solver = new Solver(fileNameTest);
        Model model = ModelFactory.getModel(SolverEngine.EMPTY);
        model.addLiterals(new int[]{1, 2, 3, 4, -5, -6, 7, -8, -9, 10, -11});
        Assert.assertTrue(solver.sat());
        Implicant pi = solver.getPrimeImplicant(model);
        Debug.println(true, pi.toPrettyString());

        Assert.assertTrue(pi.containsAll(Util.toLiteralsList(new int[]{1, 2, 3, 4, -5, -6, 7, -8, -11})));

        List<Implicant> allPi = solver.getAllPrimeImplicants(model);
        Assert.assertEquals(1, allPi.size());
        Assert.assertTrue(allPi.get(0).containsAll(Util.toLiteralsList(new int[]{1, 2, 3, 4, -5, -6, 7, -8, -11})));

    }

    @Test
    public void testPrimeImplicantUnderImplicant5() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula05.cnf";
        Solver solver = new Solver(fileNameTest);
        Implicant implicant = new Implicant();
//        implicant.addLiterals(new int[]{1,2,3,4,-5,-6,7,8,-10,-11});
        implicant.addLiterals(new int[]{1, 2, 3, 4, -5, 6, -7, -8, -9, 10, 11});
        Implicant pi = solver.getPrimeImplicant(implicant);
        Debug.println(true, pi.toPrettyString());

    }

    @Test
    public void testPrimeImplicantUnderImplicant7() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula07.cnf";
        Solver solver = new Solver(fileNameTest);

        Implicant[] implicants = new Implicant[5];

        implicants[0] = new Implicant();
        implicants[0].addLiterals(new int[]{-1, 2, -3, 4, -5});
        Implicant pi0 = solver.getPrimeImplicant(implicants[0]);
        Assert.assertEquals(2, pi0.size());
        Assert.assertTrue(pi0.contains(ac.getLiteral(2)));
        Assert.assertTrue(pi0.contains(ac.getLiteral(4)));


        implicants[1] = new Implicant();
        implicants[1].addLiterals(new int[]{-1, 2, -3, -4, 5});
        Implicant pi1 = solver.getPrimeImplicant(implicants[1]);
        Assert.assertEquals(2, pi1.size());
        Assert.assertTrue(pi1.contains(ac.getLiteral(2)));
        Assert.assertTrue(pi1.contains(ac.getLiteral(5)));


        implicants[2] = new Implicant();
        implicants[2].addLiterals(new int[]{-1, -2, 3, -4, 5});
        Implicant pi2 = solver.getPrimeImplicant(implicants[2]);
        Assert.assertEquals(2, pi2.size());
        Assert.assertTrue(pi2.contains(ac.getLiteral(3)));
        Assert.assertTrue(pi2.contains(ac.getLiteral(5)));


        implicants[3] = new Implicant();
        implicants[3].addLiterals(new int[]{-1, -2, 3, 4, -5});
        Implicant pi3 = solver.getPrimeImplicant(implicants[3]);
        Assert.assertEquals(2, pi3.size());
        Assert.assertTrue(pi3.contains(ac.getLiteral(3)));
        Assert.assertTrue(pi3.contains(ac.getLiteral(4)));


        implicants[4] = new Implicant();
        implicants[4].addLiterals(new int[]{1, 2, 3, -4, -5});
        Implicant pi4 = solver.getPrimeImplicant(implicants[4]);
        Assert.assertEquals(1, pi4.size());
        Assert.assertTrue(pi4.contains(ac.getLiteral(1)));
    }

    @Test
    public void testPrimeImplicantCover7() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula07.cnf";
        Set<Implicant> implicantSet = Problem.primeImplicantCover(fileNameTest);
        Assert.assertNotNull(implicantSet);

        ArrayList<Implicant> implicants = new ArrayList<>(implicantSet);
        Assert.assertEquals(5, implicants.size());

        Implicant[] implicantArray = new Implicant[5];
        implicantArray[0] = new Implicant(new int[]{1});
        implicantArray[1] = new Implicant(new int[]{2, 4});
        implicantArray[2] = new Implicant(new int[]{2, 5});
        implicantArray[3] = new Implicant(new int[]{3, 4});
        implicantArray[4] = new Implicant(new int[]{3, 5});

        for (int i = 0; i < implicantArray.length; i++) {
            Assert.assertTrue(implicants.contains(implicantArray[i]));
        }
        // TODO: makes more test case of the prime implicant cover
    }

    @Test
    public void testPrimeImplicantCover5() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula05.cnf";
        Set<Implicant> implicantSet = Problem.primeImplicantCover(fileNameTest);
        Assert.assertNotNull(implicantSet);

        for (Implicant i : implicantSet) {
            Debug.println(true, i.toPrettyString());
        }

//        ArrayList<Implicant> implicants = new ArrayList<>(implicantSet);
//        Assert.assertEquals(5,implicants.size());
//
//        Implicant[] implicantArray = new Implicant[5];
//        implicantArray[0] = new Implicant(new int[]{1});
//        implicantArray[1] = new Implicant(new int[]{2,4});
//        implicantArray[2] = new Implicant(new int[]{2,5});
//        implicantArray[3] = new Implicant(new int[]{3,4});
//        implicantArray[4] = new Implicant(new int[]{3,5});
//
//        for(int i = 0 ; i < implicantArray.length;i++){
//            Assert.assertTrue(implicants.contains( implicantArray[i]));
//        }
    }


    @Test
    public void testBuildTree() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula08.cnf";
        Solver solver = new Solver(fileNameTest);
        LinkedHashMap<Literal, Integer> weight = solver.weighting(solver.getClauseSet());
        Assert.assertEquals(2, (long) weight.get(ac.getLiteral(1)));
        Assert.assertEquals(3, (long) weight.get(ac.getLiteral(2)));
        Assert.assertEquals(2, (long) weight.get(ac.getLiteral(3)));
        Assert.assertEquals(2, (long) weight.get(ac.getLiteral(4)));
        Assert.assertEquals(3, (long) weight.get(ac.getLiteral(5)));
        Assert.assertEquals(2, (long) weight.get(ac.getLiteral(6)));

        ArrayList<Node> trees = new ArrayList<>();

        for (Map.Entry<Literal, Integer> entry : weight.entrySet()) {
            Literal l = ac.getLiteral(entry.getKey());
            // root is the entry point of a tree
            Node root = new Node(l);
            solver.buildTree(solver.getClauseSet(), root, l);
            trees.add(root);
        }

        Assert.assertEquals(6, trees.size());

        Node root2 = trees.get(0);
        Assert.assertEquals(ac.getLiteral(2), root2.getValue());
        Assert.assertEquals(1, root2.childrenSize());
        Assert.assertEquals(ac.getLiteral(5), root2.getChildren().get(0).getValue());

        Node root5 = trees.get(1);
        Assert.assertEquals(ac.getLiteral(5), root5.getValue());
        Assert.assertEquals(1, root5.childrenSize());
        Assert.assertEquals(ac.getLiteral(2), root5.getChildren().get(0).getValue());

        Node root1 = trees.get(2);
        Assert.assertEquals(ac.getLiteral(1), root1.getValue());
        Assert.assertEquals(5, root1.childrenSize());

        Assert.assertEquals(ac.getLiteral(2), root1.getChildren().get(0).getValue());
        Assert.assertEquals(ac.getLiteral(3), root1.getChildren().get(1).getValue());
        Assert.assertEquals(ac.getLiteral(4), root1.getChildren().get(2).getValue());
        Assert.assertEquals(ac.getLiteral(5), root1.getChildren().get(3).getValue());
        Assert.assertEquals(ac.getLiteral(6), root1.getChildren().get(4).getValue());

        Assert.assertEquals(ac.getLiteral(3), root1.getChildren().get(0).getChildren().get(0).getValue());
        Assert.assertEquals(ac.getLiteral(5), root1.getChildren().get(0).getChildren().get(1).getValue());

        Assert.assertEquals(ac.getLiteral(2), root1.getChildren().get(1).getChildren().get(0).getValue());

        Assert.assertEquals(ac.getLiteral(6), root1.getChildren().get(2).getChildren().get(0).getValue());

        Assert.assertEquals(ac.getLiteral(2), root1.getChildren().get(3).getChildren().get(0).getValue());

        Assert.assertEquals(ac.getLiteral(4), root1.getChildren().get(4).getChildren().get(0).getValue());


        Node root3 = trees.get(3);
        Assert.assertEquals(ac.getLiteral(3), root3.getValue());
        Assert.assertEquals(1, root3.childrenSize());
        Assert.assertEquals(ac.getLiteral(2), root3.getChildren().get(0).getValue());

        Node root4 = trees.get(4);
        Assert.assertEquals(ac.getLiteral(4), root4.getValue());
        Assert.assertEquals(4, root4.childrenSize());
        Assert.assertEquals(ac.getLiteral(1), root4.getChildren().get(0).getValue());
        Assert.assertEquals(ac.getLiteral(2), root4.getChildren().get(1).getValue());
        Assert.assertEquals(ac.getLiteral(5), root4.getChildren().get(2).getValue());
        Assert.assertEquals(ac.getLiteral(6), root4.getChildren().get(3).getValue());

        Assert.assertEquals(ac.getLiteral(6), root4.getChildren().get(0).getChildren().get(0).getValue());
        Assert.assertEquals(ac.getLiteral(5), root4.getChildren().get(1).getChildren().get(0).getValue());
        Assert.assertEquals(ac.getLiteral(2), root4.getChildren().get(2).getChildren().get(0).getValue());
        Assert.assertEquals(ac.getLiteral(1), root4.getChildren().get(3).getChildren().get(0).getValue());


        Node root6 = trees.get(5);
        Assert.assertEquals(ac.getLiteral(6), root6.getValue());
        Assert.assertEquals(4, root6.childrenSize());
        Assert.assertEquals(ac.getLiteral(1), root6.getChildren().get(0).getValue());
        Assert.assertEquals(ac.getLiteral(2), root6.getChildren().get(1).getValue());
        Assert.assertEquals(ac.getLiteral(4), root6.getChildren().get(2).getValue());
        Assert.assertEquals(ac.getLiteral(5), root6.getChildren().get(3).getValue());

        Assert.assertEquals(ac.getLiteral(4), root6.getChildren().get(0).getChildren().get(0).getValue());
        Assert.assertEquals(ac.getLiteral(5), root6.getChildren().get(1).getChildren().get(0).getValue());
        Assert.assertEquals(ac.getLiteral(1), root6.getChildren().get(2).getChildren().get(0).getValue());
        Assert.assertEquals(ac.getLiteral(2), root6.getChildren().get(3).getChildren().get(0).getValue());
    }

    @Test
    public void testAllPrimeImplicants() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula05.cnf";
        Solver solver = new Solver(fileNameTest);
        List<Implicant> allImplicants = solver.getAllPrimeImplicants();
        Debug.println(true, allImplicants);
        Assert.assertEquals(3, allImplicants.size());

        Implicant implicant1 = allImplicants.get(0);
        Implicant implicant2 = allImplicants.get(1);
        Implicant implicant3 = allImplicants.get(2);

        int[] value1 = {1, 3, 2, 4, 9, -8};
        Assert.assertTrue(implicant1.containsAll(Util.toLiteralsList(value1)));

        int[] value2 = {1, 3, 2, 4, 9, -6, -10};
        Assert.assertTrue(implicant2.containsAll(Util.toLiteralsList(value2)));

        int[] value3 = {1, 3, 2, 4, 9, -7, -10};
        Assert.assertTrue(implicant3.containsAll(Util.toLiteralsList(value3)));

    }

    @Test
    public void testAllPrimeImplicants2() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula07.cnf";
        Solver solver = new Solver(fileNameTest);
        Model model = ModelFactory.getModel(SolverEngine.EMPTY);
        model.addLiterals(new int[]{1, 2, 3, 4, 5});
        List<Implicant> allImplicants = solver.getAllPrimeImplicants(model);
        Debug.println(true, allImplicants);

        Assert.assertEquals(5, allImplicants.size());

        Implicant implicant1 = allImplicants.get(0);
        Implicant implicant2 = allImplicants.get(1);
        Implicant implicant3 = allImplicants.get(2);
        Implicant implicant4 = allImplicants.get(3);
        Implicant implicant5 = allImplicants.get(4);

        int[] value1 = {1};
        Assert.assertTrue(implicant1.containsAll(Util.toLiteralsList(value1)));

        int[] value2 = {2, 4};
        Assert.assertTrue(implicant2.containsAll(Util.toLiteralsList(value2)));

        int[] value3 = {2, 5};
        Assert.assertTrue(implicant3.containsAll(Util.toLiteralsList(value3)));

        int[] value4 = {3, 4};
        Assert.assertTrue(implicant4.containsAll(Util.toLiteralsList(value4)));

        int[] value5 = {3, 5};
        Assert.assertTrue(implicant5.containsAll(Util.toLiteralsList(value5)));

    }

    @Test
    public void testAllPrimeImplicants3() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula04.cnf";
        Solver solver = new Solver(fileNameTest);
        Debug.println(true, solver.getModel());
        List<Implicant> allImplicants = solver.getAllPrimeImplicants();
        Debug.println(true, allImplicants);

        Assert.assertEquals(1, allImplicants.size());

        Implicant implicant1 = allImplicants.get(0);
        int[] value1 = {-6, -3, -1, -2, -5, -9, -4};
        Assert.assertTrue(implicant1.containsAll(Util.toLiteralsList(value1)));
    }

    @Test
    public void testAllPrimeImplicants4() {
        this.ac.reset();
        final String fileNameTest = SATPATH + "formula03.cnf";
        Solver solver = new Solver(fileNameTest);
        Debug.println(true, solver.getModel());
        List<Implicant> allImplicants = solver.getAllPrimeImplicants();
        Debug.println(true, allImplicants);

        Assert.assertEquals(1, allImplicants.size());
        Implicant implicant1 = allImplicants.get(0);
        int[] value1 = {1, 2, 4};
        Assert.assertTrue(implicant1.containsAll(Util.toLiteralsList(value1)));
    }
//
//    @Test
//    public void testSatWithAssignment() {
//        this.ac.reset();
//        final String fileNameTest = SATPATH + "formula07.cnf";
//        Solver solver = new Solver(fileNameTest);
//        int[][] assumptions = new int[5][];
//        assumptions[0] = new int[]{1, -2, -3, -4, -5};
//        assumptions[1] = new int[]{-1, 2, -3, 4, -5};
//        assumptions[2] = new int[]{-1, 2, -3, -4, 5};
//        assumptions[3] = new int[]{-1, -2, 3, 4, -5};
//        assumptions[4] = new int[]{-1, -2, 3, -4, 5};
//        Assert.assertTrue(solver.sat(Util.toLiteralsList(assumptions[0])));
//        Assert.assertTrue(solver.sat(Util.toLiteralsList(assumptions[1])));
//        Assert.assertTrue(solver.sat(Util.toLiteralsList(assumptions[2])));
//        Assert.assertTrue(solver.sat(Util.toLiteralsList(assumptions[3])));
//        Assert.assertTrue(solver.sat(Util.toLiteralsList(assumptions[4])));
//    }


    /**
     * Test if each clause contains at least one pi literal.
     *
     * @param cs Clause set
     * @param pi prime implicant
     * @return True if it contains, otherwise false
     */
    private boolean containAtLeastOnePiLiteral(ClauseSet cs, Implicant pi) {
        if (cs.isEmpty() || pi.isEmpty()) return false;

        for (int j = 0; j < cs.getClauses().size(); j++) {
            Clause clause = cs.get(j);
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
}
