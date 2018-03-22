package masterthesis.main;

import masterthesis.base.ApplicationContext;
import masterthesis.utils.Debug;
import masterthesis.utils.DimacsFormatReader;
import org.logicng.datastructures.Assignment;
import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;
import org.logicng.transformations.dnf.DNFFactorization;

import java.util.ArrayList;
import java.util.List;


// TODO: testing class, should delete later
public class Test {
    public static final String path = "src/main/resources/sat/";
    public static final String fileNameTest = path + "formula07.cnf";


    public static void main(String[] args) {
//        final DNFFactorization dnfFactorization = new DNFFactorization();
//
//        final ApplicationContext ac = new ApplicationContext();
//        new DimacsFormatReader(fileNameTest).readCNF(ac);
//
//        final FormulaFactory factory = new FormulaFactory();
//        final SATSolver solver = MiniSat.miniSat(factory);
//        solver.reset();
//        final ArrayList<Literal> literals = new ArrayList<>();
//
//        List<Formula> clauses = new ArrayList<>();
//
//        ac.getCNFContent().stream().forEach( line -> {
////            literals.clear();
////            line.stream().forEach( l -> {
////                String var = "v" + Math.abs(l);
////                literals.add(l > 0 ? factory.literal(var, true) : factory.literal(var, false));
////            });
////            if (!literals.isEmpty()) {
////                solver.add(factory.or(literals));
////            }
//            literals.clear();
//            line.stream().forEach(l -> {
//                String var = String.valueOf(Math.abs(l));
//                literals.add(l > 0 ? factory.literal(var, true) : factory.literal(var, false));
//            });
//            Formula clause = factory.clause(literals);
//            clauses.add(clause);
//
//        });
//        // the formula in cnf representation
//        Formula cnf = factory.cnf(clauses);
//        // the formula in dnf representation
//        Formula nnf = cnf.transform(dnfFactorization);
//
//        Debug.println(true,cnf.toString());
//        Debug.println(true,nnf.toString());


//        Assignment assignment = new Assignment();
//        assignment.addLiteral(factory.literal("1",true));
//        assignment.addLiteral(factory.literal("2",false));
//        assignment.addLiteral(factory.literal("3",false));
//        assignment.addLiteral(factory.literal("4",false));
//        //assignment.addLiteral(factory.literal("5",true));
//        Debug.println(true, assignment.toString()) ;
//        boolean result = cnf.evaluate(assignment);
//        Debug.println(true,"evaluate assignment: "+ result);


//        List<Literal> assumption = new ArrayList<>();
//        //assumption.add(factory.literal("1",true));
//        assumption.add(factory.literal("2",true));
//        //assumption.add(factory.literal("3",false));
//        //assumption.add(factory.literal("4",false));
//        //assumption.add(factory.literal("5",false));
//        solver.reset();
//        solver.add(cnf);
//        Tristate result = solver.sat(assumption);
//        Debug.println(true,result);

//        // containAtLeastOnePiLiteral if two formula are equivalent: WRONG
//        Formula equivalent = factory.equivalence(cnf,nnf);
//        Debug.println(true,equivalent.toString());
//        solver.reset();
//        solver.add(equivalent);
//        Debug.println(true,solver.sat() == Tristate.TRUE);
    }
}
