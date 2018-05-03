
package lastmileauto;


public class RobotSend implements Runnable{
    
long start;    
    
    
    
public RobotSend(){
    
}    
    

@Override
public void run(){
    
start = System.currentTimeMillis(); //starttid 
   System.out.println("Starttid: " + System.currentTimeMillis());
     
   while (System.currentTimeMillis() - start == 2500){          //Skicka när det gårr 2500 ms
   start = System.currentTimeMillis();      //Starta om tidräkningen
   
   
   
       
       
   }    
    
    
    
    
    
}
    
}
