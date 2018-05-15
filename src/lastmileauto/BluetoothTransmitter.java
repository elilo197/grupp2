package lastmileauto;

public class BluetoothTransmitter{

 BluetoothTransceiver btc; 
 
 public BluetoothTransmitter (BluetoothTransceiver btc1)   {
      this.btc = btc1;
}
    
//Metod som skickar meddelande till AGVn med anslutningen från BluetoothTransceiver
public void send(String message){
    btc.bluetoothanslutning.println(message);
   
}
}
