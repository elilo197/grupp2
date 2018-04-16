/*
btc = instans av transciever
blutetooth_ut = en printstream skapad i transciever, funkar typ som system.out.print, 
                ska skriva ut variabeln message via btc.bluetooth_ut.println(message)
message = en string med själva meddelandet, skapad i transmitter 
 */

package lastmileauto;

import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

public class BluetoothTransmitter implements Runnable{

 private  DataStore ds;
 private String message;
 private BluetoothTransceiver btc; 
   PrintStream bluetooth_ut;
   StreamConnection anslutning;
 
//private message = new String;
    
 public BluetoothTransmitter (String message)   {
      this.ds = ds;
}
    
@Override    
public void run (){
   
    try {
         PrintStream bluetooth_ut = new 
                    PrintStream(anslutning.openOutputStream());

        
        
         System.out.println(message);
         btc = new BluetoothTransceiver();
         
         //Utan att koppla och istället använda tranciver. 
         
         btc.bluetooth_ut.println(message);             //Om bluetooth_ut från transciever används

   } catch (Exception e) {  System.out.print(e.toString());   }
       
         
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
