import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    int data;
    ArrayList<int[]> children;
    Node parent;
    int distance;

    Node(int data) {
        this.data = data;
        this.children = new ArrayList<>();
        distance = Integer.MAX_VALUE;
        parent = null;
    }
    Node(int data, ArrayList<int[]> children) {
        this.data = data;
        this.children = children;
        parent = null;
    }

    public void addChild(int node, int weight) {
        int[] childAndWeight = new int[2];
        childAndWeight[0] = node;
        childAndWeight[1] = weight;
        children.add(childAndWeight);
    }
}
