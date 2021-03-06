import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MyPanel extends JPanel {

    int startX, flag, startY, endX, endY;

    BufferedImage grid;
    Graphics2D gc;

    public MyPanel(int[] color) {
        startX = startY = 0;
        endX = endY = 100;
        int[] x;
        x = color;
    }

    public void clear() {
        grid = null;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (grid == null) {
            int w = this.getWidth();
            int h = this.getHeight();
            grid = (BufferedImage) (this.createImage(w, h));
            gc = grid.createGraphics();

        }
        g2.drawImage(grid, null, 0, 0);
    }

    public void drawing() {

        gc.drawLine(0, 255, 0, 255);
        repaint();

    }

}
