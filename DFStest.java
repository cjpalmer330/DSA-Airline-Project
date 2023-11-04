import java.util.*;
public class DFStest {
  //get child from source
  //visit child
  //iteratively visit every child
  //visit every child's child until all no further depth
  //go up one level, set children unvisited, set old source as visited, go to next source
  
  LinkedList<LinkedList<Node>> graph = new LinkedList<LinkedList<Node>>();
  ArrayList<Stack<Integer>> paths = new ArrayList<Stack<Integer>>(100);
  ArrayList<ArrayList<Integer>> pathData = new ArrayList<ArrayList<Integer>>(100);
  ArrayList<Boolean> visited = new ArrayList<>();
  
  int source = -1;
  int destination = -1;
  int currentPath = 0;
  int currentListIndex = -1;
  int[] fastestPaths = new int[3];
  int[] cheapestPaths = new int[3];
  
  public DFStest(LinkedList<LinkedList<Node>> intputGraph, int inputSource, int inputDestination) {
    this.graph = intputGraph;
    this.source = inputSource;
    this.destination = inputDestination;
    this.currentListIndex = source;
    searchPaths();
  }
  
  public ArrayList<Stack<Integer>> sortPaths(int timeCost) {
    int[] fastestTimes = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    int[] cheapestCost = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    int[] fastestIndeces = new int[3];
    int[] cheapestIndeces = new int[3];
    ArrayList<Stack<Integer>> fastestArrays = new ArrayList<>();
    ArrayList<Stack<Integer>> cheapestArrays = new ArrayList<>();
    
    //storing the value and index of the highest values so that I dont replace the second highest or smallest elemen and potentially not finishing with 3 shortest paths
    //highest time, index, expensive cost, index
    int highesttimeIndex = 2;
    int highestpriceIndex = 2;
    //getting fastest/cheapest index
    
    for(int index = 0; index < pathData.size(); index++){
      boolean sameTime = false, sameCost = false;
      if(pathData.get(index).get(0) > 0){
        for(int j = 0; j < 3; j++){
          if(pathData.get(index).get(0) == fastestTimes[j]){
            sameTime = true;
            break;
          }
        }
        if(!sameTime) {
          //if lower than one of the elements put into top 3 array
          if(pathData.get(index).get(0) < fastestTimes[highesttimeIndex]){
            fastestTimes[highesttimeIndex] = pathData.get(index).get(0);
            fastestIndeces[highesttimeIndex] = index;
            //find the new highest element so we can setup for next replacement
            for(int k = 0; k < fastestTimes.length; k++){
              if(fastestTimes[k] > fastestTimes[highesttimeIndex]){
                highesttimeIndex = k;
              }
            }
          }
        }
        for(int j = 0; j < 3; j++){
          if(pathData.get(index).get(1) == cheapestCost[j]){
            sameCost = true;
            break;
          }
        }
        if(!sameCost) {
          if(pathData.get(index).get(1) < cheapestCost[highestpriceIndex]){
            cheapestCost[highestpriceIndex] = pathData.get(index).get(1);
            cheapestIndeces[highesttimeIndex] = index;
            for(int k = 0; k < cheapestCost.length; k++){
              if(cheapestCost[k] > cheapestCost[highestpriceIndex]){
                highestpriceIndex = k;
              }
            }
          }
        }
      }
      //System.out.println("fastest: " + Arrays.toString(fastestTimes));
      
      
      
      //System.out.print("fastest times: " + Arrays.toString(fastestTimes) + ", cheapest: " + Arrays.toString(cheapestCost));
      //System.out.println(", indeces: " + Arrays.toString(fastestIndeces) + " path third fastest: " + paths.get(fastestIndeces[1]));
    }
    
    //getting fastest/cheapest paths
    for(int i = 0; i < 3; i++){
      //System.out.println("path size: " + paths.get(fastestIndeces[i]).size() + "fastest: " + Arrays.toString(fastestTimes));
      fastestArrays.add(paths.get(fastestIndeces[i]));
      cheapestArrays.add(paths.get(cheapestIndeces[i]));
    }
    
    if(timeCost == 0){
      setFastestIndeces(fastestIndeces);
      return fastestArrays;
      
    } else {
      setCheapestIndeces(cheapestIndeces);
      return cheapestArrays;
    }
    
  }
  
  private void setFastestIndeces(int[] fastestIndeces){
    this.fastestPaths = fastestIndeces;
  }
  private void setCheapestIndeces(int[] cheapestIndeces){
    this.cheapestPaths = cheapestIndeces;
  }
  
  public int[] getFastestPaths(){
    int tempValue = 0;
    for(int i =0; i < fastestPaths.length; i++){
      for(int j = 0; j < 3; j++){
        if(pathData.get(fastestPaths[j]).get(0) > pathData.get(fastestPaths[i]).get(0)){
          tempValue = fastestPaths[i];
          fastestPaths[i] = fastestPaths[j];
          fastestPaths[j] = tempValue;
        }
        
      }
    }
    return fastestPaths;
  }
  
  public int[] getCheapestPaths(){
    int tempValue = 0;
    for(int i =0; i < cheapestPaths.length; i++){
      for(int j = 0; j < 3; j++){
        if(pathData.get(cheapestPaths[j]).get(1) > pathData.get(cheapestPaths[i]).get(1)){
          tempValue = cheapestPaths[i];
          cheapestPaths[i] = cheapestPaths[j];
          cheapestPaths[j] = tempValue;
        }
        
      }
    }
    return cheapestPaths;
  }
  
