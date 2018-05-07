package lastmileauto;

// Main 

public class LastMileAuto {

    DataStore ds;
  //ControlUI cui;
    
    LastMileAuto(){
        /*
         * Initialize the DataStore call where all "global" data will be stored
         */ 
        ds = new DataStore();

        /*
         * This sets the file path and read network text file.
         */   


       //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
       ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
     // ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");

            // ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");

       //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
     // ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");

       //ds.setFileName("/Users/Veronika/Documents/grupp2/streets.txt");
     //  ds.setFileName("/Users/Veronika/Documents/grupp2/streets.txt");
        //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
      //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
            //ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
       //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
      //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");

       //ds.setFileName("/Users/Veronika/Documents/grupp2/streets.txt");

       //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
      // ds.setFileName("/Users/Veronika/Documents/grupp2/streets.txt");
       //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
       //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
       //ds.setFileName("/Users/Veronika/Desktop/streets.txt");

        //ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");

       // ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
       // ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");

       // ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");
     //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
     //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
     //ds.setFileName("/Users/Veronika/Desktop/streets.txt");
     //ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");

       //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
       //ds.setFileName("/Users/Veronika/Documents/grupp2/streets.txt");
          //ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
       // ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
         // ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");
        

       
        // ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
        // ds.setFileName("/Users/Veronika/Documents/grupp2/streets.txt");
        // ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
        // ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
        //ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");

       // ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");
    
       ds.readNet();
       ds.cui = new ControlUI(ds);
       ds.cui.setVisible(true);
       ds.cui.showStatus();
        
                                            
       ds.cui.appendStatus("Startar programmet \n" );
       ds.cui.appendStatusAgv("Meddelanden från Agda kommer nu. \n" );
              

        OptPlan op = new OptPlan(ds);
        ds.pathInt = op.createPlan();
        op.compass(ds.pathInt);
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