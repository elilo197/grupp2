/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lastmileauto;
//Denna tror vi ska kopplas ihop med Bluetooth reciver. 
import java.util.*;

public class RobotRead implements Runnable {
    private int sleepTime;
    private static Random generator = new Random();
    private ControlUI cui;
    private DataStore ds;
    private BluetoothReceiver bre;
    String Status; 
    Timer timer; 
    long time;

    public RobotRead(DataStore ds, ControlUI cui, BluetoothReceiver bre) {
        this.cui = cui;
        this.ds = ds;
        this.bre = bre;
        sleepTime = generator.nextInt (20000);
    }

@Override
public void run () {
   // timer = new Timer();
time = System.nanoTime();

    if(time < 250){
        System.out.println("Tiden är: " + time);
//        if (bre.mottaget == XY){
//        


//          } 


//        if (bre.mottaget == 1){
//        time = 0;
//    } 
    }
    else{
        cui.appendStatus("Något är fel");      
    }
    

}
}




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