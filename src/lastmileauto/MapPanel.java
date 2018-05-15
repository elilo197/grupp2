package lastmileauto;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

//Ritar ut allt på kartan som AGV, bågar och noder. 

public class MapPanel extends JPanel {
    DataStore ds; 
 
    MapPanel(DataStore ds) {
        this.ds = ds;
    }

    protected void paintComponent(Graphics g) { 
        super.paintComponent(g);
        final Color LIGHT_COLOR = new Color(150, 150, 150);
        final Color DARK_COLOR = new Color(0, 0, 0);
        final Color MAGENTA_COLOR = new Color(255, 0, 255);
        int x, y;
        int x1, y1;
        int x2, y2;
        int robotPosX;
        int robotPosY; 
        final int circlesize = 10;
        final int ysize = 350;
        final int xsize = 700;

        //Plottar data om den har lästs in på rätt sätt
        if (ds.networkRead == true) { 

            //Beräknar skalfaktor så att kartans proportioner följer gränssnittets storlek
            int height = getHeight();
            int width = getWidth();
            double xscale = 1.0 * width / xsize;
            double yscale = 1.0 * height / ysize;

            g.setColor(DARK_COLOR);

            //Ritar noder som cirklar
            for (int i = 0; i < ds.nodes; i++) {
                x = (int) (ds.nodeX[i] * xscale);
                y = (int) (ds.nodeY[i] * yscale);
                int nodnr = i+1;
                g.fillOval(x - (circlesize / 2), height - y - circlesize / 2, circlesize, circlesize);
                g.drawString(" " + nodnr, (x),((height - y ) + (height - y))/2);
            }
            
            ds.tot_arcCost = new int[ds.arcs];
            
            //Ritar bågar
            g.setColor(MAGENTA_COLOR);
            for (int i = 0; i < ds.arcs; i++) {
                if (ds.arcColor[i] == 0){
                    g.setColor(DARK_COLOR);    
                }
                else if(ds.arcColor[i]==1) {
                    g.setColor(MAGENTA_COLOR);
                }
                
                x1 = (int) (ds.nodeX[ds.arcStart[i] - 1] * xscale);
                y1 = (int) (ds.nodeY[ds.arcStart[i] - 1] * yscale);
                x2 = (int) (ds.nodeX[ds.arcEnd[i] - 1] * xscale);
                y2 = (int) (ds.nodeY[ds.arcEnd[i] - 1] * yscale);
                g.drawLine(x1, height - y1, x2, height - y2);
                               
                //Bågkostnaden
                x = Math.abs(x1 - x2);
                y = Math.abs(y1 - y2);
                ds.tot_arcCost[i] = x + y; 
 
                //Skriver ut bågkostnaderna i kartan
                //g.drawString("" + ds.tot_arcCost[i], (x1+x2)/2,((height - y1 ) + (height - y2))/2);
              }

            //Ritar AGV:n på kartan
            robotPosX = (int)((ds.robotX) * xscale);
            robotPosY = (int)((ds.robotY) * yscale);
            
            g.setColor(MAGENTA_COLOR);
          
            //Fyller i AGV:n
            g.fillOval(robotPosX-((circlesize+10)/2), height-robotPosY-(circlesize+10)/2, circlesize+10, circlesize+10);
            
        }
    } // end paintComponent
 
    
}