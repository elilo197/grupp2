package lastmileauto;

/** Helena testar!


/** Helena testar!
 *
 * @author clary35
 */
public class LastMileAuto {
    
    // hejhejhejhej !!!!!! 

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
        ds.setFileName("/home/itn/Downloads/streets.txt");
        ds.readNet();

        /*
         * Initialize and show the GUI. The constructor gets access to the DataStore
         */
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
    }
}
