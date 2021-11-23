import javax.swing.*;
import java.awt.*;

public class VisualizerPanel extends JPanel {
    double[] data = new double[1024];

    public VisualizerPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, getSize().width, getSize().height);
        int width = getSize().width / data.length;
        for (int i = 0; i < data.length; ++i) {
            int height = (int) map(data[i], 0, 30000, 0, getSize().height);
            g.setColor(new Color(200, 200, 100));
            g.fillRect(i * width, getSize().height - height, width, height);
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
