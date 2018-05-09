/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Kan vi använda Timer istället för trådning i denna?

package lastmileauto;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Uppdrag implements Runnable{
   String inkommandetext []; 
   String ink_sam;
   int IntStorlek;
   DataStore ds;
   int [] linkNod1;
   int [] linkNod2;
   OptPlan opt;
   OptPlan [] oppis;
   String narmstaPlats;// = "Start";
   String id; 
   int pax;
   String [] uppdragsid;
   String [] destination;
   int [] passant; 
   int [] samaka;
   int[] destNod1;
   int[] destNod2;
   int valtUppdragPlats;     //
   String valtUppdragId;
   String svar;
   OptPlan oppis1;
   OptPlan oppis2;
   ArrayList<Integer> oppis1path;
   ArrayList<Integer> oppis2path;
   ArrayList<Integer> oppispath;
   ArrayList<String> ink; 
   ArrayList<String> inkmess; 
   String [] splitmessfrom;  
   int bortvald_flagga = 0;
   int[] bortvald_plats;
   int i = 0; 
   
   ArrayList<String> inkuppdrag;
    
  
    public Uppdrag(DataStore ds) { 
        //aterstall("1");
        //messfromgroup();
        this.ds = ds;
      
    }
    
@Override    
    public void run(){
    try{
    messfromgroup(); //Denna ligger här uppe för att vi vill veta vilken upphämtningsplats grupp 3 är påväg mot innan vi kör
    listaplatser();
    if(ds.messfrom == null){
        System.out.println("Fanns inget meddelande att hämta, börjar köra");
    }
    else if( ds.messfrom == narmstaPlats){
        int dummyAntalUppdrag = Integer.parseInt(inkuppdrag.get(0));
        if(dummyAntalUppdrag > 1 ){
             System.out.println("Fanns mer än 1 uppdrag, börjar köra");
        }
        else 
         System.out.println("Dom har tagit uppdraget, ta en annan plats");
         //bortvald_plats[] = 1;
    }
    
   
    
    while (ink.get(0) != null) {    //Kollar så att det finns upphämtningsplatser kvar
    //System.out.println("scount: " + ds.scount);
    //System.out.println("i: " + i);
    Thread.sleep(100);      //för att slippa göra utskrifterna ovan
    
   // ds.cui.appendStatus("Bortvald flagga andra gången: " + bortvald_flagga);
    
            while(ds.scount == i){  //Kollar antal "s", stopp, kör igång loopen när s=1, adderar på varje varv
                
               
                //ds.cui.appendStatus("bortvald plats: " + bortvald_plats);
                
               
                valtUppdragPlats = listauppdrag(narmstaPlats);           //Skickar in upphämtningsplats, skickar ut vilket uppdrag vi väljer
                System.out.println("Valt uppdrag första gången: " + valtUppdragPlats);  
                
                if (bortvald_flagga == 1){      //Kör om utifall att nån upphämtningsplats har för stora uppdrag
                listaplatser();
                valtUppdragPlats = listauppdrag(narmstaPlats); 
                
                }
                  System.out.println("Valt uppdrag andra gången: " + valtUppdragPlats);        
              //  ds.cui.appendStatus("Bortvald flagga tredje gången: " + bortvald_flagga);
                pax = getPassagerare(valtUppdragPlats);                  //Skickar ut passagerarantal på det valda uppdraget
                  System.out.println("Passagerarantal " + pax);  


                //Räknar totala poängen för uppdragen. 
                int dummy; 
                dummy = (valtUppdragPlats);
                ds.totPoang = ds.totPoang + ds.poang[dummy];
                //System.out.println("Totala poäng: " + ds.totPoang);
                ds.cui.appendPoang(ds.totPoang);

                oppis1path = new ArrayList<Integer>();
                oppis2path = new ArrayList<Integer>();
                oppispath = new ArrayList<Integer>();
                ds.kommandon1 = new ArrayList<String>(); 
                ds.kommandon2 = new ArrayList<String>(); 
                ds.kommandon_done = new ArrayList<>();
                
                //messfromgroup();                      //VI HAR FLYTTAT UPP DENNA JUST NU MVH H OCH V
                messtogroup();

               String svaruppdrag = tauppdrag(narmstaPlats, valtUppdragId, pax, ds.grupp);

                    if (svaruppdrag.equals("beviljas")){

                        for(int j=0; j <128; j++){
                            ds.arcColor[j] = 0;           
                            }

                    ds.startRutt = ds.start; 
                    ds.cui.appendStatus("Robotens startnod: " + ds.startRutt);
                    ds.slutRutt = linkNod2[valtUppdragPlats];
                    ds.cui.appendStatus("Upphämtningsnod 1: " + ds.slutRutt);

                    //Oppis 1 är den optimerade rutten för upphämtningsplatsen
                    oppis1 = new OptPlan(ds);
                    oppis1path = oppis1.createPlan();
                    ds.kommandon1 = oppis1.compass(oppis1path);
                    System.out.println("Kommandon1 först: " + ds.kommandon1);
                    ds.kommandon1.add(ds.U);
                    ds.kommandon1.remove(0);
                    System.out.println("Kommandon1 sen: " + ds.kommandon1);
                    
                    
                    ds.startRutt = linkNod2[valtUppdragPlats];  
                    ds.cui.appendStatus("Upphämtningsnod 2: " + ds.startRutt);
                    ds.slutRutt = destNod1[valtUppdragPlats]; 
                    ds.cui.appendStatus("Destinationsnod: " + ds.slutRutt);
                    

                    //Oppis 2 är den optimerade rutten för uppdrage
                    oppis2 = new OptPlan(ds);
                    oppis2path = oppis2.createPlan();
                    oppis2path.add(destNod2[valtUppdragPlats]);
                    ds.kommandon2 = oppis2.compass(oppis2path);
                    System.out.println("Kommandon2 först: " + ds.kommandon2);
                    ds.kommandon2.add(ds.S);
                    System.out.println("Kommandon2 sen: " + ds.kommandon2);
   
                    for ( int j = 0; j < ds.kommandon1.size(); j++ ){ //Lägger till kommandon1 i kommandon
                    ds.kommandon_done.add(ds.kommandon1.get(j));
                    }
                     
                    for ( int j = 0; j < ds.kommandon2.size(); j++ ){ //Lägger till kommandon2 i kommandon 
                    ds.kommandon_done.add(ds.kommandon2.get(j));
                    }
                    
                    ds.sistanod = (oppis2path.get(oppis2path.size()-2));   //Lägger till sista noden i föregående rutt i en ny arraylist som ska 
                    ds.start = ds.sistanod; 
                    
                    System.out.println("Kommandon: " + ds.kommandon_done);  
                    
                     ds.cui.repaint();          //Ritar ny väg

                }
                else {System.out.println("Svar från hemsida: " + svaruppdrag);}

               // aterstall("1");
                RobotSend RSend = new RobotSend(ds);        //Startar robotsend-tråden
                Thread RobotSendThread = new Thread(RSend);
                RobotSendThread.start();


             ds.start = ds.sistanod;     //Sätt startnod på nästkommande uppdrag till nuvarande uppdrags sistanod
             i++;    //Räknar antalet S 
            ds.cui.appendRutt(" " + ds.kommandon_done); 
            }
                        
    } //while
    }catch(InterruptedException exception){
    }
}
    

    /** Här listar vi antalet upphämtningsplatser och beräknar vilken som är närmast. 
     */
    public void listaplatser() { //tagit bort static
      
    ds.cui.appendStatus("");    
    ds.cui.appendStatus("Listar platser.");    
        
     try {

         //Uppdrag http = new Uppdrag();
         String url = "http://tnk111.n7.se/listaplatser.php"; 
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

         System.out.println("\nAnropar: " + url);
 
         int mottagen_status = anslutning.getResponseCode();

         System.out.println("Statuskod: " + mottagen_status);
         BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
        
        
        ink = new ArrayList<String>();      //Skapar en arraylist som ska spara alla upphämtningsplatser

         
        while ((inkommande_text = inkommande.readLine()) != null) {
                  ink.add(inkommande_text);      
        }
         inkommande.close();
            ds.cui.appendStatus("Antal upphämtningsplatser: " + ink.get(0));
            for(int k = 1; k < ink.size()-1; k++){      
            ds.cui.appendStatus("Upphämtningsplats: " + ink.get(k));
            }
       
        
        //Variabler
        String StringStorlek = ink.get(0);      //Antal upphämtningsplatser, det som ligger på plats 0 i ink.get()
        IntStorlek = Integer.parseInt(StringStorlek);   
        String [] link = new String[IntStorlek];
        linkNod1 = new int[IntStorlek];
        linkNod2 = new int[IntStorlek];
        oppis = new OptPlan[IntStorlek];
        String [] sline;
        String [] plats = new String[IntStorlek];
        double tot_kostnad = 0;         //Totala kostnaden för en väg/alla bågar i en väg
        int narmstaNod = ds.start;
        double lagstaKostnad = 1000000;
        int [] vertexint; 
             
  
        for(int k = 1; k <IntStorlek+1 ; k++){
            sline = ink.get(k).split(";");
            link[k-1] = sline[1];
            plats[k-1] = sline[0];       
        }

        for(int j = 0; j <IntStorlek; j++){
            sline = link[j].split(",");    
            linkNod1[j] =Integer.parseInt(sline[0]);
            linkNod2[j] =Integer.parseInt(sline[1]);         
        }
        
//Nu har vi nod-nr på uppdragen. Dags att beräkna avstånd! 
                
        //Här borde en loop börja
        for (int j=0; j<IntStorlek; j++){       //Den här ska loopa över alla upphämtningsplatser
            ds.startRutt = ds.robotpos;        
            ds.slutRutt = linkNod2[j];
            //System.out.println("Startnod: " + ds.startRutt + ", slutnod: " + ds.slutRutt);
             
            oppis[j] = new OptPlan(ds);
            oppis[j].createPlan();
            
            
             vertexint = new int[oppis[j].path.size()-1];
       
            //Den här loopen räknar ut kostnaden för rutten till den aktuella upphämtningsplatsen
            for (int i=0; i< oppis[j].path.size()-1; i++){      
            
             vertexint[i] = Integer.parseInt(oppis[j].path.get(i).getId()); //Gör om path till ints
             
              }
            
            tot_kostnad = RaknaAvstand(vertexint);

            ds.cui.appendStatus("Totalkostnad: " + tot_kostnad);
              
            
             ds.cui.appendStatus("Upphämtningsplats " + plats[j] + " innebär en rutt från " + ds.startRutt + " till "  
                   + ds.slutRutt + " vilket ger en totalkostnad på "  + tot_kostnad);
           
             System.out.println();      //Blankrad
                
             if (tot_kostnad < lagstaKostnad){
                 lagstaKostnad = tot_kostnad;
                 narmstaPlats = plats[j];
                 narmstaNod = ds.slutRutt;
             }
        }
             ds.cui.appendStatus("\nNärmaste upphämtningsplats är plats " + narmstaPlats + " med nodnummer " + narmstaNod);
             System.out.println("Kostnaden för att ta sig dit är " + lagstaKostnad);

       }
    
     catch (IOException e) { System.out.print(e.toString()); }
     
    }
     
    
        /** Här ska de hända massa spännande saker.
        *Kolla om det finns uppdrag när vi kommer till upphämtningsplatsen, 
        *kan köras när som helst
        *Måste kalla på denna metod när vi stannar
            *Om det EJ finns, sök ny startnod
            *Om det finns, fortsätt på följande:
        *Spara kapacitet i en string/var, spara samåkning i en string/var
        * 
        * Om inga uppdrag finns, stanna AGVn vid närmsta båge. Sen söker vi efter 
        * en ny upphämtningsplats som har uppdrag.
        */
 
    
    
   
    public int listauppdrag(String plats){      //var static från början
      inkuppdrag = new ArrayList<String>();
       int valtupp = 0;      
     /**
     *Kalla på Compass och kör till platsen
     *String X = "A";
     */
     
     try {
         ds.cui.appendStatus("\nNu hämtas lista på uppdrag!");
         //Kalla på metoden ovan för att hämta x
        // String X = "A"; //DENNA SKA VA MED I STEGET OVAN
         //Uppdrag http = new Uppdrag();
         String url = " http://tnk111.n7.se/listauppdrag.php?plats=" + plats; //plats=A ska inte vara hårdkodad,utan beror på vilken plats som är närmst
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

         System.out.println("\nAnropar: " + url);
 
         int mottagen_status = anslutning.getResponseCode();

         System.out.println("Statuskod: " + mottagen_status);

 
         BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
        
      
        
         while ((inkommande_text = inkommande.readLine()) != null) {
                 inkommande_samlat.append(inkommande_text); 
                inkuppdrag.add(inkommande_text);  
          }

          inkommande.close();
          ds.cui.appendStatus("Antal uppdrag på platsen: " +inkuppdrag.get(0));
        
        //Variabler
        String StringStorlek = inkuppdrag.get(0);
        IntStorlek = Integer.parseInt(StringStorlek); 
        String [] sline;
        uppdragsid = new String[IntStorlek];
        destination  = new String[IntStorlek];
        passant  = new int[IntStorlek]; 
        samaka  = new int[IntStorlek];
        ds.poang = new int[IntStorlek];
        destNod1 = new int[IntStorlek];
        destNod2 = new int[IntStorlek];
     
      
        for(int k = 1; k <IntStorlek+1 ; k++){
            sline = inkuppdrag.get(k).split(";");
            uppdragsid[k-1] = sline[0];
            destination[k-1] = sline[1];
            passant[k-1] = Integer.parseInt(sline[2]);
            samaka[k-1] = Integer.parseInt(sline[3]);
            ds.poang[k-1] = Integer.parseInt(sline[4]);
           ds.cui.appendStatus("Uppdrag nr "  + uppdragsid[k-1] + " vill åka till " + destination[k-1] //HÄR HAR VI BYTT
            + ", har " + passant[k-1] + " st passagerare, har följande åsikt till samåkning: " + samaka[k-1]
            + " och ger " + ds.poang[k-1] + " poäng.");      
        }
        
             
        for(int j = 0; j <IntStorlek; j++){
            sline = destination[j].split(",");    
            destNod1[j] =Integer.parseInt(sline[0]);
            destNod2[j] =Integer.parseInt(sline[1]);
            System.out.println("Destinationens noder är: " + destNod1[j] + " och " + destNod2[j]); 
        }
        
        
        //Välj uppdrag
        for (int j=0; j<IntStorlek; j++){

            if (passant[j]<=ds.kapacitet) {
                valtUppdragPlats = j;       //Plats i arrayen som uppdraget hämtas från
                valtUppdragId = uppdragsid[j];  //Id på valt uppdrag
                bortvald_flagga = 0;

                break;
                   
            }
            
            else if (j==IntStorlek-1) {
                ds.cui.appendStatus("För mycket folk!"); //HÄR HAR VI BYTT
                bortvald_plats[j] = 1;
                bortvald_flagga = 1;
                valtUppdragPlats = j; // uppdragsid[j];
                valtUppdragId = uppdragsid[j];
             
            }
        }
        }
          
    
     catch (Exception e) { System.out.print(e.toString()); }
     

      ds.cui.appendStatus("\nValt uppdrag: " + valtUppdragId); //HÄR HAR VI BYTT
      ds.cui.appendStatus("");

     return valtUppdragPlats;
    }     

    public String getId(String valtUppdrag){
        id = uppdragsid[Integer.parseInt(valtUppdrag)];
        return id;
    }
   
      public int getPassagerare(int valtUppdrag){
      int uppdragInt = valtUppdrag;
      System.out.println("uppdragsint från getPassagerare: " + uppdragInt);
      
      int passagerardummy = valtUppdrag;
      System.out.println("passagerardummy: " + passagerardummy);
      ds.paxInt = passant[passagerardummy];//[passagerardummy-1]; 
      System.out.println("paxint: " + ds.paxInt);
   
      //pax = ds.paxInt;  
      return pax; 
    }
      
    
    //Vi har ändrat pax från string till int
    public  String tauppdrag(String plats, String id, int pax, String grupp){    //Denna var static
    //   System.out.println("I tauppdrag kommer följande in: Plats: " + plats + " Id: " + id + " Pax: " + pax + " Grupp: " + grupp);
        /**Ta första uppdaget och kolla om kapacitet är ok
            *Om kapacitet är ok --> kolla om samåkning är ok
                *Om samåkning är ok, kolla vidare i listan och spara "nuvarande" passagerare
                *Om samåkning EJ är ok, anropa nekas/beviljas 
                    *Om beviljas --> Dijkstra/OptPlan/Compass -->kör
                    *Om nekas --> sök nytt uppdrag
            *Om kapacitet Ej är ok, kolla vidare i uppdragslistan
            *Om kapacitet är = antal passagerare, kör anropa nekas/beviljas
            *Om beviljas  --> Dijkstra/OptPlan/Compass -->kör
                    *Om nekas --> sök nytt uppdrag
        */
    try {
        ds.cui.appendStatus("\nTar uppdrag.");
         String url = " http://tnk111.n7.se/tauppdrag.php?plats=" + plats + "&id="+id+"&passagerare="+pax+"&grupp="+grupp; 
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

         System.out.println("\nAnropar: " + url);
 
         int mottagen_status = anslutning.getResponseCode();

         System.out.println("Statuskod: " + mottagen_status);

 
         BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
 
        while ((inkommande_text = inkommande.readLine()) != null) {
                inkommande_samlat.append(inkommande_text);
        }
        
        inkommande.close();
        
        String inkommande_string = inkommande_samlat.toString();
        System.out.println(inkommande_string);
        svar = inkommande_string;
        ds.cui.appendStatus("Svar: " + svar);
        ds.cui.appendStatus("");
        
        
        
        }catch (Exception e) { System.out.print(e.toString()); }
       
    return svar;
    }
     
    public String aterstall(String scenario){       //var static från början

         try {
        ds.cui.appendStatus("\nÅterställer.");
         String url = " http://tnk111.n7.se/aterstall.php?scenario=A";// + scenario; 
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

        System.out.println("\nAnropar: " + url);
 
        BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
 
        while ((inkommande_text = inkommande.readLine()) != null) {
                inkommande_samlat.append(inkommande_text);
        }
        inkommande.close();
        
        String inkommande_string = inkommande_samlat.toString();
        System.out.println(inkommande_string);
        
        
         }catch (Exception e) { System.out.print("Fångad i återställs-catch." + e.toString()); }
    return svar;
    }

    public void messtogroup() {
       //skicka meddelande till den andra gruppen 
       ds.cui.appendStatus("Meddelar grupp 3 vår upphämtningsplats.");
     try {
        
         String url = " http://tnk111.n7.se/putmessage.php?groupid=2&messagetype=23&message=" +narmstaPlats; //Kom överens med grupp 3!!!!!
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

         System.out.println("\nAnropar: " + url);
 
         int mottagen_status = anslutning.getResponseCode();

         System.out.println("Statuskod: " + mottagen_status);

 
         BufferedReader inkommande = new BufferedReader(new

        InputStreamReader(anslutning.getInputStream()));
        String inkommande_text;
        StringBuffer inkommande_samlat = new StringBuffer();
        
         ArrayList <String> inkfromserv = new ArrayList<String>();
 
        while ((inkommande_text = inkommande.readLine()) != null) {
                inkommande_samlat.append(inkommande_text);
                inkfromserv.add(inkommande_text);
        }
   
        inkommande.close();
         for(int k = 0; k < inkfromserv.size(); k++){
            ds.cui.appendStatus("Meddelande server: " + inkfromserv.get(k));
         }

        System.out.println(inkommande_samlat.toString());
        }
    
     catch (IOException e) { System.out.print(e.toString()); }
     
    }

     public void messfromgroup() {
       //meddelande från den andra gruppen 
     
     try {
         //Uppdrag http = new Uppdrag();
         String url = "  http://tnk111.n7.se/getmessage.php?messagetype=32"; //Ändra messagetyp OBSOBSOBS
         URL urlobjekt = new URL(url);       
         HttpURLConnection anslutning = (HttpURLConnection)
         urlobjekt.openConnection();

         System.out.println("\nAnropar: " + url);
 
         int mottagen_status = anslutning.getResponseCode();

         System.out.println("Statuskod: " + mottagen_status);

 
         BufferedReader inkommande = new BufferedReader(new

         InputStreamReader(anslutning.getInputStream()));
         String inkommande_text;
         StringBuffer inkommande_samlat = new StringBuffer();
        
        inkmess = new ArrayList<String>();
        
        splitmessfrom = new String [2]; //String-array som sparar datum+tid, ID, upphämtningsplats
     
         
         while ((inkommande_text = inkommande.readLine()) != null) {
                inkommande_samlat.append(inkommande_text);
                inkmess.add(inkommande_text);
         }
        
         inkommande.close();
       
            for(int k = 0; k < inkmess.size(); k++){
            ds.cui.appendStatus("Meddelande från grupp 3: " + inkmess.get(k));
            splitmessfrom = inkmess.get(k).split(";");  //Splittar inkommande meddelande
            ds.messfrom = splitmessfrom[2];             //Plockar ut värdet på plats 2 eftersom det bara är det som är intressant
            ds.cui.appendStatus("\nDet som är intressant " + ds.messfrom);
         }
           
        }
  
     catch (IOException e) { System.out.print(e.toString()); }
     
    }
     
     
 public double RaknaAvstand(int nod1, int nod2){
   double  xstart= ds.nodeX[nod1];
   double xslut = ds.nodeX[nod2];
   double ystart= ds.nodeY[nod1];
   double yslut = ds.nodeY[nod2];
   //nodnr[i] = i;//+1;

    //uppdragsnr[i] = i+1;
   double resvag = Math.hypot(xslut-xstart, yslut-ystart);

  //System.out.println("Sträcka mellan nod " + nod1 + " och nod" + nod2 + ": " + resvag);

    return resvag;
}
 
    public double RaknaAvstand (int[] rutt){
   double[] resvag;        //sträcka i km mellan start- och slutnod
  
   resvag = new double [rutt.length-1]; 
   double xstart;
   double xslut; 
   double ystart;
   double yslut; 
   double tot_resvag = 0;
  
   
          for (int i=0; i < rutt.length-1; i++){
                xstart= ds.nodeX[rutt[i]];
             //   System.out.println("Startnod: " + rutt[i]);
               // System.out.println("x-koordinat för startnod: " + xstart);
                xslut = ds.nodeX[rutt[i+1]];
                ystart= ds.nodeY[rutt[i]];
                yslut = ds.nodeY[rutt[i+1]];
                //nodnr[i] = i;//+1;

                //uppdragsnr[i] = i+1;
                resvag[i] = Math.hypot(xslut-xstart, yslut-ystart);
                
                tot_resvag = tot_resvag + resvag[i];

                
                //System.out.println("Sträcka mellan nod " + rutt[i] + " och nod" + rutt[i+1] + ": " + resvag[i]);
                
            }
     System.out.println("Total resväg: " + tot_resvag);
     return tot_resvag;
}
  
}
    

