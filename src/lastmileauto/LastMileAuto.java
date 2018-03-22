package lastmileauto;

// Main 


public class LastMileAuto {

    DataStore ds;
    ControlUI cui;
    RobotRead r;
    GuiUpdate g;
    
    LastMileAuto(){

        /*
         * Initialize the DataStore call where all "global" data will be stored
         */
        ds = new DataStore();

        /*
         * This sets the file path and read network text file. Adjust for your needs.
         */
        //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
        ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
        ds.readNet();

        cui = new ControlUI(ds);
        cui.setVisible(true);
        cui.showStatus();
        
        r = new RobotRead(ds, cui);
        Thread t1 = new Thread(r);
        g =  new GuiUpdate(ds, cui);
        Thread t2 = new Thread(g);
        
        cui.appendStatus("Startar 2 trådar...\n" );
                       
        t1.start();
        t2.start();
        
        cui.appendStatus("Avslutar main.\n");
        
        OptPlan op = new OptPlan(ds);
        op.createPlan();
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /* This is the "main" method what gets called when the application starts
         * All that is done here is to make an instance of the RobotControl class,
         * and thereby, call the RobotControl constructor.
        */
        LastMileAuto x = new LastMileAuto();
        
    for (int i=0; i <1000; i++) {
    System.out.println (ds.arcColor[i]);
}

    }   
    
}
