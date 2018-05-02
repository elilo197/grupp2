package lastmileauto;

// Main 

public class LastMileAuto {

    DataStore ds;
    ControlUI cui;
    
    LastMileAuto(){
        /*
         * Initialize the DataStore call where all "global" data will be stored
         */ 
        ds = new DataStore();

        /*
         * This sets the file path and read network text file.
         */   

       //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
       ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
      // ds.setFileName("/Users/Veronika/Documents/grupp2/streets.txt");
       //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
       //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
       //ds.setFileName("/Users/Veronika/Desktop/streets.txt");
     //    ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
        //ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");
    
       
       ds.readNet();
       cui = new ControlUI(ds);
       cui.setVisible(true);
       cui.showStatus();
        
      //  r = new RobotRead(ds, cui); //,bre   //Tråd som lyssnar på AGV via Bluetoothreciever
      //  Thread t1 = new Thread(r);
                                            //Tråd som håller kontakt med AGV via BluetoothTranceiver
                                            //Tråd för resten, typ main
                                            
        cui.appendStatus("Startar programmet \n" );
                  
       //t1.start();

        
        cui.appendStatus("Avslutar main.\n");
        
        OptPlan op = new OptPlan(ds);
        //int [] pathInt = new int[ds.pathInt.length];
 
          ds.pathInt = op.createPlan();
       // for (int i =0; i< ds.pathInt.length; i++){
        //System.out.println("Hej här kommer pathInt från LastMileAuto!" + ds.pathInt);
        op.compass(ds.pathInt);
//        for (int i=0; i<ds.nodeX.length; i++) {
//        System.out.println("Kommandon "+ i + " : " + ds.kommandon[i]);
//             }

    }
        
        
        //com = new Compass();
        //Thread t6 = new Thread(com);
        //t6.start();
        
    //}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /* This is the "main" method what gets called when the application starts
         * All that is done here is to make an instance of the RobotControl class,
         * and thereby, call the RobotControl constructor.
        */
        LastMileAuto x = new LastMileAuto();
        

    }       
}