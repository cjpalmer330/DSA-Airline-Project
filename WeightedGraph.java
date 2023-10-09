import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class WeightedGraph<V> {
    //Simply an array list to store the cities' names as strings so we as users don't need to memorize index numbers
    ArrayList<String> Cities = new ArrayList<>();

    //Adjacency List
    /*
    Column is each city as the origin
    each row is that cities' arraylist of road connections as Edges
    ex. Cities
        {
        Dallas => {[Dallas,Austin],[Dallas,Houston],[Dallas, Oklahoma City]}
        Houston => {[Houston, Dallas],[Houston, San Antonio]}
        }
     */
    ArrayList<ArrayList<Edge>> neighbors = new ArrayList<>();
    public WeightedGraph(String fileName){
        try {
            //file reader and buffered reader to get info from data.txt
            FileReader reader = new FileReader(fileName);
            BufferedReader buffer = new BufferedReader(reader);

            //Data file starts with number of edges so i take that info first
            int numberOfEdges = Integer.parseInt(buffer.readLine());

            String currentLine = buffer.readLine();


            //iterate through file until no lines left
            int vertexIndex = 0;
            while(currentLine != null){

                String[] tempTerms = currentLine.split("\\|");
                int originCity = -1, connectingCity = -1;

                //if atleast one city in the array
                if(!Cities.isEmpty()){
                    for(int i = 0; i < Cities.size(); i++){
                        //origin city was already in array
                        if(tempTerms[0].equals(Cities.get(i))){
                            originCity = i;
                        } else if(tempTerms[1].equals(Cities.get(i))){
                            connectingCity = i;
                        }

                        //if both vertices already in ArrayList can stop searching
                        //and break out of the loop
                        if(originCity >= 0 && connectingCity >= 0){
                            break;
                        }
                    }

                    //Origin city was not in ArrayList
                    if(originCity == -1 ){
                        addCity(tempTerms[0]);
                        ArrayList<Edge> originArrayList = new ArrayList<>();
                        neighbors.add(originArrayList);
                        //giving origin city the last index and iterating
                        originCity = vertexIndex;
                        vertexIndex++;
                    }
                    //connecting city was not in the ArrayList
                    if (connectingCity == -1) {
                        this.addCity(tempTerms[1]);
                        //giving connecting city the last index and iterating
                        connectingCity = vertexIndex;
                        ArrayList<Edge> connectingArrayList = new ArrayList<>();
                        neighbors.add(connectingArrayList);
                        vertexIndex++;
                    }



                    //add edges of new cities, in both directions
                    Edge tempEdge = new Edge(originCity,connectingCity, Cities.get(originCity), Cities.get(connectingCity));
                    neighbors.get(originCity).add(tempEdge);
                   // System.out.println(tempEdge.originName + ", " + tempEdge.destinationName +" just added to " + Cities.get(originCity));
                    //Adding reverse connection as our roads form an undirected graph
                    Edge tempEdge2 = new Edge(connectingCity,originCity,Cities.get(connectingCity), Cities.get(originCity));
                    neighbors.get(connectingCity).add(tempEdge2);
                    //System.out.println(tempEdge2.originName + ", " + tempEdge2.destinationName +" just added to " + Cities.get(connectingCity));


                } else { // the city array is empty
                    //adding both cities to the arrayList
                    this.addCity(tempTerms[0]);
                    this.addCity(tempTerms[1]);
                    vertexIndex += 2;

                    /*
                    neighbors
                    Cities origin
                    Dallas => {houston, austin}
                    Austin => {houston, Dallas, San Antonio}
                    edge(0,1) example -> Dallas - austin
                    tempEdgeConnection ex -> Dallas: [Dallas - Austin]
                     */
                    //add edges of new cities, in both directions
                    //since first two cities, we know the index is 0 and 1
                    Edge tempEdge = new Edge(0,1, Cities.get(0), Cities.get(1));
                    ArrayList<Edge> tempEdgeConnection = new ArrayList<Edge>();
                    tempEdgeConnection.add(0, tempEdge);
                    neighbors.add(tempEdgeConnection);
                    //System.out.println(tempEdge.originName + ", " + tempEdge.destinationName +" just added to " + Cities.get(0));
                    //Adding reverse connection as undirected graph
                    Edge tempEdge2 = new Edge(1,0, Cities.get(1), Cities.get(0));
                    ArrayList<Edge> tempEdgeConnection2 = new ArrayList<Edge>();
                    tempEdgeConnection2.add(0, tempEdge2);
                    neighbors.add(tempEdgeConnection2);
                    //System.out.println(tempEdgeConnection2.get(0).originName + ", " + tempEdgeConnection2.get(0).destinationName +" just added to " + Cities.get(neighbors.indexOf(tempEdgeConnection2)));
                }

                //iterating to next edge in Data file
                currentLine = buffer.readLine();
            }

            buffer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    };

    //add vertex to the graph, and create new edge ArrayList
    public void addCity(String cityName) {
        if (!Cities.contains(cityName)) {
            Cities.add(cityName);
        }
    }

    //when called check if edge already in the neighbors list, if not add relation
    public void addEdge(Edge e) {
        //if neighbors doesn't have Dallas' connection to v city, add to neighbors list
        if (!neighbors.get(e.u).contains(e.v)) {
            neighbors.get(e.u).add(e);
        }
    }
    public void printAdjacencyList(){
        //for each cities' origin Arraylist, print out the name of the origin city, and it's connections
        //Dallas' origin arraylist => edgeList
        for (ArrayList<Edge> originCityList : neighbors) {
            //edgeList[0] is dallas, print the name in Cities with same index
            System.out.print("\nCity " + originCityList.get(0).originName + ": ");
            //for each edge in the origin city's list of edges, print the connecting city name
            for(Edge connection: originCityList){
                System.out.print(Cities.get(connection.u) +" -> " + Cities.get(connection.v)+"  ");
            }
        }
    }
}
