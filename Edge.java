public class Edge {
  int u;
  int v;
  int distance;
  int timeToTravel;
  String originName;
  String destinationName;

  //connection constructor
  public Edge(int tempu, int tempv) {
    this.u = tempu;
    this.v = tempv;
  }

  //connection constructor with the names of cities
  public Edge(int tempu, int tempv, String inputOrigin, String inputDestination) {
    this.u = tempu;
    this.v = tempv;
    this.originName = inputOrigin;
    this.destinationName = inputDestination;
  }
  public void swap(){
    int tempU = u;
    this.u = v;
    this.v = tempU;
    String tempOriginName = originName;
    this.originName = destinationName;
    this.destinationName = tempOriginName;
  }

  public boolean equals(Edge e){
    return this.u == e.u && this.v == e.v;
  }
}
