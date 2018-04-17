//
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lastmileauto;

import java.time.Clock;


public class Compass implements Runnable{
    
    BluetoothTransmitter btm;
    DataStore ds; 
    OptPlan opt;
    double x; 
    double y; 
    String F; 
    String R; 
    String L;  
     
    // n = vår position, noden vi står i just nu
    // n+1 = nästa position, noden vi är på väg till
    
    @Override
    public void run(){
        
        ds = new DataStore();
        opt = new OptPlan(ds);
        //x = ds.nodeX;
        //y = ds.nodeY;
        F = ds.F;
        R = ds.R;
        L = ds.L;
        
        for(int i =0; i <opt.path.size(); i++)
        {
        //System.out.println(opt.path.get(i));
        
            x = ds.nodeX[i]; 
            y = ds.nodeY[i];
            
            System.out.println("" +x+", " +y);
        }   
               
       if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) == 0)){ //Agda kör österut
           //Kolla två framåt: x(n+2)-x(n+1)
           if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) == 0)){
               btm = new BluetoothTransmitter(F);
           }
           else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) < 0)){
               btm = new BluetoothTransmitter(R);
           } 
           else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) > 0)){
               btm = new BluetoothTransmitter(L);
           }  
           else if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) > 0)){
               btm = new BluetoothTransmitter(F);
           }
       } 
       else if((x(n+1) - x(n) < 0) && (y(n+1) - y(n) == 0)){ //Agda kör västerut
           //Kolla två framåt: x(n+2)-x(n+1)
           if((x(n+1) - x(n) < 0) && (y(n+1) - y(n) == 0)){
                btm = new BluetoothTransmitter(F);
           }
           else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) > 0)){
                btm = new BluetoothTransmitter(R);
           }
           else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) < 0)){
                btm = new BluetoothTransmitter(L);
           } 

       }
       else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) > 0)){ //Agda kör norrut
           //Kolla två framåt: x(n+2)-x(n+1)
           if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) > 0)){
                btm = new BluetoothTransmitter(F);
           }
           else if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) == 0)){
                btm = new BluetoothTransmitter(R);
           }
           else if((x(n+1) - x(n) < 0) && (y(n+1) - y(n) == 0)){
                btm = new BluetoothTransmitter(L);
           } 
           else if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) > 0)){
               btm = new BluetoothTransmitter(R);
           }
           else if((x(n+1) - x(n) < 0) && (y(n+1) - y(n) < 0)){
               btm = new BluetoothTransmitter(L);
           }
       }
       else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) < 0)) { //Agda kör söderut
           //Kolla två framåt: x(n+2)-x(n+1)
           if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) < 0)){
                btm = new BluetoothTransmitter(F);
           }
           else if((x(n+1) - x(n) < 0) && (y(n+1) - y(n) == 0)){
                btm = new BluetoothTransmitter(R);
           }
           else if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) == 0)){
                btm = new BluetoothTransmitter(L);
           } 
           else if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) > 0)){
               btm = new BluetoothTransmitter(L);
           }
           else if((x(n+1) - x(n) < 0) && (y(n+1) - y(n) < 0)){
               btm = new BluetoothTransmitter(R);
           }
       }
    }
}
