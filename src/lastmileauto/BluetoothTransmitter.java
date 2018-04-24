/*
btc = instans av transciever
blutetooth_ut = en printstream skapad i transciever, funkar typ som system.out.print, 
                ska skriva ut variabeln message via btc.bluetooth_ut.println(message)
message = en string med själva meddelandet, skapad i transmitter 
 */

package lastmileauto;

public class BluetoothTransmitter{

 private BluetoothTransceiver btc; 

//private message = new String;
    
 public BluetoothTransmitter (BluetoothTransceiver btc1)   {
      this.btc = btc1;
}
    
public void send(String message){
   btc.bluetoothanslutning.println(message);
        }
}
