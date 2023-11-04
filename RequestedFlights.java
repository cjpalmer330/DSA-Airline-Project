import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class RequestedFlights {
  
  //0 is time sort, 1 is cost sort
  ArrayList<ArrayList<String>> requestedFlights = new ArrayList<ArrayList<String>>();
  public void returnFlights(){
    WeightedGraph graph = new WeightedGraph("Data.txt");
    
    for(ArrayList<String> instanceRequest : requestedFlights){
      //return Path with source, destination, time/cost
      int tempTimeCost = -1;
      if(instanceRequest.get(2).equals("T")){
        tempTimeCost = 0;
      } else {
        tempTimeCost = 1;
      }
      graph.returnPath(instanceRequest.get(0), instanceRequest.get(1), tempTimeCost);
    }
  }
  public RequestedFlights(){
    String requestedFileName = "Requested.txt";
    int tempTimeCost = -1;
    try{
      FileReader reqReader = new FileReader(requestedFileName);
      BufferedReader reqBuffer = new BufferedReader(reqReader);
      String currentLine = reqBuffer.readLine();
      while(currentLine != null){
        String[] requestTerms = currentLine.split("\\|");
        ArrayList<String> tempRequest = new ArrayList<String>();
        tempRequest.add(0, requestTerms[0]);
        tempRequest.add(1, requestTerms[1]);
        tempRequest.add(2, requestTerms[2]);
        requestedFlights.add(tempRequest);
        currentLine = reqBuffer.readLine();
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  };
}
