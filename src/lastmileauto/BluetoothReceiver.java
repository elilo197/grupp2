/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lastmileauto;  //Denna är inte rätt, inte heller cui är klar, 

//hcitool scan = visar alla tillgängliga enheter
//sdptool browse 20:16:01:20:56:82:1 

import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;


public class BluetoothReceiver{
  BluetoothTransceiver btc;
  InputStream bluetooth_in;
  StreamConnectionNotifier service;
  
    
    public void BluetoothReceiver(BluetoothTransceiver btc1) {
        this.btc = btc1;
}
    public void recive(ControlUI cui){
        try{
            while(true){
                bluetooth_in = btc.anslutning.openInputStream();
                
                byte buffer[] = new byte[80];
                int antal_bytes = bluetooth_in.read(buffer);
                String mottaget = new String(buffer, 0, antal_bytes);
                System.out.println("/n" + "Mottaget meddelande: " + mottaget);
                cui.appendStatus("/n" + "Mottaget meddelande: " + mottaget);
            }
        }catch (IOException e){
            System.out.print(e.toString());
            System.out.println("Gick dåligt!!");
        }
    }
    
}
