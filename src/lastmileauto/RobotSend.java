
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
  
   for(int i = 0; i <ds.kommandon.size(); i++){//ds.kommandon.size()
        
       while(ds.dcount == i){
        Thread.sleep(1000);
             
        ds.ncount = i;
        String kommando =ds.kommandon.get(i); 
        ds.btm.send(kommando);
        ds.cui.appendStatus("Skickat meddelande: " + ds.kommandon.get(i));
       
       }
      
   }
   ds.dcount = 0; 
      
    }
    catch (InterruptedException exeption) {
        
    }
}
}
