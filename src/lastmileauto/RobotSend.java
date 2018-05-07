
package lastmileauto;


public class RobotSend implements Runnable{
long start;  
DataStore ds;
      
public RobotSend(DataStore ds){
    this.ds = ds;  
}      

@Override
public void run(){

   
    if (ds.breakflag == 0) {    
        try { 
  
            for(int i = 0; i <ds.kommandon.size(); i++){//ds.kommandon.size()
            //dcount räknar antalet "D" (Done) som fås från AGVn
            
               while(ds.dcount == i){
                Thread.sleep(1000);

                ds.ncount = i; //Räknar antalet noder som passeras 
                String kommando =ds.kommandon.get(i); 
                ds.btm.send(kommando);
                ds.cui.appendStatus("Skickat meddelande: " + ds.kommandon.get(i));

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
   
    }
    
}
