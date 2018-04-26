/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Kan vi använda Timer istället för trådning i denna?

package lastmileauto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.*;
import java.util.ArrayList;

public class Uppdrag {
    String inkommandetext []; 
    String ink_sam;
    int IntStorlek;
    DataStore ds;
    int [] linkNod1;
    int [] linkNod2;
    OptPlan opt;
    MapPanel map;
    

    public Uppdrag(DataStore ds1, OptPlan op1) {
        this.ds = ds1;
        this.opt = op1; 
        listaplatser();
         // listauppdrag("A");
     
    }   

    /** Här listar vi antalet upphämtningsplatser och vi måste även beräkna vilken
     * upphämtningsplats som är närmast. Sen ska vi skicka informationen till 
     * AGVn
     */
    public  void listaplatser() { //tagit bort static
   
     try {

         //Uppdrag http = new Uppdrag();
         String url = "http://tnk111.n7.se/listaplatser.php"; 
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

         System.out.println("\nAnropar: " + url);
 
         int mottagen_status = anslutning.getResponseCode();

         System.out.println("Statuskod: " + mottagen_status);
         BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
        
        
        //Arraylist är dynamiska, vi behöver alltså inte ha en "längd" 
         ArrayList<String> ink = new ArrayList<String>();

         
        while ((inkommande_text = inkommande.readLine()) != null) {
            //System.out.println("Inkommande: " + inkommande_text);
                inkommande_samlat.append(inkommande_text); 
                ink.add(inkommande_text);      
        }
         inkommande.close();
         
        System.out.println("Ink:");
        for(int k = 0; k < ink.size(); k++){
            System.out.println(ink.get(k));
            
        }
       
        
        //Variabler
        String StringStorlek = ink.get(0);
        IntStorlek = Integer.parseInt(StringStorlek);
        String [] link = new String[IntStorlek];
        linkNod1 = new int[IntStorlek];
        linkNod2 = new int[IntStorlek];
        String [] sline;
      
        for(int k = 1; k <IntStorlek+1 ; k++){
            sline = ink.get(k).split(";");
            link[k-1] = sline[1];
           // System.out.println(link[k-1]);         
        }

        for(int j = 0; j <IntStorlek; j++){
            sline = link[j].split(",");    
            linkNod1[j] =Integer.parseInt(sline[0]);
            linkNod2[j] =Integer.parseInt(sline[1]);
//           System.out.println(linkNod1[j]);
//           System.out.println(linkNod2[j]);
           
        }
        
//Nu har vi nod-nr på uppdragen. Dags att beräkna avstånd! 
        //Sätt startnod och slutnod i ds till robotens position och linkNod1
    
        
        //Här borde en loop börja
            ds.startRutt = 2+1;     //Om man lägger till en etta på noden man vill ha blir det rätt
            ds.slutRutt = 37+1;     //Här behöver man också adda en etta för att få rätt nod
            double tot_kostnad = 0;         //Totala kostnaden för en väg/alla bågar i en väg
            double kostnad = 0;             //Kostnad för en båge
            int total_arccost[];              //Kostnad för samtliga bågar
            
            OptPlan oppis1 = new OptPlan(ds);
            total_arccost = new int[1000];
            oppis1.createPlan();

            //Bågarna i path (med ovanstående noder) ska in som index i tot_arccost. 
            //Sen ska alla arccostnader plussas ihop för att få ut totala kostnaden/avståndet
            for (int i=0; i< oppis1.path.size(); i++){
                
              int vertexint = Integer.parseInt(oppis1.path.get(i).getId()); //Gör om path till ints
                                     
              total_arccost = map.getTotalArcCost();          //PROBLEM!!! Blir null.
              kostnad = total_arccost[vertexint];
              tot_kostnad = tot_kostnad + kostnad;
              
              System.out.println("Oppis1 totalkostnad so far: " + tot_kostnad);

            }
            /* När vi löst det andra problemet så vill vi skapa en lopp som 
            gör detta, alltså vi vill skapa instanser av optplan i loopen */

       }
    
     catch (IOException e) { System.out.print(e.toString()); }
     
    }
     
    
        /** Här ska de hända massa spännande saker.
        *Kolla om det finns uppdrag när vi kommer till upphämtningsplatsen, 
        *kan köras när som helst
        *Måste kalla på denna metod när vi stannar
            *Om det EJ finns, sök ny startnod
            *Om det finns, fortsätt på följande:
        *Spara kapacitet i en string/var, spara samåkning i en string/var
        * 
        * Om inga uppdrag finns, stanna AGVn vid närmsta båge. Sen söker vi efter 
        * en ny upphämtningsplats som har uppdrag.
        */
    
   
    public static String listauppdrag(String plats){
        String x = "Hej"; //Ta bort sen
       
     /**Någon funktion som räknar ut vilken upphämtningsplats som är närmst, spara som en var/string(?)
     *Använd Dijkstra och OptPlan för att hitta närmsta plats
     *Kalla på Compass och kör till platsen
     *String X = "A";
     */
     
     try {
         //Kalla på metoden ovan för att hämta x
         String X = "A"; //DENNA SKA VA MED I STEGET OVAN
         //Uppdrag http = new Uppdrag();
         String url = " http://tnk111.n7.se/listauppdrag.php?plats=" + X; //plats=A ska inte vara hårdkodad,utan beror på vilken plats som är närmst
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

         System.out.println("\nAnropar: " + url);
 
         int mottagen_status = anslutning.getResponseCode();

         System.out.println("Statuskod: " + mottagen_status);

 
         BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
 
        while ((inkommande_text = inkommande.readLine()) != null) {
                inkommande_samlat.append(inkommande_text);
                
                String ink_sam = inkommande_samlat.toString();
               // String[] ink_s = ink_sam.split(" ");
               //System.out.println(ink_s);
               for(String ink : ink_sam.split(" ")){
                    System.out.println(ink);}
        }
        
       
        inkommande.close();
  
        }
    
     catch (Exception e) { System.out.print(e.toString()); }
      return x; //Ta bort sen
    }     
    
