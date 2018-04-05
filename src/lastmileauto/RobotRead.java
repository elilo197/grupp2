/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lastmileauto;
//Denna tror vi ska kopplas ihop med Bluetooth reciver. 

import java.util.Random;

public class RobotRead implements Runnable {
    private int sleepTime;
    private static Random generator = new Random();
    private ControlUI cui;
    private DataStore ds;
    private BluetoothReceiver bre;

    public RobotRead(DataStore ds, ControlUI cui, BluetoothReceiver bre) {
        this.cui = cui;
        this.ds = ds;
        this.bre = bre;
        sleepTime = generator.nextInt (20000);
    }

@Override
public void run () {
    try { 
        cui.appendStatus("RobotRead kommer att köra i " + sleepTime + 
                " millisekunder.");
        
           //int i = 1;
           while (true) {
            Thread.sleep (sleepTime / 20);
            cui.appendStatus("Jag är tråd RobotRead!");
            //cui.appendStatus("Jag är tråd RobotRead! För "+i+":te gången.");
            bre.BluetoothReceiver(cui);
            
//            if (i ==11){
//                ds.updateUIflag=true;
//            }
//            i++;
          
            }
        } catch (InterruptedException exception) {
        }
    
        cui.appendStatus("RobotRead är nu klar!");
    }
}
