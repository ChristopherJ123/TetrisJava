package Tetris;

public class TetrisSpeed extends Thread{

    private final TetrisApp tetrisApp;

    TetrisSpeed(TetrisApp instance) {
        tetrisApp = instance;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
            tetrisApp.move(0, 1);
        while (true) {
                Thread.sleep(1000);
                tetrisApp.move(0, 1);
                if (tetrisApp.hasHitObstacle(0, 1)) {
                    if (tetrisApp.isHardLocked) tetrisApp.place();
                    else tetrisApp.isHardLocked = true;
                }
            }
        } catch (InterruptedException ignored) {
        }
    }
}
