/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lastmileauto;
import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

/**
 *
 * @author eliselord
 */
public class BluetoothTransceiver {
       public BluetoothTransceiver(){
    try {
        StreamConnection anslutning = (StreamConnection) 
    Connector.open("btspp://00809824156D:8");
        
    PrintStream bluetooth_ut = new PrintStream(anslutning.openOutputStream());
    
    BufferedReader bluetooth_in = new BufferedReader(new InputStreamReader(anslutning.openInputStream()));
    
    BufferedReader tangentbord = new BufferedReader(new InputStreamReader(System.in));

    while(true) {
        String meddelande_ut = tangentbord.readLine();
        if (meddelande_ut==null) {
            break;
        }
        bluetooth_ut.println(meddelande_ut);
        String meddelande_in = bluetooth_in.readLine();
        System.out.println("Mottaget: " + meddelande_in);
    }

    anslutning.close();
    } catch (Exception e) {  System.out.print(e.toString());   }
       }   
}
