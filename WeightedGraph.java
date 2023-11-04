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
          Node tempNode = new Node(connectingCity, Integer.parseInt(tempTerms[2]), Integer.parseInt(tempTerms[3]), CitiesNames.get(originCity), CitiesNames.get(connectingCity));
          tempNode.changeParent(originCity);
          neighbors.get(originCity).add(tempNode);
          //Adding reverse connection as our roads form an undirected graph
          Node tempNode2 = new Node(originCity, Integer.parseInt(tempTerms[2]), Integer.parseInt(tempTerms[3]), CitiesNames.get(connectingCity), CitiesNames.get(originCity));
          tempNode2.changeParent(connectingCity);
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
          Node tempNode = new Node(1, Integer.parseInt(tempTerms[2]), Integer.parseInt(tempTerms[3]), CitiesNames.get(0), CitiesNames.get(1));
          tempNode.changeParent(Integer.parseInt(tempTerms[3]));
          LinkedList<Node> tempNodeConnection = new LinkedList<>();
          tempNodeConnection.add(0, tempNode);
          neighbors.add(tempNodeConnection);
          //Adding reverse connection as undirected graph
          Node tempNode2 = new Node(0, Integer.parseInt(tempTerms[2]), Integer.parseInt(tempTerms[3]),CitiesNames.get(1), CitiesNames.get(0));
          tempNode.changeParent(Integer.parseInt(tempTerms[2]));
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
  
  public void printAdjacencyList(){
    //for each cities' origin Arraylist, print out the name of the origin city, and it's connections
    //Dallas' origin arraylist => edgeList
    for (LinkedList<Node> originCityList : neighbors) {
      //edgeList[0] is dallas, print the name in Cities with same index
      System.out.print("\nCity " + CitiesNames.get(neighbors.indexOf(originCityList)) + ": ");
      //for each edge in the origin city's list of edges, print the connecting city name
      for(Node connection: originCityList){
        System.out.print(CitiesNames.get(neighbors.indexOf(originCityList)) +" - " + connection.cost + " -> " + CitiesNames.get(connection.cityIndex)+"  ");
      }
    }
  }
  
  /*

  TODO ADD INPUT FILE READING TO THIS METHOD SO YOU JUST INPUT PATH REQUEST
   */
  public void returnPath(String inputSource, String inputDestination, int inputTimeCost){
    int source = CitiesNames.indexOf(inputSource);
    int destination = CitiesNames.indexOf(inputDestination);
    
    //getting all paths using DFS
    ArrayList<Stack<Integer>> DFS_Paths= new ArrayList<>();
    DFStest searchDFS = new DFStest(neighbors, source, destination);
    DFS_Paths = searchDFS.sortPaths(inputTimeCost);
    int[]pathIndeces = new int[3];
    String tcString;
    if(inputTimeCost == 0) {
      pathIndeces = searchDFS.getFastestPaths();
      tcString = "  (Time)";
    } else {
      pathIndeces = searchDFS.getCheapestPaths();
      tcString = "  (Cost)";
    }
    //Print the source and destination city names
    System.out.print("\n\nSource city: " + neighbors.get(source).get(0).sourceName + "\t\tDestination City: " + neighbors.get(destination).get(0).sourceName + tcString);

    //DFS_Paths = searchDFS.paths;
    
    //print out all the paths
    for(int pathIndex = 0; pathIndex < DFS_Paths.size(); pathIndex++){
      if(searchDFS.pathData.get(pathIndeces[pathIndex]).get(0) > 0){
        System.out.println("\nFlight " + (pathIndex + 1) +" from " + CitiesNames.get(source) + " to " + CitiesNames.get(destination));
        int[] tempArray = new int[DFS_Paths.get(pathIndex).size()];
        for(int element = DFS_Paths.get(pathIndex).size() - 1; element > 0; element--){
          tempArray[element] = DFS_Paths.get(pathIndex).pop();
        }
        if(!DFS_Paths.get(pathIndex).isEmpty()) {
          int tempPathLength = tempArray.length;
          System.out.print(CitiesNames.get(source));
          for (int cityIndex = 1; cityIndex < tempPathLength; cityIndex++) {
            int tempCity = tempArray[cityIndex];
            System.out.print(" -> " + CitiesNames.get(tempCity));
          }
        }
        System.out.print(". Time: " + searchDFS.pathData.get(pathIndeces[pathIndex]).get(0) + "  Cost: " + searchDFS.pathData.get(pathIndeces[pathIndex]).get(1));
      }
      
    }
  }
}
