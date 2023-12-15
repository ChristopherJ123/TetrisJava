package Tetris;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TetrisApp {

    TetrisGUI tetrisGUI;

    int[][][] tetrominoes = Arrays.stream(TetrisContants.TETROMINOES).map(int[][]::clone).toArray(int[][][]::new);

    int[][] tetrisArea = TetrisContants.TETRIS_AREA;
    int[][] tetrominoArea = TetrisContants.TETROMINO_AREA;
    int[][] tetrominoBoxArea = TetrisContants.TETROMINO_BOX_AREA;

    int tetrominoType;
    int tetrominoHoldType;

    boolean timerOn = true;
    boolean enableMovement = true;
    boolean isHolding = false;
    boolean hasHold = false;
    boolean hasClearAllLine = true;
    boolean updateScore = false; //meh
    Integer[] ordersFinal = {0,1,2,3,4,5,6};
    Integer[] orders = new Integer[14];
    int gameOverCondition = 0;
    int score = 0;
    int linesCleared = 0;
    int b2bCombo = 0;
    int combo = 0;
    int y = 0;
    int x = 3;
    int yOutline = y;
    int xOutline = x;
    int newTetrominoInterval= 0;

    public void tetrisTimer() throws InterruptedException {
        newTetromino(); //update menu display on launch

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
            Thread.sleep(1000000 / (score + 1000)); //score = x
        }
    }

    public void generateRandomTetromino() {
        //Random 6 bag system by me
        if (orders[0] == null) {
            List<Integer> ordersList = Arrays.asList(ordersFinal);
            Collections.shuffle(ordersList);
            ordersList.toArray(orders);
        }
        if (orders[7] == null) {
            List<Integer> ordersList = Arrays.asList(ordersFinal);
            Collections.shuffle(ordersList);
            for (int i = 7; i < 14; i++) {
                orders[i] = ordersList.get(i - 7);
            }
            System.out.println(Arrays.toString(orders));
        }
        for (int i = 1; i < orders.length; i++) { //Turun list
            orders[i-1] = orders[i];
        }
        orders[orders.length - 1] = null;
    }

    public void newTetromino() {
        if (isHolding) hasHold = false;
        boolean lineIsFull = checkIfLineFull();
        if (!lineIsFull) combo = 0;
        x = 3;
        y = 0;
        xOutline = x;
        yOutline = y;
        tetrominoes = Arrays.stream(TetrisContants.TETROMINOES).map(int[][]::clone).toArray(int[][][]::new);
        newTetrominoInterval = 0;
//        tetrominoType = (int) (Math.random() * tetrominoes.length);
        generateRandomTetromino();
        tetrominoBoxReDraw();
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
            if (newTetrominoInterval == 3) newTetromino();
            return;
        }
        TetrisSounds.playSound("/Assets/Sounds/softdrop.wav"); //sounds
        yOutline = y;
        xOutline = x;
        outlineMoveDown();
        tetrominoReDraw(1,0);
        y++;
    }

    public void moveDownInstant() {
        while (!hasHitFloor()) {
            tetrominoReDraw(1,0);
            y++;
        }
        TetrisSounds.playSound("/Assets/Sounds/floor.wav"); //sounds
        newTetromino();
        yOutline = y;
        xOutline = x;
        outlineMoveDown();
        tetrominoReDraw(0,0);
    }

    public void move(int m) {
        if (hasHitWall(m)) return;
        TetrisSounds.playSound("/Assets/Sounds/move.wav"); //sounds
        outlineTetrominoErase();
        yOutline = y;
        xOutline = x;
        outlineTetrominoReDraw(0,m);
        xOutline = x + m;
        outlineMoveDown();
        tetrominoReDraw(0,m);
        x = x + m;
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
//                        System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino bottom edge because the value below is " + tetrominoes[tetrominoType][row+1][item]);
                        if (tetrisArea[row+1 + y][item + x] != 0 && tetrisArea[row+1 + y][item + x] != 'O') {
//                            System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") Has hit floor");
//                            System.out.println("The floor is "+"(x:"+(item + 1)+" y:"+(row+1 + y + 1)+")" + " The floor value is: " + tetrisArea[row+1 + y][item + x]);
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
//                    System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino bottom edge because the value below is null");
                    if (tetrisArea[row+1 + y][item + x] != 0 && tetrisArea[row+1 + y][item + x] != 'O') {
//                        System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") Has hit floor");
//                        System.out.println("The floor is "+"(x:"+(item + 1)+" y:"+(row+1 + y + 1)+")" + " The floor value is: " + tetrisArea[row+1 + y][item + x]);
                        return true;
                    }
                }
            }
        }
