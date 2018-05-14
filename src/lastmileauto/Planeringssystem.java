
package lastmileauto;

// Main 

public class Planeringssystem {

    DataStore ds;
       
    Planeringssystem(){
        ds = new DataStore();

<<<<<<< HEAD
        ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
 
       
=======
        //ds.setFileName("/Users/hannamellqvist/Documents/Kandidat/grupp2/streets.txt");
      // ds.setFileName("/Users/aliceneu/Documents/grupp2/streets.txt");
       //ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
      //ds.setFileName("/Users/eliselord/Documents/grupp2/streets.txt");
      ds.setFileName("/Users/Veronika/Documents/grupp2/streets.txt");
      //ds.setFileName("/home/itn/NetBeansProjects/Planeringssystem/streets.txt");
      
>>>>>>> a45d4bc547c8bede92eea342caf6f4f3d335bea1
      

    
       ds.readNet();
       ds.cui = new ControlUI(ds);
       ds.cui.setVisible(true);
       ds.cui.showStatus();
       ds.cui.appendPoang(ds.totPoang);
        
                                            
       ds.cui.appendStatus("Startar programmet \n" );
       ds.cui.appendStatusAgv("Meddelanden från Agda kommer nu. \n" );
              

       
    }
         
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
   
        Planeringssystem x = new Planeringssystem();
    }       
}