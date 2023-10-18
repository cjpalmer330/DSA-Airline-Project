import java.util.*;

public class dijkstraShortestPath {
  //arbitrary initial value
  int vertices = -1;
  LinkedList<LinkedList<Node>> graph = new LinkedList<LinkedList<Node>>();
  LinkedList<String> cityNames = new LinkedList<>();
  int source = -1;
  
  //For any source the children have a set index with source.IndexOf(connectingCity)
  //the grandchildren we can add to the visited and distance with the index of
  /*
            Dallas(1) -> Memphis(4), Houston(5)
  Austin(0) -> Houston(2) ->
            Chicago(3) ->
   
   
   
   
   distance and visited could be an arraylist of nodes
   then when doing check see if have the same city name so the order doesn't matter
   if distance from Austin -> dallas(node) > Austin -> Houston -> Dallas {
   distance to Dallas = austin - houston - dallas, mark dallas visited.
   
   
   
   
   
   
   
   
   
   
   */
  public dijkstraShortestPath(LinkedList<LinkedList<Node>> neighborNodes, int inputSourceIndex){
    graph = neighborNodes;
    //source is the index we are searching from: example used is all comments is Austin
    source = inputSourceIndex;
    vertices = graph.size();
    //distance from Austin to all cities
    ArrayList<Integer> distance = new ArrayList<Integer>();
    Stack<Node> parentNodes = new Stack<>();
    
    //if direct child of source, set to travel time,
    //Austin is index 1 and the source city
    // need dallas houston and chicago to be added to distance in 1,2,3
    //otherwise set to infinity
    //first n distances are source's children plus the source node
    distance.add(0, 0);
    cityNames.add(0, graph.get(graph.get(source).get(0).cityIndex).get(source).destinationName);
    parentNodes.add(graph.get(graph.get(source).get(0).cityIndex).get(source));
    System.out.println(parentNodes.toString());
    for(int v = 0; v < vertices - 1; v++){
      if(v < graph.get(source).size()){
        distance.add(graph.get(source).get(v).timeToTravel);
        cityNames.add(graph.get(source).get(v).destinationName);
        parentNodes.add(graph.get(source).get(v));
        graph.get(source).get(v).parentIndex = source;
      } else {
        distance.add(Integer.MAX_VALUE);
        cityNames.add("NOTHING");
      }
    }
    System.out.println("INITIALIZATION: " + distance);
    
    //for all cities that directly connect to Austin
    for(int u = 0; u < graph.get(source).size(); u++){
      //find the index of the closest child of Austin
      int closestChild = minDistance(distance);
      closestChild++;
      
      //for every grandchild of Austin
      for(int v = 0; v < graph.get(closestChild).size(); v++) {
        
        
        //if haven't visited all of Austin's grandchildren
        int grandchildIndex = graph.get(closestChild).get(v).cityIndex;
        
        /*
        boolean temp1 = graph.get(closestChild).get(v).timeToTravel != 0;
        boolean temp2 = distance.get(closestChild) != Integer.MAX_VALUE;
        boolean temp3 = (distance.get(closestChild) + graph.get(closestChild).get(v).timeToTravel) < distance.get(grandchildIndex);
        //System.out.println("\nvisited?: \t" + graph.get(source).get(closestChild - 1).visited);
        System.out.println("Node: " + graph.get(closestChild).get(v).destinationName + ", Visited: " + graph.get(closestChild).get(v).visited);
        System.out.println("graph.get(closestChild).get(v).timeToTravel != 0: \n" + temp1);
        System.out.println("distance.get(closestChild) != Integer.MAX_VALUE: \n" + temp2);
        System.out.println("((distance.get(closestChild) + graph.get(closestChild).get(v).timeToTravel) < distance.get(grandchild)\n" + temp3);
        //System.out.println("closest distance: " + distance.get(closestChild) + "\t" + graph.get(source).get(closestChild - 1).destinationName + "\t" + graph.get(closestChild).get(v).timeToTravel);
        System.out.println(graph.get(closestChild).get(v).timeToTravel + "\n" +
          (distance.get(closestChild) + graph.get(closestChild).get(v).timeToTravel));
        System.out.println("distance grandchild: " + distance.get(graph.get(closestChild).get(v).cityIndex));
        */
        //haven't visited grandchild
        
        if(!graph.get(closestChild).get(v).visited &&
          //and the grandchild isn't Austin
          //graph.get(closestChild).get(v).timeToTravel != 0 &&
          //origin is known
          distance.get(closestChild) != Integer.MAX_VALUE &&
          //distance to closestChild  + distance src -> closest -> grandchild < source -> grandchild
          ((distance.get(closestChild) + graph.get(closestChild).get(v).timeToTravel) < distance.get(grandchildIndex))
        ) {
          System.out.println("\nBEFORE -  distance[closest]: " + distance.get(closestChild) + " ,distance[v]: " + distance.get(grandchildIndex));
          int newDistance = distance.get(closestChild) + graph.get(closestChild).get(v).timeToTravel;
          distance.set(grandchildIndex, newDistance);
          cityNames.set(grandchildIndex, graph.get(closestChild).get(v).destinationName);
          graph.get(closestChild).get(v).parentIndex = closestChild;
          System.out.println("distance[0]: " + distance.get(0) + "distance[grandchild]: " + distance.get(grandchildIndex) + ", closestChild.v: " + graph.get(closestChild).get(v).timeToTravel);
          //System.out.println(Arrays.toString())
          //graph.get(closestChild).get(v).timeToTravel = distance.get(v);
        }
        graph.get(closestChild).get(v).visited = true;
      }
      int closestChildSourceIndex = closestChild--;
      graph.get(source).get(closestChildSourceIndex).visited = true;
      //childCity.visited = true;
      //mark that closestChild as visited
      //visited[j][closestChild] = true;
    }
    //visit
    printSolution(distance, vertices);
    System.out.println("\n\ndistance array: " + distance);
  }
  
