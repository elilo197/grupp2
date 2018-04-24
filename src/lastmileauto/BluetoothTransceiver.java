/*

 */
package lastmileauto;

//Bluetoothnamn flr Arduino 20:16:01:20:56:82
import java.io.*;
import javax.microedition.io.*;

public class BluetoothTransceiver{
    String meddelande_in;
<<<<<<< HEAD
     BufferedReader bluetooth_in;
=======
    PrintStream bluetoothanslutning;
   // ControlUI cui;
>>>>>>> 0c7076932f4d2f650958311f1a588de830ba4bef

    public BluetoothTransceiver() {
        
       try {
             StreamConnection anslutning = (StreamConnection)
             Connector.open("btspp://201601205682:1");
             bluetooth_in = new BufferedReader(new InputStreamReader(anslutning.openInputStream()));
             //cui.appendStatus("Bluetoothanslutning upprättad");
         
       } catch (Exception e) {  System.out.print(e.toString());   }
    }
    
    public String getMessage(){
        return meddelande_in;
    }
    }