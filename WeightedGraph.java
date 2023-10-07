import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class WeightedGraph<V> {
    ArrayList<String> Cities = new ArrayList<>();
    //Adjacency List
    ArrayList<ArrayList<Integer>> Edges = new ArrayList<>();
    public WeightedGraph(String fileName){
        try {
            //file reader and buffered reader to get info from data.txt
            FileReader reader = new FileReader(fileName);
            BufferedReader buffer = new BufferedReader(reader);

            //Data file starts with number of edges so i take that info first
            int numberOfEdges = Integer.parseInt(buffer.readLine());
            //declare city arrayList and edge array

            String currentLine = buffer.readLine();


            //iterate through file until no lines left
            int vertexCount = 0, edgeCount = 0;
            while(currentLine != null){

                String[] tempTerms = currentLine.split("|");
                int originCity = 0, connectingCity = 0;

                //if atleast one city in the array
                if(vertexCount > 0){
                    for(int i = 0; i < Cities.size(); i++){
                        //origin city was already in array
                        if(tempTerms[0].equals(Cities.get(i))){
                            originCity = i;
                        } else if(tempTerms[1].equals(Cities.get(i))){
                            connectingCity = i;
                        }

                        //if both vertices already in ArrayList can stop searching
                        //and break out of the loop
                        if(originCity != 0 && connectingCity != 0){
                            break;
                        }
                    }

                    //Origin city was not in ArrayList
                    if(originCity == 0 ){
                        Cities.set(vertexCount, tempTerms[0]);
                        //giving origin city the last index and iterating
                        originCity = vertexCount;
                        vertexCount++;
                    }
                    //connecting city was not in the ArrayList
                    if (connectingCity == 0) {
                        Cities.set(vertexCount, tempTerms[1]);
                        //giving connecting city the last index and iterating
                        connectingCity = vertexCount;
                        vertexCount++;
                    }

                    //add edges of new cities
                    ArrayList<Integer> tempVertexArray = new ArrayList<>();
                    tempVertexArray.set(0,originCity);
                    tempVertexArray.set(1,connectingCity);
                    Edges.set(edgeCount, tempVertexArray);
                    edgeCount++;

                } else { // the city array is empty
                    //adding both cities to the arrayList
                    Cities.set(0, tempTerms[0]);
                    Cities.set(1, tempTerms[1]);
                    vertexCount += 2;

                    //add edges of new cities
                    ArrayList<Integer> tempVertexArray2 = new ArrayList<>();
                    tempVertexArray2.set(0,originCity);
                    tempVertexArray2.set(1,connectingCity);
                    Edges.set(edgeCount, tempVertexArray2);
                    edgeCount++;
                }

                //iterating to next edge in Data file
                currentLine = buffer.readLine();
            }

            buffer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    };

    //create graph and call adjacency list
    public WeightedGraph(V[] vertices, int[][] edges){
        for(int i = 0; i < vertices.length; i++){
            addVertex(vertices[i]);
        }
        createAdjacencyList(edges);
    }

    //add vertex to the graph, and create new edge ArrayList
    public void addVertex(String vertex) {
        if (!Cities.contains(vertex)) {
            Cities.add(vertex);
            Edges.add(new ArrayList<>());
        }
    }
    private void createAdjacencyList(int[][] edges){
        for(int i = 0; i < edges.length; i++){
            Edge temp = new Edge(edges[i][0], edges[i][1]);
            addEdge(temp);
        }
    }

    //when called check if edge already in the neighbors list, if not add relation
    public void addEdge(Edge e) {
        if (!Edges.get(e.u).contains(e)) {
            Edges.get(e.u).add(e);
        }
    }
    public void PalmerPrintGraph(){
        for (int u = 0; u < Edges.size(); u++) {
            System.out.print("City " + Cities.get(u) + ": ");
            for (Edge e: Edges.get(u)) {
                System.out.print(Cities.get(e.v) + ", ");
            }
            System.out.println();
        }
    }
}
