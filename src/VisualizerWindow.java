import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VisualizerWindow extends JFrame implements ActionListener {
    JMenuBar menuBar;
    JMenu inputMenu;
    JRadioButtonMenuItem inputSelections[];
    VisualizerPanel panel;

    // take list of audio inputs
    // populate menu bar
    // Main.setInput
    //
    public VisualizerWindow(ArrayList<Line.Info> inputs) {
        menuBar = new JMenuBar();
        inputMenu = new JMenu("Input");
        menuBar.add(inputMenu);

        //for each audio input create radio button
        ButtonGroup inputButtonGroup = new ButtonGroup();
        for (int i = 0; i < inputs.size(); ++i) {
            var inputSelection = new JRadioButtonMenuItem(inputs.get(i).toString());
            if (i == 0)
                inputSelection.setSelected(true);
            inputSelection.setActionCommand(String.valueOf(i));
            inputButtonGroup.add(inputSelection);
            inputMenu.add(inputSelection);
            inputSelection.addActionListener(this);
        }

        panel = new VisualizerPanel();
        add(panel, BorderLayout.CENTER);

//        setLayout(new BorderLayout());
        setJMenuBar(menuBar);
        //setSize(500, 400);
        setTitle("CS3310 Group10 FFT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Visualizer.changeInput(Integer.parseInt(e.getActionCommand()));
    }

    public void setFFTData(double[] magnitudes) {
        panel.setFFTData(magnitudes);
    }
}
