import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Dijkstra {
    // Structure to hold the Nodes.
    private static HashMap<Integer, Node> nodeMap = new HashMap<>();

    public static void main(String[] args) {
        // Take in the cmd input.
        String fileName = args[0];
        int selectedNode = Integer.parseInt(args[1]);
        // Parse the file.
        readFile(fileName);
        // Run dijkstra.
        dijkstra(nodeMap, nodeMap.get(selectedNode));

        // Loop through the hashmap to get the final distance values.
        for(Node node: nodeMap.values()) {
            if(!node.equals(nodeMap.get(selectedNode))) {
                // Check if the node was unreachable.
                if(node.distance == 1234567890) {
                    System.out.println("[" + node.data + "]unreachable");
                } else {
                    String path = reconstructPath(node);
                    System.out.println("[" + node.data + "]" + "shortest path:" + path + " shortest distance:" + node.distance);
                }
            }
        }
    }

    private static String reconstructPath(Node endNode) {
        StringBuilder path = new StringBuilder();
        path.append("(");
        // Get the list, so we can reverse it when appending.
        ArrayList<Node> reversedList = new ArrayList<>();
        for(Node node = endNode; node != null; node = node.parent) {
            reversedList.add(node);
        }
        for(int i = reversedList.size() - 1; i >= 0; i--) {
            path.append(reversedList.get(i).data);
            // Don't append a comma at the end.
            if(i != 0) {
                path.append(",");
            }
        }
        path.append(")");
        return path.toString();
    }

    public static void dijkstra(HashMap<Integer, Node> nodeHashMap, Node startNode) {
        // Initialize a min heap.
        Node[] minHeap = new Node[nodeHashMap.size()];
        int heapSize = 0;

        // Set up the initial values.
        for(Node node: nodeHashMap.values()) {
            node.distance = 1234567890;
            node.parent = null;
            minHeap[heapSize++] = node;
        }

        // Set node we are checking distance to zero.
        startNode.distance = 0;

        buildMinHeap(minHeap, heapSize);

        while (heapSize != 0) {
            // Take the min from the minHeap.
            Node u = extractMin(minHeap, heapSize);
            // Relax edges.
            relaxEdge(u, minHeap, heapSize);
            // Decrease the heapsize.
            heapSize--;
        }
    }

    private static void relaxEdge(Node u, Node[] minHeap, int heapSize) {
        // Iterate through the nodes children.
        for(Integer childId : u.children.keySet()) {
            // Get the node we want to look at.
            Node v = nodeMap.get(childId);
            // Get the weight.
            int weight = u.children.get(childId);

            if(v.distance > u.distance + weight) {
                v.distance = u.distance + weight;
                minHeapify(minHeap, 0, heapSize);
                v.parent = u;
            }
        }
    }

    // Repurposed from TopoSort assignment.
    private static void buildMinHeap(Node[] heap, int heapSize) {
        for(int i = (int)Math.floor(heap.length / 2.0) - 1; i >= 0; i--) {
            minHeapify(heap, i, heap.length);
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


    // Method to add their children to the node.
    public static void nextHopNeighbor(Node node, int neighbor, int weight) {
        node.addChild(neighbor, weight);
        nodeMap.put(node.data, node);
    }

    public static void readFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner fileScan = new Scanner(file);

            while(fileScan.hasNext()) {
                String line = fileScan.nextLine();
                String[] pieces = line.split(":");
                // Parse out the first node in the list.
                int nodeData = Integer.parseInt(pieces[0]);
                // Create the node.
                Node node = new Node(nodeData);
                if(pieces.length > 1) {
                    if(pieces[1].contains(";")) {
                        // Get the children.
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
                    // They have no children.
                    nodeMap.put(node.data, node);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found!");
        }
    }
}
