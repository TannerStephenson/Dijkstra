import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Dijkstra {
    private static HashMap<Integer, Node> nodeMap = new HashMap<>();

    public static void main(String[] args) {
        String fileName = args[0];
        int selectedNode = Integer.parseInt(args[1]);
        readFile(fileName);
        dijkstra(nodeMap, nodeMap.get(selectedNode));

        for(Node node: nodeMap.values()) {
            if(!node.equals(nodeMap.get(selectedNode))) {
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
        ArrayList<Node> reversedList = new ArrayList<>();
        for(Node node = endNode; node != null; node = node.parent) {
            reversedList.add(node);
        }
        for(int i = reversedList.size() - 1; i >= 0; i--) {
            path.append(reversedList.get(i).data);
            if(i != 0) {
                path.append(",");
            }
        }
        path.append(")");
        return path.toString();
    }

    public static void dijkstra(HashMap<Integer, Node> nodeHashMap, Node startNode) {
        Node[] minHeap = new Node[nodeHashMap.size()];
        int heapSize = 0;

        for(Node node: nodeHashMap.values()) {
            node.distance = 1234567890;
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
        for(Integer childId : u.children.keySet()) {
            Node v = nodeMap.get(childId);
            int weight = u.children.get(childId);

            if(v.distance > u.distance + weight) {
                v.distance = u.distance + weight;
                minHeapify(minHeap, 0, heapSize);
                v.parent = u;
            }
        }
    }

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
