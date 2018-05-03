
package lastmileauto;


public class RobotSend implements Runnable{
long start;  
DataStore ds;
      
public RobotSend(DataStore ds){
    this.ds = ds;
    
}      

@Override
public void run(){
    
   start = System.currentTimeMillis(); //starttid 
   System.out.println("Starttid: " + System.currentTimeMillis());
     
   while (System.currentTimeMillis() - start >= 2400 && System.currentTimeMillis() - start <= 2600 ){          //Skicka när det gårr 2500 ms
   start = System.currentTimeMillis();      //Starta om tidräkningen
   
   for(int i = 0; i < ds.kommandon.size(); i++){
       
       while(ds.dcount == i){
        ds.ncount = i;
        ds.btm.send(ds.kommandon.get(i));
       
       
//       if(ds.dcount == i){
//           break;
//       }
       }
      
   }
   ds.dcount = 0; 
      
   }       
}
}
