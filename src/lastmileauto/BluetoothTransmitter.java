/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lastmileauto;

import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

public class BluetoothTransmitter implements Runnable{

 private  DataStore ds;
 private String message;
 private BluetoothTransceiver btc; 
 
 
//private message = new String;
    
 public BluetoothTransmitter (String message)   {
      this.ds = ds;
}
    
@Override    
public void run (){
    


        
         System.out.println(message);
         btc = new BluetoothTransceiver();
         
         //Utan att koppla och istället använda tranciver. 
<<<<<<< HEAD
         
         btc.bluetooth_ut.println(message); 
=======

         btc.bluetooth_ut.println(message);
>>>>>>> 9b9e0b1f562f2056228e6e5eea32982cf01a8a5c

       
         
       //Från labben;   
//    try {
//        StreamConnection anslutning = (StreamConnection) 
//    Connector.open("btspp://001A7DDA7106:1");
//        
//       PrintStream bluetooth_ut = new 
//        PrintStream(anslutning.openOutputStream());
//
//        bluetooth_ut.println(massage);
//       
//        Thread.sleep(500);
//        anslutning.close();
//
//    } catch (Exception e) {  System.out.print(e.toString());   }


        }
}
