package lastmileauto;

public class BluetoothTransmitter{


 BluetoothTransceiver btc; 

 
 public BluetoothTransmitter (BluetoothTransceiver btc1)   {
      this.btc = btc1;
}
    
 //Skapar en metod som skickar meddelande till AGVn med anslutningen från 
 //bluetoothTansceiver
public void send(String message){
   btc.bluetoothanslutning.println(message);
        }


}
