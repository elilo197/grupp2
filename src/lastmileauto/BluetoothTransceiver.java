/*
anslutning = en streamconnenction
bluetooth_ut = en printstram som använder anslutning på nått sätt
 */
package lastmileauto;

//Bluetoothnamn flr Arduino 20:16:01:20:56:82
import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

public class BluetoothTransceiver{
    PrintStream bluetoothanslutning;
    StreamConnection anslutning;
    DataStore ds;
    ControlUI cui;

    public BluetoothTransceiver() {
        
        System.out.println("Nu är vi i tranciver");
        
         try {
             anslutning = (StreamConnection)
                     Connector.open("btspp://201601205682:1");
             bluetoothanslutning = new PrintStream(anslutning.openDataOutputStream());
             cui.appendStatus("Bluetoothanslutning upprättad");
         
       } catch (Exception e) {  System.out.print(e.toString());   }
        
   
    }
    }