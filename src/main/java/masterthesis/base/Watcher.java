package masterthesis.base;

import java.util.ArrayList;
import java.util.Map;

public class Watcher {
    private Clause clause;
    private int watchedIndex1;
    private int watchedIndex2;
    private Status status;

    public enum Status {SUCCESS, DEFAULT}

    public Watcher(Clause clause) {
        this.clause = clause;
        status = Status.DEFAULT;
    }

    public void initWatchedIndex(final Implicant primeImplicant, final Map<Literal, ArrayList<Watcher>> watchedList, Implicant implicant) {
        if (clause.isEmpyt()) return;

        if (clause.size() > 1) {
            watchedIndex1 = selectWatchedindex(implicant, 0);
            if (watchedIndex1 == -1) {
                throw new IllegalArgumentException("Cannot initialize watched literal ");
            }
            watchedList.computeIfAbsent(clause.get(watchedIndex1), k -> new ArrayList<>()).add(this);

            watchedIndex2 = selectWatchedindex(implicant, watchedIndex1 + 1);
            // if not found the second satisfied literal, we need to add the first to pi
            if (watchedIndex2 == -1) {
                primeImplicant.addLiteral(clause.get(watchedIndex1));
                clause.setState(Clause.STATE.PRIME);
            } else {
                watchedList.computeIfAbsent(clause.get(watchedIndex2), k -> new ArrayList<>()).add(this);
            }
        } else {
            watchedIndex1 = 0;
            primeImplicant.addLiteral(clause.get(0));
            clause.setState(Clause.STATE.PRIME);
        }
    }

    public int selectWatchedindex(Implicant implicant, int index) {
        int result = -1;
        for (int from = index; from < this.clause.size(); from++) {
            if (implicant.containsVariable(this.clause.get(from).getValue())) {
                result = from;
                break;
            }
        }
        return result;
    }

    public Literal getNextSatisfiedLiteral(Implicant implicant) {
        Literal rotatable = null;
        for (int from = watchedIndex1 > watchedIndex2 ? watchedIndex1 + 1 : watchedIndex2 + 1; from < this.clause.size(); from++) {
            if (implicant.contains(this.clause.get(from))) {
                rotatable = this.clause.get(from);
                break;
            }
        }
        return rotatable;
    }

    public Literal getOtherWatchedLiteral(Literal l) {
        int otherIndex = this.clause.getLiterals().indexOf(l) == watchedIndex1 ? watchedIndex2 : watchedIndex1;
        return this.clause.get(otherIndex);
    }

    // set the watched index of l to be index of rotatable literal index.
    public void moveWatchedIndex(Literal l, Literal rotatable) {
        int rIndex = this.clause.getLiterals().indexOf(rotatable);
        int lIndex = this.clause.getLiterals().indexOf(l);
        if (lIndex == watchedIndex1) {
            watchedIndex1 = rIndex;
        } else {
            watchedIndex2 = rIndex;
        }
    }

    @Override
    public String toString() {
        return "(" + this.clause.toString() + ")  index1:" + this.watchedIndex1 + ", index2:" + this.watchedIndex2 + "  " + this.status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Clause getClause() {
        return clause;
    }

}
