package masterthesis.main;

import masterthesis.base.*;
import masterthesis.utils.*;
import org.logicng.datastructures.Tristate;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class Main {

    public static final String path = "src/main/resources/sat/";
    public static final String fileNameTest = path + "formula07.cnf";
    public static final String filename = path + "D1119_M23.cnf";
    //public static final String fileName = path +"dp02s02.shuffled.cnf";


    public static void main(String[] args) throws CloneNotSupportedException {
        mainMethod();
//        testGenOneModelFrom4J();
        //testGenModelsFrom4J();
        //testPISat4j();

//        testProblem();
        //testAssignmentSat();

    }

    private static void testProblem(){
        Problem.primeImplicantCover(fileNameTest);
    }

    private static void testAssignmentSat(){

        Solver mySolver = new Solver(fileNameTest);
        ClauseSet cs = mySolver.getClauseSet();
        final FormulaFactory factory = new FormulaFactory();
        final SATSolver logicSolver = MiniSat.miniSat(factory);
        logicSolver.reset();

        final ArrayList<Literal> literals = new ArrayList<>();
        cs.getClauses().forEach(clause -> {
            literals.clear();
            clause.getLiterals().forEach(literal -> {
                String var = "v" + Math.abs(literal.getValue());
                literals.add(literal.getPolarity() ? factory.literal(var,true): factory.literal(var,false));
            });
            if(!literals.isEmpty()){
                logicSolver.add(factory.or(literals));
            }
        });

        List<Literal> assumption = new ArrayList<>();
        assumption.add(factory.literal("1",true));
        assumption.add(factory.literal("2",false));
        assumption.add(factory.literal("3",false));
        assumption.add(factory.literal("4",false));
        assumption.add(factory.literal("5",false));
        Tristate result = logicSolver.sat(assumption);
        Debug.println(true,result);

    }

    private static void mainMethod() {
        Solver solver = new Solver(fileNameTest);
        Model model = ModelFactory.getModel(SolverEngine.EMPTY);
        model.addLiterals(new int[]{2,-3,-4,5});
        if (solver.sat()) {
            Implicant pi = solver.getPrimeImplicant(model);
            Debug.println(true, pi);
        }

//        // Test:
//        boolean correct = Tester.containAtLeastOnePiLiteral(clauseSet,primeImplicant);
//        System.out.println("contain At Least One Pi Literal = "+correct);
//
//        boolean testReduceOneLiteral = Tester.testRemoveOneLiteralFromPI(clauseSet,primeImplicant);
//        Debug.println(true,"Testing with removing one literal from Pi = "+testReduceOneLiteral);
    }

//    private static void testGenOneModelFrom4J(){
//        final ApplicationContext ac = new ApplicationContext();
//        new DimacsFormatReader(fileNameTest).readCNF(ac);
//        //new DimacsFormatReader(fileName).readCNF(ac);
//
//        final ClauseSet clauseSet = new ClauseSet(ac);
//        final Model model = ModelFactory.getModel(ModelFactory.Engine.SAT4J,ac);
//        Debug.println(true,"One Model from Sat4j: ",model.toString());
//    }

//    private static void testGenModelsFrom4J(){
//        final ApplicationContext ac = new ApplicationContext();
//        new DimacsFormatReader(fileNameTest).readCNF(ac);
//        //new DimacsFormatReader(fileName).readCNF(ac);
//
//        final ClauseSet clauseSet = new ClauseSet(ac);
//        final List<Model> models = ModelFactory.getAllModels(ModelFactory.Engine.SAT4J,ac);
//        Debug.println(true,"All Models of "+fileNameTest+" from Sat4j: ");
//        for(Model m : models){
//            Debug.println(true,m);
//        }
//    }

//    private static void testPISat4j() throws CloneNotSupportedException {
//        final ApplicationContext ac = new ApplicationContext();
//        new DimacsFormatReader(fileNameTest).readCNF(ac);
//        //new DimacsFormatReader(fileName).readCNF(ac);
//
//        final ClauseSet clauseSet = new ClauseSet(ac);
//
//        final Model model4j = ModelFactory.getModel(ModelFactory.Engine.SAT4J,ac);
//        Debug.println(true,"One Model from Sat4j: ",model4j.toString());
//        final Implicant implicant = ImplicantFactory.getPrimeImplicant(ac);
//        Debug.println(true,"One Implicant from Sat4j: ",implicant.toString());
//        Debug.println(true,"\n");
//
//        int[] mLiterals = {1,2,3,4,-5,-6,7,-8,-9,-10,-11};
//        final Model model = ModelFactory.getModel(ModelFactory.Engine.EMPTY,ac);
//        model.addLiterals(mLiterals);
//        final Implicant primeImplicant = new Implicant(ac);
//
//        final WatchedList watchedList = new WatchedList(ac);
//        watchedList.initWatchesAndSubset(clauseSet,primeImplicant,model);
//        ac.primeByWatches(clauseSet, primeImplicant, model,watchedList );
//        Debug.println(true,primeImplicant.toStringPretty());
//        // Unit test if my pi is equals to the pi from sat4j
//    }
}
