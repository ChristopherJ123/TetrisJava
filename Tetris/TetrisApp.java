package Tetris;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class TetrisApp {

    TetrisGUI tetrisGUI;

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
            {G,G,G,G,G,G,G,G,G,G}
    };

    int[][] tetrominoArea = {
            {0,0,0,0}, //tetrisArea[y][x]
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}
    };

    int[][] tetrominoBoxArea = {
            {0,0,0,0}, //tetrisArea[y][x]
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}
    };

    int[][][] tetrominoes = Arrays.stream(TetrisContants.TETROMINOES).map(int[][]::clone).toArray(int[][][]::new);

    int tetrominoType;
    int tetrominoHoldType;

    boolean timerOn = true;
    boolean enableMovement = true;
    boolean isHolding = false;
    boolean hasHold = false;
    Integer[] orders = new Integer[6];
    int gameOverCondition = 0;
    int score = 0;
    int y = 0;
    int x = 3;
    int yOutline = y;
    int xOutline = x;
    int newTetrominoInterval= 0;

    public void tetrisTimer() throws InterruptedException {
        updateTetrisAreaDisplay(); //update tetris area display on launch
        newTetromino(); //update menu display on launch
        updateTetrominoHoldBoxDisplay();

        tetrisGUI.rightAction = new RightAction();
        tetrisGUI.leftAction = new LeftAction();
        tetrisGUI.downAction = new DownAction();
        tetrisGUI.spaceAction = new SpaceAction();
        tetrisGUI.rotateClockwiseAction = new RotateClockwiseAction();
        tetrisGUI.rotateAntiClockwiseAction = new RotateAntiClockwiseAction();
        tetrisGUI.holdAction = new HoldAction();
        tetrisGUI.exitAction = new ExitAction();


        tetrisGUI.body.getActionMap().put("rightActionKey", tetrisGUI.rightAction);
        tetrisGUI.body.getActionMap().put("leftActionKey", tetrisGUI.leftAction);
        tetrisGUI.body.getActionMap().put("downActionKey", tetrisGUI.downAction);
        tetrisGUI.body.getActionMap().put("spaceActionKey", tetrisGUI.spaceAction);
        tetrisGUI.body.getActionMap().put("rotateClockwiseActionKey", tetrisGUI.rotateClockwiseAction);
        tetrisGUI.body.getActionMap().put("rotateAntiClockwiseActionKey", tetrisGUI.rotateAntiClockwiseAction);
        tetrisGUI.body.getActionMap().put("holdActionKey", tetrisGUI.holdAction);
        tetrisGUI.body.getActionMap().put("exitActionKey", tetrisGUI.exitAction);

        while (timerOn) {
            moveDown();
            Thread.sleep(TetrisContants.TIMER_IN_MILLISECONDS);
        }
    }

    public void generateRandomTetromino() {
        for (int i = 0; i < orders.length; i++) { //check if orders is null
            if (orders[i] == null) {
                orders[i] = (int) (Math.random() * tetrominoes.length);
            }
        }
        for (int i = 1; i < orders.length; i++) { //Turun list
            orders[i-1] = orders[i];
        }
        orders[orders.length - 1] = (int) (Math.random() * tetrominoes.length);
    }

    public void newTetromino() {
        if (isHolding) hasHold = false;
        checkIfLineFull();
        x = 3;
        y = 0;
        xOutline = x;
        yOutline = y;
        tetrominoes = Arrays.stream(TetrisContants.TETROMINOES).map(int[][]::clone).toArray(int[][][]::new);
        newTetrominoInterval = 0;
//        tetrominoType = (int) (Math.random() * tetrominoes.length);
        generateRandomTetromino();
        tetrominoBoxReDraw();
        updateTetrominoBoxDisplay();
        tetrominoType = orders[0];
        gameOverCheck();
    }

    public void gameOverCheck() {
        if (hasHitFloor()) {
            gameOverCondition++;
            if (gameOverCondition >= 2) {
                JOptionPane.showMessageDialog(null, "Game over!", "GAME OVER", JOptionPane.WARNING_MESSAGE);
                timerOn = false;
                enableMovement = false;
            }
        } else
            gameOverCondition = 0;
    }

    public void moveDown() {
        if (hasHitFloor()) {
            newTetrominoInterval++;
            if (newTetrominoInterval == 5) newTetromino();
            return;
        }
        yOutline = y;
        xOutline = x;
        outlineMoveDown();
        tetrominoReDraw(1,0);
        y++;
        updateTetrisAreaDisplay();
    }

    public void moveDownInstant() {
        while (!hasHitFloor()) {
            tetrominoReDraw(1,0);
            y++;
        }
        newTetromino();
        yOutline = y;
        xOutline = x;
        outlineMoveDown();
        tetrominoReDraw(0,0);
        updateTetrisAreaDisplay();
    }

    public void move(int m) {
        if (hasHitWall(m)) return;
        outlineTetrominoErase();
        yOutline = y;
        xOutline = x;
        outlineTetrominoReDraw(0,m);
        xOutline = x + m;
        outlineMoveDown();
        tetrominoReDraw(0,m);
        x = x + m;
        updateTetrisAreaDisplay();
    }

    public int[][] rotate(int[][] array, int direction) {
        int[][] matrix = Arrays.stream(array).map(int[]::clone).toArray(int[][]::new);
        int size = matrix.length;
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        if (direction == 1) { //right rotation
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
        }
        else if (direction == -1) { //left rotation
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
                        if (tetrisArea[row+1 + y][item + x] != 0 && tetrisArea[row+1 + y][item + x] != 'O') {
                            System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") Has hit floor");
                            System.out.println("The floor is "+"(x:"+(item + 1)+" y:"+(row+1 + y + 1)+")" + " The floor value is: " + tetrisArea[row+1 + y][item + x]);
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino bottom edge because the value below is null");
                    if (tetrisArea[row+1 + y][item + x] != 0 && tetrisArea[row+1 + y][item + x] != 'O') {
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

    public boolean outlineHasHitFloor() {
        //Check if hit floor
        for (int row = 0; row < tetrominoes[tetrominoType].length; row++) {
            for (int item = 0; item < tetrominoes[tetrominoType][row].length; item++) {
                try {
                    if (tetrominoes[tetrominoType][row][item] != 0 && tetrominoes[tetrominoType][row + 1][item] == 0) { //cari bawah
                        if (tetrisArea[row+1 + yOutline][item + xOutline] != 0 && tetrisArea[row+1 + yOutline][item + xOutline] != 'O') {
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    try {
                        if (tetrisArea[row+1 + yOutline][item + xOutline] != 0 && tetrisArea[row+1 + yOutline][item + xOutline] != 'O') { //cari bawah
                            return true;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored2) {
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
                        if (tetrisArea[row + y][item+m + x] != 0 && tetrisArea[row + y][item+m + x] != 'O') {
                            System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") Has hit side edge");
                            System.out.println("The side wall is "+"(x:"+(item+m + 1)+" y:"+(row + y + 1)+")" + " The  value is: " + tetrisArea[row + y][item+m + x]);
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino side edge because the value on the beside is null");
                    try {
                        if (tetrisArea[row + y][item+m + x] != 0 && tetrisArea[row + y][item+m + x] != 'O') {
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

    public boolean canRotate (int direction) {
        int[][] tempTetrominoRotation = rotate((Arrays.stream(tetrominoes[tetrominoType]).map(int[]::clone).toArray(int[][]::new)), direction);
        tetrominoErase();
        for (int row = 0; row < tempTetrominoRotation.length; row++) {
            for (int item = 0; item < tempTetrominoRotation[row].length; item++) {
                if (tempTetrominoRotation[row][item] != 0) {
                    System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is a tetromino block because the value is " + tempTetrominoRotation[row][item]);
                    try {
                        if (tetrisArea[row + y][item + x] != 0 && tetrisArea[row + y][item + x] != 'O') {
                            System.out.println("(x:" + (item + 1) + " y:" + (row + 1) + ") Has hit rotation block");
                            System.out.println("The rotation block is " + "(x:" + (item + x + 1) + " y:" + (row + y + 1) + ")" + " The  value is: " + tetrisArea[row + y][item + x]);
                            return false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        //For rotating near borders (WIP)
//                        int errorValue = Integer.parseInt(String.valueOf(e.getMessage().charAt(6)) + e.getMessage().charAt(7));
//                        try {
//                            x = x + direction;
//                            tetrominoErase();
//                        } catch (IndexOutOfBoundsException ignored) {
//                            x = x - 2 * direction;
//                        }
//                        System.out.println(errorValue);
                        System.out.println("(x:" + (item + 1) + " y:" + (row + 1) + ") Has hit rotation block");
                        System.out.println("The rotation block is " + "(x:" + (item + x + 1) + " y:" + (row + y + 1) + ")" + " The value is: null");
                        return false;
                    }
                }
            }
        }
        tetrominoReDraw(0,0);
        return true;
    }

    public boolean outlineCanRotate (int direction) {
        int[][] tempTetrominoRotation = rotate((Arrays.stream(tetrominoes[tetrominoType]).map(int[]::clone).toArray(int[][]::new)), direction);
        tetrominoErase();
        for (int row = 0; row < tempTetrominoRotation.length; row++) {
            for (int item = 0; item < tempTetrominoRotation[row].length; item++) {
                if (tempTetrominoRotation[row][item] != 0) {
                    try {
                        if (tetrisArea[row + yOutline][item + xOutline] != 0 && tetrisArea[row + yOutline][item + xOutline] != 'O') {
                            System.out.println("Outline can't rotate");
                            return false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Outline can't rotate");
                        return false;
                    }
                }
            }
        }
        tetrominoReDraw(0,0);
        return true;
    }

    public void checkIfLineFull() {
        boolean isFull = false;
        for (int row = tetrisArea.length - 2; row > -1; row--) {
            for (int item : tetrisArea[row]) {
                if (item == 0) {
                    isFull = false;
                    break;
                } else {
                    isFull = true;
                }
            }
            if (isFull) {
                clearLine(row);
                break;
            }
        }
    }

    public void clearLine(int index) {
        System.out.println("row/index is " + index);
        System.out.println("Line clear!");
        score = score + 10;
        tetrisGUI.scoreLabel.setText("score: " + score);
        for (int row = index - 1; row > -1; row--) {
            for (int item = 0; item < tetrisArea[row].length; item++) {
                tetrisArea[row+1][item] = tetrisArea[row][item];
                updateTetrisAreaDisplay();
            }
        }
        checkIfLineFull();
    }

    //GUI work
    public void updateTetrisAreaDisplay() {
        for (int row = 0; row < 22; row++) {
            for (int columnInRow = 0; columnInRow < 10; columnInRow++) {
                switch (tetrisArea[row][columnInRow]) {
                    case 1 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.ORANGE);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrisBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 2 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.BLUE);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrisBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 3 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.GREEN);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrisBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 4 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.RED);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrisBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 5 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.TEAL);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrisBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 6 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.YELLOW);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrisBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 7 -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.PURPLE);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrisBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 'O' -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.BLACK);
                        tetrisGUI.tetrisBox[row][columnInRow].setBorder(BorderFactory.createDashedBorder(TetrisContants.GRAY_OUTLINE, 2, 5));
                    }
                    default -> {
                        tetrisGUI.tetrisBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                        tetrisGUI.tetrisBox[row][columnInRow].setBackground(TetrisContants.BLACK);
                        tetrisGUI.tetrisBox[row][columnInRow].setOpaque(true);
                    }
                }
            }
        }
    }

    public void updateTetrominoBoxDisplay() {
        for (int row = 0; row < 15; row++) {
            for (int columnInRow = 0; columnInRow < 4; columnInRow++) {
                switch (tetrominoArea[row][columnInRow]) {
                    case 1 -> {
                        tetrisGUI.tetrominoBox[row][columnInRow].setBackground(TetrisContants.ORANGE);
                        tetrisGUI.tetrominoBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 2 -> {
                        tetrisGUI.tetrominoBox[row][columnInRow].setBackground(TetrisContants.BLUE);
                        tetrisGUI.tetrominoBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 3 -> {
                        tetrisGUI.tetrominoBox[row][columnInRow].setBackground(TetrisContants.GREEN);
                        tetrisGUI.tetrominoBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 4 -> {
                        tetrisGUI.tetrominoBox[row][columnInRow].setBackground(TetrisContants.RED);
                        tetrisGUI.tetrominoBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 5 -> {
                        tetrisGUI.tetrominoBox[row][columnInRow].setBackground(TetrisContants.TEAL);
                        tetrisGUI.tetrominoBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 6 -> {
                        tetrisGUI.tetrominoBox[row][columnInRow].setBackground(TetrisContants.YELLOW);
                        tetrisGUI.tetrominoBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 7 -> {
                        tetrisGUI.tetrominoBox[row][columnInRow].setBackground(TetrisContants.PURPLE);
                        tetrisGUI.tetrominoBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    default -> {
                        tetrisGUI.tetrominoBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                        tetrisGUI.tetrominoBox[row][columnInRow].setBackground(TetrisContants.BLACK);
                        tetrisGUI.tetrominoBox[row][columnInRow].setOpaque(true);
                    }
                }
            }
        }
    }

    public void updateTetrominoHoldBoxDisplay() {
        for (int row = 0; row < 4; row++) {
            for (int columnInRow = 0; columnInRow < 4; columnInRow++) {
                switch (tetrominoBoxArea[row][columnInRow]) {
                    case 1 -> {
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBackground(TetrisContants.ORANGE);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 2 -> {
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBackground(TetrisContants.BLUE);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 3 -> {
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBackground(TetrisContants.GREEN);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 4 -> {
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBackground(TetrisContants.RED);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 5 -> {
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBackground(TetrisContants.TEAL);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 6 -> {
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBackground(TetrisContants.YELLOW);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    case 7 -> {
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBackground(TetrisContants.PURPLE);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setOpaque(true);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                    }
                    default -> {
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setBackground(TetrisContants.BLACK);
                        tetrisGUI.tetrominoHoldBox[row][columnInRow].setOpaque(true);
                    }
                }
            }
        }
    }

    public void outlineMoveDown() { //untuk move down
        while (!outlineHasHitFloor()) { //Forloop insta down
            outlineTetrominoReDraw(1,0);
            yOutline++;
        }
        updateTetrisAreaDisplay();
    }

    public void outlineTetrominoErase() {
        for (int row = 0; row < tetrominoes[tetrominoType].length; row++) {
            for (int item = 0; item < tetrominoes[tetrominoType][row].length; item++) {
                if (String.valueOf(tetrominoes[tetrominoType][row][item]).matches("[1-7]")) {
                    tetrisArea[row + yOutline][item + xOutline] = 0;
                }
            }
        }
    }

    public void outlineTetrominoReDraw(int yVal, int xVal) { //untuk move kanan kiri
        outlineTetrominoErase();
        for (int row = 0; row < tetrominoes[tetrominoType].length; row++) {
            System.out.println("Row value is: " + row);
            for (int item = 0; item < tetrominoes[tetrominoType][row].length; item++) {
                if (String.valueOf(tetrominoes[tetrominoType][row][item]).matches("[1-7]"))
                    tetrisArea[row+yOutline + yVal][item+xOutline + xVal] = 'O';
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

    public void tetrominoBoxErase() {
        for (int row = 0; row < 15; row++) {
            for (int item = 0; item < 4; item++) {
                tetrominoArea[row][item] = 0;
            }
        }
    }

    public void tetrominoBoxReDraw() {
        tetrominoBoxErase();
        for (int i = 2; i < 7; i++) {
            for (int row = 0; row < tetrominoes[orders[i-1]].length; row++) {
                for (int item = 0; item < tetrominoes[orders[i-1]][row].length; item++) {
                    switch (tetrominoes[orders[i-1]][row][item]) {
                        case 1 -> tetrominoArea[row + ((i - 2) * 3)][item] = 1;
                        case 2 -> tetrominoArea[row + ((i - 2) * 3)][item] = 2;
                        case 3 -> tetrominoArea[row + ((i - 2) * 3)][item] = 3;
                        case 4 -> tetrominoArea[row + ((i - 2) * 3)][item] = 4;
                        case 5 -> tetrominoArea[row + ((i - 2) * 3)][item] = 5;
                        case 6 -> tetrominoArea[row + ((i - 2) * 3)][item] = 6;
                        case 7 -> tetrominoArea[row + ((i - 2) * 3)][item] = 7;
                    }
                }
            }
        }
    }

    public void tetrominoHoldBoxErase() {
        for (int row = 0; row < 4; row++) {
            for (int item = 0; item < 4; item++) {
                tetrominoBoxArea[row][item] = 0;
            }
        }
    }

    public void tetrominoHoldBoxReDraw() {
        tetrominoHoldBoxErase();
        for (int row = 0; row < tetrominoes[tetrominoType].length; row++) {
            for (int item = 0; item < tetrominoes[tetrominoType][row].length; item++) {
                switch (tetrominoes[tetrominoType][row][item]) {
                    case 1 -> tetrominoBoxArea[row][item] = 1;
                    case 2 -> tetrominoBoxArea[row][item] = 2;
                    case 3 -> tetrominoBoxArea[row][item] = 3;
                    case 4 -> tetrominoBoxArea[row][item] = 4;
                    case 5 -> tetrominoBoxArea[row][item] = 5;
                    case 6 -> tetrominoBoxArea[row][item] = 6;
                    case 7 -> tetrominoBoxArea[row][item] = 7;
                }
            }
        }
    }

    public TetrisApp() throws InterruptedException {
        tetrisGUI = new TetrisGUI();
        tetrisTimer();
    }

    public static void main(String[] args) throws InterruptedException {
        new TetrisApp();
    }

    //Key binds
    public class RightAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (enableMovement) move(1);
        }
    }
    public class DownAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (enableMovement) moveDown();
        }
    }
    public class LeftAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (enableMovement) move(-1);
        }
    }
    public class SpaceAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (enableMovement) moveDownInstant();
        }
    }
    public class RotateClockwiseAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (canRotate(1) && enableMovement) {
                tetrominoErase();
                outlineTetrominoErase();
                tetrominoes[tetrominoType] = rotate(tetrominoes[tetrominoType], 1);
                yOutline = y;
                xOutline = x;
                outlineMoveDown();
                while (!outlineCanRotate(1)) yOutline--; //buat kondisi can rotate outline
                while (!outlineHasHitFloor()) yOutline++; // buat kondisi hit floor outline ketika outline belum hit floor
                outlineTetrominoReDraw(0,0);
                tetrominoReDraw(0,0); //value 0,0 buat redraw rotation (no value change in rotation)
                updateTetrisAreaDisplay();
            }
        }
    }
    public class RotateAntiClockwiseAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (canRotate(-1) && enableMovement) {
                tetrominoErase();
                outlineTetrominoErase();
                tetrominoes[tetrominoType] = rotate(tetrominoes[tetrominoType], -1);
                yOutline = y;
                xOutline = x;
                outlineMoveDown();
                while (!outlineCanRotate(-1)) yOutline--; //buat kondisi can rotate outline
                while (!outlineHasHitFloor()) yOutline++; // buat kondisi hit floor outline
                outlineTetrominoReDraw(0,0);
                tetrominoReDraw(0,0); //value 0,0 buat redraw rotation (no value change in rotation)
                updateTetrisAreaDisplay();
            }
        }
    }
    public class HoldAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (enableMovement) {
                if (isHolding) {
                    if (hasHold) {
                        return;
                    }
                    int tempTetrominoHoldType = tetrominoHoldType;
                    tetrominoHoldBoxReDraw();
                    updateTetrominoHoldBoxDisplay();
                    //untuk erase/redraw
                    tetrominoErase();
                    outlineTetrominoErase();
                    x = 3;
                    y = 0;
                    // VVV copy dari movedown instant
                    tetrominoHoldType = tetrominoType;
                    tetrominoType = tempTetrominoHoldType;
                    yOutline = y;
                    xOutline = x;
                    outlineMoveDown();
                    tetrominoReDraw(0,0);
                    updateTetrisAreaDisplay();
                    hasHold = true;
                } else { //untuk hold pertama kali (dari else kebawah ini)
                    tetrominoHoldBoxReDraw();
                    updateTetrominoHoldBoxDisplay();
                    tetrominoHoldType = tetrominoType;
                    //untuk erase/redraw
                    tetrominoErase();
                    outlineTetrominoErase();
                    // VVV copy dari movedown instant
                    newTetromino();
                    yOutline = y;
                    xOutline = x;
                    outlineMoveDown();
                    tetrominoReDraw(0, 0);
                    updateTetrisAreaDisplay();
                    isHolding = true;
                    hasHold = true;
                }
            }
        }
    }
    public static class ExitAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
