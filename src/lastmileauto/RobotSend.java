
package lastmileauto;


public class RobotSend implements Runnable{
long start;  
DataStore ds;
int countPax = 0;

      
public RobotSend(DataStore ds){
    this.ds = ds;  
}      

@Override
public void run(){

    try {
    Thread.sleep(500);     //Detta är för att ds.kommandon ska hinna köras och få värden innan denna tråd startar
   
    if (ds.breakflag == 0) {    
        try { 
            System.out.println("Kommandon_done: " + ds.kommandon_done);
       
            
            for(int i = 0; i <ds.kommandon_done.size(); i++){
            //dcount räknar antalet "D" (Done) som fås från AGVn
            
             int dummyInt  = ds.paxRutt;
            String dummyString = Integer.toString(dummyInt);

            
               while(ds.dcount == i){
                Thread.sleep(1000);

                ds.ncount = i; //Räknar antalet noder som passeras 
                ds.cui.appendStatus("Antal utförda svängar: " +ds.dcount);
                String kommando =ds.kommandon_done.get(i) + dummyString;
                ds.btm.send(kommando);
                ds.cui.appendStatus("Skickat meddelande: " + ds.kommandon_done.get(i) + dummyString);
                }

               if (ds.kommandon_done.get(i).equals("S")) {
                   ds.scount = ds.scount +1;
                   ds.kommandon_done.clear();            //Tömmer kommandon när alla är körda
                   ds.paxRutt = 0;                   //Nollställer passagerarantal
                   ds.pax = 0; 
                 //  ds.passant  = new int[IntStorlek]; 
                 countPax = 0;
                 ds.taUppdrag = false; 
                 ds.skickatP = false; 

               }
               else if (ds.kommandon_done.get(i).equals("P")){
                   countPax  = 1 ; 
                   ds.paxRutt = ds.pax; 
                   ds.taUppdrag = true; 
                   Thread.sleep(1000);

               }
               
                }
                     ds.dcount = 0; 

                }   catch (InterruptedException exeption) {
              }
             }
 
    else if (ds.breakflag == 1) {
                try {Thread.sleep(10000);
            }   catch (InterruptedException exeption) {
               }
        }
   
    
} catch (InterruptedException exeption) {
               }   
}
}