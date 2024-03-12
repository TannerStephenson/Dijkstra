import java.util.ArrayList;

public class Node {
    int data;
    ArrayList<Node> children;
    String color;
    Node parent;
    int time;

    Node(int data) {
        this.data = data;
        this.color = "white";
        this.children = new ArrayList<>();
        parent = null;
        time = 0;
    }
    Node(int data, ArrayList<Node> children) {
        this.data = data;
        this.color = "white";
        this.children = children;
        parent = null;
        time = 0;
    }
}
