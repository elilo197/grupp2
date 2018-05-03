
package lastmileauto;

//söka efter kommandon från robotread, uppdaterar hur roboten rör sig

import java.util.Random;

public class GuiUpdate implements Runnable {
    private int sleepTime;
    private static Random generator = new Random();
    private ControlUI cui;
    private DataStore ds;
    private int xy;
 

    public GuiUpdate(DataStore ds, ControlUI cui, int xy){
        this.cui = cui;
        this.ds = ds;
        this.xy = xy;
        sleepTime = generator.nextInt(20000);
    }
@Override
public void run (){
   
    try {
        cui.appendStatus("GuiUpdate startar och kommer att köra i "
        + sleepTime + " millisekunder." );
        ds.updateUIflag = true;
        int i = 1;    
        while(i <= 20){
            Thread.sleep(sleepTime /20);
            if(ds.updateUIflag == true){
//            cui.appendStatus ("Jag är tråd GuiUpdate! För " 
//            + i + ":te gången.");
        //  ds.robotX = ds.robotX - 10; 
            
//        ds.robotX = ds.nodeX[xy]; 
//        ds.robotY = ds.nodeY[xy];

          ds.robotX = ds.nodeX[ds.mottagenInt]; 
          ds.robotY = ds.nodeY[ds.mottagenInt];        
          
            cui.repaint();   
            i++;  
            
            }
                       }
        
            }
    
    catch (InterruptedException exception) {
        }
    
       
    
        cui.appendStatus("GuiUpdate är nu klar!");
          ds.robotX = ds.nodeX[30]; 
          ds.robotY = ds.nodeY[30];
          cui.repaint();
    }

}
