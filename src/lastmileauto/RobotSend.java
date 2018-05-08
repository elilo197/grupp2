
package lastmileauto;


public class RobotSend implements Runnable{
long start;  
DataStore ds;
      
public RobotSend(DataStore ds){
    this.ds = ds;  
}      

@Override
public void run(){

    try {
    Thread.sleep(2000);     //Detta är för att ds.kommandon ska hinna köras och få värden innan denna tråd startar
   
    if (ds.breakflag == 0) {    
        try { 
            
            System.out.println("Kommandon: " + ds.kommandon);
            
            for(int i = 0; i <ds.kommandon.size(); i++){
            //dcount räknar antalet "D" (Done) som fås från AGVn
            
               while(ds.dcount == i){
                Thread.sleep(1000);

                ds.ncount = i; //Räknar antalet noder som passeras 
                String kommando =ds.kommandon.get(i); 
                ds.btm.send(kommando);
                ds.cui.appendStatus("Skickat meddelande: " + ds.kommandon.get(i));

                }
               
               if (ds.kommandon.get(i).equals("S")) {
                   ds.scount = ds.scount +1;
                   ds.kommandon.clear();            //Tömmer kommandon när alla är körda
                   ds.paxInt = 0;                   //Nollställer passagerarantal
                 //  ds.passant  = new int[IntStorlek]; 

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