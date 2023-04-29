package Tetris;

import javax.sound.sampled.*;
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
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        event.getLine().close();
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
}
