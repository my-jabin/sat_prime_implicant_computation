package masterthesis.base;

import java.util.ArrayList;

public class ClauseSet{

    private ArrayList<Clause> clauses;
    private final ApplicationContext ac;

//    public ClauseSet(final ApplicationContext ac) {
//        this(ac,false);
//    }

    public ClauseSet(){
        clauses = new ArrayList<>();
        this.ac = ApplicationContext.getInstance();
    }
//    public ClauseSet(final ApplicationContext ac, boolean empty){
//        this.ac = ac;
//        clauses = new ArrayList<>();
//        if(!empty)
//            init();
//    }

    public void init(){
        if(ac.getCNFContent().isEmpty()){
            System.out.println("content is empty, cannot obtain clause set");
            clear();
            return;
        }

        if(this.isEmpty()){
            ac.getCNFContent().stream().forEach( line -> {
                final Clause clause = new Clause();
                line.stream().forEach(l ->{
                    clause.addLiteral( ac.getLiteral(l));
                });
                this.addClause(clause);
            });
        }
    }

    public void addClause(final Clause clause) {
        if (clause != null)
            this.clauses.add(clause);
    }

    public boolean removeClause(final Clause clause){
        return this.clauses.remove(clause);
    }

    public boolean isEmpty(){
        return this.clauses.isEmpty();
    }

    public void clear(){
        this.clauses.clear();
    }

    public ArrayList<Clause> getClauses() {
        return clauses;
    }

    public Clause get(int index){
        return this.clauses.get(index);
    }

}
