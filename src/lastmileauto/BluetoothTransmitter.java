/*

 */

package lastmileauto;

public class BluetoothTransmitter{

<<<<<<< HEAD
 private BluetoothTransceiver btc; 
=======
 BluetoothTransceiver btc; 

//private message = new String;
>>>>>>> 0c7076932f4d2f650958311f1a588de830ba4bef
    
 public BluetoothTransmitter (BluetoothTransceiver btc1)   {
      this.btc = btc1;
}
    
public void send(String message){
   btc.bluetoothanslutning.println(message);
        }
}
