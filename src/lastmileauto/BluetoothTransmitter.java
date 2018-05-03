package lastmileauto;

public class BluetoothTransmitter{


 BluetoothTransceiver btc; 

    
 public BluetoothTransmitter (BluetoothTransceiver btc1)   {
      this.btc = btc1;
}
    
public void send(String message){
   btc.bluetoothanslutning.println(message);
        }


}
