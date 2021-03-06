package lastmileauto;

import java.util.*;
import javax.bluetooth.*;

//Skapar en optimerad plan och skapar kommandon som ska skickas till Agv:n. 
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
    
    //Skapar en plan med noder för hur Agv:n ska åka
    public ArrayList<Integer>  createPlan(){ 
        List<Vertex> nodes =new ArrayList<Vertex>();
        List<Edge> edges =new ArrayList<Edge>();
        ds.pathInt = new ArrayList<Integer>();
        
        // Sätter upp nätverket med noderna
        for(int i =0; i <ds.nodes; i++)
        {
            Vertex location =new Vertex("" + (i +1), "Nod #" + (i +1));
            nodes.add(location);
        }
        //Sätter upp länkar 
        for(int i =0; i <ds.arcs; i++)
        {
            Edge lane =new Edge("" + (i +1), nodes.get(ds.arcStart[i]-1), 
                    nodes.get(ds.arcEnd[i]-1),1); // Last argument is arc cost 
            edges.add(lane);
        }
        Graph graph =new Graph(nodes,edges);
        DijkstraAlgorithm dijkstra =new DijkstraAlgorithm(graph);

        //Kallar på dijkstra och skapar en väg från startnod till slutnod    
        dijkstra.execute(nodes.get(ds.startRutt-1)); //Startnod      
        System.out.println("startrutt från dij: " +  nodes.get(ds.startRutt-1));
        path =dijkstra.getPath(nodes.get(ds.slutRutt-1)); //Slutnod
        System.out.println("slutrutt från dij: " +  nodes.get(ds.slutRutt-1));
    
        //Markerar de vägar som ingår i den optimerade rutten. 
        for(int i=0; i <path.size()-1; i++)
        {
             for(int j = 0; j <ds.arcs; j++)
            {
                if(ds.arcStart[j]==Integer.parseInt(path.get(i).getId())
                        && ds.arcEnd[j]==Integer.parseInt(path.get(i+1).getId()))
                {
                    ds.arcColor[j] = 1;     //Markerar de bågar som ingår i en rutt


                }
           
            }
            ds.pathInt.add(Integer.parseInt(path.get(i).getId()));

        }     
    
        ds.pathInt.add(ds.slutRutt);
    return ds.pathInt;    //Returnerar den optimerade vägen i form av noder.   

    }
