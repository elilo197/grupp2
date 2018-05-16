
package lastmileauto;
//kommentera tillbaka rad 33








// Denna klass används för att skicka meddelanden till Agv:n och är en trådad klass. 

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
    Thread.sleep(500);    //Detta är för att ds.kommandon ska hinna köras och få värden innan denna tråd startar
        if(ds.breakflag == 0){ //Kör tills knappen avsluta bli intryckt. 
            try { 

//Här skapas en loop som skickar alla kommandom till Agv:n.  
               for(int i = 0; i <ds.kommandon_done.size(); i++){
                    int dummyInt  = ds.paxRutt;
                    String dummyString = Integer.toString(dummyInt);
                                        //Dcount räknar antalet "Done" som skickats från Agv:n 
                    while(ds.dcount == i){  //Denna while-loop gör så att samma kommando skickas tills att
                    Thread.sleep(1000);    //systemet mottagit ett "Done" då skickas nästa. 
                    ds.ncount = i;        //Räknar antalet noder som passeras.  
                    String kommando = ds.kommandon_done.get(i) + dummyString; //Skickar kommandot + passagerar antal
                    ds.btm.send(kommando);
                    ds.cui.appendStatus("Skickat meddelande: " + kommando);

                    }
               
                    if (ds.kommandon_done.get(i).equals("S")) { //När ett "S" skickas till Agv:n betyder det att rutten 
                        ds.scount = ds.scount +1;        //är färdig. Scount räknar antalet skickade "S.
                        ds.kommandon_done.clear();            //Tömmer kommandon när alla är körda
                        ds.paxRutt = 0;                   //Nollställer passagerarantal
                        ds.pax = 0;                        //Nollställer passagerarantal
                        countPax = 0;
                        ds.taUppdrag = false;               //Återställer flaggor. 
                        ds.skickatP = false;
                       
                    }
//När ett P skickas så börjar passagerar antalet skickas med. 
               else if (ds.kommandon_done.get(i).equals("P")){
                   ds.taUppdrag = true; 
                   if(ds.paxRutt == 0){ //Detta sker vid första upphämtningen 
                       ds.paxRutt = ds.pax_tot; 
                   }else{ //Dettta sker vid samåkning vid första avlämningsplats
                       ds.paxRutt = ds.pax_tot-ds.paxSamaka;
                   }
                   countPax  = 1 ; 
                   
                   Thread.sleep(1000);

                }
               
            }
             ds.dcount = 0; //Nollställer dcount när rutten är klar.  

                }catch (InterruptedException exeption) {  }
        }
        else if (ds.breakflag == 1) { //Detta händer när någon trycker på avsluta
                try {Thread.sleep(100000);
            }catch (InterruptedException exeption) { }
        }
} catch (InterruptedException exeption) {  }   
}
}