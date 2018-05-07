package lastmileauto;

// Main 

public class Planeringssystem {

    DataStore ds;
    
    Planeringssystem(){
        ds = new DataStore();

        ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
      // ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");
       //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
      //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
      //ds.setFileName("/Users/Veronika/Documents/grupp2/streets.txt");
      
      

    
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
   
        Planeringssystem x = new Planeringssystem();
    }       

}