import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Visualizer {

    static ArrayList<Line.Info> inputs = new ArrayList<>();
    static TargetDataLine openInput;
    static VisualizerWindow window;
    // 44100Hz mono audio stream represented by signed big endian 16 bit integers
    static final AudioFormat preferredFormat = new AudioFormat(44100, 16, 1, true, true);
    static boolean stopped = false;

    public static void main(String[] args) throws LineUnavailableException {
        var info = new DataLine.Info(TargetDataLine.class, preferredFormat);

        var mixerInfos = new ArrayList<>(Arrays.asList(AudioSystem.getMixerInfo()));
        var portInfo = new Line.Info(Port.class);
        for (var mixerInfo : mixerInfos) {
            var mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(info)) {
                var input = new ArrayList<>(Arrays.asList(mixer.getTargetLineInfo()));
                for (Line.Info srcInfo : input) {
                    if (srcInfo.getLineClass().equals(TargetDataLine.class)) {
                        inputs.add(srcInfo);
                    }
                }
            }
        }
        window = new VisualizerWindow(inputs);

        changeInput(0);
        System.out.printf("open line buffer size: %d%n", openInput.getBufferSize());


        Thread dataRunner = new Thread(() -> {
            int bytesRead;
            byte[] data = new byte[openInput.getBufferSize()];
            while (!stopped) {
                if (!openInput.isOpen()) continue;
                bytesRead = openInput.read(data, 0, data.length);
                double[] samples = new double[data.length / 2];
                for (int i = 1; i < data.length; i *= 2) {
                    samples[i - 1] = data[i] << 8 | data[i - 1] & 0xFF;
                }
                double[] samplesImag = new double[data.length / 2];
                double[] magnitudes = Rad2FFT.Radix2FFT(samples, samplesImag);
//                System.out.println(Arrays.toString(magnitudes));
                window.setFFTData(magnitudes);
            }
        });
        dataRunner.start();
    }

    public static void changeInput(int i) {
        if (openInput != null && openInput.isOpen()) openInput.close();
        try {
            openInput = (TargetDataLine) AudioSystem.getLine(inputs.get(i));
            openInput.open(preferredFormat, 1024);
            openInput.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        stopped = true;
    }
}
