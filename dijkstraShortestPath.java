import java.util.*;

public class dijkstraShortestPath {
  //arbitrary initial value
  int vertices = -1;
  LinkedList<LinkedList<Node>> graph = new LinkedList<LinkedList<Node>>();
  LinkedList<String> cityNames = new LinkedList<>();
  int source = -1;
  int destination = -1;
  ArrayList<Integer> distance = new ArrayList<Integer>();
  ArrayList<Boolean> visited = new ArrayList<>();
  ArrayList<Integer> parentIndex = new ArrayList<>();
  
  //For any source the children have a set index with source.IndexOf(connectingCity)
  //the grandchildren we can add to the visited and distance with the index of
  /*
            Dallas(1) -> Memphis(4), Houston(5)
  Austin(0) -> Houston(2) ->
            Chicago(3) ->
   
   
   
   
   distance and visited could be an arraylist of nodes
   then when doing check see if have the same city name so the order doesn't matter
   if distance from Austin -> dallas(node) > Austin -> Houston -> Dallas {
   distance to Dallas = austin - houston - dallas, mark dallas visited
   
   */
  public dijkstraShortestPath(LinkedList<LinkedList<Node>> neighborNodes, int inputSourceIndex, int inputDestinationIndex){
    graph = neighborNodes;
    //source is the index we are searching from: example used is all comments is Austin
    source = inputSourceIndex;
    destination = inputDestinationIndex;
    vertices = graph.size();
    //distance from Austin to all cities

    Stack<Integer> nextToProcess = new Stack<>();
    
    //Setting initial distance, visited, and a name values for the graph
    //adding source Node by referencing connection to this child
    for(int v = 0; v < vertices; v++){
      distance.add(v, Integer.MAX_VALUE);
      cityNames.add(v, "UNKNOWN");
      visited.add(v, false);
      parentIndex.add(-1);
    }
    //initialization of the source in all respective arrays
    visited.set(source,true);
    distance.set(source,0);
    cityNames.set(source,graph.get(source).get(0).sourceName);
    parentIndex.set(source,source);
    nextToProcess.push(source);
    //setting all children nodes to have parent index as source
    for(Node directChild : graph.get(source)){
      parentIndex.set(directChild.cityIndex, source);
    }
    
    //for all cities that directly connect to Austin
    //for(int u = 0; u < graph.get(source).size(); u++)
    int x = 0;
    while(!nextToProcess.isEmpty() || x > 50){
      x++;
      //find the index of the closest child of Austin
      //int closestChild = minDistance(distance);
      //closestChild++;
      //sort the stack as to get the closest Node
      nextToProcess = sortStack(nextToProcess, distance);
      
      //take top node and add all it's children into the queue
      //int nextNode = graph.get(nextToProcess.pop());
      int closestChild = nextToProcess.pop();
      //add all of closestChild's children
      for (Node grandchild: graph.get(closestChild)) {
        if(!visited.get(grandchild.cityIndex) && !nextToProcess.contains(grandchild.cityIndex)) {
          nextToProcess.add(grandchild.cityIndex);
        }
      }
      //for every child of closestNode
      for(int v = 0; v < graph.get(closestChild).size(); v++) {
        
        int grandchildIndex = graph.get(closestChild).get(v).cityIndex;
        
        //haven't visited grandchild
        if(!visited.get(grandchildIndex) &&
          //and the grandchild isn't Austin
          graph.get(closestChild).get(v).timeToTravel != 0 &&
          //origin is known
          distance.get(closestChild) != Integer.MAX_VALUE &&
          //distance to closestChild  + distance src -> closest -> grandchild < source -> grandchild
          ((distance.get(closestChild) + graph.get(closestChild).get(v).timeToTravel) < distance.get(grandchildIndex))
        ) {
          int newDistance = distance.get(closestChild) + graph.get(closestChild).get(v).timeToTravel;
          distance.set(grandchildIndex, newDistance);
          cityNames.set(grandchildIndex, graph.get(grandchildIndex).get(0).sourceName);
          //graph.get(closestChild).get(v).parentIndex = closestChild;
          parentIndex.set(graph.get(closestChild).get(v).cityIndex, closestChild);
        }
      }
      visited.set(closestChild, true);
    }
    printAdjacencyList(distance, vertices);
    System.out.println("\n\ndistance array: " + distance);
  }
  
  private Stack<Integer> sortStack(Stack<Integer> currentNodeStack, ArrayList<Integer> distance) {
    Stack<Integer> tempStack = new Stack<>();
    while(!currentNodeStack.isEmpty())
    {
      Integer temp = currentNodeStack.pop();
      
      // while temporary stack is not empty and
      // top of stack is lesser than temp
      while(!tempStack.isEmpty() && distance.get(tempStack.peek())
        < distance.get(temp))
      {
        // pop from temporary stack and
        // push it to the input stack
        currentNodeStack.push(tempStack.pop());
      }
      
      // push temp in temporary of stack
      tempStack.push(temp);
    }
    return tempStack;
  }
  
  public void printAdjacencyList(ArrayList<Integer> distance, int vertices){
    //we know the index of the source city
    System.out.println("\n\nVertex\t\t\tDistance from source");
    //iterate through the source city linked lists
    for(int distIndex = 0; distIndex < distance.size(); distIndex++){
      System.out.println( cityNames.get(distIndex) + "\t\t\t" + distance.get(distIndex));
    }
  }
  
  public void printPath(){
    Stack<String> flightPath = new Stack<>();
    System.out.println("\n\nFlight 1: " + cityNames.get(source) + ", " + cityNames.get(destination) + " (Time)");
    System.out.print("Path 1: " + cityNames.get(source));
    //
    int nextParent = destination;
    int tempGrandchildIndex = graph.get(nextParent).get(0).cityIndex;
    //getting initial destination city Node
    for(int potentialSource = 0; potentialSource < graph.size(); potentialSource++){
      //if the destination is the same as this node's index, we know the parentIndex of the node is correct
      flightPath.push(cityNames.get(nextParent));
      nextParent = parentIndex.get(nextParent);
      if(nextParent == parentIndex.get(nextParent)){ break; }
    }
    
    //printing out the elements in the stack
    while(!flightPath.isEmpty()){
      String layover = flightPath.pop();
      System.out.print(" -> " + layover);
    }
    
    System.out.print("\tTime: " + distance.get(destination));
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
}
