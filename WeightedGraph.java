import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class WeightedGraph {
    //Simply an array list to store the cities' names as strings so we as users don't need to memorize index numbers
    ArrayList<String> CitiesNames = new ArrayList<>();

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
    LinkedList<LinkedList<Node>> neighbors = new LinkedList<>();
    public WeightedGraph(){}
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
                if(!CitiesNames.isEmpty()){
                    for(int i = 0; i < CitiesNames.size(); i++){
                        //origin city was already in array
                        if(tempTerms[0].equals(CitiesNames.get(i))){
                            originCity = i;
                        } else if(tempTerms[1].equals(CitiesNames.get(i))){
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
                        LinkedList<Node> originArrayList = new LinkedList<>();
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
                        LinkedList<Node> connectingArrayList = new LinkedList<>();
                        neighbors.add(connectingArrayList);
                        vertexIndex++;
                    }



                    //add edges of new cities, in both directions
                    Node tempNode = new Node(connectingCity, Integer.parseInt(tempTerms[2]), Integer.parseInt(tempTerms[3]), CitiesNames.get(connectingCity));
                    neighbors.get(originCity).add(tempNode);
                    //Adding reverse connection as our roads form an undirected graph
                    Node tempNode2 = new Node(originCity, Integer.parseInt(tempTerms[2]), Integer.parseInt(tempTerms[3]), CitiesNames.get(originCity));
                    neighbors.get(connectingCity).add(tempNode2);
                    


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
                    Node tempNode = new Node(1, Integer.parseInt(tempTerms[2]), Integer.parseInt(tempTerms[3]), CitiesNames.get(1));
                    LinkedList<Node> tempNodeConnection = new LinkedList<>();
                    tempNodeConnection.add(0, tempNode);
                    neighbors.add(tempNodeConnection);
                    //Adding reverse connection as undirected graph
                    Node tempNode2 = new Node(0, Integer.parseInt(tempTerms[2]), Integer.parseInt(tempTerms[3]), CitiesNames.get(0));
                    LinkedList<Node> tempNodeConnection2 = new LinkedList<>();
                    tempNodeConnection2.add(0, tempNode2);
                    neighbors.add(tempNodeConnection2);
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
        if (!CitiesNames.contains(cityName)) {
            CitiesNames.add(cityName);
        }
    }

    /*
    //when called check if Node already in the neighbors list, if not add relation
    public void addNode(Node sourceNode, Node) {
        //if neighbors doesn't have Dallas' connection to v city, add to neighbors list
        if (!neighbors.get(neighbors.indexOf(sourceNode)).get()) {
            neighbors.get(sourceNode.u).add(sourceNode);
        }
    }
     */
  
    public void printAdjacencyList(){
        //for each cities' origin Arraylist, print out the name of the origin city, and it's connections
        //Dallas' origin arraylist => edgeList
        for (LinkedList<Node> originCityList : neighbors) {
            //edgeList[0] is dallas, print the name in Cities with same index
            System.out.print("\nCity " + CitiesNames.get(neighbors.indexOf(originCityList)) + ": ");
            //for each edge in the origin city's list of edges, print the connecting city name
            for(Node connection: originCityList){
                System.out.print(CitiesNames.get(neighbors.indexOf(originCityList)) +" - " + connection.timeToTravel + " -> " + CitiesNames.get(connection.cityIndex)+"  ");
            }
        }
    }

    /*

    TODO ADD INPUT FILE READING TO THIS METHOD SO YOU JUST INPUT PATH REQUEST
     */
    //Dijkstra's call output is to console
    public void shortestPath(int source, int destination){
        System.out.println("City: " + neighbors.get(1).get(2).destinationName + neighbors.get(1).get(2).timeToTravel);
        //System.out.println("Houston: " + neighbors.get(2).get(2).destinationName + neighbors.get(2).get(2).timeToTravel);
        dijkstraShortestPath sourceDijk = new dijkstraShortestPath(neighbors, source);


    }
}
