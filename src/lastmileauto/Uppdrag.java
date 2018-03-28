/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lastmileauto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Helena
 */
public class Uppdrag {
    
    //http://tnk111.n7.se/listaplatser.php}
    public static void main(String[] args) {
        // TODO code application logic here
     try {

         Uppdrag http = new Uppdrag();
         String url = " http://facebook.se"; 
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
    
     catch (Exception e) { System.out.print(e.toString()); }

}
}
