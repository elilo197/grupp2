/*

 */
package lastmileauto;

//Bluetoothnamn flr Arduino 20:16:01:20:56:82
import java.io.*;
import javax.microedition.io.*;

public class BluetoothTransceiver{
    String meddelande_in;
    PrintStream bluetoothanslutning;
   // ControlUI cui;

    public BluetoothTransceiver() {
        
       try {
             StreamConnection anslutning = (StreamConnection)
             Connector.open("btspp://201601205682:1");
             BufferedReader bluetooth_in = new BufferedReader(new InputStreamReader(anslutning.openInputStream()));
             //cui.appendStatus("Bluetoothanslutning upprättad");
         
       } catch (Exception e) {  System.out.print(e.toString());   }
    }
    
    public String getMessage(){
        return meddelande_in;
    }
    }