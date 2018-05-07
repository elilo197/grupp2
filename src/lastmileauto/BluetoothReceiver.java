
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
        try{
  
        start = System.currentTimeMillis(); //initiering starttid 

            //Kör följande loop så länge det inte gått mer än 2500 ms
            while (System.currentTimeMillis() - start < 2500){

              ds.meddelande_in = btc.bluetooth_in.readLine();

                //Korsning avklarad, meddelande in är ett D
                if(ds.meddelande_in.equals("D")){                     
                    ds.cui.appendStatusAgv("Korsning passerad.");
                    start = System.currentTimeMillis(); //starta om tiden
                    ds.dcount = ds.dcount +1;           //räkna upp för att skicka nytt meddelande
                }

                //Meddelande in är allt annat än en nod
                else{
                     
                     try {
                        ds.meddelande_int = Integer.parseInt(ds.meddelande_in); //Gör om string till in
                        ds.cui.appendStatusAgv("Nod " + ds.meddelande_int);             
                        ds.robotX = ds.nodeX[ds.meddelande_int-1];  //Sätt x-koordinat på roboten baserat på inkommande nodnr
                        ds.robotY = ds.nodeY[ds.meddelande_int-1];  //Sätt x-koordinat på roboten baserat på inkommande nodnr
                        ds.cui.repaint();
                     } catch (NumberFormatException nfe){       //Om meddelande_in inte är en int hamnar vi här
                      ds.cui.appendStatusAgv("Fel typ av meddelande. Kan ej behandlas.");
                     
                     start = System.currentTimeMillis();//starta om tiden
                     }



                }
            } // Utanför While-loopen. 



   ds.cui.appendStatusAgv("Nu har det gått för lång tid. Är det något fel på Agda?"); 
  
        }catch (Exception e){System.out.print("\n Fångad i catch: " + e.toString()); }
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
