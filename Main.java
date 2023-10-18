import java.util.Vector;

public class Main {
    public static void main(String[] args){

        String fileName = "Data.txt";
        WeightedGraph Graph = new WeightedGraph(fileName);

        Graph.printAdjacencyList();
        Graph.shortestPath(1, 3);

    }
}
