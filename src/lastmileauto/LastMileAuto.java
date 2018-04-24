package lastmileauto;

// Main 

public class LastMileAuto {

    DataStore ds;
    ControlUI cui;
    RobotRead r;
    GuiUpdate g;
    BluetoothReceiver bre;
    
    LastMileAuto(){
        /*
         * Initialize the DataStore call where all "global" data will be stored
         */
        ds = new DataStore();
        bre = new BluetoothReceiver();

        /*
         * This sets the file path and read network text file. Adjust for your needs.
         */
        
<<<<<<< HEAD
       ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
=======
       //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
<<<<<<< HEAD

       //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
       //ds.setFileName("/Users/Veronika/Desktop/streets.txt");
       //ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
       ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");

      //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
      //ds.setFileName("/Users/Veronika/Desktop/streets.txt");
      //ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
      //ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");
     
=======
<<<<<<< HEAD
>>>>>>> a1bbe858abb12783aa186a1b4beb14e5489e8ef1
     //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
      //ds.setFileName("/Users/Veronika/Desktop/streets.txt");
     ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
=======
<<<<<<< HEAD
     //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
      ds.setFileName("/Users/Veronika/Desktop/streets.txt");
=======
       //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
       //ds.setFileName("/Users/Veronika/Desktop/streets.txt");
       //ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
     //  ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");
     ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
      //ds.setFileName("/Users/Veronika/Desktop/streets.txt");
<<<<<<< HEAD
=======
>>>>>>> 288dc0b602fb35a3b1a32c7b834ea040db37a260
>>>>>>> a1bbe858abb12783aa186a1b4beb14e5489e8ef1
     //ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
>>>>>>> a1bbe858abb12783aa186a1b4beb14e5489e8ef1
       //ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");
       
>>>>>>> a04fd04d42133ff46a215bf6dcef07f4e90c59da
        ds.readNet();
        cui = new ControlUI(ds);
        cui.setVisible(true);
        cui.showStatus();
        
        r = new RobotRead(ds, cui, bre);    //Tråd som lyssnar på AGV via Bluetoothreciever
        Thread t1 = new Thread(r);
                                            //Tråd som håller kontakt med AGV via BluetoothTranceiver
                                            //Tråd för resten, typ main
                                            
        cui.appendStatus("Startar programmet \n" );
                  
       t1.start();

        
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
        

    }       
}