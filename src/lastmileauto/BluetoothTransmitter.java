/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetoothtransmitter;

import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

public class BluetoothTransmitter {

    public static void main(String args[]) {
    try {
        StreamConnection anslutning = (StreamConnection) 
    Connector.open("btspp://001A7DDA7106:1");
        
       PrintStream bluetooth_ut = new 
        PrintStream(anslutning.openOutputStream());

        bluetooth_ut.println("Test från grupp 2");

        Thread.sleep(500);
        anslutning.close();

    } catch (Exception e) {  System.out.print(e.toString());   }
    }
}