  //getting every path from the source to the destination node
  public void searchPaths() {
    
    //initialize values
    Stack<Integer> sourceInitializer = new Stack<>();
    sourceInitializer.push(source);
    //initializing lists with dummy values
    for (int i = 0; i < (100); i++) {
      ArrayList<Integer> tempArray = new ArrayList<>();
      tempArray.add(0);
      tempArray.add(0);
      pathData.add(tempArray);
      paths.add(i, sourceInitializer);
      visited.add(false);
    }
    
    
    //initialize path with source
    Stack<Integer> tempStack = new Stack<>();
    tempStack.push(source);
    boolean visitedAllSourceChildren = false;
    
    int lastTimeAdded = 0, lastCostAdded = 0;
    //search for connection to destination
    while (!tempStack.isEmpty()) {
      //to check when we need to backtrack, will be set to false when there is an available child
      boolean everythingVisited = true;
      
      for (int city = 0; city < graph.get(currentListIndex).size(); city++) {
        int graphIndex = graph.get(currentListIndex).get(city).cityIndex;
        
        //if the city has a connection to destination add to path structure
        if (graphIndex == destination) {
          
          //adding destination
          tempStack.push(graphIndex);
          //creating temp stack and adding path to arraylist
          Stack<Integer> newPathStack = new Stack<>();
          newPathStack.addAll(tempStack);
          paths.set(currentPath, newPathStack);
          
          
          ArrayList<Integer> tempArray2 = new ArrayList<>();
          tempArray2.clear();
          tempArray2.add(pathData.get(currentPath).get(0));
          tempArray2.add(pathData.get(currentPath).get(1));
          //System.out.print("firstpath: " + currentPath + "current time: " + tempArray2.get(0));
          
          //increment time and cost values with destination
          ArrayList<Integer> tempArray = new ArrayList<>();
          tempArray.add(pathData.get(currentPath).get(0) + graph.get(currentListIndex).get(city).timeToTravel);
          tempArray.add(pathData.get(currentPath).get(1) + graph.get(currentListIndex).get(city).cost);
          pathData.set(currentPath, tempArray);
          
          //System.out.println("\ntempStack: " + tempStack + ", path: " + currentPath + "\tadding: " + graph.get(currentListIndex).get(city).timeToTravel + ", total: " + pathData.get(currentPath));
          //System.out.print
          
          //next path
          this.currentPath++;
          
          //setting next path to the data of previous without the destination
          pathData.set(currentPath, tempArray2);
          tempStack.pop();
          continue;
        }

        
        //if haven't visited the child yet, add it to the stack and move deeper
        if (!graph.get(currentListIndex).get(city).visited && !tempStack.contains(graphIndex)) {
          //System.out.print(" nodest path: " + tempStack + " time: " + pathData.get(currentPath).get(0));
          //increment time and cost values
          ArrayList<Integer> tempArray = new ArrayList<>();
          tempArray.add(pathData.get(currentPath).get(0) + graph.get(currentListIndex).get(city).timeToTravel);
          tempArray.add(pathData.get(currentPath).get(1) + graph.get(currentListIndex).get(city).cost);
          //System.out.println(", adding: " + graph.get(currentListIndex).get(city).timeToTravel + ", current lo: " + currentListIndex + ", dest: " + graphIndex);
          pathData.set(currentPath, tempArray);
          
          lastTimeAdded = graph.get(currentListIndex).get(city).timeToTravel;
          lastCostAdded = graph.get(currentListIndex).get(city).cost;
          
          everythingVisited = false;
          graph.get(currentListIndex).get(city).visited = true;
          tempStack.push(graphIndex);
          currentListIndex = graphIndex;
          break;
        }
      }
      
      //backtracking
      if (everythingVisited) {
        //System.out.print("path current: " + tempStack);
        int previousTop = tempStack.pop();
        if(tempStack.isEmpty()){ break;}
        int currentTop = tempStack.peek();

        //System.out.println(", previous: " + previousTop +"\t currentTop: " + currentTop);
        for(Node child : graph.get(currentTop)){
          if(child.cityIndex == previousTop){
            //decrement time and cost values
            ArrayList<Integer> tempArray = new ArrayList<>();
            tempArray.add(pathData.get(currentPath).get(0) - lastTimeAdded);
            tempArray.add(pathData.get(currentPath).get(1) - lastCostAdded);
            //System.out.println("current time: " + pathData.get(currentPath).get(0) + "\tsubtracting: " + child.timeToTravel);
            pathData.set(currentPath, tempArray);
            
          }
        }
        currentListIndex = currentTop;
        //update previous time/cost added
        int temppop = tempStack.pop();
        boolean skipForLoop = false;
        if(tempStack.isEmpty()){
          lastTimeAdded = 0;
          lastCostAdded = 0;
          skipForLoop = true;
        }
        if(!skipForLoop){
          for(int i = 0; i < graph.get(tempStack.peek()).size(); i++){
            if(graph.get(tempStack.peek()).get(i).cityIndex == temppop){
              lastTimeAdded = graph.get(tempStack.peek()).get(i).timeToTravel;
              lastCostAdded = graph.get(tempStack.peek()).get(i).cost;
              break;
            }
          }
        }
        
        tempStack.push(temppop);
        
      }
    }
  }
}