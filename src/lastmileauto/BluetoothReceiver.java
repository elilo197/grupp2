
package lastmileauto;
//hcitool scan = visar alla tillgängliga enheter
//sdptool browse 20:16:01:20:56:82:1 

public class BluetoothReceiver implements Runnable{
  BluetoothTransceiver btc;
  DataStore ds;
  String mottaget;
  
    
    public BluetoothReceiver(BluetoothTransceiver btc1) {
        this.btc = btc1;
}
    
    @Override
    public void run(){
        try{
            while(true){
                
                String meddelande_in = btc.bluetooth_in.readLine();
                System.out.println("Mottaget: " + meddelande_in);
     
            }
        }catch (Exception e){System.out.print(e.toString()); }
    }
    
}
