
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
   
        
//        for(int i = 0; i < ds.vaderStrack.size(); i++){
//            
//            
//            while(ds.ncount == i){
//                if(ds.vaderStrack.get(i).equals("O")){
//                  ds.robotX = ds.robotX + 10;  
//                  cui.repaint();
//                }
//                else if(ds.vaderStrack.get(i).equals("V")){
//                  ds.robotX = ds.robotX - 10; 
//                  cui.repaint();
//                }
//                else if(ds.vaderStrack.get(i).equals("N")){
//                  ds.robotY = ds.robotY + 10; 
//                  cui.repaint();
//                }
//                else if(ds.vaderStrack.get(i).equals("S")){
//                  ds.robotY = ds.robotY - 10; 
//                  cui.repaint();
//                }
//                else {
//                    ds.cui.appendStatus("Oläsbart vädersträck");
//                }
//                
//            }
//        }    
    }
}
    
        
//        ds.updateUIflag = true;
//        int i = 1;    
//        while(i <= 20){
//            Thread.sleep(sleepTime /20);
//            if(ds.updateUIflag == true){
//

        //  ds.robotX = ds.robotX - 10; 
        //  ds.robotY = ds.robotY -10;
            
//        ds.robotX = ds.nodeX[xy]; 
//        ds.robotY = ds.nodeY[xy];

//          ds.robotX = ds.nodeX[ds.mottagenInt]; 
//          ds.robotY = ds.nodeY[ds.mottagenInt];        
          
//            cui.repaint();   
//            i++;  
//            
//            }
//    
//
//    
//       
//    
//        cui.appendStatus("GuiUpdate är nu klar!");
////          ds.robotX = ds.nodeX[30]; 
////          ds.robotY = ds.nodeY[30];
//          cui.repaint();
//    }
//
//}
