/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Kan vi använda Timer istället för trådning i denna?

package lastmileauto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.*;
public class Uppdrag {
    
    public Uppdrag() {
    
    //System.out.println("Vi är i Uppdrag! Good job!");    
        
     try {

         //Uppdrag http = new Uppdrag();
         String url = " http://tnk111.n7.se/listaplatser.php"; 
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
               

        System.out.println(inkommande_samlat.toString());
        }
    
     catch (Exception e) { System.out.print(e.toString()); }
     
     //Någon funktion som räknar ut vilken upphämtningsplats som är närmst, spara som en var/string(?)
     //Använd Dijkstra och OptPlan för att hitta närmsta plats
     //Kalla på Compass och kör till platsen
     //String X = "A";
     
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
     
     //Här ska de hända massa spännande saker.
     //Kolla om det finns uppdrag
            //Om det EJ finns, sök ny startnod
            //Om det finns, fortsätt på följande:
        //Spara kapacitet i en string/var, spara samåkning i en string/var
        //Ta första uppdaget och kolla om kapacitet är ok
            //Om kapacitet är ok --> kolla om samåkning är ok
                //Om samåkning är ok, kolla vidare i listan och spara "nuvarande" passagerare
                //Om samåkning EJ är ok, anropa nekas/beviljas 
                    //Om beviljas --> Dijkstra/OptPlan/Compass -->kör
                    //Om nekas --> sök nytt uppdrag
            //Om kapacitet Ej är ok, kolla vidare i uppdragslistan
            //Om kapacitet är = antal passagerare, kör anropa nekas/beviljas
                    //Om beviljas  --> Dijkstra/OptPlan/Compass -->kör
                    //Om nekas --> sök nytt uppdrag
    
            
     
}
    
}