//Denna metod skapar kommandon som ska skickas till Agv:n 
    public ArrayList<String> compass(ArrayList<Integer> nodlista){
               
        int[] nodlistaInt = new int[nodlista.size()-1]; 

      x = new double[nodlista.size()]; 
      y = new double[nodlista.size()];
      int uplats = 0;
      int count = 0;
      ArrayList<Integer> compare = new ArrayList<Integer>();
      int countFLF=1;
      int  countBort = 0; 

      ds.kommandon = new ArrayList<String>();
                      
        System.out.println("Nodlista från compass: " + nodlista);
        
        
      for(int i =0; i <nodlista.size(); i++)
        {
        nodlistaInt = nodlista.stream().mapToInt(k -> k).toArray();  
        } 
     
      //Tar reda på x och y koordinaterna för noderna som är med i rutten. 
      for(int i =0; i <nodlista.size(); i++)  
        { 
                x[i] = ds.nodeX[nodlistaInt[i]-1]; 
                y[i]= ds.nodeY[nodlistaInt[i]-1];           
        }

//Här översätts koordinaterna till körkommandon som Agv:n förstår, kommentarenera gäller för samtliga for-loopar nedåt
     //Nästkommande nod jämförs med nuvarande nod. 
      for(int i =0; i <nodlista.size()-2; i++) {    
           if((x[i+1] - x[i] > 0) && (y[i+1] - y[i] == 0))
           { //Agda kör österut
              // System.out.println("Nu kör Agda österut.");
                ds.vaderStrack.add("O"); 
                    //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i] == 0))
                    {
                       // System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0))
                    {
                        //System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    } 
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0))
                    {
                        ds.kommandon.add(ds.L);
                        //System.out.println("Nu ska Agda svänga vänster.");
                    }  
                    else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] > 0))
                    {
                        //System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
            }
           //Jämför nuvarande nod med nästkommande nod
           else if((x[i+1] - x[i] < 0) && (y[i+1] - y[i] == 0)){ //Agda kör västerut
               //System.out.println("Nu kör Agda västerut.");
               ds.vaderStrack.add("V"); 
                    //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0))
                    {
                        //System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0))
                    {
                        //System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0))
                    {
                        //System.out.println("Nu ska Agda svänga vänster.");
                        ds.kommandon.add(ds.L);
                    } 
            }
           //Jämför nuvarande nod med nästkommande nod
           else if((x[i+1] - x[i] == 0) && (y[i+1] - y[i] > 0)){ //Agda kör norrut
              // System.out.println("Nu kör Agda norrut.");
               ds.vaderStrack.add("N"); 
                    //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0))
                    {
                //        System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] == 0))
                    {
                  //      System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    }
                    else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0))
                    {
                    //    System.out.println("Nu ska Agda svänga vänster.");
                        ds.kommandon.add(ds.L);
                    } 
                    else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] > 0))
                    {
                      //  System.out.println("Nu ska Agda svänga höger snedsväng."); //Snedsväng
                        ds.kommandon.add(ds.R);
                    }
                    else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] < 0))
                    {
                        //System.out.println("Nu ska Agda köra vänster snedsväng."); //Snedsväng
                        ds.kommandon.add(ds.L);
                    }
              
            }
           //Jämför nuvarande nod med nästkommande nod
           else if((x[i+1] - x[i] == 0) && (y[i+1] - y[i] < 0)) { //Agda kör söderut
                //System.out.println("Nu kör Agda söderut.");
                ds.vaderStrack.add("S"); 
                //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0))
                    {
                       // System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0))
                    {
                        //System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    }
                    else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] == 0))
                    {
                       // System.out.println("Nu ska Agda svänga vänster.");
                        ds.kommandon.add(ds.L);
                    } 
                    else if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i+1] > 0))
                    {
                        //System.out.println("Nu ska Agda svänga vänster snedsväng."); //Snedsväng
                        ds.kommandon.add(ds.L);
                    }
                    else if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] < 0))
                    {
                       // System.out.println("Nu ska Agda svänga höger snedsväng.");  //Snedsväng
                        ds.kommandon.add(ds.R);
                    }
            
         
            }
             if((x[i+1] - x[i] >0 ) && (y[i+1] - y[i] >0))
           { //Agda kör ÖSTER FRÅN SNEVÄG
               //System.out.println("Nu kör Agda österut, från snedsväng.");
                ds.vaderStrack.add("O"); 
                    //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] > 0) && (y[i+2] - y[i] == 0))
                    {
                        //System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0))
                    {
                        //System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    } 
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0))
                    {
                        ds.kommandon.add(ds.L);
                       // System.out.println("Nu ska Agda svänga vänster.");
                    }  
                   
            }
           //Jämför nuvarande nod med nästkommande nod
           else if((x[i+1] - x[i] <0 ) && (y[i+1] - y[i] <0)){ //Agda kör västerut FRÅN SNESVÄNG
               //System.out.println("Nu kör Agda västerut, från snedsväng.");
               ds.vaderStrack.add("V"); 
                    //Jämför nästkommande nod med noden två steg framåt
                    if((x[i+2] - x[i+1] < 0) && (y[i+2] - y[i+1] == 0))
                    {
                       // System.out.println("Nu ska Agda köra rakt fram.");
                        ds.kommandon.add(ds.F);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] > 0))
                    {
                       // System.out.println("Nu ska Agda svänga höger.");
                        ds.kommandon.add(ds.R);
                    }
                    else if((x[i+2] - x[i+1] == 0) && (y[i+2] - y[i+1] < 0))
                    {
                        //System.out.println("Nu ska Agda svänga vänster.");
                        ds.kommandon.add(ds.L);
                    } 
            }
          }  
//Modifierar om kommando arrayen till kommandon som passar AGV:n, vi kollar även hur många lång kommando 
//arrayen är för veta om vissa modifieringar inte är nödvändiga. 
    System.out.println("Hela kommando i slutet av loopen innan modifiering: " + ds.kommandon);
    if(ds.kommandon.size() > 1 ){
        
       if(ds.kommandon.size()>3 ){  
        for (int i = 0; i<ds.kommandon.size()-3; i++){
             //Kollar om en viss kommando följd är FLLF och isåfall görs denna om till U. 
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
        
//Kollar om en viss kommando följd är FLF och isåfall görs denna om till L. 
    if(ds.kommandon.size()>2 ){
    for (int i = 0; i<ds.kommandon.size()-2; i++){
         
        if (ds.kommandon.get(i).equals("F") && ds.kommandon.get(i+1).equals("L") &&  ds.kommandon.get(i+2).equals("F")){
            ds.kommandon.remove(i);     //Ta bort första F:et
            ds.kommandon.remove(i+1);   //Ta bort andra F:et
             System.out.println("FLF = L ");
            compare.add(i); 
         } 
    }
    }
    int countStor = compare.size();
           
//Här modifieras resten av kommandona, se separata kommentarer för varje modifiering
     for (int i = 0; i<ds.kommandon.size()-1; i++){
         int kommandocount = ds.kommandon.size();
         //Ersätt FLF med L 
         if(countStor != 0 && i == ((compare.get(countFLF-1))-countBort)){      //har skapat en compare arraylist som sparar på vilka platser som FLF "kommandon" finns
                                                //och om det finns på fler platser jämför dessa också tills att countStor är uppfylld. 
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
            ds.kommandon.remove(i);   //Ta bort första F:et
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
   
         return ds.kommandon; //Returnerar den färdiga kommando arrayen som sedan anvönds för att skicka kommandon till AGV:n. 

    }
}