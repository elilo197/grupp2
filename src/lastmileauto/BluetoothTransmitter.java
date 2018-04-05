/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lastmileauto;

import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

public class BluetoothTransmitter {

 private  DataStore ds;
    
    public BluetoothTransmitter() {
        this.ds = ds;
         System.out.println("Vi är i Trasnmitter! Awesome!");

//    try {
//        StreamConnection anslutning = (StreamConnection) 
//    Connector.open("btspp://001A7DDA7106:1");
//        
//       PrintStream bluetooth_ut = new 
//        PrintStream(anslutning.openOutputStream());
//
//        bluetooth_ut.println(ds.F);
//       
//        Thread.sleep(500);
//        anslutning.close();
//
//    } catch (Exception e) {  System.out.print(e.toString());   }
 }
}