  /*
  public dijkstraShortestPath(LinkedList<LinkedList<Node>> neighbors, int source, int size) {
  }
  
   */
  
  public void printSolution(ArrayList<Integer> distance, int vertices){
    //we know the index of the source city
    System.out.print("\n\nsource list size: " + vertices);
    System.out.println("\n\nVertex\t\tDistance from source");
    //iterate through the source city linked lists
    for(int distIndex = 0; distIndex < distance.size(); distIndex++){
      System.out.println( cityNames.get(distIndex) + "\t\t" + distance.get(distIndex));
    }
  }

  int minDistance(ArrayList<Integer> distance){
    //set min distance to infinity
    int min = Integer.MAX_VALUE;
    int min_index = -1;
    //check each vertex
    //if not visited, and this distance to the node is less than infinity
    for (int i = 0; i < graph.get(source).size() - 1; i++) {
      if (!graph.get(source).get(i).visited && distance.get(i + 1) <= min) {
        min = distance.get(i + 1);
        //distance.set(i, childCity.timeToTravel);
        //return index of the city relative to the source city NOT THE GRAPH
        min_index = i;
      }
    }

    return min_index;
  }
  /*
  public String[] shortestTime(int originCity, int destinationCity){
    int v = adjacencyList.size();
    boolean visited[] = new boolean[v];
    Stack<Integer> pathResultStack = new Stack<Integer>();
    Stack<Edge> nextToProcess = new Stack<Edge>();
    //putting source in the results stack
    pathResultStack.push(originCity);
    for(int i = 1; i < v - 1; i++){
      nextToProcess.push();
    }

    //get origin city's linked list
    for( Edge road: adjacencyList.get(originCity)){
      int citySmallestTime = findMinCity(time, visited);
      //explore connections
      //for(int)
    }

   */
    //String[] possiblePaths = new String[3];



    //return possiblePaths;
  //}

  //private int findMinCity(int[] timeToCity, boolean[] visited){}
}
