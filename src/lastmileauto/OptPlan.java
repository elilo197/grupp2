
package lastmileauto;

import java.util.*;
import javax.bluetooth.*;
// Genomför billigaste väg beräkningar + Gör färdplanering

public class OptPlan {
    private DataStore ds; 
    LinkedList<Vertex>path;
    int[]path1;
    BluetoothTransmitter btm; 
    BluetoothTransceiver btc;
    double [] x; 
    double [] y;
    String [] kommandon;
   
    public OptPlan(DataStore ds){
        this.ds = ds; 
    }
    
    // Genomför billigaste väg beräkningen
    public ArrayList<Integer>  createPlan(){      //enna var void först
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
        dijkstra.execute(nodes.get(ds.startRutt-1)); //Startnod 
        path =dijkstra.getPath(nodes.get(ds.slutRutt-1)); //Slutnod  
    
    // Get shortest path
    

        for(int i=0; i <path.size()-1; i++)
        {
             for(int j = 0; j <ds.arcs; j++)
            {
                if(ds.arcStart[j]==Integer.parseInt(path.get(i).getId())
                        && ds.arcEnd[j]==Integer.parseInt(path.get(i+1).getId()))
                {
                    ds.arcColor[j] = 1;
                   // System.out.println("arcStart: "+ds.arcStart[j]);
                    //System.out.println("arcEnd: "+ds.arcEnd[j]);
                }
           
            }
            
           //System.out.println(path.get(i));  // Path innehåller noder. 
            
            //Gör om till int. 
            ds.pathInt.add(Integer.parseInt(path.get(i).getId()));
  
 
            //System.out.println("pathInt: " + ds.pathInt);

            
            
            //x[i] = ds.nodeX[ds.pathInt[i]]; 
            //y[i]= ds.nodeY[ds.pathInt[i]];
                    
        }   
                ds.pathInt.add(ds.slutRutt);
    return ds.pathInt;     

    }

    public String[] compass(ArrayList<Integer> nodlista){
      int[] nodlistaInt = new int[nodlista.size()];//+2];       //+2 är bara fulkodning för att testa
     // ds.kommandon = new String[nodlista.size()-2];
      x = new double[nodlista.size()];
      y = new double[nodlista.size()];
      
        
          //nodlista = createPlan(); 
          System.out.println("Nodlista från compass: " + nodlista);
          String[] test = {"testar", "testarigen"};
          
          //Här gör vi om arraylist till array med ints
          for(int i =0; i <nodlista.size(); i++)
        {
         nodlistaInt = nodlista.stream().mapToInt(k -> k).toArray();  
       //  System.out.println("Nodlista: " + nodlistaInt[i]);
        } 
     
          
    for(int i =0; i <nodlista.size(); i++)  {    
         
        x[i] = ds.nodeX[nodlistaInt[i]-1]; //+2 är fulkodning
        y[i]= ds.nodeY[nodlistaInt[i]-1];  //+2 är fulkodning  
       //System.out.println("Koordinater från compass: " +x[i]+", " +y[i]);
    }
    
       // btc= new BluetoothTransceiver();
        //btm = new BluetoothTransmitter(btc); 
     for(int i =0; i <ds.kommandon.length; i++)  {    //nodlista.size(); i++)  {
           if((x[i+1] - x[i] > 0) && (y[i+1] - y[i] == 0)){ //Agda kör österut
               System.out.println("Nu kör Agda österut.");
           //Kolla två framåt: x(n+2)-x(n+1)
                if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i] == 0)){
                //btm.send(ds.F); 
                System.out.println("Nu ska Agda köra rakt fram.");
                ds.kommandon[i] = ds.F;
                }
                else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0)){
               // btm.send(ds.R);  
               ds.kommandon[i] = ds.R;
               System.out.println("Nu ska Agda svänga höger.");
                } 
                else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0)){
                 // btm.send(ds.L);
                ds.kommandon[i] = ds.L;
                System.out.println("Nu ska Agda svänga vänster.");
                }  
                else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] > 0)){
                    //btm.send(ds.F);
                    System.out.println("Nu ska Agda köra rakt fram.");
                   ds.kommandon[i] = ds.F;
                }
            }
           
           else if((x[i+1] - x[i] < 0) && (y[i+1] - y[i] == 0)){ //Agda kör västerut
               System.out.println("Nu kör Agda västerut.");
           //Kolla två framåt: x(n+2)-x(n+1)
               if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0)){
                   // btm = new BluetoothTransmitter(F);
                   System.out.println("Nu ska Agda köra rakt fram.");
                   ds.kommandon[i] = ds.F;
               }
               else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0)){
                   // btm = new BluetoothTransmitter(R);
                   System.out.println("Nu ska Agda svänga höger.");
                   ds.kommandon[i] = ds.R;
               }
               else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0)){
                 //   btm = new BluetoothTransmitter(L);
                 System.out.println("Nu ska Agda svänga vänster.");
                 ds.kommandon[i] = ds.L;
               } 
            }
           
           else if((x[i+1] - x[i] == 0) && (y[i+1] - y[i] > 0)){ //Agda kör norrut
               //Kolla två framåt: x(n+2)-x(n+1)
               System.out.println("Nu kör Agda norrut.");
               if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0)){
                   // btm = new BluetoothTransmitter(F);
                   System.out.println("Nu ska Agda köra rakt fram.");
                   ds.kommandon[i] = ds.F;
               }
               else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] == 0)){
                  //  btm = new BluetoothTransmitter(R);
                  System.out.println("Nu ska Agda svänga höger.");
                  ds.kommandon[i] = ds.R;
               }
               else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0)){
               //     btm = new BluetoothTransmitter(L);
               System.out.println("Nu ska Agda svänga vänster.");
               ds.kommandon[i] = ds.L;
               } 
               else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] > 0)){
               //    btm = new BluetoothTransmitter(R);
               System.out.println("Nu ska Agda svänga höger snedsväng.");
               ds.kommandon[i] = ds.R;
               }
               else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] < 0)){
                  // btm = new BluetoothTransmitter(L);
                  System.out.println("Nu ska Agda köra vänster snedsväng.");
                  ds.kommandon[i] = ds.L;
               }
              // System.out.println("Kommandon: " + ds.kommandon[i]);
               
            }
       
           else if((x[i+1] - x[i] == 0) && (y[i+1] - y[i] < 0)) { //Agda kör söderut
               System.out.println("Nu kör Agda söderut.");
               //Kolla två framåt: x(n+2)-x(n+1)
               if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0)){
                 //   btm = new BluetoothTransmitter(F);
                 System.out.println("Nu ska Agda köra rakt fram.");
                 ds.kommandon[i] = ds.F;
               }
               else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0)){
                  //  btm = new BluetoothTransmitter(R);
                  System.out.println("Nu ska Agda svänga höger.");
                  ds.kommandon[i] = ds.R;
               }
               else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] == 0)){
                 //   btm = new BluetoothTransmitter(L);
                 System.out.println("Nu ska Agda svänga vänster.");
                 ds.kommandon[i] = ds.L;
               } 
               else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] > 0)){
                 //  btm = new BluetoothTransmitter(L);
                 System.out.println("Nu ska Agda svänga vänster snedsväng.");
                 ds.kommandon[i] = ds.L;
               }
               else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] < 0)){
                  // btm = new BluetoothTransmitter(R);
                  System.out.println("Nu ska Agda svänga höger snedsväng.");
                  ds.kommandon[i] = ds.R;
               }
            }
         
         //    System.out.println(ds.kommandon[i]); 
     }
   

         return ds.kommandon;
}
}