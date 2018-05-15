package lastmileauto;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

// Sparar data + håller i kopplingen till GUIUpdate och RobotRead

public class DataStore {

    String fileName = null;
    int nodes; //Innehåller noder
    int arcs; //Innehåller bågar
    double[] nodeX; //Array med noders x-koordinater
    double[] nodeY; //Array med noders y-koordinater
    int[] nodenr; //Nodnummer
    int[] arcStart; //Array med bågars startnoder
    int[] arcEnd; //Array med bågars slutnoder
    int[] arcCost; //Bågkostnader
    boolean networkRead;
    boolean updateUIflag;
    double robotX; //Robotens position i x-koordinater  
    double robotY; //Robotens position i y-koordinater
    int start = 41; //Roboten ska stå på en högre än start-värde
    int[] arcColor; //Array med bågar som ska färgläggas
    int startRutt = 5; //startnod, om vi säger 1 tar den 2 osv
    int slutRutt = 33; //slutnod, om vi säger 1 tar den 2 osv
    String F = "F"; //Kör forward 
    String R = "R"; //Kör Right
    String L = "L"; //Kör Left 
    String S = "S"; //Stop i 5 sek. 
    String C = "C"; //Cancel
    String U = "U"; //Gör U-sväng
    String P = "P"; //Pick-up punkt, stanna 5 sek
    ArrayList<Integer> pathInt;
    //int[] pathInt; //Noderna i ints
    BluetoothTransceiver btc; //Instans av BluetoothTransceiver-klassen
    BluetoothTransmitter btm; //Instans av BluetoothTransmitter-klassen
    BluetoothReceiver btr; //Instans av BluetoothReceiver-klassen
    ControlUI cui; //Instans av ControlUI-klassen
    DijkstraAlgorithm dij; //Instans av DijkstraAlgorithm-klassen 
    int[] tot_arcCost; //Totala bågkostnaden
    int robotpos = start-1; //Robotens aktuella position, initieras till startpositionen
    ArrayList<String> kommandon; //ArrayList som sparar kommandon för en rutt
    int kapacitet = 20; //AGV:ns platser
    String grupp = "2"; //Gruppnummer
    String  meddelande_in = "100"; 
    int mottagenInt = 21;
    int btstatus = 0; 
    int dcount = 0; //Räknar antalet utförda kommandon från AGV:n (D=done) 
    ArrayList<String> vaderStrack; //ArraList som sparar riktningen på AGV:n
    int ncount = 0; //
    int meddelande_int;
    int sistanod1;
    int sistanod2 ;
    String sistaRik; //Sparar sista riktningen AGV:n står i 
    int [] poang; //Sparar antalet poäng från uppdragen
    int totPoang = 0; //Lägger ihop poäng från uppdrag
    int breakflag = 0;
    int scount = 0; //Räknar antalet S=stopp
    String messfrom; //Mottagna meddelanden från grupp 3
    ArrayList<String> kommandon1; //Kommando till upphämtningsplats
    ArrayList<String> kommandon2; //Kommando till avlämningsplats
    ArrayList<String> kommandon3; //Kommando om samåkning finns
    ArrayList<String> kommandon_done; //Alla kommandon ihopsatta
    int pax = 0; 
    int paxRutt = 0; 
    int [] linkNod1; //Första noden på bågen för upphämtningsplatsen
    int [] linkNod2; //Sista noden på bågen för upphämtningsplatsen
    int startStart = 38; //Startposition
    boolean taUppdrag = false; 
    boolean skickatP = false;
    int paxSamaka=0;
    int pax_tot = 0;
    
    //Initierar DataStore med arrayer som har fix storlek för att lagra data    
    public DataStore() {
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

            //Läser in antalet noder
            line = (scanner.nextLine());
            nodes = Integer.parseInt(line.trim());
            line = scanner.nextLine();
            arcs = Integer.parseInt(line.trim());
            
            //Läser in noder som koordinater (x,y)
            for (int i=0; i < nodes; i++){
                line = scanner.nextLine();
                //Separerar vid mellanrum
                sline = line.split(" ");
                nodenr[i] = Integer.parseInt(sline[0].trim());
                nodeX[i] = Double.parseDouble(sline[1].trim());
                nodeY[i] = Double.parseDouble(sline[2].trim());
            }

            //Läser in som båglista start- och slutnodnummer
            for (int i=0; i < arcs; i++){
                line = scanner.nextLine();
                //Separerar vid mellanrum
                sline = line.split(" ");
                arcStart[i] = Integer.parseInt(sline[0].trim());
                arcEnd[i] = Integer.parseInt(sline[1].trim());
                arcCost[i] = Integer.parseInt(sline[2].trim());    
            }
           
            //Indikerar att all data finns i DataStore
            networkRead = true;
                         
        } catch (Exception e) { e.printStackTrace(); }
        
        //Nodindex för startposition
        robotX = nodeX[start];
        robotY = nodeY[start];
    }

}