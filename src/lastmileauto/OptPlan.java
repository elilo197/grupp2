
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
    public ArrayList<Integer>  createPlan(){      //denna var void först
        //double [] x; 
        //double [] y;
        List<Vertex> nodes =new ArrayList<Vertex>();
        List<Edge> edges =new ArrayList<Edge>();
        ds.pathInt = new ArrayList<Integer>();
        
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

        //Beräknar billigaste vägen från Dijkstra-classen    
        dijkstra.execute(nodes.get(ds.startRutt-1)); //Startnod      
        System.out.println("startrutt från dij: " +  nodes.get(ds.startRutt-1));
        path =dijkstra.getPath(nodes.get(ds.slutRutt-1)); //Slutnod
        System.out.println("slutrutt från dij: " +  nodes.get(ds.slutRutt-1));
    
        
        for(int i=0; i <path.size()-1; i++)
        {
             for(int j = 0; j <ds.arcs; j++)
            {
                if(ds.arcStart[j]==Integer.parseInt(path.get(i).getId())
                        && ds.arcEnd[j]==Integer.parseInt(path.get(i+1).getId()))
                {
                    ds.arcColor[j] = 1;     //Ritar ut billigaste vägen i kartan

             //System.out.println("Arc in shortest path: "+j);
             //System.out.println("arcStart: "+ds.arcStart[j]);
             //System.out.println("arcEnd: "+ds.arcEnd[j]);

                }
           
            }
             
            //Gör om till int. 
            ds.pathInt.add(Integer.parseInt(path.get(i).getId()));

        }     
    
        ds.pathInt.add(ds.slutRutt);
    return ds.pathInt;     

    }

    public ArrayList<String> compass(ArrayList<Integer> nodlista){
               
        int[] nodlistaInt = new int[nodlista.size()-1];//+2];     //Skapar en array med noderna längst "billigaste vägen"  
                            //+2 är bara fulkodning för att testa
     // ds.kommandon = new String[nodlista.size()-2];
   
     
      x = new double[nodlista.size()];  //
      y = new double[nodlista.size()];
      int uplats = 0;
      int count = 0;
      ArrayList<Integer> compare = new ArrayList<Integer>();
      int countFLF=1;
      int  countBort = 0; 

      ds.kommandon = new ArrayList<String>();
                  
       //Här gör vi om arraylist till array med ints
              
        System.out.println("Nodlista från compass: " + nodlista);
            
         //Här gör vi om arraylist till array med ints
      for(int i =0; i <nodlista.size(); i++)
        {
        nodlistaInt = nodlista.stream().mapToInt(k -> k).toArray();  
       //  System.out.println("Nodlista: " + nodlistaInt[i]);
        } 
     
        // nodlista.add(ds.sistanod);
      for(int i =0; i <nodlista.size(); i++)  
        { 
            //Kolla om nodlista innehåller en upphämtningsplats, ge x och y dummyvärden
                x[i] = ds.nodeX[nodlistaInt[i]-1]; 
                y[i]= ds.nodeY[nodlistaInt[i]-1];    
               //System.out.println("Koordinater från compass: " +x[i]+", " +y[i]);
                
        }


      for(int i =0; i <nodlista.size()-2; i++) { 
              
           if((x[i+1] - x[i] > 0) && (y[i+1] - y[i] == 0))
           { //Agda kör österut
               System.out.println("Nu kör Agda österut.");
                ds.vaderStrack.add("O"); 
                    //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i] == 0))
                    {
                        System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0))
                    {
                        System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    } 
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0))
                    {
                        ds.kommandon.add(ds.L);
                        System.out.println("Nu ska Agda svänga vänster.");
                    }  
                    else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] > 0))
                    {
                        System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
            }
           //Jämför nuvarande nod med nästkommande nod
           else if((x[i+1] - x[i] < 0) && (y[i+1] - y[i] == 0)){ //Agda kör västerut
               System.out.println("Nu kör Agda västerut.");
               ds.vaderStrack.add("V"); 
                    //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0))
                    {
                        System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0))
                    {
                        System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0))
                    {
                        System.out.println("Nu ska Agda svänga vänster.");
                        ds.kommandon.add(ds.L);
                    } 
            }
           //Jämför nuvarande nod med nästkommande nod
           else if((x[i+1] - x[i] == 0) && (y[i+1] - y[i] > 0)){ //Agda kör norrut
               System.out.println("Nu kör Agda norrut.");
               ds.vaderStrack.add("N"); 
                    //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0))
                    {
                        System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] == 0))
                    {
                        System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    }
                    else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0))
                    {
                        System.out.println("Nu ska Agda svänga vänster.");
                        ds.kommandon.add(ds.L);
                    } 
                    else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] > 0))
                    {
                        System.out.println("Nu ska Agda svänga höger snedsväng."); //Snedsväng
                        ds.kommandon.add(ds.R);
                    }
                    else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] < 0))
                    {
                        System.out.println("Nu ska Agda köra vänster snedsväng."); //Snedsväng
                        ds.kommandon.add(ds.L);
                    }
              
            }
           //Jämför nuvarande nod med nästkommande nod
           else if((x[i+1] - x[i] == 0) && (y[i+1] - y[i] < 0)) { //Agda kör söderut
                System.out.println("Nu kör Agda söderut.");
                ds.vaderStrack.add("S"); 
                //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0))
                    {
                        System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0))
                    {
                        System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    }
                    else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] == 0))
                    {
                        System.out.println("Nu ska Agda svänga vänster.");
                        ds.kommandon.add(ds.L);
                    } 
                    else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] > 0))
                    {
                        System.out.println("Nu ska Agda svänga vänster snedsväng."); //Snedsväng
                        ds.kommandon.add(ds.L);
                    }
                    else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] < 0))
                    {
                        System.out.println("Nu ska Agda svänga höger snedsväng.");  //Snedsväng
                        ds.kommandon.add(ds.R);
                    }
            
         
            }
             if((x[i+1] - x[i] >0 ) && (y[i+1] - y[i] >0))
           { //Agda kör ÖSTER FRÅN SNEVÄG
               System.out.println("Nu kör Agda österut, från snedsväng.");
                ds.vaderStrack.add("O"); 
                    //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i] == 0))
                    {
                        System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0))
                    {
                        System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    } 
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0))
                    {
                        ds.kommandon.add(ds.L);
                        System.out.println("Nu ska Agda svänga vänster.");
                    }  
                   
            }
           //Jämför nuvarande nod med nästkommande nod
           else if((x[i+1] - x[i] <0 ) && (y[i+1] - y[i] <0)){ //Agda kör västerut FRÅN SNESVÄNG
               System.out.println("Nu kör Agda västerut, från snedsväng.");
               ds.vaderStrack.add("V"); 
                    //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0))
                    {
                        System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0))
                    {
                        System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0))
                    {
                        System.out.println("Nu ska Agda svänga vänster.");
                        ds.kommandon.add(ds.L);
                    } 
            }
          }  
    System.out.println("Hela kommando i slutet av loopen innan modellering: " + ds.kommandon);
    if(ds.kommandon.size() > 1 ){
        
       if(ds.kommandon.size()>3 ){  
        for (int i = 0; i<ds.kommandon.size()-3; i++){
           if(ds.kommandon.get(i).equals("F") && ds.kommandon.get(i+1).equals("L")&& ds.kommandon.get(i+2).equals("L")&& ds.kommandon.get(i+3).equals("F")){
            
            ds.kommandon.add(i, ds.U);     
            ds.kommandon.remove(i+1);
            ds.kommandon.remove(i+1);
            ds.kommandon.remove(i+1);
            ds.kommandon.remove(i+1);
          
   
            System.out.println("FLLF = U ");
            compare.add(i); 
            
              
          }
        }
       }
        

    if(ds.kommandon.size()>2 ){
    for (int i = 0; i<ds.kommandon.size()-2; i++){
         
        if (ds.kommandon.get(i).equals("F") && ds.kommandon.get(i+1).equals("L") &&  ds.kommandon.get(i+2).equals("F")){
            ds.kommandon.remove(i);     //Ta bort första F:et
            ds.kommandon.remove(i+1);   //Ta bort andra F:et
 //           System.out.println("Kommandon kvar efter radering, borde bara vara L: " + ds.kommandon.get(i));
            System.out.println("FLF = L ");
            compare.add(i); 
            
            
         } 
    }
    }
    int countStor = compare.size();
           
     //Modifierar kommando-arrayen så att vänstersvängarna funkar

     for (int i = 0; i<ds.kommandon.size()-1; i++){
         int kommandocount = ds.kommandon.size();
         //Ersätt FLF med L 

         if(countStor != 0 && i == ((compare.get(countFLF-1))-countBort)){      //har skapat en compare arraylist som sparar på vilka platser som FLF "kommandon" finns
                                                //och om det finns på fler platser jämför dessa också tills att countStor är uppfylld. 
            // System.out.println("I är lika med compare:" + i + " hoppar över" ); //tror detta kan bli problem med i men de borde gå att fixa fort / eelise
             if(countFLF < countStor){                                   
             countFLF = countFLF +1; 
             }
         }
         else{
        //Ersätt FLS med LS
        if (ds.kommandon.get(i).equals("F") && ds.kommandon.get(i+1).equals("L")){
                // System.out.println("I andra if-satsen för modifiering.");

             ds.kommandon.remove(i);     //Ta bort första F:et
             kommandocount = kommandocount -1;
             System.out.println("FL = L ");
             countBort = countBort -1;
         }
         
         //Ersätt LF med L
          else if (ds.kommandon.get(i).equals("L") && ds.kommandon.get(i+1).equals("F")){
                //  System.out.println("I tredje if-satsen för modifiering.");
              ds.kommandon.remove(i+1);   //Ta bort andra F:et
              kommandocount = kommandocount - 1;
              System.out.println("LF = L");
              countBort = countBort -1;
              
         } 
          //Ersätter FF med F 
          else if(ds.kommandon.get(i).equals("F") && ds.kommandon.get(i+1).equals("F")){
              // System.out.println("I fjärde if-satsen för modifiering.");
           //     System.out.println("Kommando på plats i, ska vara kvar: " + ds.kommandon.get(i));
            ds.kommandon.remove(i);   //Ta bort första F:et
  //          ds.kommandon.remove(i);//Detta kan bli strul ! 
              kommandocount = kommandocount-1 ;
              System.out.println("FF = F ");
              countBort = countBort -1;
              
          }

           if (i == kommandocount){            
              
               break;
           }
       }
      System.out.println("Färdiga kommandon:"+ ds.kommandon);
     }
    }
     
     System.out.println(ds.vaderStrack);
   
         return ds.kommandon;

    }
}