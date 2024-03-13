import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Dijkstra {
    private static HashMap<Integer, Node> nodeMap = new HashMap<>();

    public static void main(String[] args) {
        //String fileName = args[0];
        String fileName = "data.txt";
        //int selectedNode = Integer.parseInt(args[1]);
        int selectedNode = 3;
        readFile(fileName);
        dijkstra(nodeMap, nodeMap.get(selectedNode));

        for(Node node: nodeMap.values()) {
            System.out.println(node.distance);
        }
    }

    public static void dijkstra(HashMap<Integer, Node> nodeHashMap, Node startNode) {
        Node[] minHeap = new Node[nodeHashMap.size()];
        int heapSize = 0;

        for(Node node: nodeHashMap.values()) {
            node.distance = Integer.MAX_VALUE;
            node.parent = null;
            minHeap[heapSize++] = node;
        }

        startNode.distance = 0;

        buildMinHeap(minHeap, heapSize);

        while (heapSize != 0) {
            Node u = extractMin(minHeap, heapSize);
            relaxEdge(u, minHeap, heapSize);
            heapSize--;
        }
    }

    private static void relaxEdge(Node u, Node[] minHeap, int heapSize) {
        for(Integer childId : u.children.keySet()) { // Assuming children is a Map<Integer, Integer> with node ID and weight
            Node v = nodeMap.get(childId);
            int weight = u.children.get(childId);

            if(v.distance > u.distance + weight) {
                v.distance = u.distance + weight;
                v.parent = u;
                decreaseKey(minHeap, heapSize, v, v.distance); // Make sure this method correctly updates the heap
            }
        }
    }

    private static void buildMinHeap(Node[] heap, int heapSize) {
        for (int i = heapSize / 2 - 1; i >= 0; i--) {
            minHeapify(heap, i, heapSize);
        }
    }

    private static void minHeapify(Node[] heap, int i, int heapSize) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smallest = i;

        if (left < heapSize && heap[left].distance < heap[smallest].distance) {
            smallest = left;
        }
        if (right < heapSize && heap[right].distance < heap[smallest].distance) {
            smallest = right;
        }

        if (smallest != i) {
            Node temp = heap[i];
            heap[i] = heap[smallest];
            heap[smallest] = temp;

            // Update indices due to swap
            heap[i].index = i;
            heap[smallest].index = smallest;

            minHeapify(heap, smallest, heapSize);
        }
    }

    private static Node extractMin(Node[] heap, int heapSize) {
        Node min = heap[0];
        heap[0] = heap[heapSize - 1];
        minHeapify(heap, 0, heapSize - 1);
        return min;
    }

    private static void decreaseKey(Node[] heap, int heapSize, Node node, int newDistance) {
        node.distance = newDistance;
        int i = node.index;
        while (i > 0 && heap[(i - 1) / 2].distance > heap[i].distance) {
            // Swap the current node with its parent
            Node temp = heap[i];
            heap[i] = heap[(i - 1) / 2];
            heap[(i - 1) / 2] = temp;

            // Update indices due to swap
            heap[i].index = i;
            heap[(i - 1) / 2].index = (i - 1) / 2;

            i = (i - 1) / 2; // Move to the parent index
        }
    }


    public static void nextHopNeighbor(Node node, int neighbor, int weight) {
        node.addChild(neighbor, weight);
        nodeMap.put(node.data, node);

    }

    public static void readFile(String fileName) {
        //ArrayList<Node> nodeList = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner fileScan = new Scanner(file);

            while(fileScan.hasNext()) {
                String line = fileScan.nextLine();
                String[] pieces = line.split(":");
                int nodeData = Integer.parseInt(pieces[0]);
                Node node = new Node(nodeData);
                if(pieces.length > 1) {
                    if(pieces[1].contains(";")) {
                        String[] nextHopNeighbors = pieces[1].split(";");
                        for(String singleHopNeighbor: nextHopNeighbors) {
                            String[] neighbor = singleHopNeighbor.split(",");
                            nextHopNeighbor(node, Integer.parseInt(neighbor[0]), Integer.parseInt(neighbor[1]));

                        }
                    } else {
                        String[] neighbor = pieces[1].split(",");
                        nextHopNeighbor(node, Integer.parseInt(neighbor[0]), Integer.parseInt(neighbor[1]));
                    }
                } else {
                    nodeMap.put(node.data, node);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found!");
        }
    }
}