    //Kan behövas ändras till en String[], själva metoden
    public static String tauppdrag(String plats, String id, String pax, String grupp){
       
        /**Ta första uppdaget och kolla om kapacitet är ok
            *Om kapacitet är ok --> kolla om samåkning är ok
                *Om samåkning är ok, kolla vidare i listan och spara "nuvarande" passagerare
                *Om samåkning EJ är ok, anropa nekas/beviljas 
                    *Om beviljas --> Dijkstra/OptPlan/Compass -->kör
                    *Om nekas --> sök nytt uppdrag
            *Om kapacitet Ej är ok, kolla vidare i uppdragslistan
            *Om kapacitet är = antal passagerare, kör anropa nekas/beviljas
                    *Om beviljas  --> Dijkstra/OptPlan/Compass -->kör
                    *Om nekas --> sök nytt uppdrag
        */
    try {

         //Uppdrag http = new Uppdrag();
         //Glöm ej att ändra gruppnummer till 2
         String X = "Test";
//         String id = "1";
//         String pax = "8";
//         String grupp = "2";
         String url = " http://tnk111.n7.se/tauppdrag.php?plats=" + plats + "&id="+id+"&passagerare="+pax+"&grupp="+grupp; 
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

         System.out.println("\nAnropar: " + url);
 
         int mottagen_status = anslutning.getResponseCode();

         System.out.println("Statuskod: " + mottagen_status);

 
         BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
 
        while ((inkommande_text = inkommande.readLine()) != null) {
                inkommande_samlat.append(inkommande_text);
        }
        //String [] ink_sam = inkommande_samlat.split(" "); Skapa mellanrum mellan de olika raderna EJ KLART
        inkommande.close();
        
        //Testa följande typ av loop för att försöka splitta
//        for (int i=0; i < nodes; i++){
//                line = scanner.nextLine();
//                //split space separated data on line
//                sline = line.split(" ");
//                nodeX[i] = Double.parseDouble(sline[1].trim());

        System.out.println(inkommande_samlat.toString());
        System.out.println("Hej");
        
        }
    
     catch (Exception e) { System.out.print(e.toString()); }
       
    return plats;
    }
     
    public static String aterstall(String scenario){
    return scenario;
    }

    public static void messtogroup() {
       //skicka meddelande till den andra gruppen 
     try {
            //Olika if-satser beroende på vad vi vill skicka
                //if x = 1, y = uppdragsid + pax + antal vi tar
                //if x = 2, 
         String x = "Hej";
         String y = "test"; 
         //Uppdrag http = new Uppdrag();
         String url = " http://tnk111.n7.se/putmessage.php?groupid=23&messagetype= " +x+"&message="+y; 
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

         System.out.println("\nAnropar: " + url);
 
         int mottagen_status = anslutning.getResponseCode();

         System.out.println("Statuskod: " + mottagen_status);

 
         BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
 
        while ((inkommande_text = inkommande.readLine()) != null) {
                inkommande_samlat.append(inkommande_text);
        }
   
        inkommande.close();
              

        System.out.println(inkommande_samlat.toString());
        }
    
     catch (IOException e) { System.out.print(e.toString()); }
     
    }

     public static void messfromgroup() {
       //meddelande från den andra gruppen 
     try {
            
         String x = "Hej"; 
         //Uppdrag http = new Uppdrag();
         String url = "  http://tnk111.n7.se/getmessage.php?messagetype=" + x; //x är det meddelande vi hämtar
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

         System.out.println("\nAnropar: " + url);
 
         int mottagen_status = anslutning.getResponseCode();

         System.out.println("Statuskod: " + mottagen_status);

 
         BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
 
        while ((inkommande_text = inkommande.readLine()) != null) {
                inkommande_samlat.append(inkommande_text);
        }
   
        inkommande.close();
              

        System.out.println(inkommande_samlat.toString());
        }
    
     catch (IOException e) { System.out.print(e.toString()); }
     
    }    
    
public void avstand() {

// double [] distance; 
// double xstart;
// double ystart; 
// double xslut;
// double yslut;  
//
//                  
// distance = new double [IntStorlek];    
//         
//
//        
//            for (int i =0; i<IntStorlek; i++) {
//            xstart= ds.nodeX[linkNod1[i]];
//            //System.out.println("Startnod: " + linkNod1[i]);
//            xslut = ds.nodeX[linkNod1[i]];
//            ystart= in.nodY[in.startnod[i]];
//            yslut = in.nodY[in.slutnod[i]];
//            System.out.println("X: " + xstart + " - " + xslut);
//            System.out.println("Y: " + ystart + " - " + yslut);
//            
//            
//            int j=i+1;
//            distance[i] = Math.hypot(xslut-xstart, yslut-ystart);    
//           // System.out.println("Avstånd för uppdrag " + j + ": " + distance[i]);
//            }
//    
//    
    
}     
     
            
     
}
    

