import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Dijkstra {
    private static HashMap<Integer, Node> nodeMap = new HashMap<>();

    public static void main(String[] args) {
        //String fileName = args[0];
        String fileName = "data.txt";
        //int selectedNode = Integer.parseInt(args[1]);
        int selectedNode = 0;
        readFile(fileName);
        dijkstra(nodeMap, nodeMap.get(selectedNode));
    }

    public static void dijkstra(HashMap<Integer, Node> nodeHashMap, Node node) {
        for(int i = 0; i < nodeHashMap.size(); i++) {
            nodeHashMap.get(i).setDistance(Integer.MAX_VALUE);
        }
        node.setDistance(0);
        
    }

    /*public static void heapSort(int[] input) {
        // Build min-heap before performing heap sort.
        buildMinHeap(input);
        int temp = 0;
        for(int i = input.length - 1; i >= 1; i--) {
            temp = input[i];
            input[i] = input[0];
            input[0] = temp;
            minHeapify(input, 0, i);
        }
    }

    public static void buildMinHeap(int[] input) {
        // Building of the min-heap.
        for(int i = (int)Math.floor(input.length / 2.0) - 1; i >= 0; i--) {
            minHeapify(input, i, input.length);
        }
    }

    public static void minHeapify(int[] input, int i, int heapSize) {
        // Setting the child nodes in the min-heap.
        int leftChild = 2 * i;
        int rightChild = 2 * i + 1;
        int smallest = i;

        //Checking for input being smaller on left side
        if(leftChild < heapSize && input[leftChild] < input[smallest]) {
            smallest = leftChild;
        }

        //Checking for input being smaller on right side
        if(rightChild < heapSize && input[rightChild] < input[smallest]) {
            smallest = rightChild;
        }

        //Swapping here.
        if(smallest != i) {
            int temp = input[i];
            input[i] = input[smallest];
            input[smallest] = temp;
            minHeapify(input, smallest, heapSize);
        }
    }*/

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
