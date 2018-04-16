/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lastmileauto;


public class Compass {
    
    BluetoothTransmitter btm;
    DataStore ds; 
    double[] x; 
    double[] y; 
    String F; 
    String R; 
    String L; 
    
    // n = vår position, noden vi står i just nu
    // n+1 = nästa position, noden vi är på väg till
    
    public Compass(){
        
        ds = new DataStore();
        x = ds.nodeX;
        y = ds.nodeY;
        F = ds.F;
        R = ds.R;
        L = ds.L;
        
       if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) == 0)){ //Agda kör österut
           if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) == 0)){
               btm = new BluetoothTransmitter(F);
           }
           else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) < 0)){
               btm = new BluetoothTransmitter(R);
           } 
           else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) > 0)){
               btm = new BluetoothTransmitter(L);
           }  
       } 
       else if((x(n+1) - x(n) < 0) && (y(n+1) - y(n) == 0)){ //Agda kör västerut
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
           if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) > 0)){
                btm = new BluetoothTransmitter(F);
           }
           else if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) == 0)){
                btm = new BluetoothTransmitter(R);
           }
           else if((x(n+1) - x(n) < 0) && (y(n+1) - y(n) == 0)){
                btm = new BluetoothTransmitter(L);
           } 
       }
       else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) < 0)) { //Agda kör söderut 
           if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) < 0)){
                btm = new BluetoothTransmitter(F);
           }
           else if((x(n+1) - x(n) < 0) && (y(n+1) - y(n) == 0)){
                btm = new BluetoothTransmitter(R);
           }
           else if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) == 0)){
                btm = new BluetoothTransmitter(L);
           }  
       }
    }
}

