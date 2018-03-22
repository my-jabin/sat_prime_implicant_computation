package masterthesis.allPrimeImplicant;

import masterthesis.base.*;
import masterthesis.utils.Debug;
import masterthesis.utils.DimacsFormatReader;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final String path = "src/main/resources/sat/";
    public static final String fileNameTest = path + "formula08.cnf";

    public static void main(String[] args) {
//        final ApplicationContext ac = new ApplicationContext();
//        new DimacsFormatReader(fileNameTest).readCNF(ac);
//        //new DimacsFormatReader(fileName).readCNF(ac);
//
//        final ClauseSet clauseSet = new ClauseSet(ac);
//        //final Model model = new LoginNGModel(ac);
//        //int i[] = {1, 2, 3, 4, 5};
//        //final Model model = Tool.buildModel(ac, i);
//
//
//        LinkedHashMap<Literal, Integer> weight = weight(clauseSet);
//        printWeight(weight);
//
//
//        ArrayList<Implicant> primeImplicants = new ArrayList<>();
//        ArrayList<Node> trees = new ArrayList<>();
//
//        Iterator iterator = weight.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<Literal, Integer> entry = (Map.Entry<Literal, Integer>) iterator.next();
//            //Implicant pi = new Implicant(ac);
//            Literal l = ac.getLiteral(entry.getKey());
//            //pi.addLiteral(l);
//            // root is the entry point of a tree
//            Node<Literal> root = new Node<>(l);
//            recursion(ac, clauseSet, l,root);
//
//            //primeImplicants.add(pi);
//            trees.add(root);
//        }
//
//        Debug.println(true,trees.size());

//        LinkedHashMap<Literal, Integer> highestWeight = getHighestWeight(clauseSet);
//        printWeight(highestWeight);

    }

    /**
     * From clause set remove clauses that contains literal l.
     * Calculate again the weight of each literal, then add the literals with highest weight into pi.
     * recursively process.
     *
     * @param ac Application
     * @param cs Clause set
     * @param l  Literal already added to pi
     */
    public static void recursion(ApplicationContext ac, ClauseSet cs,Literal l, Node<Literal> node) {
//        ClauseSet reducedCS = new ClauseSet(ac,true);
//        for (Clause clause : cs.getClauses()) {
//            if(!clause.contains(l))
//                reducedCS.addClause(clause);
//        }
//
//        if (reducedCS.isEmpty()){
//            //primeImplicants.add(pi);
//            return;
//        }
//
//        List<Literal> highestWeightLiterals = getHighestWeightLiterals(reducedCS);
//        for(Literal li : highestWeightLiterals){
//            Node<Literal> child = new Node<>(li);
//            node.addChild(child);
//            recursion(ac,reducedCS,li,child);
//        }
//        Iterator iterator = highestWeight.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<Literal, Integer> entry = (Map.Entry<Literal, Integer>) iterator.next();
//            Literal literal = ac.getLiteral(entry.getValue());
//            pi.addLiteral(literal);
//            recursion(ac, reducedCS, literal, pi);
//        }

    }

    public static List<Literal> getHighestWeightLiterals(ClauseSet cs) {
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

    public static LinkedHashMap<Literal, Integer> weight(ClauseSet cs) {
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

        List<Map.Entry<Literal, Integer>> sortedList = weightMap.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toList());

        LinkedHashMap<Literal, Integer> result = new LinkedHashMap<>();

        for (Map.Entry<Literal, Integer> entry : sortedList) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static void printWeight(Map<Literal, Integer> weight) {
        Set set = weight.entrySet();
        Iterator iterator = set.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey()).append("(").append(entry.getValue()).append("), ");
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        Debug.println(true, sb.toString());
    }


}
