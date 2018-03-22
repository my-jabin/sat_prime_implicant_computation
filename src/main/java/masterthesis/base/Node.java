package masterthesis.base;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private List<Node> children = new ArrayList<>();

    private Node parent = null;

    private Literal value = null;

    public Node(Literal value) {
        this.value = value;
    }

    public void addChild(Node node) {
        node.parent = this;
        children.add(node);
    }

    public void addChild(Literal data) {
        Node child = new Node(data);
        addChild(child);
    }

    public int childrenSize(){
        return children.size();
    }

    public List<Node> getChildren() {
        return children;
    }

    public Literal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Node{" + value + " }";
    }
}
