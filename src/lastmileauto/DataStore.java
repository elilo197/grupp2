package lastmileauto;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

// Sparar data + håller i kopplingen till GUIUpdate och RobotRead

public class DataStore {

    String fileName = null;
    int nodes;
    int arcs;
    double[] nodeX;
    double[] nodeY;
    int[] nodenr;
    int[] arcStart;
    int[] arcEnd;
    int[] arcCost;
    boolean networkRead;
    boolean updateUIflag;
    double robotX;          
    double robotY; 
    // Dessa två bör ha samma nummer (tror vi) 
    // Nod 71 i streets är  nod 70 här..
    int start = 37;         //Roboten ska stå på en högre än start-värde
   // int startY = startX; 
    int[] arcColor;
    int startRutt = 5; //startnod, om vi säger 1 tar den 2 osv
    int slutRutt = 33; //slutnod, om vi säger 1 tar den 2 osv
    String F = "F"; //Kör forward 
    String R = "R";  //Kör Right
    String L = "L";   //Kör Left 
    String S = "S";   //Stop i 5 sek. 
    String C = "C";
    ArrayList<Integer> pathInt; 
    //int[] pathInt; //Noderna i ints
    BluetoothTransceiver btc;
    BluetoothTransmitter btm;
    BluetoothReceiver btr; 
    int[] tot_arcCost;
    int robotpos = start;       //Robotens aktuella position, initieras till startpositionen
    //String[] kommandon;
    ArrayList<String> kommandon;
    ArrayList<String> kortaddekommandon;
    int kapacitet = 10;
    String grupp = "2";
    ControlUI cui;
   //String meddelande_string = "hej";
    String  meddelande_in = "100"; 
    int mottagenInt = 21;
    int btstatus =0; 
    int dcount = 0; 
    ArrayList<String> vaderStrack; 
    int ncount = 0;
    int meddelande_int;
    int sistanod;
    int [] poang;
    int totPoang = 0;
    int breakflag = 0;
    int scount = 0;

    String messfrom;

    int paxInt = 0; //passagerarantal i det aktuella uppdraget


    public DataStore() {
        // Initialize the datastore with fixed size arrays for storing the network data
        nodes = 0; 
        arcs = 0;
        nodeX = new double[1000];
        nodeY = new double[1000];
        nodenr = new int[1000];
        arcStart = new int[1000];
        arcEnd = new int[1000];
        arcCost = new int[1000];
        updateUIflag = false; 
        networkRead = false;
        arcColor = new int[128];
        //pathInt = new ArrayList<Integer>();
        //kommandon = new String[3];//3 är fulkodning, denna ska va 2 mindre än pathInt
        
        vaderStrack = new ArrayList<String>();

        
        
        

    }

    public void setFileName(String newFileName) {
        this.fileName = newFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void readNet() {
        String line;

        if (fileName == null) {
            System.err.println("No file name set. Data read aborted.");
            return;
        }
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file, "iso-8859-1");
            String[] sline;

            // Read number of nodes
            line = (scanner.nextLine());
            nodes = Integer.parseInt(line.trim());
            line = scanner.nextLine();
            arcs = Integer.parseInt(line.trim());

            // Debug printout: network size data
           // System.out.println("Nodes: "+nodes);
            //System.out.println("Arcs: "+arcs);
            
            // Read nodes as number, x, y
            for (int i=0; i < nodes; i++){
                line = scanner.nextLine();
                //split space separated data on line
                sline = line.split(" ");
                nodenr[i] = Integer.parseInt(sline[0].trim());
                nodeX[i] = Double.parseDouble(sline[1].trim());
                nodeY[i] = Double.parseDouble(sline[2].trim());
                
                //int j = i+1; //Ändrar nodnummer så att den börjar vid 1
                
              //  System.out.println("Node "+ nodenr[i] +": "+nodeX[i]+" "+nodeY[i]); //loopar igenom alla noder och visar position
            }

            // Debug printout: print data for node 1
            //System.out.println("Node 1: "+nodeX[0]+" "+nodeY[0]);

            // Read arc list as start node number, end node number
            for (int i=0; i < arcs; i++){
                line = scanner.nextLine();
                //split space separated data on line
                sline = line.split(" ");
                arcStart[i] = Integer.parseInt(sline[0].trim());
                arcEnd[i] = Integer.parseInt(sline[1].trim());
                arcCost[i] = Integer.parseInt(sline[2].trim());
                
            }
            

            networkRead = true;  // Indicate that all network data is in place in the DataStore
            
           // for-loop som kör igenom alla arcs, for-loop som kopplar ihop noder med arcs
                       //for-loop som identifierar koordinater på noderna och tar absolutbeloppet av skillnaden mellan x1 och x2, y1 och y2 och sedan adderar dem och sparar som bågkostand
            
            
    
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        robotX = nodeX[start]; //Node index för startposition
        robotY = nodeY[start];
    }

}