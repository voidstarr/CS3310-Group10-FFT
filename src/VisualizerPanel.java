import javax.swing.*;
import java.awt.*;

public class VisualizerPanel extends JPanel {
    double[] data = new double[1024];

    public VisualizerPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    /*
    * Paint FFT graph
    * */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, getSize().width, getSize().height);
        int width = getSize().width / data.length * 8;
        int j = getSize().width;
        for (int i = data.length / 8; i > 0; --i) {
            int height = (int) map(data[i], 0, 2000, 0, 300);
            g.setColor(new Color(200, 200, 100));
            g.fillRect((i + 8) * width, getSize().height - height, width, height);
        }
    }

    /*
    * Receive FFT data and repaint this component
    * */
    public void setFFTData(double[] magnitudes) {
        data = magnitudes;
        repaint();
    }

    /*
    * Clamps a value to a range
    * */
    static public double map(double value, double istart, double istop, double ostart, double ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }
}
