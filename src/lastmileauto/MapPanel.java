package lastmileauto;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

// Ritar ut allt på kartan, ex. robot osv. 

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


        if (ds.networkRead == true) { // Only try to plot is data has been properly read from file

            // Compute scale factor in order to keep the map in proportion when the window is resized
            int height = getHeight();
            int width = getWidth();
            double xscale = 1.0 * width / xsize;
            double yscale = 1.0 * height / ysize;

            g.setColor(DARK_COLOR);

            // Draw nodes as circles
            for (int i = 0; i < ds.nodes; i++) {
                x = (int) (ds.nodeX[i] * xscale);
                y = (int) (ds.nodeY[i] * yscale);
                
                g.fillOval(x - (circlesize / 2), height - y - circlesize / 2, circlesize, circlesize);
               //g.drawString(" " + ds.nodeNr[i], x, y);
            }
            
            ds.tot_arcCost = new int[ds.arcs];
            
            // Draw arcs
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
                
                
                // arcCost
                x = Math.abs(x1 - x2);
                y = Math.abs(y1 - y2);
                
                ds.tot_arcCost[i] = x + y; // här är skillnaden, fyller liksom ds variabeln här så vi kan hämta den i ds sen 
 
                g.drawString("" + ds.tot_arcCost[i], (x1+x2)/2,((height - y1 ) + (height - y2))/2);
              }

            // Draw robot
            robotPosX = (int)((ds.robotX) * xscale);
            robotPosY = (int)((ds.robotY)*yscale);
            
             g.setColor(MAGENTA_COLOR);
             // Bara linjer. 
            //g.drawOval(robotPosX-((circlesize+10)/2), height-robotPosY-(circlesize+10)/2, circlesize+10, circlesize+10);
          
            //Ifylld cirkel
            g.fillOval(robotPosX-((circlesize+10)/2), height-robotPosY-(circlesize+10)/2, circlesize+10, circlesize+10);
            
        }
    } // end paintComponent
 
    
}