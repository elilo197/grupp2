

// Fråga till clas: kan vi på något sätt se om det finns en anslutning ? 
package lastmileauto;
//Denna tror vi ska kopplas ihop med Bluetooth reciver. 
import java.util.*;

public class RobotRead implements Runnable  {
    int sleepTime;
    private static Random generator = new Random();
    ControlUI cui;
    DataStore ds;
    GuiUpdate gui; 
    BluetoothReceiver bre;
    BluetoothTransceiver btc;
    long start;
    long timemilli = 0;
    int mottagenInt;
    String meddelande; 

    public RobotRead(DataStore ds, ControlUI cui ){       //, BluetoothReceiver bre) {
        this.cui = cui;
        this.ds = ds;
        this.bre = bre;
        sleepTime = generator.nextInt (20000);
    }

@Override
public void run () {
 while (true) {// timenano = System.nanoTime();
//    mottagenInt = Integer.parseInt(bre.mottaget); //Gör om deras string till en int innehåll nodnummer.

    //meddelande = bre.getMessage();
    if(meddelande != null){
        cui.appendStatus(meddelande);
    }  
    
   start = System.currentTimeMillis(); //start tid 
   System.out.println("Starttid: " + System.currentTimeMillis());
   
   
   while (System.currentTimeMillis() - start < 2500){
      
       // System.out.println("Tiden i millisekunder är: " + (System.currentTimeMillis() - start));
//        if (bre.mottaget == XY){
            // här vill vi kalla på gui update. 

        System.out.println("Tiden i millisekunder är: " + (System.currentTimeMillis() - start));
       // System.out.println("Tiden i millisekunder är: " + (System.currentTimeMillis() - start));
//        if (bre.mottaget == XY){
            // här vill vi kalla på gui update. 
        //System.out.println("Tiden i millisekunder är: " + (System.currentTimeMillis() - start));



//        if (bre.mottaget =="ok"){
//          start = System.currentTimeMillis();
//      }
//        else{
//           gui = new GuiUpdate(ds, cui, mottagenInt);
//       }
////    
//   }
   cui.appendStatus("Nu har det gått för lång tid."); 
   //Kolla connection, om ok börja om från run()
   
//Här borde det hända nått för att tiden är ute
// 

//}
 btc = new BluetoothTransceiver(); 
 
 
}
}}}



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