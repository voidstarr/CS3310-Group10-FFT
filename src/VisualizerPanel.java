import javax.swing.*;
import java.awt.*;

public class VisualizerPanel extends JPanel {
    double[] data = new double[1024];

    public VisualizerPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 400);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < getSize().width; ++i) {
            if (i >= data.length) break;
            int height = (int) map(data[i], 0, 30000, 0, getSize().height);
            g.setColor(new Color(200, 200, height));
            g.drawOval(i, height, 2, 2);
        }
    }

    public void setFFTData(double[] magnitudes) {
        data = magnitudes;
        repaint();
    }

    static public double map(double value, double istart, double istop, double ostart, double ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }
}
