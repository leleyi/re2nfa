import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Nfa {

    public enum ANCHOR {
        NONE,
        START,
        END,
        BOTH
    }

    //this is ε
    public static final int EPSILON = -1;
    // the is cll
    public static final int CCL = -2;
    // the node have not out edge
    public static final int EMPTY = -3;

    public int st = 0;

    private static final int ASCII_COUNT = 127;

    // record the edge type, null or ε or cll
    private int edge;

    public int getEdge() {
        return edge;
    }

    public void setEdge(int type) {
        edge = type;
    }

    // save the char set
    public Set<Byte> inputSet;

    // to next state can be null
    public Nfa next;

    //to the other state
    public Nfa next2;

    // if start with ^ or end with $ or both
    private ANCHOR anchor;

    // num of node
    private int stateNum;

    // node if visit
    private boolean visited = false;

    public void setVisited() {
        visited = true;
    }

    public boolean isVisited() {
        return visited;
    }


    public Nfa() {
        inputSet = new HashSet<Byte>();
        clearState();
    }

    public void setStateNum(int num) {
        stateNum = num;
    }

    public int getStateNum() {
        return stateNum;
    }

    public void addToSet(Byte b) {
        inputSet.add(b);
    }

    public void setComplement() {

        Set<Byte> newSet = new HashSet<Byte>();
        for (byte b = 0; b < ASCII_COUNT; b++) {
            if (inputSet.contains(b) == false) {
                newSet.add(b);
            }
        }

        inputSet = null;
        inputSet = newSet;
    }


    public void setAnchor(ANCHOR anchor) {
        this.anchor = anchor;
    }

    public Nfa.ANCHOR getAnchor() {
        return this.anchor;
    }

    public void clearState() {
        inputSet.clear();
        next = next2 = null;
        anchor = ANCHOR.NONE;
        stateNum = -1;
    }

    public void cloneNfa(Nfa nfa) {
        inputSet.clear();
        Iterator<Byte> it = nfa.inputSet.iterator();
        while (it.hasNext()) {
            inputSet.add(it.next());
        }

        anchor = nfa.getAnchor();
        this.next = nfa.next;
        this.next2 = nfa.next2;
        this.edge = nfa.getEdge();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
