
package lastmileauto;

//Bluetoothnamn flr Arduino 20:16:01:20:56:82
import java.io.*;
import javax.microedition.io.*;

public class BluetoothTransceiver{
    //String meddelande_in;
     BufferedReader bluetooth_in;
    PrintStream bluetoothanslutning;
    
    public BluetoothTransceiver() {
        
       try {
             StreamConnection anslutning = (StreamConnection)
             Connector.open("btspp://201601205682:1");
             bluetooth_in = new BufferedReader(new InputStreamReader(anslutning.openInputStream()));
         
       } catch (Exception e) {  System.out.print(e.toString());   }
    }
    
//    public String getMessage(){
//        return meddelande_in;
//    }
    }


