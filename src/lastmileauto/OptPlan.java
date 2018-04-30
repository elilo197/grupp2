
package lastmileauto;

import java.util.*;

// Genomför billigaste väg beräkningar + Gör färdplanering

public class OptPlan {
    private DataStore ds; 
    LinkedList<Vertex>path;

     
   
    public OptPlan(DataStore ds){
        this.ds = ds; 
    }
    
    // Genomför billigaste väg beräkningen
    public void createPlan(){
        double x; 
        double y;
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
            
            
            x = ds.nodeX[ds.pathInt[i]]; 
            y = ds.nodeY[ds.pathInt[i]];
            
            System.out.println("" +x+", " +y);
            
            
        }   
        
        // Arc in the shoretest path
//        for(int i =0; i <path.size()-1; i++)
//        {
//            for(int j = 0;j <ds.arcs; j++)
//            {
//                if(ds.arcStart[j]==Integer.parseInt(path.get(i).getId())
//                        && ds.arcEnd[j]==Integer.parseInt(path.get(i+1).getId()))
//                {
//                    System.out.println("Arc in shortest path: "+j);
//                    ds.arcColor[j-1] = 1;
//
//                }
//           
//            }
//        }
    }

}
