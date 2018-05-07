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

   try {  

 

//   start = System.currentTimeMillis(); //starttid 
//  // System.out.println("Starttid: " + System.currentTimeMillis());
//     
//   while (System.currentTimeMillis() - start < 5000){
//        
       Thread.sleep(100);
     // cui.appendStatusAgv("Meddelande in i while-loop: " + ds.meddelande_in);

            if(ds.meddelande_in.equals("D")){
                cui.appendStatusAgv("Vi fick meddelandet: " + ds.meddelande_in +". Borde vara D");
                start = System.currentTimeMillis();
                ds.dcount = ds.dcount +1; 
            }

            else if (isNumeric(ds.meddelande_in) == true) {//Vi fick in en nod.
                // cui.appendStatusAgv("Här är vi i if-satsen för noder " + ds.meddelande_in);
                 ds.mottagenInt = Integer.parseInt(ds.meddelande_in); //Gör om deras string till en int innehåll nodnummer.
                 cui.appendStatusAgv("Mottagen int: " + ds.mottagenInt);
                 start = System.currentTimeMillis();
            }
            else{  
                cui.appendStatus("Oläsbart värde: " + ds.meddelande_in);
                start = System.currentTimeMillis();
            }
} // Utanför While-loopen. 
  catch (InterruptedException exeption) {
        
    }


 //  cui.appendStatusAgv("Nu har det gått för lång tid. Är det något fel på Agda?"); 
  // ds.btstatus = 1;   
   
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

