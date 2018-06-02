package masterthesis.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public void initGlobalWatcher(final HashSet<Literal> backbone, final Map<Literal, ArrayList<Watcher>> watchedList) {
        if (clause.isEmpyt()) return;
        if (clause.size() > 1) {
            watchedIndex1 = 0;
            watchedIndex2 = 1;
            watchedList.computeIfAbsent(clause.get(0), k -> new ArrayList<>()).add(this);
            watchedList.computeIfAbsent(clause.get(1), k -> new ArrayList<>()).add(this);
        } else {
            watchedIndex1 = 0;
            backbone.add(clause.get(0));
            this.status = Status.SUCCESS;
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

    /**
     * @param backbone               A subset of backbone
     * @param unitPropagatedLiterals The unit propagated literals
     * @return -1: found unit literal. null: not found unassigned literals. otherwise: found unassigned literal
     */
    public Integer hasNextUnAssignedLiteral(final Set<Literal> backbone, final Set<Literal> unitPropagatedLiterals) {
        Integer index = null;
        for (int from = watchedIndex1 > watchedIndex2 ? watchedIndex1 + 1 : watchedIndex2 + 1; from < this.clause.size(); from++) {
            Literal current = this.clause.get(from);

            if (backbone.contains(current) || unitPropagatedLiterals.contains(current)) {
                index = -1;
                break;
            } else if (backbone.contains(current.getComplementary()) || unitPropagatedLiterals.contains(current.getComplementary())) {

            } else {
                index = from;
                break;
            }
        }
        return index;
    }

    public Literal getOtherWatchedLiteral(Literal l) {
        int otherIndex = this.clause.getLiterals().indexOf(l) == watchedIndex1 ? watchedIndex2 : watchedIndex1;
        return this.clause.get(otherIndex);
    }

    /**
     * move the watched index from literal l to literal next
     *
     * @param l    the literal that currently being watched.
     * @param next The literal that will be watched
     */
    public void moveWatchedIndex(Literal l, Literal next) {
        int rIndex = this.clause.getLiterals().indexOf(next);
        int lIndex = this.clause.getLiterals().indexOf(l);
        if (lIndex == watchedIndex1) {
            watchedIndex1 = rIndex;
        } else {
            watchedIndex2 = rIndex;
        }
    }

    /**
     * The watcher gives up to watch literal l again, and watches the new literal in position index.
     *
     * @param l         the literal that currently being watched.
     * @param nextIndex The literal that will be watched
     */
    public void moveWatchedIndex(Literal l, int nextIndex) {
        int lIndex = this.clause.getLiterals().indexOf(l);
        if (lIndex == watchedIndex1) {
            watchedIndex1 = nextIndex;
        } else {
            watchedIndex2 = nextIndex;
        }
    }

    public Literal getLiteral(int index) {
        return this.clause.get(index);
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
