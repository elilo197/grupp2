
package lastmileauto;

import java.time.Clock;


public class Compass implements Runnable{
    
    BluetoothTransmitter btm;
    DataStore ds; 
    OptPlan opt;
   // double [] x; 
    //double [] y; 
    String F; 
    String R; 
    String L;  
     
    // n = vår position, noden vi står i just nu
    // n+1 = nästa position, noden vi är på väg till
    
    @Override
    public void run(){
        
        ds = new DataStore();
        opt = new OptPlan(ds);
        //x = ds.nodeX[path.get;
        //y = ds.nodeY;
        F = ds.F;
        R = ds.R;
        L = ds.L;
       
    //  x = new double[10];
      //y = new double[ds.pathInt.length];
        
        for(int i =0; i <ds.pathInt.length; i++)
        {

        System.out.println("kolla hit" + ds.pathInt[i]);

         

           // x[i] = ds.nodeX[ds.pathInt[i]]; 
            //y[i] = ds.nodeY[ds.pathInt[i]];

            
            System.out.println("tjena" +ds.x[i]+", " +y[i]);
           if((x[i+1] - x[i] > 0) && (y[i+1] - y[i] == 0)){ //Agda kör österut
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
        }   
               
//       if((x[i+1] - x[i] > 0) && (y[i+1] - y[i] == 0)){ //Agda kör österut
//           //Kolla två framåt: x(n+2)-x(n+1)
//           if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) == 0)){
//               btm = new BluetoothTransmitter(F);
//           }
//           else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) < 0)){
//               btm = new BluetoothTransmitter(R);
//           } 
//           else if((x(n+1) - x(n) == 0) && (y(n+1) - y(n) > 0)){
//               btm = new BluetoothTransmitter(L);
//           }  
//           else if((x(n+1) - x(n) > 0) && (y(n+1) - y(n) > 0)){
//               btm = new BluetoothTransmitter(F);
//           }
//       } 
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
