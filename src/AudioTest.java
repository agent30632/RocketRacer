import java.io.File;
import java.net.URL;
import javax.swing.*;
import javax.sound.sampled.*;

public class AudioTest {

    public static void main(String[] args) throws Exception {
        File file = new File("music/Trackmania 2 Valley Soundtrack - Vast Veridian.wav");
        Clip clip = AudioSystem.getClip();

        // getAudioInputStream() also accepts a File or InputStream
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        clip.open(ais);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-10);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        SwingUtilities.invokeLater(() -> {
            // A GUI element to prevent the Clip's daemon Thread
            // from terminating at the end of the main()
            JOptionPane.showMessageDialog(null, "Close to exit!");
        });
    }
}