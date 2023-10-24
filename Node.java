public class Node {
  int cityIndex = -1;
  int cost;
  int timeToTravel;
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
    this.cost = inputCost;
    this.timeToTravel = inputTime;
    this.sourceName = inputSourceName;
    this.destinationName = inputDestination;
  }

  public boolean equals(Node checkNode){
    return this.cost == checkNode.cost && this.cityIndex == checkNode.cityIndex;
  }
}
