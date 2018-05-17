package lastmileauto;

//Bluetoothnamn för Arduino 20:16:01:20:56:82
import java.io.*;
import javax.microedition.io.*;

public class BluetoothTransceiver{
    BufferedReader bluetooth_in;
    PrintStream bluetoothanslutning;
    
    public BluetoothTransceiver() {
       
        //Skapar anslutningen mellan dator och AGV
        try {
            StreamConnection anslutning = (StreamConnection)
            //Connector.open("btspp://98D331F62137:1"); //Arduino
            Connector.open("btspp://98D331F72A6C:1");  //Agda
              
             bluetoothanslutning = new PrintStream(anslutning.openOutputStream());          
             bluetooth_in = new BufferedReader(new InputStreamReader(anslutning.openInputStream()));

        } catch (Exception e) {System.out.print("Fångad i transceiver-catch." + e.toString());}
    }
}


