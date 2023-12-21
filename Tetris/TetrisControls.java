package Tetris;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TetrisControls implements ActionListener {
    private final TetrisApp tetrisApp;
    private final static String PRESSED = "pressed ";
    private final static String RELEASED = "released ";
    private final Map<String, String> pressedKeys = new HashMap<>();
    private final JComponent component;
    private final Timer timer;
    private String keyType;

    TetrisControls(TetrisApp tetrisApp, JComponent component) {
        this.tetrisApp = tetrisApp;
        this.component = component;
        timer = new Timer(TetrisContants.ARR, this);
        timer.setInitialDelay(TetrisContants.DAS);
    }

    private class AnimationAction extends AbstractAction implements ActionListener {
        String key;
        String func;
        String type;

        AnimationAction(String key, String func, String type) {
            this.key = key;
            this.func = func;
            this.type = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            handleKeyEvent(key, func, type);
        }
    }

    public void addLoopAction(String keyStroke, String func) {
        int offset = keyStroke.lastIndexOf(" ");
        String key = offset == -1 ? keyStroke : keyStroke.substring(offset + 1);
        String modifiers = keyStroke.replace(key, "");

        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        // Pressed
        Action pressedAction = new AnimationAction(key, func, "pressed");
        String pressedKey = modifiers + PRESSED + key;
        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(pressedKey);
        inputMap.put(pressedKeyStroke, pressedKey);
        actionMap.put(pressedKey, pressedAction);

        // Released
        Action releasedAction = new AnimationAction(key, null, "released");
        String releasedKey = modifiers + RELEASED + key;
        KeyStroke releasedKeyStroke = KeyStroke.getKeyStroke(releasedKey);
        inputMap.put(releasedKeyStroke, releasedKey);
        actionMap.put(releasedKey, releasedAction);
    }

    private void handleKeyEvent(String key, String func, String type) {
        String keyType = key + " " + type;
        if (func == null) {
            pressedKeys.remove(key);
            this.keyType = keyType;
        } else {
            if (!Objects.equals(this.keyType, keyType)) {
                callMethod(func, false);
                this.keyType = keyType;
            }
            if (!Objects.equals(func, "rotateRight") &&
                    !Objects.equals(func, "rotateLeft") &&
                    !Objects.equals(func, "place") &&
                    !Objects.equals(func, "hold") &&
                    !Objects.equals(func, "rotate180")
            )
                pressedKeys.put(key, func);
        }

        if (pressedKeys.size() >= 1) {
            timer.start();
        }
        else {
            timer.stop();
        }
    }

    private void callMethod(String func, boolean onTimer) {
        switch (func) {
            case "moveRight" -> {
                if (TetrisContants.ARR == 1 && onTimer) while (!tetrisApp.hasHitObstacle(1, 0)) tetrisApp.move(1, 0);
                else tetrisApp.move(1, 0);

                if (!tetrisApp.hasHitObstacle(1, 0)) tetrisApp.tetrisSounds.playSFX("move");
//                else tetrisApp.tetrisSounds.playSFX("hit");
            }
            case "moveLeft" -> {
                if (TetrisContants.ARR == 1 && onTimer) while (!tetrisApp.hasHitObstacle(-1, 0)) tetrisApp.move(-1, 0);
                else tetrisApp.move(-1, 0);

                if (!tetrisApp.hasHitObstacle(-1, 0)) tetrisApp.tetrisSounds.playSFX("move");
//                else tetrisApp.tetrisSounds.playSFX("hit");
            }
            case "moveDown" -> {
                if (TetrisContants.SDF == 1) {
                    boolean isMove = false;
                    while (!tetrisApp.hasHitObstacle(0, 1)) {
                        tetrisApp.move(0, 1);
                        isMove = true;
                    }
                    if (isMove) tetrisApp.tetrisSounds.playSFX("softdrop");
                }
                else {
                    tetrisApp.move(0, 1);
                    if (!tetrisApp.hasHitObstacle(0, 1)) tetrisApp.tetrisSounds.playSFX("softdrop");
//                    else tetrisApp.tetrisSounds.playSFX("hit");
                }
            }
            case "rotateRight" -> {
                if (tetrisApp.rotate(1)) tetrisApp.tetrisSounds.playSFX("rotate");
            }
            case "rotateLeft" -> {
                if (tetrisApp.rotate(-1)) tetrisApp.tetrisSounds.playSFX("rotate");
            }
            case "rotate180" -> {
                if (tetrisApp.rotate(2)) tetrisApp.tetrisSounds.playSFX("rotate");
            }
            case "place" -> {
                tetrisApp.place();
                tetrisApp.tetrisSounds.playSFX("harddrop");
            }
            case "hold" -> {
                if (tetrisApp.hold()) tetrisApp.tetrisSounds.playSFX("hold");
            }
            case "exit" -> System.exit(0);
            case "sound" -> {
                System.out.println(tetrisApp.tetrisSounds.path);
                if (!Objects.equals(tetrisApp.tetrisSounds.path, "/Assets/Sounds/Tetrio/")) tetrisApp.tetrisSounds.path = "/Assets/Sounds/Tetrio/";
                else tetrisApp.tetrisSounds.path = "/Assets/Sounds/";
            }

        }
        tetrisApp.isHardLocked = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) { // Timer listener
        for (String func : pressedKeys.values()) {
            callMethod(func, true);
        }
    }
}
