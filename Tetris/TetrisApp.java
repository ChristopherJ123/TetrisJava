package Tetris;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TetrisApp {

    TetrisGUI tetrisGUI = new TetrisGUI();

    char G = 'G';
    int[][] tetrisArea = {
            {0,0,0,0,0,0,0,0,0,0}, //tetrisArea[y][x]
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {G,G,G,G,G,G,G,G,G,G},
    };

    int[][][] tetrominoes = TetrisContants.TETROMINOES;

    int tetrominoType = (int) (Math.random() * tetrominoes.length);


    boolean timerOn = true;
    int y = 0;
    int x = 0;
    int newTetrominoInterval= 0;

    public void tetrisTimer() throws InterruptedException {

        tetrisGUI.rightAction = new RightAction();
        tetrisGUI.leftAction = new LeftAction();
        tetrisGUI.downAction = new DownAction();
        tetrisGUI.spaceAction = new spaceAction();
        tetrisGUI.rotateClockwiseAction = new rotateClockwiseAction();
        tetrisGUI.rotateAntiClockwiseAction = new rotateAntiClockwiseAction();

        tetrisGUI.tetrisBody.getActionMap().put("rightActionKey", tetrisGUI.rightAction);
        tetrisGUI.tetrisBody.getActionMap().put("leftActionKey", tetrisGUI.leftAction);
        tetrisGUI.tetrisBody.getActionMap().put("downActionKey", tetrisGUI.downAction);
        tetrisGUI.tetrisBody.getActionMap().put("spaceActionKey", tetrisGUI.spaceAction);
        tetrisGUI.tetrisBody.getActionMap().put("rotateClockwiseActionKey", tetrisGUI.rotateClockwiseAction);
        tetrisGUI.tetrisBody.getActionMap().put("rotateAntiClockwiseActionKey", tetrisGUI.rotateAntiClockwiseAction);

        while (timerOn) {
            moveDown();
            Thread.sleep(1000);
        }
    }

    public void newRandomTetromino() {
        x = 0;
        y = 0;
        newTetrominoInterval = 0;
        tetrominoType = (int) (Math.random() * tetrominoes.length);
    }

    public void moveDown() {
        if (hasHitFloor()) {
            newTetrominoInterval++;
            if (newTetrominoInterval == 5) newRandomTetromino();
            return;
        }
        tetrominoReDraw(1,0);
        y++;
        updateDisplay();
    }

    public void moveDownInstant() {
        while (!hasHitFloor()) {
            tetrominoReDraw(1,0);
            y++;
        }
        newRandomTetromino();
        tetrominoReDraw(0,0);
        updateDisplay();
    }

    public void move(int m) {
        if (hasHitWall(m)) return;
        tetrominoReDraw(0,m);
        x = x + m;
        updateDisplay();
    }

    public int[][] clockwiseRotate(int[][] matrix) {
        int size = matrix.length;
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        for (int i = 0; i < size; i++) {
            int low = 0, high = size - 1;
            while (low < high) {
                int temp = matrix[i][low];
                matrix[i][low] = matrix[i][high];
                matrix[i][high] = temp;
                low++;
                high--;
            }
        }
        return matrix;
    }

    public int[][] antiClockwiseRotate(int[][] matrix) {
        int size = matrix.length;
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        for (int i = 0; i < size; i++) {
            int low = 0, high = size - 1;
            while (low < high) {
                int temp = matrix[low][i];
                matrix[low][i] = matrix[high][i];
                matrix[high][i] = temp;
                low++;
                high--;
            }
        }
        return matrix;
    }

    public boolean hasHitFloor() {
        //Check if hit floor
        for (int row = 0; row < tetrominoes[tetrominoType].length; row++) {
            for (int item = 0; item < tetrominoes[tetrominoType][row].length; item++) {
                try {
                    if (tetrominoes[tetrominoType][row][item] != 0 && tetrominoes[tetrominoType][row + 1][item] == 0) {
                        System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino bottom edge because the value below is " + tetrominoes[tetrominoType][row+1][item]);
                        if (tetrisArea[row+1 + y][item + x] != 0) {
                            System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") Has hit floor");
                            System.out.println("The floor is "+"(x:"+(item + 1)+" y:"+(row+1 + y + 1)+")" + " The floor value is: " + tetrisArea[row+1 + y][item + x]);
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino bottom edge because the value below is null");
                    if (tetrisArea[row+1 + y][item + x] != 0) {
                        System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") Has hit floor");
                        System.out.println("The floor is "+"(x:"+(item + 1)+" y:"+(row+1 + y + 1)+")" + " The floor value is: " + tetrisArea[row+1 + y][item + x]);
                        return true;
                    }
                }
            }
        }
        System.out.println("Has not hit floor");
        return false;
    }

    public boolean hasHitWall(int m) {
        for (int row = 0; row < tetrominoes[tetrominoType].length; row++) {
            for (int item = 0; item < tetrominoes[tetrominoType][row].length; item++) {
                try {
                    if (tetrominoes[tetrominoType][row][item] != 0 && tetrominoes[tetrominoType][row][item + m] == 0) {
                        System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino side edge because the value on the beside is " + tetrominoes[tetrominoType][row + y][item+m + x]);
                        if (tetrisArea[row + y][item+m + x] != 0) {
                            System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") Has hit side edge");
                            System.out.println("The side wall is "+"(x:"+(item+m + 1)+" y:"+(row + y + 1)+")" + " The  value is: " + tetrisArea[row + y][item+m + x]);
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino side edge because the value on the beside is null");
                    try {
                        if (tetrisArea[row + y][item+m + x] != 0) {
                            System.out.println("(x:" + (item + 1) + " y:" + (row + 1) + ") Has hit side edge");
                            System.out.println("The side wall is " + "(x:" + (item+m + x + 1) + " y:" + (row + y + 1) + ")" + " The  value is: " + tetrisArea[row + y][item+m + x]);
                            return true;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored2) {
                        System.out.println("(x:" + (item + 1) + " y:" + (row + 1) + ") Has hit side edge");
                        System.out.println("The side wall is " + "(x:" + (item+m + x + 1) + " y:" + (row + y + 1) + ")" + " The  value is: null");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //GUI work
    private void updateDisplay() {
        for (int row = 0; row < 22; row++) {
            for (int columnInRow = 0; columnInRow < 10; columnInRow++) {
                switch (tetrisArea[row][columnInRow]) {
                    case 1 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.ORANGE);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 2 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.BLUE);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 3 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.GREEN);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 4 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.RED);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 5 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.TEAL);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 6 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.YELLOW);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 7 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.PURPLE);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    default -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.BLACK);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                    }
                }
            }
        }
    }

    public void tetrominoErase() {
        for (int row = 0; row < tetrominoes[tetrominoType].length; row++) {
            for (int item = 0; item < tetrominoes[tetrominoType][row].length; item++) {
                if (String.valueOf(tetrominoes[tetrominoType][row][item]).matches("[1-7]")) {
                    tetrisArea[row+y][item+x] = 0;
                }
            }
        }
    }

    public void tetrominoReDraw(int yVal, int xVal) {
        tetrominoErase();
        for (int row = 0; row < tetrominoes[tetrominoType].length; row++) {
            for (int item = 0; item < tetrominoes[tetrominoType][row].length; item++) {
                switch (tetrominoes[tetrominoType][row][item]) {
                    case 1 -> tetrisArea[row+y + yVal][item+x + xVal] = 1;
                    case 2 -> tetrisArea[row+y + yVal][item+x + xVal] = 2;
                    case 3 -> tetrisArea[row+y + yVal][item+x + xVal] = 3;
                    case 4 -> tetrisArea[row+y + yVal][item+x + xVal] = 4;
                    case 5 -> tetrisArea[row+y + yVal][item+x + xVal] = 5;
                    case 6 -> tetrisArea[row+y + yVal][item+x + xVal] = 6;
                    case 7 -> tetrisArea[row+y + yVal][item+x + xVal] = 7;
                }
            }
        }
    }

    public TetrisApp() throws InterruptedException {
        tetrisTimer();
    }

    public static void main(String[] args) throws InterruptedException {
        new TetrisApp();
    }

    //Key binds
    public class RightAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            move(1);
        }
    }
    public class DownAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveDown();
        }
    }
    public class LeftAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            move(-1);
        }
    }
    public class spaceAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveDownInstant();
        }
    }
    public class rotateClockwiseAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            tetrominoErase();
            tetrominoes[tetrominoType] = clockwiseRotate(tetrominoes[tetrominoType]);
            tetrominoReDraw(0,0);
            updateDisplay();
        }
    }
    public class rotateAntiClockwiseAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            tetrominoErase();
            tetrominoes[tetrominoType] = antiClockwiseRotate(tetrominoes[tetrominoType]);
            tetrominoReDraw(0,0);
            updateDisplay();
        }
    }
}
