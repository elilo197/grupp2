 package lastmileauto; 

import java.io.*;
 import javax.microedition.io.*;
 import javax.bluetooth.*;

public class BluetoothTransmitter {
    public BluetoothTransmitter(){
    try{
    StreamConnection anslutning = (StreamConnection) 
    Connector.open("btspp://00809824156D:8");

    PrintStream bluetooth_ut = new PrintStream(anslutning.openOutputStream());

    bluetooth_ut.println("Test från grupp 2");

    Thread.sleep(500);
    
    anslutning.close();} 
    catch (Exception e) {System.out.print(e.toString());  }  
}
}