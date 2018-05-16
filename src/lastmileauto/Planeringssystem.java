
package lastmileauto;

//Huvudklass för projektet, startar igång allt
public class Planeringssystem {

    DataStore ds;
       
    Planeringssystem(){
       ds = new DataStore();
        
       //Vägen till kartfilen
       ds.setFileName("C:\\Users\\Helena\\Documents\\GitHub\\grupp2/streets.txt");
  
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