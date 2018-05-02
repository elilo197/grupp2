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
    OptPlan [] oppis;
   String narmstaPlats;// = "Start";
   String id; 
   String pax;
   String [] uppdragsid;
   String [] destination;
   int [] passant; 
   int [] samaka;
   int [] poang;
   int[] destNod1;
   int[] destNod2;
   String valtUppdrag;// = "Start";
   String svar;
   OptPlan oppis1;
   OptPlan oppis2;
   MapPanel map;
   



    

    public Uppdrag(DataStore ds) {
        this.ds = ds;
        listaplatser();
        valtUppdrag = listauppdrag(narmstaPlats);           //Skickar in upphämtningsplats, skickar ut vilket uppdrag vi väljer
        pax = getPassagerare(valtUppdrag);                  //Skickar ut passagerarantal på det valda uppdraget
        
        
       String svaruppdrag = tauppdrag(narmstaPlats, valtUppdrag, pax, ds.grupp);
       
            if (svaruppdrag.equals("beviljas")){
                
            ds.startRutt = ds.robotpos;        
            ds.slutRutt = linkNod1[Integer.parseInt(valtUppdrag)-1];
                         
            oppis1 = new OptPlan(ds);
            oppis1.createPlan();
            map = new MapPanel(ds);
//            map.paintComponent(g);
//            
            ds.startRutt = linkNod1[Integer.parseInt(valtUppdrag)-1];       
            ds.slutRutt = destNod1[Integer.parseInt(valtUppdrag)-1];
                         
            oppis2 = new OptPlan(ds);
            oppis2.createPlan();
            
            
            
                
                
            ds.startRutt = destNod1[Integer.parseInt(valtUppdrag)-1];
  
        }
        else {System.out.println("Svar från hemsida: " + svaruppdrag);}
        
        
        
        aterstall("1");
        
        
    
    }

    /** Här listar vi antalet upphämtningsplatser och beräknar vilken som är närmast. 
     */
    public void listaplatser() { //tagit bort static
   
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
        
        
         ArrayList<String> ink = new ArrayList<String>();

         
        while ((inkommande_text = inkommande.readLine()) != null) {
            System.out.println("Inkommande: " + inkommande_text);
                inkommande_samlat.append(inkommande_text); 

                 inkommande_samlat.append(inkommande_text); 
                ink.add(inkommande_text);      
        }
         inkommande.close();
         
            for(int k = 0; k < ink.size(); k++){
            System.out.println("Ink: " + ink.get(k));
         }
       
        
        //Variabler
        String StringStorlek = ink.get(0);
        IntStorlek = Integer.parseInt(StringStorlek);
        String [] link = new String[IntStorlek];
        linkNod1 = new int[IntStorlek];
        linkNod2 = new int[IntStorlek];
        oppis = new OptPlan[IntStorlek];
        String [] sline;
        String [] plats = new String[IntStorlek];
        double tot_kostnad = 0;         //Totala kostnaden för en väg/alla bågar i en väg
        int narmstaNod = ds.start;
        double lagstaKostnad = 1000000;
        double kostnad = 0;             //Kostnad för en båg
      
        for(int k = 1; k <IntStorlek+1 ; k++){
            sline = ink.get(k).split(";");
            link[k-1] = sline[1];
            plats[k-1] = sline[0];       
        }

        for(int j = 0; j <IntStorlek; j++){
            sline = link[j].split(",");    
            linkNod1[j] =Integer.parseInt(sline[0]);
            linkNod2[j] =Integer.parseInt(sline[1]);         
        }
        
//Nu har vi nod-nr på uppdragen. Dags att beräkna avstånd! 
                
        //Här borde en loop börja
        for (int j=0; j<IntStorlek; j++){       //Den här ska loopa över alla upphämtningsplatser
            ds.startRutt = ds.robotpos;        
            ds.slutRutt = linkNod1[j];
            //System.out.println("Startnod: " + ds.startRutt + ", slutnod: " + ds.slutRutt);
             
            oppis[j] = new OptPlan(ds);
            oppis[j].createPlan();
            
            //Den här loopen räknar ut kostnaden för rutten till den aktuella upphämtningsplatsen
            for (int i=0; i< oppis[j].path.size(); i++){      
         
            int vertexint = Integer.parseInt(oppis[j].path.get(i).getId()); //Gör om path till ints
             
             // total_arccost = map.getTotalArcCost();          //PROBLEM!!! Blir null.
             // Istället för detta skriver vi bara ds.tot_arcCost[i] och den vi vill ha se nedan.                                  
              kostnad = ds.tot_arcCost[vertexint];
              tot_kostnad = tot_kostnad + kostnad;
              
            }
             System.out.println("Upphämtningsplats " + plats[j] + " innebär en rutt från " + ds.startRutt + " till "  
                   + ds.slutRutt + " vilket ger en totalkostnad på "  + tot_kostnad);
           
             System.out.println();      //Blankrad
                
             if (tot_kostnad < lagstaKostnad){
                 lagstaKostnad = tot_kostnad;
                 narmstaPlats = plats[j];
                 narmstaNod = ds.slutRutt;
             }
             
        }
             System.out.println("Närmaste upphämtningsplats är plats " + narmstaPlats + " med nodnummer " + narmstaNod);
             System.out.println("Kostnaden för att ta sig dit är " + lagstaKostnad);

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
    
   
    public String listauppdrag(String plats){      //var static från början
       ArrayList<String> inkuppdrag = new ArrayList<String>();
             
     /**
     *Kalla på Compass och kör till platsen
     *String X = "A";
     */
     
     try {
         //Kalla på metoden ovan för att hämta x
        // String X = "A"; //DENNA SKA VA MED I STEGET OVAN
         //Uppdrag http = new Uppdrag();
         String url = " http://tnk111.n7.se/listauppdrag.php?plats=" + plats; //plats=A ska inte vara hårdkodad,utan beror på vilken plats som är närmst
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
                inkuppdrag.add(inkommande_text);  
          }

          inkommande.close();
  
             for(int k = 0; k < inkuppdrag.size(); k++){
               }
        
        //Variabler
        String StringStorlek = inkuppdrag.get(0);
        IntStorlek = Integer.parseInt(StringStorlek); 
        String [] sline;
        uppdragsid = new String[IntStorlek];
        destination  = new String[IntStorlek];
        passant  = new int[IntStorlek]; 
        samaka  = new int[IntStorlek];
        poang = new int[IntStorlek];
        destNod1 = new int[IntStorlek];
        destNod2 = new int[IntStorlek];
     
      
        for(int k = 1; k <IntStorlek+1 ; k++){
            sline = inkuppdrag.get(k).split(";");
            uppdragsid[k-1] = sline[0];
            destination[k-1] = sline[1];
            passant[k-1] = Integer.parseInt(sline[2]);
            samaka[k-1] = Integer.parseInt(sline[3]);
            poang[k-1] = Integer.parseInt(sline[4]);
            System.out.println("Uppdrag nr "  + uppdragsid[k-1] + " vill åka till " + destination[k-1]
            + ", har " + passant[k-1] + " st passagerare, har följande åsikt till samåkning: " + samaka[k-1]
            + " och ger " + poang[k-1] + " poäng.");      
        }
        
             
        for(int j = 0; j <IntStorlek; j++){
            sline = destination[j].split(",");    
            destNod1[j] =Integer.parseInt(sline[0]);
            destNod2[j] =Integer.parseInt(sline[1]);
            System.out.println("Destinationens noder är: " + destNod1[j] + " och " + destNod2[j]);
        }
        
          System.out.println("Uppdragsid: " + uppdragsid[0]);
 
        //Välj uppdrag
        for (int j=0; j<IntStorlek; j++){

            if (passant[j]<=ds.kapacitet) {
                valtUppdrag = uppdragsid[j];
                break;
                   
            }
            
            else if (j==IntStorlek-1) {
                System.out.println("För mycket folk!");
                //Här borde vi börja om på nått sätt, typ kalla på listaplatser och listauppdrag igen
            }
        }
        }
          
    
     catch (Exception e) { System.out.print(e.toString()); }
     
     System.out.println("Valt uppdrag: " + valtUppdrag);
     return valtUppdrag;
    }     

    public String getId(String valtUppdrag){
        id = uppdragsid[Integer.parseInt(valtUppdrag)];
        return id;
    }
   
      public String getPassagerare(String valtUppdrag){
      int uppdragInt = Integer.parseInt(valtUppdrag);
      System.out.println("Passant index: " + Integer.parseInt(valtUppdrag));
       int paxInt = passant[Integer.parseInt(valtUppdrag)-1]; 
   
        pax = Integer.toString(paxInt);  
        //pax = "2";
      return pax; 
    }
      
    
    //Kan behövas ändras till en String[], själva metoden
    public  String tauppdrag(String plats, String id, String pax, String grupp){    //Denna var static
       System.out.println("I tauppdrag kommer följande in: Plats: " + plats + " Id: " + id + " Pax: " + pax + " Grupp: " + grupp);
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
        
        String inkommande_string = inkommande_samlat.toString();
        System.out.println(inkommande_string);
        svar = inkommande_string;

        }
    
     catch (Exception e) { System.out.print(e.toString()); }
       
    return svar;
    }
     
    public String aterstall(String scenario){       //var static från början
         try {

         String url = " http://tnk111.n7.se/aterstall.php?scenario=" + scenario; 
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

        System.out.println("\nAnropar: " + url);
 
        BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
 
        while ((inkommande_text = inkommande.readLine()) != null) {
                inkommande_samlat.append(inkommande_text);
        }
  
        inkommande.close();
        
        String inkommande_string = inkommande_samlat.toString();
        System.out.println(inkommande_string);
         }
          catch (Exception e) { System.out.print(e.toString()); }
    return svar;
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
    
   
            
     
}
    