//        System.out.println("Has not hit floor");
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
//        System.out.println("Has not hit floor");
        return false;
    }

    public boolean hasHitWall(int m) {
        for (int row = 0; row < tetrominoes[tetrominoType].length; row++) {
            for (int item = 0; item < tetrominoes[tetrominoType][row].length; item++) {
                try {
                    if (tetrominoes[tetrominoType][row][item] != 0 && tetrominoes[tetrominoType][row][item + m] == 0) {
//                        System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino side edge because the value on the beside is " + tetrominoes[tetrominoType][row + y][item+m + x]);
                        if (tetrisArea[row + y][item+m + x] != 0 && tetrisArea[row + y][item+m + x] != 'O') {
//                            System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") Has hit side edge");
//                            System.out.println("The side wall is "+"(x:"+(item+m + 1)+" y:"+(row + y + 1)+")" + " The  value is: " + tetrisArea[row + y][item+m + x]);
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
//                    System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino side edge because the value on the beside is null");
                    try {
                        if (tetrisArea[row + y][item+m + x] != 0 && tetrisArea[row + y][item+m + x] != 'O') {
//                            System.out.println("(x:" + (item + 1) + " y:" + (row + 1) + ") Has hit side edge");
//                            System.out.println("The side wall is " + "(x:" + (item+m + x + 1) + " y:" + (row + y + 1) + ")" + " The  value is: " + tetrisArea[row + y][item+m + x]);
                            return true;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored2) {
//                        System.out.println("(x:" + (item + 1) + " y:" + (row + 1) + ") Has hit side edge");
//                        System.out.println("The side wall is " + "(x:" + (item+m + x + 1) + " y:" + (row + y + 1) + ")" + " The  value is: null");
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
//                    System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is a tetromino block because the value is " + tempTetrominoRotation[row][item]);
                    try {
                        if (tetrisArea[row + y][item + x] != 0 && tetrisArea[row + y][item + x] != 'O') {
//                            System.out.println("(x:" + (item + 1) + " y:" + (row + 1) + ") Has hit rotation block");
//                            System.out.println("The rotation block is " + "(x:" + (item + x + 1) + " y:" + (row + y + 1) + ")" + " The  value is: " + tetrisArea[row + y][item + x]);
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
                        //DEBUG VVV
//                        System.out.println("(x:" + (item + 1) + " y:" + (row + 1) + ") Has hit rotation block");
//                        System.out.println("The rotation block is " + "(x:" + (item + x + 1) + " y:" + (row + y + 1) + ")" + " The value is: null");
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
//                            System.out.println("Outline can't rotate");
                            return false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
//                        System.out.println("Outline can't rotate");
                        return false;
                    }
                }
            }
        }
        tetrominoReDraw(0,0);
        return true;
    }

    public boolean checkIfLineFull() {
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
            //Untuk check line full
            if (isFull) {
                updateScore = true;
                linesCleared++;
                clearLine(row);
                if (hasClearAllLine) combo++;
                hasClearAllLine = false;
                return true;
            }
        }
        if (updateScore) {
            updateScore();
            updateScore = false;
        }
        hasClearAllLine = true;
        linesCleared = 0; //reset
        return false;
    }

    public void clearLine(int index) {
        if (linesCleared < 3) TetrisSounds.playSound("/Assets/Sounds/clearline.wav"); //sounds
        else if (linesCleared == 4 && b2bCombo > 0) TetrisSounds.playSound("/Assets/Sounds/clearbtb.wav"); //sounds
        else if (linesCleared == 4) TetrisSounds.playSound("/Assets/Sounds/clearquad.wav"); //sounds
        if (combo == 1) TetrisSounds.playSound("/Assets/Sounds/combo_1.wav"); //sounds
        else if (combo == 2) TetrisSounds.playSound("/Assets/Sounds/combo_2.wav"); //sounds
        else if (combo == 3) TetrisSounds.playSound("/Assets/Sounds/combo_3.wav"); //sounds
        else if (combo == 4) TetrisSounds.playSound("/Assets/Sounds/combo_4.wav"); //sounds
        else if (combo == 5) TetrisSounds.playSound("/Assets/Sounds/combo_5.wav"); //sounds
        else if (combo == 6) TetrisSounds.playSound("/Assets/Sounds/combo_6.wav"); //sounds
        else if (combo == 7) TetrisSounds.playSound("/Assets/Sounds/combo_7.wav"); //sounds
        System.out.println("row/index is " + index);
        System.out.println("Line clear!");
        System.out.println("combo is: " + combo);
        for (int row = index - 1; row > -1; row--) {
            for (int item = 0; item < tetrisArea[row].length; item++) {
                tetrisArea[row+1][item] = tetrisArea[row][item];
            }
        }
        checkIfLineFull(); //method to call clearLine again if there's still other full line
    }

    public void updateScore() {
        if (linesCleared == 4) {
            b2bCombo++;
            System.out.println("b2bCombo = " + b2bCombo);
        }
        else if (linesCleared < 3) {
            System.out.println("linesCleared not 4");
            b2bCombo = 0;
        }
        score = score + (10 * linesCleared) + (10 * combo * linesCleared) + (40 * b2bCombo);
        tetrisGUI.scoreLabel.setText("score: " + score);
    }

    public void outlineMoveDown() { //untuk move down
        while (!outlineHasHitFloor()) { //Forloop insta down
            outlineTetrominoReDraw(1,0);
            yOutline++;
        }
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
//            System.out.println("Row value is: " + row);
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
        tetrisGUI.tetrisGraphics.repaint();
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
                TetrisSounds.playSound("/Assets/Sounds/rotate.wav"); //sounds
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
            }
        }
    }
    public class RotateAntiClockwiseAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (canRotate(-1) && enableMovement) {
                TetrisSounds.playSound("/Assets/Sounds/rotate.wav"); //sounds
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
                    TetrisSounds.playSound("/Assets/Sounds/hold.wav"); //sounds
                    int tempTetrominoHoldType = tetrominoHoldType;
                    tetrominoHoldBoxReDraw();
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
                    hasHold = true;
                } else { //untuk hold pertama kali (dari else kebawah ini)
                    TetrisSounds.playSound("/Assets/Sounds/hold.wav"); //sounds
                    tetrominoHoldBoxReDraw();
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
