package lastmileauto;


import java.util.*;

public class RobotRead implements Runnable  {
    int sleepTime;
    private static Random generator = new Random();
    ControlUI cui;
    DataStore ds;
    BluetoothReceiver bre;
    BluetoothTransceiver btc;
    long start;
    String meddelande; 

    public RobotRead(DataStore ds, ControlUI cui ){ 
        this.cui = cui;
        this.ds = ds;
        sleepTime = generator.nextInt (20000);
    }

@Override
public void run () {
 while (true) {
     
    if(ds.meddelande_in != null){
        cui.appendStatus(ds.meddelande_in);
    }  
    
   start = System.currentTimeMillis(); //starttid 
   System.out.println("Starttid: " + System.currentTimeMillis());
     
   while (System.currentTimeMillis() - start < 2500){
      
        System.out.println("Tiden i millisekunder är: " + (System.currentTimeMillis() - start));

            if(ds.meddelande_in.equals("D")){
                System.out.println("Vi fick meddelandet: " + ds.meddelande_in +". Borde vara D");
                start = System.currentTimeMillis();
                ds.dcount = ds.dcount +1; 
            }
            else if (isNumeric(ds.meddelande_in) == true) {//Vi fick in en nod.
                 ds.mottagenInt = Integer.parseInt(ds.meddelande_in); //Gör om deras string till en int innehåll nodnummer.
                 start = System.currentTimeMillis();
           
            }
            else{  
                //cui.appendStatus("Oläsbart värde: " + ds.meddelande_in);
            }
} // Utanför While-loopen. 
   cui.appendStatus("Nu har det gått för lång tid. Är det något fel på Agda?"); 
   //Vi borde kolla hur hon mår och om vi kan hjälpa till


   
   
}}

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




//Från labben; 
//    try { 
//        cui.appendStatus("RobotRead kommer att köra i " + sleepTime + 
//                " millisekunder.");
//        
//           //int i = 1;
//           while (true) {
//            Thread.sleep (sleepTime / 20);
//            cui.appendStatus("Jag är tråd RobotRead!");
//            //cui.appendStatus("Jag är tråd RobotRead! För "+i+":te gången.");
//            bre.BluetoothReceiver(cui);
//            
////            if (i ==11){
////                ds.updateUIflag=true;
////            }
////            i++;
//          
//            }
//        } catch (InterruptedException exception) {
//        }
//    
//        cui.appendStatus("RobotRead är nu klar!");
//    }