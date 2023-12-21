package Tetris;

import javax.sound.sampled.*;
import java.net.URL;

public class TetrisSounds { //src StackOverflow

    String path = "/Assets/Sounds/Tetrio/";

    public void playSFX(String sfx) {
        String format = ".wav";
        playSoundWAV(path + sfx + format);
    }

    public synchronized void playSoundWAV(String path) {
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
