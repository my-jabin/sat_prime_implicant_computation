package masterthesis.utils;

import masterthesis.base.*;
import org.logicng.datastructures.Assignment;
import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tester {

    // to containAtLeastOnePiLiteral if each clause contains at least one pi literal.
    public static boolean containAtLeastOnePiLiteral(ClauseSet cs, Implicant pi){
        if(cs.isEmpty() || pi.isEmpty()) return false;

        for(int j = 0 ; j < cs.getClauses().size(); j++){
            Clause clause = cs.get(j);
            // does each clause contains at least one pi literal.
            boolean r = false;
            for(int i = 0 ; i < clause.getLiterals().size();i++){
                if(pi.contains(clause.get(i))) {
                    r = true;
                    break;
                }
            }
            if(!r){
                return false;
            }
        }
        return true;
    }

    public static boolean testEquivalenceWithLogicNG(ClauseSet cs, Implicant pi){
        //final DNFFactorization dnfFactorization = new DNFFactorization();
        final FormulaFactory factory = new FormulaFactory();
        final SATSolver solver = MiniSat.miniSat(factory);
        solver.reset();
        final ArrayList<Literal> literals = new ArrayList<>();
        List<Formula> clauses = new ArrayList<>();

        cs.getClauses().forEach(clause ->{
            literals.clear();
            clause.getLiterals().forEach(literal -> {
                literals.add(factory.literal(String.valueOf(literal),literal.getPolarity()));
            });
            Formula c = factory.clause(literals);
            clauses.add(c);
        });

        Formula originalFormula = factory.cnf(clauses);
        Debug.println(true,originalFormula);

        List<Formula> formulars = new ArrayList<>();
        pi.getLiterals().stream().forEach(literal -> {
            formulars.add(factory.literal(String.valueOf(literal),literal.getPolarity()));
        });
        Formula PiToDnf = factory.and(formulars);
        Debug.println(true,PiToDnf);

        Formula equivalent = factory.equivalence(originalFormula,PiToDnf);
        Debug.println(true,equivalent);

        solver.add(equivalent);
        Tristate result = solver.sat();
        Debug.println( true,solver.model());;
        Debug.println(true,solver.sat() == Tristate.TRUE);
        return result == Tristate.TRUE;
    }



    public static boolean testReduceOneLiteral(ClauseSet cs, Implicant pi){
        final FormulaFactory factory = new FormulaFactory();
        final SATSolver solver = MiniSat.miniSat(factory);
        solver.reset();
        final ArrayList<Literal> literals = new ArrayList<>();
        List<Formula> clauses = new ArrayList<>();

        cs.getClauses().forEach(clause ->{
            literals.clear();
            clause.getLiterals().forEach(literal -> {
                literals.add(factory.literal(String.valueOf(literal),literal.getPolarity()));
            });
            Formula c = factory.clause(literals);
            clauses.add(c);
        });

        Formula originalFormula = factory.cnf(clauses);


        //ArrayList clonePI = (ArrayList) pi.getLiterals().clone();
        for(int i = 0 ; i < pi.getLiterals().size();i++){
            literals.clear();
            ArrayList<masterthesis.base.Literal> clone = (ArrayList) pi.getLiterals().clone();
            clone.remove(pi.getLiterals().get(i));
            clone.stream().forEach(l ->{
                literals.add(factory.literal(String.valueOf(l),l.getPolarity()));
            });

            Assignment assignment = new Assignment(literals);
            boolean result = originalFormula.evaluate(assignment);
            if(result){
                return false;
            }
        }

        return true;
    }

}
