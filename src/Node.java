import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    int data;
    HashMap<Integer, Integer> children;
    Node parent;
    int distance;

    Node(int data) {
        this.data = data;
        this.children = new HashMap<>();
        distance = Integer.MAX_VALUE;
        parent = null;
    }
    Node(int data, HashMap<Integer, Integer> children) {
        this.data = data;
        this.children = children;
        parent = null;
    }

    public void addChild(int node, int weight) {
        children.put(node, weight);
    }

    public void setDistance(int dis) {
        this.distance = dis;
    }
}
