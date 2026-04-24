import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Ground {







    public BufferedImage createImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        g.dispose();
        return bi;
    }

    public int getImageColor(BufferedImage sampleImage) {
        int width = sampleImage.getWidth();
        int height = sampleImage.getHeight();
        int result;
        result = sampleImage.getRGB(width, height);
        return result;
    }
}


