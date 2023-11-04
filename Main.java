import java.util.Vector;

public class Main {
    public static void main(String[] args){

        //uses Data.Txt for the graph, and Requested.txt for the input file.
        RequestedFlights graph = new RequestedFlights();
        graph.returnFlights();
        
        
        //for as many lines in requested flights file call Graph.returnPath and with the two cities
        //gra.printAdjacencyList();
        //System.out.println(Graph.neighbors.get(1));
        //Graph.returnPath(5, 9, 0);

    }
}
