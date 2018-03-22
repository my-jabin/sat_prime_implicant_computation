package masterthesis.base;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

    private List<Node<T>> children = new ArrayList<>();

    private Node<T> parent = null;

    private T data = null;

    public Node(T data) {
        this.data = data;
    }

    public void addChild(Node<T> node) {
        node.parent = this;
        children.add(node);
    }

    public void addChild(T data) {
        Node<T> child = new Node<T>(data);
        addChild(child);
    }

    public int childrenSize(){
        return children.size();
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Node{" + data + " }";
    }
}
