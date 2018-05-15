package lastmileauto;

public class BluetoothReceiver implements Runnable{
BluetoothTransceiver btc;
DataStore ds;
long start;
   
public BluetoothReceiver(BluetoothTransceiver btc1, DataStore ds) {
    this.btc = btc1;
    this.ds = ds;
}
    
@Override
public void run(){
    while (true) {
        try{

        //Initiering av starttid
        start = System.currentTimeMillis();  

            //Kör följande loop så länge det inte gått mer än 250 ms
            while (System.currentTimeMillis() - start < 250){

              ds.meddelande_in = btc.bluetooth_in.readLine();

                //Korsning avklarad, meddelande in är ett D
                if(ds.meddelande_in.equals("D")){                     
                    ds.cui.appendStatusAgv("Korsning passerad.");

                    //Starta om tiden
                    start = System.currentTimeMillis();

                    //Räknar upp för att skicka nytt meddelande
                    ds.dcount = ds.dcount +1;           
                }

                //Meddelande in är allt annat än en nod
                else{

                    try {
                        //Gör om string till int
                        ds.meddelande_int = Integer.parseInt(ds.meddelande_in); 
                        ds.cui.appendStatusAgv("Nod " + ds.meddelande_int); 

                        //Sätter x- och y-koordinat på AGV:n baserat på inkommande nodnummer
                        ds.robotX = ds.nodeX[ds.meddelande_int-1];
                        ds.robotY = ds.nodeY[ds.meddelande_int-1]; 

                        //Uppdaterar utritningen
                        ds.cui.repaint();

                        //Startar tiden
                        start = System.currentTimeMillis();

                    }
                     //Om meddelande_in inte är en int hamnar vi här
                    catch (NumberFormatException nfe){       
                        ds.cui.appendStatusAgv("Fel typ av meddelande. Kan ej behandlas.");

                        //Startar tiden
                        start = System.currentTimeMillis();
                    }
                }
            } // Utanför While-loopen. 

        ds.cui.appendStatusAgv("Nu har det gått för lång tid. Är det något fel på Agda?"); 

        }catch (Exception e){System.out.print("\n Fångad i receiver-catch: " + e.toString()); }
    }
}

//Den här är för att kolla om inkommande string går att göra om till int
public static boolean isNumeric(String str)  {  
    try{  
        double d = Double.parseDouble(str);
        
    }catch(NumberFormatException nfe){return false;} 
    
  return true;  
}    
}
