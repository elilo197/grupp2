
package lastmileauto;

import java.util.*;

// Genomför billigaste väg beräkningar + Gör färdplanering

public class OptPlan {
    private DataStore ds; 
    LinkedList<Vertex>path;
    BluetoothTransmitter btm; 
    BluetoothTransceiver btc;
      double [] x; 
      double [] y;
   
    public OptPlan(DataStore ds){
        this.ds = ds; 
    }
    
    // Genomför billigaste väg beräkningen
    public void createPlan(){
        //double [] x; 
        //double [] y;
        List<Vertex> nodes =new ArrayList<Vertex>();
        List<Edge> edges =new ArrayList<Edge>();
        
        // Set up network
        for(int i =0; i <ds.nodes; i++)
        {
            Vertex location =new Vertex("" + (i +1), "Nod #" + (i +1));
            nodes.add(location);
        }
        
        for(int i =0; i <ds.arcs; i++)
        {
            Edge lane =new Edge("" + (i +1), nodes.get(ds.arcStart[i]-1), 
                    nodes.get(ds.arcEnd[i]-1),1); // Last argument is arc cost 
            edges.add(lane);
        }
        
        Graph graph =new Graph(nodes,edges);
            DijkstraAlgorithm dijkstra =new DijkstraAlgorithm(graph);

     // Compute shortest path
        dijkstra.execute(nodes.get(ds.startRutt)); //Startnod 
        path =dijkstra.getPath(nodes.get(ds.slutRutt)); //Slutnod   ändrade här!!!! 
    
    // Get shortest path
    
      x = new double[ds.pathInt.length];
      y = new double[ds.pathInt.length];
      
        for(int i=0; i <path.size()-1; i++)
        {
             for(int j = 0; j <ds.arcs; j++)
            {
                if(ds.arcStart[j]==Integer.parseInt(path.get(i).getId())
                        && ds.arcEnd[j]==Integer.parseInt(path.get(i+1).getId()))
                {
                    ds.arcColor[j] = 1;
                    System.out.println("Arc in shortest path: "+j);
                }
           
            }
            
           //System.out.println(path.get(i));  // Path innehåller noder. 
            
            //Gör om till int. 
            ds.pathInt[i] = Integer.parseInt(path.get(i).getId());
            //System.out.println(Integer.parseInt(path.get(i).getId()));
            //System.out.println(ds.pathInt[i]);
            
            
            x[i] = ds.nodeX[ds.pathInt[i]]; 
            y[i]= ds.nodeY[ds.pathInt[i]];
            

           System.out.println("Koordinater från createPlan: " +x[i]+", " +y[i]);
           // System.out.println("" +x+", " +y);

            
            
        }   
         

    }

    public String[] compass(){
          createPlan(); 
          String[] test = {"testar", "testarigen"};
        for(int i =0; i <ds.pathInt.length; i++)
        {
//             x = new double[ds.pathInt.length];
//             y = new double[ds.pathInt.length];
//            x[i] = ds.nodeX[ds.pathInt[i]]; //Måste vi kalla på dessa igen? kan det göras på något annat sätt?
//            y[i]= ds.nodeY[ds.pathInt[i]];
            //ds.pathInt[i] = Integer.parseInt(path.get(i).getId());
           
        System.out.println("Nodnr från compass: " + ds.pathInt[i]); //bågens nummer
        System.out.println("Koordinater från compass: " +x[i]+", " +y[i]);
        //btc= new BluetoothTransceiver();
        //btm = new BluetoothTransmitter(btc); 
   
           // System.out.println("tjena" +ds.nodeX[i]+", " +ds.nodeY[i]);
           if((x[i+1] - x[i] > 0) && (y[i+1] - y[i] == 0)){ //Agda kör österut
           //Kolla två framåt: x(n+2)-x(n+1)
                if((x[i+1] - x[i] > 0) && (y[i+1] - y[i] == 0)){
                //btm.send(ds.F);    
                }
                else if((x[i+1] - x[i] == 0) && (y[i+1] - y[i] < 0)){
               // btm.send(ds.R);      
                } 
                else if((x[i+1] - x[i] == 0) && (y[i+1] - y[i] > 0)){
                  // btm.send(ds.L);
                }  
                else if((x[i+1] - x[i] > 0) && (y[i+1] - y[i] > 0)){
                    //btm.send(ds.F);
                }
            } 
        }
        return test;
    }
}
