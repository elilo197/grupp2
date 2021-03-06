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
   String inkommandetext []; //Variabel för inläsning
   String ink_sam; //Variabel för inläsning
   int IntStorlek; 
   int IntStorlekPlatser;
   DataStore ds; //Instans av DataStore-klassen
   OptPlan opt; //Instans av OptPlan-klassen
   OptPlan [] oppis; //Sparar hela den optimerade rutten
   String narmstaPlats; //Närmasta upphämtningsplats;
   String nastnarmstaPlats; 
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
   int i = 0; 
   int upphamtningplatsPlats;       //Arrayplats för vald upphämtningsplats
   int valtUppdragsplatsSamaka;     //Arrayplats för samåkningsuppdraget
   String valtUppdragsIDSamaka;     //Id-nr på det valda samåkningsuppdraget
   String valtUppdragsplatsSamakaString;    //Id-nr på det valda samåkningsuppdraget som string
   ArrayList<String> inkuppdrag;        //Inlästa uppdrag
   String svaruppdragSamaka = "nekas";  //Svar från bokningscentralen på förfårgan om samåkningsuppdraget
   int platsKvar;
  
    public Uppdrag(DataStore ds) { 
        this.ds = ds;
      
    }
    
@Override    
    public void run(){  
    try{
        
    messfromgroup(); //Söker meddelanden från grupp 3
    listaplatser();  
    
    //Om inget meddelande från grupp 3 finns, gör inget
    if(ds.messfrom == null){
        System.out.println("Fanns inget meddelande att hämta, börjar köra.");
    }
    
    //Om grupp 3 är på väg till samma plats som vi och det finns fler än 1 uppdrag, kör
    else if( ds.messfrom == narmstaPlats){
        int dummyAntalUppdrag = Integer.parseInt(inkuppdrag.get(0));
        if(dummyAntalUppdrag > 1 ){
             System.out.println("Fanns mer än 1 uppdrag, börjar köra.");
        }
        
        //Grupp 3 har tagit uppdraget vi var på väg mot
         else 
         System.out.println("Grupp 3 har tagit uppdraget, ta en annan plats.");
    }
    
    
    while (ink.get(0) != null) {    //Kollar så att det finns upphämtningsplatser kvar
    Thread.sleep(100);             //Ger övriga trådar tid
        
        //Kör denna loop en gång för varje "s" (stopp)
            while(ds.scount == i){ 
                
               //Välj uppdrag
                listaplatser();
                valtUppdragPlats = listauppdrag(narmstaPlats); 
       
                //Om det inte finns fler uppdrag på närmsta platsen, välj näst närmsta
                if (IntStorlek == 0){
                    valtUppdragPlats = listauppdrag(nastnarmstaPlats); 
                    if(IntStorlek == 0){
                        while(true){
                            Thread.sleep(1000);
                        ds.cui.appendStatus("Slut på uppdrag!");
                        }
                    }
                }
                
                System.out.println("Valt uppdrag: " + valtUppdragPlats);  
                
                //Sätter passagerarantal till antalet på det valda uppdraget
                ds.pax = getPassagerare(valtUppdragPlats);      
                
                //Kollar hur mycket plats som finns kvar i AGV:n
                platsKvar = ds.kapacitet - ds.pax;  
               
                //Om passagerarantalet överskrider kapaciteten, ta bara så många som får plats
                if(ds.pax > ds.kapacitet){
                   ds.pax = -(platsKvar); 
                   ds.cui.appendStatus("Del av uppdrag taget.\n");
                }
                
                ds.pax_tot = ds.pax;
                
           /*Om kunden vill samåka, det finns plats kvar i fordonet och det finns tillräckligt med uppdrag, 
           välj nytt uppdrag*/
          if(samaka[valtUppdragPlats] == 1 && passant[valtUppdragPlats] < ds.kapacitet && IntStorlek > 1 ){
                       
                System.out.println("Plats kvar: " + platsKvar);
                
                  valtUppdragsplatsSamaka = valtUppdragPlats + 1; 
                  valtUppdragsplatsSamakaString = Integer.toString(valtUppdragsplatsSamaka);
                  valtUppdragsIDSamaka = getId(valtUppdragsplatsSamakaString);  
                  ds.paxSamaka = getPassagerare(valtUppdragsplatsSamaka);
                  ds.pax_tot = ds.pax + ds.paxSamaka;
                
                 //Lägg till ett till uppdrag om kund nr 1 vill samåka och plats finns
                  for(int i = 0; i<IntStorlek; i++){
                    
                    System.out.println("Plats kvar: " + platsKvar);
                    System.out.println("Valt uppdragsplats: " + valtUppdragPlats +"\n"); 
                   
                    //Kolla om passagerarantalet överskrider den kvarvarande platsen
                    if(passant[i] >= platsKvar && i != valtUppdragPlats){
                        valtUppdragsplatsSamaka = i;    //Välj det första uppdraget som är möjligt
                        valtUppdragsplatsSamakaString = Integer.toString(valtUppdragsplatsSamaka);
                        System.out.println("valtUppdragplatsSamakaString: " + valtUppdragsplatsSamakaString );
                        valtUppdragsIDSamaka = getId(valtUppdragsplatsSamakaString);
                    
                    //Uppdatera det totala passagerarantalet   
                       if(passant[i] == platsKvar){
                       ds.paxSamaka = getPassagerare(valtUppdragsplatsSamaka);
                       ds.pax_tot = ds.pax + ds.paxSamaka; 
                       break;
                       }else{
                           ds.paxSamaka = platsKvar;
                           ds.pax_tot = ds.pax + ds.paxSamaka; 
                           break;
                       }
                    }
      
        
                }
            }
                
                //Räknar totala poängen för uppdragen. 
                int dummy; 
                dummy = (valtUppdragPlats);
                ds.totPoang = ds.totPoang + ds.poang[dummy];
                ds.cui.appendPoang(ds.totPoang);
                
                //Skapar arrayer för ruttplanering och kommandon
                oppis1path = new ArrayList<Integer>();
                oppis2path = new ArrayList<Integer>();
                oppis3path = new ArrayList<Integer>();
                oppis1pathNY = new ArrayList<Integer>();
                oppis2pathNY = new ArrayList<Integer>();
                oppis3pathNY = new ArrayList<Integer>();
                ds.kommandon1 = new ArrayList<String>(); 
                ds.kommandon2 = new ArrayList<String>(); 
                ds.kommandon_done = new ArrayList<>();
                
                //Meddela grupp 3 vart vi är på väg
                messtogroup();
                
                //Sätt alla bågar till svarta igen
                for(int j=0; j <128; j++){
                ds.arcColor[j] = 0;           
                }
    
   //Oppis1 börjar här. 
   //Oppis1 går från robotens position (sista noden i förra uppdraget) till upphämtningsplatsen
            
                    ds.startRutt = ds.start; 
                    ds.cui.appendRutt("Robotens startposition: " + ds.startRutt);
                    ds.slutRutt = ds.linkNod1[upphamtningplatsPlats];   //Sätter slutnod till den andra noden i upphämtningslänken
                    oppis1 = new OptPlan(ds);
                    Thread.sleep(100);
                    oppis1path = oppis1.createPlan();
                    
                    /*Lägger till en nod extra i början och i slutet på oppis1path för att möjliggöra färdkommandon
                    för första och sista korsningen. Sparar detta i oppis1pathNY*/
                    oppis1pathNY.add(0, ds.startStart);
                    for(int k = 1; k <oppis1path.size() + 1; k++){
                        oppis1pathNY.add(k, oppis1path.get(k-1));
                    }
                    oppis1pathNY.add(ds.linkNod2[upphamtningplatsPlats]);
                    
                    /*Skapar kommandon mha compass-metoden. 
                    Adderar ett P i slutet för att AGV:n ska stanna och hämta upp passagerare*/
                    ds.kommandon1 = oppis1.compass(oppis1pathNY);
                    ds.kommandon1.add(ds.P);
                    
                    //Lägger till kommandon1 i kommandon-strängen för samtliga kommandon                  
                    for ( int j = 0; j < ds.kommandon1.size(); j++ ){ 
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
                + "Inställning till samåkning: " + samaka[upphamtningplatsPlats] + "\n"); 
            
                
               ds.cui.appendRutt("Kommando för första delrutten: "+ ds.kommandon1);
                
    //Kör denna loop en gång för varje P vi får in, dvs för varje gång en ny delrutt ska köras           
     while(ds.skickatP  == false){
                Thread.sleep(100);
                if(ds.taUppdrag == true){//Nu är Agv:n vid upphämtningsplatsen och vi kan ta uppdrag
                
                String svaruppdrag = tauppdrag(narmstaPlats, valtUppdragId, ds.pax, ds.grupp); //denna ska flyttas sen 
               
                 System.out.println("valtUppdragsIDSamaka: " + valtUppdragsIDSamaka);
                 System.out.println("ds.paxSamaka:" + ds.paxSamaka);
                  
                 
   if (svaruppdrag.equals("beviljas")){
       skapaRutter(); //Kollar samkörning, skapar färdplaner 
       
           ds.skickatP = true;
    }

    //Om uppdrag nekas     
    else { 
       ds.cui.appendStatus("Uppdraget nekades."); 
       
       //Alternativ 1, gör om samma procedur som i if-satsen, men kollar ej om uppdragen beviljats = inte bra
     // valtUppdragPlats = listauppdrag(narmstaPlats);   //Listar uppdrag igen, nu bör det nekade uppdraget vara borta
       //String svaruppdrag2 = tauppdrag(narmstaPlats, valtUppdragId, ds.pax, ds.grupp); //denna ska flyttas sen 
       //skapaRutter();   //Kolla samåkning, gör färdbeskrivningar m.m. (allt som fanns i if-satsen) 
       
       //Alternativ 2, sätt ds.skickatP = true på rad 360 i if-satsen ovan och breaka loopen här 
      // break;
       
        }
                
           //   ds.skickatP = true; //Denna var här medan koden funkade
      }
                
   }

             i++;    //Räknar antalet S 
        
             
             
        //Utskrift     
               
                //Skriv bara ut om oppis3 finns, dvs samåkning sker
                //System.out.println("oppis3 =!" + oppis3);
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
    
    public void skapaRutter(){
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
    

    /** Här listar vi antalet upphämtningsplatser och beräknar vilken som är närmast. 
     */
    public void listaplatser() { 
      
    ds.cui.appendStatus("");    
    ds.cui.appendStatus("Listar platser.");    
        
     try {
       //Läser in information från bokningscentralen
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
         
         
        //Skriver ut vald upphämtningsplats och antal uppdrag på denna 
        ds.cui.appendStatus("Antal upphämtningsplatser: " + ink.get(0));
        for(int k = 1; k < ink.size()-1; k++){      
        ds.cui.appendStatus("Upphämtningsplats: " + ink.get(k));
        }
       
        
        //Variabler
        String StringStorlek = ink.get(0);      //Antal upphämtningsplatser
        IntStorlekPlatser = Integer.parseInt(StringStorlek);   
        String [] link = new String[IntStorlekPlatser];
        ds.linkNod1 = new int[IntStorlekPlatser];      //första noden för upphämtningslänken
        ds.linkNod2 = new int[IntStorlekPlatser];      //andra noder för upphämtningslänken
        oppis = new OptPlan[IntStorlekPlatser];
        String [] sline;
        String [] plats = new String[IntStorlekPlatser];
        double tot_kostnad = 0;         //Totala kostnaden för en väg/alla bågar i en väg
        int narmstaNod = ds.start;
        double lagstaKostnad = 1000000;
        double nastLagstaKostad = 1000000;
        int [] vertexint; 
        
        //Splittar inkommande data vid semikolon
        for(int k = 1; k <IntStorlekPlatser+1 ; k++){
            sline = ink.get(k).split(";");
            link[k-1] = sline[1];
            plats[k-1] = sline[0];       
        }
        
        //Splittar data ytterligare en gång vid komma, vilket ger två arrayer med noderna för upphämtningslänkarna
        for(int j = 0; j <IntStorlekPlatser; j++){
            sline = link[j].split(",");    
            ds.linkNod1[j] =Integer.parseInt(sline[0]);
            System.out.println("linknod i uppfrag: " + ds.linkNod1[j]);
            ds.linkNod2[j] =Integer.parseInt(sline[1]);  
        }
        
    //Beräknar avstånd till de ovan inlästa noderna 
        for (int j=0; j<IntStorlekPlatser; j++){       //loopar över alla upphämtningsplatser
            ds.startRutt = ds.start;        
            ds.slutRutt = ds.linkNod2[j];   //
                      
            oppis[j] = new OptPlan(ds);
            oppis[j].createPlan();
            
            vertexint = new int[oppis[j].path.size()-1];
       
            //Skapar array med alla nodnr i rutten som ints
            for (int i=0; i< oppis[j].path.size()-1; i++){      
            vertexint[i] = Integer.parseInt(oppis[j].path.get(i).getId());
            }
            
            //Räknar ut avståndet för rutten
            tot_kostnad = RaknaAvstand(vertexint);

            ds.cui.appendStatus("Totalkostnad: " + tot_kostnad);
              
            //Skriver ut vilka uppdrag som finns och deras kostnader
             ds.cui.appendStatus("Upphämtningsplats " + plats[j] + " innebär en rutt från " + ds.startRutt + " till "  
                   + ds.slutRutt + " vilket ger en totalkostnad på "  + tot_kostnad);
           
             System.out.println();     
          
            //Räknar ut vilken upphämtningsplats som är närmast 
           if (tot_kostnad < lagstaKostnad){
                 lagstaKostnad = tot_kostnad;           
                 narmstaPlats = plats[j];
                 narmstaNod = ds.slutRutt;
                 upphamtningplatsPlats = j;  
                 
                 //Sparar näst närmsta plats
                 if(j == 0){
                     nastnarmstaPlats = Integer.toString(j+1);
                 }else if(j ==oppis[j].path.size()-1){
                  nastnarmstaPlats = Integer.toString(j+1);
                 }else{
                     nastnarmstaPlats = Integer.toString(j-1);
                 }             
             }
             tot_kostnad = 0;            
        }
        
        //Skriver ut vald upphämtningsplats
             ds.cui.appendStatus("\nNärmaste upphämtningsplats är plats " + narmstaPlats + " med nodnummer " + narmstaNod);
             System.out.println("Kostnaden för att ta sig dit är " + lagstaKostnad);

       } catch (IOException e) { System.out.print(e.toString()); }
     
    }
     
    
     
   //Denna metod används för att lista vilka uppdag som finns på en vald plats. 
    //Vi väljer även vilket uppdrag på platsen vi ska ta. Först kallar vi på hemsidan och läser in uppdragen som finns. 
    public int listauppdrag(String plats){    
     inkuppdrag = new ArrayList<String>();
     
     try {
         ds.cui.appendStatus("\nNu hämtas lista på uppdrag!");
         String url = " http://tnk111.n7.se/listauppdrag.php?plats=" + plats;
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
     
   //Sparar relevant information i olika arrayer. 
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
        
//Sparar noderna för de avlämnsingslänkar som finns              
        for(int j = 0; j <IntStorlek; j++){
            sline = destination[j].split(",");    
            destNod1[j] =Integer.parseInt(sline[0]);
            destNod2[j] =Integer.parseInt(sline[1]);
            System.out.println("Destinationens noder är: " + destNod1[j] + " och " + destNod2[j]); 
        }
        
        
        //Välj första uppdraget
         valtUppdragPlats = 0;       //Plats i arrayen som uppdraget ligger på 
         valtUppdragId = uppdragsid[0];  //Id på valt uppdrag
          System.out.print("ValtuppdragId: " + valtUppdragId);

//Vi hade även gjort en annan variant på hur man väljer uppdrag istället för att bara ta en del av det första uppdraget. 
//Dock användes inte denna på uppvisningen. Denna varian kollar kapaciteten på AGV:n och väljer det uppdrag som
//AGV:n klarar att ta hela av. Om det inte finns något uppdrag som AGV:n kan ta hela så väljs uppdrag 1. 
     //Välj uppdrag
    //    for (int j=0; j<IntStorlek; j++){
            
//            if (passant[j]<=ds.kapacitet) {
//                valtUppdragPlats = j;       //Plats i arrayen som uppdraget hämtas från
//                valtUppdragId = uppdragsid[j];  //Id på valt uppdrag
//                //bortvald_flagga = 0;
//                System.out.print("ValtuppdragId: " + valtUppdragId);
//
//                break;
//                   
//            }
            
//            else if (j==IntStorlek-1) {
//                ds.cui.appendStatus("För mycket folk!"); 
//                valtUppdragPlats = 0;       //Plats i arrayen som uppdraget hämtas från
//                valtUppdragId = uppdragsid[0];  //Id på valt uppdrag
//                System.out.print("ValtuppdragId: " + valtUppdragId);
//
//            }
      //  }
        }
          
    
     catch (Exception e) { System.out.print(e.toString()); }
     

      ds.cui.appendStatus("\nValt uppdrag: " + valtUppdragId); 
      ds.cui.appendStatus("");
//Returnerar platsen i arrayen som valt uppdrag ligger på
     return valtUppdragPlats;
    }     
    //Denna metod används för att returnera ID för det valda uppdraget.  
    public String getId(String valtUppdrag){
        id = uppdragsid[Integer.parseInt(valtUppdrag)];
        return id;
    }

    //Denna metod används för att returnera antalet passagerare för ett valt uppdrag. 
      public int getPassagerare(int valtUppdrag){
      int dummy = 0;

      dummy = passant[valtUppdrag];
      System.out.println("paxint: " + dummy);
   
      return dummy;
    }
      
    
//Denna metod används för att ta uppdrag, information om uppdraget skickas upp till hemsidan
//sedan fås ett svar om uppdraget beviljas eller inte. 
    public  String tauppdrag(String plats, String id, int pax, String grupp){ 

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
    
//En metod som återställer uppdragen på hemsidan, denna är bortkommenterad eftersom den inte används.    
//    public String aterstall(String scenario){
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

//Denna metod används för att skicka meddelande till en hemsida.    
    public void messtogroup() {
//Kallar på hemsidan och skickar upp ett meddelande. 
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

//Denna metod används för att hämta meddelande från en hemsida från den andra gruppen i samma företagsgrupp.     
     public void messfromgroup() {  
     
     try {
         //Kallar på hemsidan, sparar ner informationen och delar upp meddelandet. 
         String url = "http://tnk111.n7.se/getmessage.php?messagetype=32"; 
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
    
//En loop som delar på meddalandet och sparar relevant information.          
            for(int k = 0; k < inkmess.size(); k++){
            splitmessfrom = inkmess.get(k).split(";");  //Splittar inkommande meddelande
            ds.messfrom = splitmessfrom[2];             //Plockar ut värdet på plats 2 eftersom det bara är det som är intressant
            ds.cui.appendStatus("\nGrupp 3 är på väg mot upphämtningsplats  " + ds.messfrom + ".");
         }
           
        }
  
     catch (IOException e) { System.out.print(e.toString()); }
     
    }
     
 //Metod som beräknar avstånd mellan två noder. Tar som inparameter två noder och returnerar avståndet mellan dessa    
 public double RaknaAvstand(int nod1, int nod2){
  //Hämtar x- och y-koordinater för noden och räknar ut avståndet mellan dessa
   double  xstart= ds.nodeX[nod1];
   double xslut = ds.nodeX[nod2];
   double ystart= ds.nodeY[nod1];
   double yslut = ds.nodeY[nod2];
   
   double resvag = Math.hypot(xslut-xstart, yslut-ystart);

    return resvag;
}
 
 //Metod som beräknar den totala resvägen för en rutt. Tar som inparameter en int-array med de noder som ingår i rutten
   public double RaknaAvstand (int[] rutt){
   
   //Varibeldeklaration
   double[] resvag;        
   resvag = new double [rutt.length-1]; 
   double xstart;
   double xslut; 
   double ystart;
   double yslut; 
   double tot_resvag = 0;
  
   
   //Hämtar x- och y-koordinater för noden och räknar ut avståndet mellan dessa
          for (int i=0; i < rutt.length-1; i++){
                xstart= ds.nodeX[rutt[i]];
                xslut = ds.nodeX[rutt[i+1]];
                ystart= ds.nodeY[rutt[i]];
                yslut = ds.nodeY[rutt[i+1]];

                resvag[i] = Math.hypot(xslut-xstart, yslut-ystart);
                
                tot_resvag = tot_resvag + resvag[i];               
             }
     System.out.println("Total resväg: " + tot_resvag);
     return tot_resvag;
}
  
}
    

