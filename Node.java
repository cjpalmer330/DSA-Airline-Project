public class Node {
  int cityIndex = -1;
  int timeToTravel;
  int cost;
  boolean visited = false;
  int parentIndex = -1;
  int dijkstraIndex = -1;
  String sourceName;
  String destinationName;
  //connection constructor
  public Node(int tempv) {
    this.cityIndex = tempv;
  }
  
  //connection constructor with the names of cities
  public Node(int tempv, int inputCost, int inputTime, String inputSourceName, String inputDestination) {
    this.cityIndex = tempv;
    this.timeToTravel = inputCost;
    this.cost = inputTime;
    this.sourceName = inputSourceName;
    this.destinationName = inputDestination;
  }
  
  public void changeParent(int inputParent){
    parentIndex = inputParent;
  }

  public boolean equals(Node checkNode){
    return this.timeToTravel == checkNode.timeToTravel && this.cityIndex == checkNode.cityIndex;
  }
}
