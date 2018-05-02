
package lastmileauto;
//hcitool scan = visar alla tillgängliga enheter
//sdptool browse 20:16:01:20:56:82:1 

public class BluetoothReceiver implements Runnable{
  BluetoothTransceiver btc;
  DataStore ds;
  
    
    public BluetoothReceiver(BluetoothTransceiver btc1, DataStore ds) {
        this.btc = btc1;
        this.ds = ds;
}
    
    @Override
    public void run(){
        try{
            while(true){
                
                ds.meddelande_in = btc.bluetooth_in.readLine();
                System.out.println("Mottaget: " + ds.meddelande_in);
     
            }
        }catch (Exception e){System.out.print(e.toString()); }
    }
    
}
