/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lastmileauto;


//hcitool scan = visar alla tillgängliga enheter
//sdptool browse 20:16:01:20:56:82:1 

import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

public class BluetoothReceiver {

    public void BluetoothReceiver() {
        System.out.println("YEY det funkade!! ");

        try {

            StreamConnectionNotifier service = (StreamConnectionNotifier) 

          Connector.open("btspp://201601205682:1" + new UUID(0x1101).toString() + 
                  ";name=TNK111-test");

            StreamConnection anslutning = (StreamConnection) 
        service.acceptAndOpen();
             InputStream bluetooth_in = anslutning.openInputStream();

             byte buffer[] = new byte[80];
             int antal_bytes = bluetooth_in.read(buffer);
             String mottaget = new String(buffer, 0, antal_bytes);

             System.out.println("\n"+"Mottaget meddelande: " + mottaget);
              //cui.appendStatus("\n"+"Mottaget meddelande: " + mottaget);

             anslutning.close();

        } catch (IOException e) 
{   System.err.print(e.toString());    }
}
    
}
