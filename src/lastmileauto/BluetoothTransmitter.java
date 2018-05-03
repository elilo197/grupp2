package lastmileauto;

public class BluetoothTransmitter{


 BluetoothTransceiver btc; 

    
 public BluetoothTransmitter (BluetoothTransceiver btc1)   {
      this.btc = btc1;
}
    
public void send(String message){
   btc.bluetoothanslutning.println(message);
        }

<<<<<<< HEAD
 
=======

>>>>>>> 8fec529fb1af660d56c8f9c28da62959e3bbf9b0

}
