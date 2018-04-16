/*
anslutning = en streamconnenction
bluetooth_ut = en printstram som använder anslutning på nått sätt
 */
package lastmileauto;

//Bluetoothnamn flr Arduino 20:16:01:20:56:82
import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;

public class BluetoothTransceiver implements Runnable {
    PrintStream bluetooth_ut;
    
    ControlUI cui;


    public BluetoothTransceiver() {
         
        
   
    }
   
    
@Override    
public void run (){
    
    System.out.println("Nu är vi i Transceiver! Good job!");
        try {

            StreamConnection anslutning = (StreamConnection) 


                    Connector.open("btspp://201601205682:1");

            
            //testar lite 
            cui.appendBluetoothAdress("Bluetooth adress: 201601205682" );
            cui.appendBluetoothKanal("Kanal: 1");

            PrintStream bluetooth_ut = new 
                    PrintStream(anslutning.openOutputStream());

            BufferedReader bluetooth_in = new BufferedReader(new 
                InputStreamReader(anslutning.openInputStream()));


            BufferedReader tangentbord = new BufferedReader(new 
                    InputStreamReader(System.in));

            while(true) {

                String meddelande_ut = tangentbord.readLine();
              

                if (meddelande_ut==null) {
                            break;

                }

                bluetooth_ut.println(meddelande_ut);
                          
                String meddelande_in = bluetooth_in.readLine();
                System.out.println("Mottaget: " + meddelande_in);
                
                }
               
            
            anslutning.close();

        } catch (Exception e) {  System.out.print(e.toString());   }

}
}

