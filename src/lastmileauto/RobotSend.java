
package lastmileauto;

// Denna klass används för att skicka meddelanden till Agv:n och är en trådad klass. 

public class RobotSend implements Runnable{
long start;  
DataStore ds;
      
public RobotSend(DataStore ds){
    this.ds = ds;  
}      

@Override
public void run(){

    try {
    Thread.sleep(500);    //Detta är för att ds.kommandon ska hinna köras och få värden innan denna tråd startar
        if(ds.breakflag == 0){ //Kör tills knappen avsluta bli intryckt. 
            try { 

//Här skapas en loop som skickar alla kommandom till Agv:n.  
               for(int i = 0; i <ds.kommandon.size(); i++){
                                        //Dcount räknar antalet "Done" som skickats från Agv:n 
                    while(ds.dcount == i){  //Denna while-loop gör så att samma kommando skickas tills att
                    Thread.sleep(1000);    //systemet mottagit ett "Done" då skickas nästa. 

                    ds.ncount = i;        //Räknar antalet noder som passeras.  
                    String kommando =ds.kommandon.get(i); 
                    ds.btm.send(kommando);
                    ds.cui.appendStatus("Skickat meddelande: " + ds.kommandon.get(i));

                    }
               
                    if (ds.kommandon.get(i).equals("S")) { //När ett "S" skickas till Agv:n betyder det att rutten 
                        ds.scount = ds.scount +1;        //är färdig. Scount räknar antalet skickade "S.
                        ds.kommandon.clear();            //Tömmer kommandon när alla är körda
                        ds.paxInt = 0;                   //Nollställer passagerarantal
                    }
               
               
                }
                        ds.dcount = 0; //Nollställer dcount när rutten är klar.  

                }catch (InterruptedException exeption) {
              }
}
 
    else if (ds.breakflag == 1) { //Detta händer när någon trycker på avsluta
                try {Thread.sleep(100000);
            }   catch (InterruptedException exeption) {
               }
        }
   
    
} catch (InterruptedException exeption) {
               }   
}
}