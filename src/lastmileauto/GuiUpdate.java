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
//   
// try {   
//        
//        for(int i = 0; i < ds.vaderStrack.size(); i++){
//             
//            while(ds.ncount == i){
//                Thread.sleep(100);          //Om denna minskas ökas hastigheten
//                if(ds.vaderStrack.get(i).equals("O")){
//                  ds.robotX = ds.robotX + 2; 
//                  cui.repaint();
//                }
//                else if(ds.vaderStrack.get(i).equals("V")){
//                  ds.robotX = ds.robotX - 2; 
//                  cui.repaint();
//                }
//                else if(ds.vaderStrack.get(i).equals("N")){
//                 ds.robotY = ds.robotY + 2; 
//                  cui.repaint();
//                }
//                else if(ds.vaderStrack.get(i).equals("S")){
//                  ds.robotY = ds.robotY - 2; 
//                  cui.repaint();
//                }
//                else {
//                    ds.cui.appendStatus("Oläsbart vädersträck");
//                }
//                
//            }
//        }  
//        
// }catch (InterruptedException exception) {
//     
//        }
    }
}
