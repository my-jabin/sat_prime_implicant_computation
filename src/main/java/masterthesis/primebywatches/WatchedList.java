//package masterthesis.primebywatches;
//
//import com.sun.org.apache.xpath.internal.SourceTree;
//import masterthesis.base.*;
//import masterthesis.utils.Debug;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class WatchedList {
//
//    private Map<Literal, ArrayList<Watcher>> list;
//    private ApplicationContext ac;
//
//    public WatchedList(ApplicationContext ac) {
//        this.ac = ac;
//        this.list = new HashMap<>();
//    }
//
//    public void initWatchesAndSubset(final ClauseSet cs, final Implicant primeImplicant, final Model model) throws CloneNotSupportedException {
//        if(model.isEmpty()){
//            System.out.println("Model doesn't exist, watched List cannot be initialized");
//            return;
//        }
//        cs.getClauses().stream().forEach( c -> {
//            final Watcher watcher = new Watcher(c);
//            watcher.initWatchedIndex(primeImplicant,this, ac);
//        });
//
//        // foreach l in PI do
//        //  foreach w in w(l) do
//        //      set watcher status to be success
//        primeImplicant.getLiterals().stream().forEach( l -> {
//            this.get(l).stream().forEach( w -> {
//                w.setStatus(Watcher.Status.SUCCESS);
//            });
//        });
//
//        // prime Unit propagation
//        Debug.println(false,"First of all, add unit literals to PI", primeImplicant);
//        primeUnitPropagation(cs,primeImplicant,model);
//    }
//
//    private void primeUnitPropagation(final ClauseSet cs, final Implicant primeImplicant, final Model model) throws CloneNotSupportedException {
//        Implicant uppi = (Implicant) primeImplicant.clone();
//        do{
//            uppi.getLiterals().forEach(l -> this.get(l).clear());
//            primeImplicant.addLiterals(uppi.getLiterals());
//            Implicant tempPI = (Implicant) uppi.clone();
//            uppi.clear();
//            tempPI.getLiterals().forEach(l -> {
//                this.ac.unwatchAndPropagate(l.getComplementary(),cs,uppi,model,this);
//            });
//        }while ( !uppi.isEmpty());
//    }
//
//    public Map<Literal, ArrayList<Watcher>> getWatchedList(){
//        return this.list;
//    }
//
//    public ArrayList<Watcher> get(Literal key){
//        ArrayList<Watcher> value = this.list.get(key);
//        if(value == null){
//            value = new ArrayList<>();
//            list.put(key,value);
//        }
//        return value;
//    }
//
//
//    public ArrayList<Watcher> get(Integer key){
//        return get(this.ac.getLiteral(key));
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        this.list.entrySet().stream().forEach( entry -> {
//            sb.append("\nLiteral: " + entry.getKey() + "\n============================\n");
//            entry.getValue().forEach( w ->{
//                sb.append(w.toString()).append("\n");
//            });
//        });
//        return sb.toString();
//    }
//}
