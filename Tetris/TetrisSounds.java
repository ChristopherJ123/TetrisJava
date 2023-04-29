package Tetris;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.net.URL;

public class TetrisSounds { //src StackOverflow
    public static synchronized void playSound(String path) {
        try {
            URL url = TetrisSounds.class.getResource(path);
            Clip clip = AudioSystem.getClip();
            // getAudioInputStream() also accepts a File or InputStream
            assert url != null;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip.open(ais);
            clip.start();
            SwingUtilities.invokeLater(() -> {
                // A GUI element to prevent the Clip's daemon Thread
                // from terminating at the end of the main()
            });
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
}
