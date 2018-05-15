/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
   String inkommandetext []; //Osäker var den här används och gör?
   String ink_sam; //Osäker var den här används och gör?
   int IntStorlek;
   DataStore ds; //Instans av DataStore-klassen
   OptPlan opt; //Instans av OptPlan-klassen
   OptPlan [] oppis; //Sparar hela den optimerade rutten
   String narmstaPlats; // = "Start";
   String id; //Uppdragsid 
   String [] uppdragsid; //Array med uppdragsid
   String [] destination; //Array med nodnummer för destinationen
   int [] passant; //Passagerarantal
   int [] samaka;  //Samåkning
   int[] destNod1; //Noden AGV:n kommer in på först på avlämningsbågen
   int[] destNod2; //Nästa nod på avlämningsbågen
   int valtUppdragPlats; //Plats i array för valt uppdrag
   String valtUppdragId; //Id på valt uppdrag
   String svar; //Skickar beviljas eller nekas om uppdraget kan tas
   OptPlan oppis1; //Optimerad rutt till upphämtningsplatsen
   OptPlan oppis2; //Optimerad rutt till avlämningsplatsen
   OptPlan oppis3; //Optimerad rutt vid samåkning
   ArrayList<Integer> oppis1path;       
   ArrayList<Integer> oppis1pathNY; //skapat ny oppis 
   ArrayList<Integer> oppis2path;
   ArrayList<Integer> oppis2pathNY; //skapat ny oppis 
   ArrayList<Integer> oppis3path;
   ArrayList<Integer> oppis3pathNY;
   ArrayList<String> ink; 
   ArrayList<String> inkmess; 
   String [] splitmessfrom;  
   int bortvald_flagga = 0;
   int[] bortvald_plats;
   int i = 0; 
   int upphamtningplatsPlats;
   int valtUppdragsplatsSamaka;
   String valtUppdragsIDSamaka;
   String valtUppdragsplatsSamakaString;
   ArrayList<String> inkuppdrag;
   String svaruppdragSamaka = "nekas";
    
  
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
    }
    
   
    
    while (ink.get(0) != null) {    //Kollar så att det finns upphämtningsplatser kvar
    Thread.sleep(100);      //för att slippa göra utskrifterna ovan
 
            while(ds.scount == i){  //Kollar antal "s", stopp, kör igång loopen när s=1, adderar på varje varv
                
                listaplatser();
                
                //Välj första uppdraget
                valtUppdragPlats = listauppdrag(narmstaPlats);           //Skickar in upphämtningsplats, skickar ut vilket uppdrag vi väljer
                System.out.println("Valt uppdrag: " + valtUppdragPlats);  
                ds.pax = getPassagerare(valtUppdragPlats);      //Sätter passagerarantal till antalet på det valda uppdraget


                //Om kunden vill samåka, det finns plats kvar i fordonet och det finns tillräckligt med uppdrag 
          if(samaka[valtUppdragPlats] == 1 && passant[valtUppdragPlats] < ds.kapacitet && IntStorlek > 1 ){
                int platsKvar = ds.kapacitet - passant[valtUppdragPlats];          
                System.out.println("Plats kvar: " + platsKvar);
                
                  valtUppdragsplatsSamaka = valtUppdragPlats + 1; 
                  valtUppdragsplatsSamakaString = Integer.toString(valtUppdragsplatsSamaka);
                  valtUppdragsIDSamaka = getId(valtUppdragsplatsSamakaString);  
                  ds.paxSamaka = getPassagerare(valtUppdragsplatsSamaka);
                  ds.pax_tot = ds.pax + ds.paxSamaka;
                
                for(int i = 0; i<IntStorlek; i++){
                    
                    //Lägg till ett till uppdrag om kund nr 1 vill samåka och plats finns
                    System.out.println("\nMellan for och if när vi tänker ta samåkningsuppdrag."); 
                    System.out.println("Plats kvar: " + platsKvar);
                    System.out.println("Valt uppdragsplats: " + valtUppdragPlats +"\n"); 
            
                    
                    if(passant[i] >= platsKvar && i != valtUppdragPlats){
                        valtUppdragsplatsSamaka = i;    //Välj det första uppdraget som är möjligt
                        valtUppdragsplatsSamakaString = Integer.toString(valtUppdragsplatsSamaka);
                        System.out.println("valtUppdragplatsSamakaString: " + valtUppdragsplatsSamakaString );
                        valtUppdragsIDSamaka = getId(valtUppdragsplatsSamakaString);
                       
                       if(passant[i] == platsKvar){
                       ds.paxSamaka = getPassagerare(valtUppdragsplatsSamaka);
                       ds.pax_tot = ds.pax + ds.paxSamaka; //Uppdatera passagerarantalet
                       break;
                       }else{
                           ds.paxSamaka = platsKvar;
                           ds.pax_tot = ds.pax + ds.paxSamaka;  //Uppdatera passagerarantalet
                           break;
                       }
                    }
      
                    
        
                }
            }
                
                //Räknar totala poängen för uppdragen. 
                int dummy; 
                dummy = (valtUppdragPlats);
                ds.totPoang = ds.totPoang + ds.poang[dummy];
                //System.out.println("Totala poäng: " + ds.totPoang);
                ds.cui.appendPoang(ds.totPoang);

                oppis1path = new ArrayList<Integer>();
                oppis2path = new ArrayList<Integer>();
                oppis3path = new ArrayList<Integer>();
                oppis1pathNY = new ArrayList<Integer>();
                oppis2pathNY = new ArrayList<Integer>();
                oppis3pathNY = new ArrayList<Integer>();
                ds.kommandon1 = new ArrayList<String>(); 
                ds.kommandon2 = new ArrayList<String>(); 
                ds.kommandon_done = new ArrayList<>();
                
                //messfromgroup();                      //VI HAR FLYTTAT UPP DENNA JUST NU MVH H OCH V
                messtogroup();
                            for(int j=0; j <128; j++){
                            ds.arcColor[j] = 0;           
                            }
    
   //Oppis 1 börjar här. 
   //Oppis1 går från robotens position (sista noden i förra uppdraget) till upphämtningsplatsen
            
                    ds.startRutt = ds.start; 
                    ds.cui.appendRutt("Robotens startposition: " + ds.startRutt);
                    ds.slutRutt = ds.linkNod1[upphamtningplatsPlats];
                    //ds.cui.appendRutt("Slutnod i delrutt 1: " + ds.slutRutt);
                    oppis1 = new OptPlan(ds);
                    Thread.sleep(100);
                    oppis1path = oppis1.createPlan();
                    
                    //Lägger till en nod extra i början och i slutet på oppis1path och sparar detta i oppis1pathNY
                    oppis1pathNY.add(0, ds.startStart);
                    for(int k = 1; k <oppis1path.size() + 1; k++){
                        oppis1pathNY.add(k, oppis1path.get(k-1));
                    }
                    oppis1pathNY.add(ds.linkNod2[upphamtningplatsPlats]);
                    
                    
                    ds.kommandon1 = oppis1.compass(oppis1pathNY);
                    ds.kommandon1.add(ds.P);
                                      
                    for ( int j = 0; j < ds.kommandon1.size(); j++ ){ //Lägger till kommandon1 i kommandon_done
                        ds.kommandon_done.add(ds.kommandon1.get(j));
                    }
                    ds.cui.repaint(); 
                 
                //Startar robotsend-tråden    
                RobotSend RSend = new RobotSend(ds);       
                Thread RobotSendThread = new Thread(RSend);
                RobotSendThread.start();
                
               //Skriver ut vad som valts hittills
                ds.cui.appendRutt("Valt uppdrag: " +  valtUppdragId + ".\n" 
                + "Antal passagerare: " + ds.pax + "\nUpphämtningsplats: "
                   + narmstaPlats + ", mellan nod " + ds.linkNod1[upphamtningplatsPlats] + " och nod " 
                   + ds.linkNod2[upphamtningplatsPlats] + "\nAvlämningsplats: Länk mellan nod " + destNod1[valtUppdragPlats]
                    + " och nod " +destNod2[valtUppdragPlats] + ".\n" 
                + "Inställning till samåkning: " + samaka[upphamtningplatsPlats]); 
            
                
               ds.cui.appendRutt("Kommando för första delrutten: "+ ds.kommandon1 + "\n");
                
                while(ds.skickatP  == false){
                Thread.sleep(100);
                if(ds.taUppdrag == true){//Nu är Agv:n vid upphämtningsplatsen och vi kan ta uppdrag
                
                 String svaruppdrag = tauppdrag(narmstaPlats, valtUppdragId, ds.pax, ds.grupp); //denna ska flyttas sen 
               
                 System.out.println("valtUppdragsIDSamaka: " + valtUppdragsIDSamaka);
                 System.out.println("ds.paxSamaka:" + ds.paxSamaka);
                  
                 
   if (svaruppdrag.equals("beviljas")){
                  
                //Om det valda uppdraget kan tas, kolla om det går att ta fler uppdrag samtidigt     
                     if(samaka[valtUppdragPlats] == 1 && passant[valtUppdragPlats] < ds.kapacitet && IntStorlek > 1){
                     System.out.println("Första uppdaget vill och kan samåka.");
                     System.out.println("Ta uppdrag: " + narmstaPlats + valtUppdragsIDSamaka + ds.paxSamaka + ds.grupp);
                     svaruppdragSamaka = tauppdrag(narmstaPlats, valtUppdragsIDSamaka, ds.paxSamaka, ds.grupp);
                     ds.cui.appendRutt("Ta uppdrag: " + svaruppdragSamaka); 
                     }

   //Oppis 2 börjar här. 
   //Oppis2 går från upphämtningsplatsen till första avlämningsplatsen
    
                    ds.startRutt = ds.linkNod2[upphamtningplatsPlats];  
                   // ds.cui.appendRutt("Upphämtningsnod i delrutt 2: " + ds.startRutt);
                    ds.slutRutt = destNod1[valtUppdragPlats];                    
                   // ds.cui.appendRutt("Destinationsnod i delrutt 2: " + ds.slutRutt);
                    ds.start = destNod2[valtUppdragPlats];  //Vart nästa optimering ska starta
                    System.out.println("Startnod: " + ds.start);
                    ds.startStart=destNod1[valtUppdragPlats];
    
                    oppis2 = new OptPlan(ds);
                    oppis2path = oppis2.createPlan();
                    
                    
                   oppis2pathNY.add(0, ds.linkNod1[upphamtningplatsPlats]);
                    for(int k = 1; k <oppis2path.size() + 1; k++){
                        oppis2pathNY.add(k, oppis2path.get(k-1));
                    }
                    oppis2pathNY.add(destNod2[valtUppdragPlats]);
                     
                    for(int i = 0; i <oppis2pathNY.size(); i++){
                        System.out.println("oppispath2 : " + oppis2pathNY.get(i));
                    }
                    
                    ds.kommandon2 = oppis2.compass(oppis2pathNY);      
                   
                    //Om sakåkning sker, lägg till ett P för avsläppning efter oppis2
                   if(svaruppdragSamaka.equals("beviljas")){
                    ds.kommandon2.add(ds.P);
                   }
                   else {
                       ds.kommandon2.add(ds.S);     //Om samåkning ej sker, lägg till S för stopp
                   }
                    
                    for ( int j = 0; j < ds.kommandon2.size(); j++ ){ //Lägger till kommandon2 i kommandon 
                    ds.kommandon_done.add(ds.kommandon2.get(j));
                    }
                    
                    ds.sistanod2 = (oppis2path.get(oppis2path.size()-2));   //Lägger till sista noden i föregående rutt i en ny arraylist som ska 
                  
                    ds.cui.appendRutt("Kommandon i andra delrutten: " + ds.kommandon2);
                    System.out.println("Kommandon i Oppis2: " + ds.kommandon_done); 
                    
                    
                     ds.cui.repaint();          //Ritar ny väg
                     

   //Oppis 3 börjar här. 
  //Oppis3 går från första avlämningsplatsen till andra avlämningsplatsen

            
        //Allt i oppis 3 ska bara göras om kunden i det valda uppdraget vill samåka och det finns plats i fordonet
        if(svaruppdragSamaka.equals("beviljas")){
            
            ds.cui.appendRutt("Ta uppdrag för samåkning: " + svaruppdragSamaka);    
                              
                    //Rutten startar vid föregående rutts sista nod, dvs avlämningsplatsen för oppis2
                    ds.startRutt =destNod2[valtUppdragPlats];  
                    
                    
                    
                    ds.cui.appendStatus("Startnod i delrutt 3: " + ds.startRutt);
                    System.out.println("Startrutt för samåkning: " + ds.startRutt);
                    //Rutten slutar vid den första noden i avlämnings-länken för samåkningsuppdraget
                    ds.slutRutt = destNod1[valtUppdragsplatsSamaka]; 
                    ds.cui.appendStatus("Startnod i delrutt 3: " + ds.slutRutt);
                    System.out.println("Slutrutt för samåkning: " + ds.slutRutt);
                    ds.cui.repaint(); 
                    //Variabler för att förenkla hantering av info vid optimeringen 
                    ds.start = destNod2[valtUppdragsplatsSamaka];  //Var nästa optimering ska starta
                    ds.startStart=destNod1[valtUppdragsplatsSamaka]; 

                    oppis3 = new OptPlan(ds);
                    oppis3path = oppis3.createPlan();
                      
                   oppis3pathNY.add(0, destNod1[valtUppdragPlats]);
                    for(int k = 1; k <oppis3path.size() + 1; k++){
                        oppis3pathNY.add(k, oppis3path.get(k-1));
                    }
                    oppis3pathNY.add(destNod2[valtUppdragsplatsSamaka]);
              
                    for(int i = 0; i <oppis3pathNY.size(); i++){
                        System.out.println("oppispath3 : " + oppis3pathNY.get(i));
                    }
                    
                    //Skapar körkommandon
                     ds.kommandon3 = oppis3.compass(oppis3pathNY);       
                    ds.kommandon3.add(ds.S); //Lägger till stoppkommando i slutet

                    for ( int j = 0; j < ds.kommandon3.size(); j++ ){ //Lägger till kommandon3 i kommandon 
                    ds.kommandon_done.add(ds.kommandon3.get(j));
                    }
                    
                    System.out.println("Kommandon i Oppis3: " + ds.kommandon_done); 

                     ds.cui.repaint();          //Ritar ny väg
                         
                     
           }
    }

         
    else {System.out.println("Svar från hemsida: " + svaruppdrag);}
                
                ds.skickatP = true;
                }
                
                }

             i++;    //Räknar antalet S 
        
             
             
        //Utskrift     
               
                //Skriv bara ut om oppis3 finns, dvs samåkning sker
                System.out.println("oppis3 =!" + oppis3);
                if (oppis3 != null) {
                    ds.cui.appendRutt("Samåkning sker med uppdrag med id " + valtUppdragsIDSamaka + ".\n" 
                   + "Detta uppdraget har " + ds.paxSamaka + " passagerare. \n Avlämningsplats: Mellan nod " 
                   + destNod1[valtUppdragsplatsSamaka] + " och nod " +destNod2[valtUppdragsplatsSamaka] + ".\n");
                  ds.cui.appendRutt("Kommandon för tredje delrutten: "+ ds.kommandon3 + " \n");
                }
                
                 ds.cui.appendRutt("Kommandon för hela rutten: "+ ds.kommandon_done + " \n");
                 
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
        ds.linkNod1 = new int[IntStorlek];      //första noden för upphämtningslänken
        ds.linkNod2 = new int[IntStorlek];      //andra noder för upphämtningslänken
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
            ds.linkNod1[j] =Integer.parseInt(sline[0]);
            System.out.println("linknod i uppfrag: " + ds.linkNod1[j]);
            ds.linkNod2[j] =Integer.parseInt(sline[1]);  
        }
        
//Nu har vi nod-nr på uppdragen. Dags att beräkna avstånd! 
                
        //Här borde en loop börja
        for (int j=0; j<IntStorlek; j++){       //Den här ska loopa över alla upphämtningsplatser
            ds.startRutt = ds.start;        
            ds.slutRutt = ds.linkNod2[j];
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
                 upphamtningplatsPlats = j;
             }
             tot_kostnad = 0; 
        }
             ds.cui.appendStatus("\nNärmaste upphämtningsplats är plats " + narmstaPlats + " med nodnummer " + narmstaNod);
             System.out.println("Kostnaden för att ta sig dit är " + lagstaKostnad);

       } catch (IOException e) { System.out.print(e.toString()); }
     
    }
     
    
     
   
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
      int dummy = 0;

      dummy = passant[valtUppdrag];
      System.out.println("paxint: " + dummy);
   
      return dummy;
    }
      
    

    public  String tauppdrag(String plats, String id, int pax, String grupp){    //Denna var static

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
     
//    public String aterstall(String scenario){       //var static från början
//
//         try {
//        ds.cui.appendStatus("\nÅterställer.");
//         String url = " http://tnk111.n7.se/aterstall.php?scenario=A";// + scenario; 
//         URL urlobjekt = new URL(url);       
//         HttpURLConnection anslutning = (HttpURLConnection)
//         urlobjekt.openConnection();
//
//        System.out.println("\nAnropar: " + url);
// 
//        BufferedReader inkommande = new BufferedReader(new
//
//        InputStreamReader(anslutning.getInputStream()));
//        String inkommande_text;
//        StringBuffer inkommande_samlat = new StringBuffer();
// 
//        while ((inkommande_text = inkommande.readLine()) != null) {
//                inkommande_samlat.append(inkommande_text);
//        }
//        inkommande.close();
//        
//        String inkommande_string = inkommande_samlat.toString();
//        System.out.println(inkommande_string);
//        
//        
//         }catch (Exception e) { System.out.print("Fångad i återställs-catch." + e.toString()); }
//    return svar;
//    }

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
    

