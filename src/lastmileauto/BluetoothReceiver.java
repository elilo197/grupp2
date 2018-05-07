
package lastmileauto;
//hcitool scan = visar alla tillgängliga enheter
//sdptool browse 20:16:01:20:56:82:1 


public class BluetoothReceiver implements Runnable{
  BluetoothTransceiver btc;
  DataStore ds;
    long start;
 //   String meddelande; 
  
    
    public BluetoothReceiver(BluetoothTransceiver btc1, DataStore ds) {
        this.btc = btc1;
        this.ds = ds;
}
    
    @Override
    public void run(){
        try{
             
            while(true){
                start = System.currentTimeMillis(); //initiering starttid 

            //                //System.out.println("Mottaget: " + ds.meddelande_in);
//                ds.cui.appendStatusAgv(ds.meddelande_string);
//                String testtid = Long.toString(System.currentTimeMillis() - start);
//                ds.cui.appendStatusAgv(testtid);
//            
//      
//     
//          ds.meddelande_in = Integer.parseInt(ds.meddelande_string);
              
         while (System.currentTimeMillis() - start < 2500){
             ds.cui.appendStatusAgv(ds.meddelande_in);
              //ds.cui.appendStatusAgv(ds.meddelande_string);
             ds.meddelande_in = btc.bluetooth_in.readLine();
 
            //System.out.println("Tiden i millisekunder är: " + (System.currentTimeMillis() - start));
     
            if(ds.meddelande_in.equals("D")){   //ds.meddelande_in == 100){
                ds.cui.appendStatusAgv("Korsning passerad.");
                start = System.currentTimeMillis();
                ds.dcount = ds.dcount +1; 
            }

            else{//Vi fick in en nod.
                 start = System.currentTimeMillis();
                 ds.cui.appendStatusAgv("Nod " + ds.meddelande_in);  
                 
                 try {
                    ds.meddelande_int = Integer.parseInt(ds.meddelande_in);
                    ds.robotX = ds.nodeX[ds.meddelande_int-1];
                    ds.robotY = ds.nodeY[ds.meddelande_int-1];
                    ds.cui.repaint();
                 } catch (NumberFormatException nfe){
                 ds.cui.appendStatusAgv("Fel typ av meddelande. Kan ej behandlas.");
                 start = System.currentTimeMillis();
             }
                 
                 
                 
            }
        } // Utanför While-loopen. 
         


   ds.cui.appendStatusAgv("Nu har det gått för lång tid. Är det något fel på Agda?"); 
   ds.btstatus = 1;   
  
            }
        }catch (Exception e){System.out.print(e.toString()); }
    }
 
    //Den här är för att kolla om inkommande string går att göra om till int
    public static boolean isNumeric(String str)  
{  
  try  
  {  
    double d = Double.parseDouble(str);  
  }  
  catch(NumberFormatException nfe)  
  {  
    return false;  
  }  
  return true;  
} 
    
}
