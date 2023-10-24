import java.util.Vector;

public class Main {
    public static void main(String[] args){

        String fileName = "Data.txt";
        WeightedGraph Graph = new WeightedGraph(fileName);

        Graph.printAdjacencyList();
        //System.out.println(Graph.neighbors.get(1));
        Graph.shortestPath(5, 9);

    }
}
