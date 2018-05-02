

// Fråga till clas: kan vi på något sätt se om det finns en anslutning ? 
package lastmileauto;
//Denna tror vi ska kopplas ihop med Bluetooth reciver. 
import java.util.*;

public class RobotRead implements Runnable  {
    int sleepTime;
    private static Random generator = new Random();
    ControlUI cui;
    DataStore ds;
    BluetoothReceiver bre;
    BluetoothTransceiver btc;
    long start;
    long timemilli = 0;
    int mottagenInt;
    String meddelande; 

    public RobotRead(DataStore ds, ControlUI cui ){       //, BluetoothReceiver bre) {
        this.cui = cui;
        this.ds = ds;
        sleepTime = generator.nextInt (20000);
    }

@Override
public void run () {
 while (true) {// timenano = System.nanoTime();
     
     
    mottagenInt = Integer.parseInt(ds.meddelande_in); //Gör om deras string till en int innehåll nodnummer.

    if(ds.meddelande_in != null){
        cui.appendStatus(ds.meddelande_in);
    }  
    
   start = System.currentTimeMillis(); //start tid 
   System.out.println("Starttid: " + System.currentTimeMillis());
   
   
   while (System.currentTimeMillis() - start < 2500){
      
       // System.out.println("Tiden i millisekunder är: " + (System.currentTimeMillis() - start));
            if(ds.meddelande_in.equals("OK")){
                System.out.println("Vi fick meddelandet: " + ds.meddelande_in +"Borde vara OK");
                start = System.currentTimeMillis();
            }
            else if(ds.meddelande_in.equals("D")){
                System.out.println("Vi fick meddelandet: " + ds.meddelande_in +"Borde vara D");
                
            }
            else{ //Vi fick in en nod. 
                
            }
} // Utanför While-loopen. 
   cui.appendStatus("Nu har det gått för lång tid är det något fel på Agda?"); 
 

}}}



  // System.out.println("Tiden i millisekunder är: " + (System.currentTimeMillis() - start));
       // System.out.println("Tiden i millisekunder är: " + (System.currentTimeMillis() - start));
        //System.out.println("Tiden i millisekunder är: " + (System.currentTimeMillis() - start));



//Från labben; 
//    try { 
//        cui.appendStatus("RobotRead kommer att köra i " + sleepTime + 
//                " millisekunder.");
//        
//           //int i = 1;
//           while (true) {
//            Thread.sleep (sleepTime / 20);
//            cui.appendStatus("Jag är tråd RobotRead!");
//            //cui.appendStatus("Jag är tråd RobotRead! För "+i+":te gången.");
//            bre.BluetoothReceiver(cui);
//            
////            if (i ==11){
////                ds.updateUIflag=true;
////            }
////            i++;
//          
//            }
//        } catch (InterruptedException exception) {
//        }
//    
//        cui.appendStatus("RobotRead är nu klar!");
//    }