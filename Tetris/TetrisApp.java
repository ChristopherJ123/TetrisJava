package Tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TetrisApp {

    TetrisGUI tetrisGUI;
    TetrisSpeed tetrisSpeed;
    TetrisSounds tetrisSounds;

    // Areas
    int[][][] tetrominos = Arrays.stream(TetrisContants.TETROMINOS).map(int[][]::clone).toArray(int[][][]::new);
    int[][] dp_lockfield = TetrisContants.TETRIS_AREA;
    int[][] dp_playfield = TetrisContants.VISUAL_AREA;
    int[][] dp_queue = TetrisContants.TETROMINO_AREA;
    int[][] dp_hold = TetrisContants.TETROMINO_BOX_AREA;
    ArrayList<Integer> queue = new ArrayList<>(Arrays.asList(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1));

    // Current piece
    int[][] currentPiece;
    int currentPieceType;
    int currentRotation = 0;
    int currentYPos = 17; // todo update using constant
    int currentXPos = TetrisContants.TETRIS_AREA[0].length/2;
    int currentYGapToObstacle;

    // Current Hold
    int currentHoldPieceType = -1;
    boolean hasHold = false;

    // Timing
    boolean isHardLocked = false;

    // Scoring
    boolean isGameStart = false;
    int score = 0;
    int level = 1;
    int combo = -1;
    int b2bCombo = -1;
    boolean isSpinClear = false;

    public void generateRandomQueue() {
        List<Integer> list = Arrays.asList(0,1,2,3,4,5,6);
        if (queue.get(0) == -1) {
            Collections.shuffle(list);
            for (int i = 0; i < 7; i++) {
                queue.set(i, list.get(i));
            }
        }
        if (queue.get(7) == -1) {
            Collections.shuffle(list);
            for (int i = 7; i < 14; i++) {
                queue.set(i, list.get(i-7));
            }
        }
        draw_dp_queue();
    }

    public void nextPiece() {
        scoring();
        if (queue.get(7) == -1) generateRandomQueue();
        currentPiece = Arrays.stream(tetrominos[queue.get(0)]).map(int[]::clone).toArray(int[][]::new);
        currentPieceType = queue.get(0);
        queue.remove(0);
        queue.add(-1);

        // Reset current variables && reset hasHold
        currentYPos = 17;
        currentXPos = (TetrisContants.TETRIS_AREA[0].length/2) - 2;
        currentRotation = 0;
        isSpinClear = false;

        hasHold = false;

        tetrisSpeed.interrupt();
        tetrisSpeed = new TetrisSpeed(this);
        tetrisSpeed.start();

        isGameStart = true;

        draw_dp_queue();
        draw_dp_playfield();
    }

    public boolean move(int x, int y) {
        if (hasHitObstacle(x, y)) {
            return false;
        }
        currentXPos += x;
        currentYPos += y;
        draw_dp_playfield();
        return true;
    }

    public boolean rotate(int dir) {
        int[][] initial_tetromino = Arrays.stream(currentPiece).map(int[]::clone).toArray(int[][]::new);
        int initial_currentXPos = currentXPos;
        int initial_currentYPos = currentYPos;

        rotateMatrix(currentPiece, dir);
        if (!checkSpinIfAny(currentRotation, dir)) {
            currentPiece = Arrays.stream(initial_tetromino).map(int[]::clone).toArray(int[][]::new);
            currentXPos = initial_currentXPos;
            currentYPos = initial_currentYPos;
            return false;
        }
        currentRotation+=dir;
        if (currentRotation == -1) currentRotation = 3;
        else if (currentRotation >= 4) currentRotation -= 4;
        draw_dp_playfield();
        return true;
    }

    public void place() {
        while (move(0, 1));
        draw_dp_lockfield();
        nextPiece();
    }

    public boolean hold() {
        if (!hasHold) {
            hasHold = true;
            if (currentHoldPieceType == -1) {
                currentHoldPieceType = currentPieceType;
                nextPiece();
                hasHold = true;
            } else {
                currentPiece = Arrays.stream(tetrominos[currentHoldPieceType]).map(int[]::clone).toArray(int[][]::new);
                int temp = currentPieceType;
                currentPieceType = currentHoldPieceType;
                currentHoldPieceType = temp;
                // Reset current variables
                currentYPos = 17;
                currentXPos = (TetrisContants.TETRIS_AREA[0].length/2) - 2;
                currentRotation = 0;
                isSpinClear = false;
            }
            draw_dp_hold();
            draw_dp_playfield();
            return true;
        }
        return false;
    }

    public void scoring() {
        if (!isGameStart) return;

        boolean isComboBreak = false;

        int lineCleared = lineClearIfAny();
        if (lineCleared > 0) combo++;
        else if (lineCleared == 0) {
            if (combo > 2) isComboBreak = true;
            combo = -1;
        }

        // Scoring

        boolean isPerfectClear = true;
        for (int[] row : dp_lockfield) {
            for (int col : row) {
                if (col != 0) {
                    isPerfectClear = false;
                    break;
                }
            }
        }

        if (isSpinClear || lineCleared == 4) b2bCombo++;
        else if (lineCleared > 0) b2bCombo = -1;

        switch (lineCleared) {
            case 1 -> {
                if (isSpinClear) score += ((b2bCombo>0?800*1.5:800) * level);
                else score += (100 * level);
                if (isPerfectClear) score += (800 * level);
            }
            case 2 -> {
                if (isSpinClear) score += ((b2bCombo>0?1200*1.5:1200) * level);
                else score += (300 * level);
                if (isPerfectClear) score += (1200 * level);
            }
            case 3 -> {
                if (isSpinClear) score += ((b2bCombo>0?1600*1.5:1600) * level);
                else score += (500 * level);
                if (isPerfectClear) score += (800 * level);
            }
            case 4 -> {
                score += ((b2bCombo>0?800*1.5:800) * level);
                if (isPerfectClear) score += (800 * level);
            }
        }

        tetrisGUI.scoreLabel.setText("Score : " + score);

        // Sounds part
        if (lineCleared != 0) {
            System.out.println(b2bCombo);
            if (isPerfectClear) tetrisSounds.playSFX("allclear");

            if (b2bCombo > 0) tetrisSounds.playSFX("clearbtb");
            else if (lineCleared == 4) tetrisSounds.playSFX("clearquad");
            else if (lineCleared > 0) {
                if (isSpinClear) tetrisSounds.playSFX("clearspin");
                else tetrisSounds.playSFX("clearline");
            }

            if (combo > 0 && combo <= 16) tetrisSounds.playSFX("combo_" + combo);
            else if (combo > 16 ) tetrisSounds.playSFX("combo_16");

            if (b2bCombo == 2) tetrisSounds.playSFX("btb_1");
            else if (b2bCombo == 5) tetrisSounds.playSFX("btb_2");
            else if (b2bCombo >= 8 && b2bCombo % 8 == 0) tetrisSounds.playSFX("btb_3");
        } else if (isComboBreak) {
            tetrisSounds.playSFX("combobreak");
        }
    }

    public int lineClearIfAny() {
        int lineCleared = 0;
        for (int i = 0; i < dp_lockfield.length; i++) {
            boolean isFull = false;
            for (int j = 0; j < dp_lockfield[i].length; j++) {
                if (dp_lockfield[i][j] != 0) isFull = true;
                else {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                lineCleared++;
                for (int j = i; j > 0; j--) {
                    System.arraycopy(dp_lockfield[j-1], 0, dp_lockfield[j], 0, dp_lockfield[j].length);
                }
            }
        }
        return lineCleared;
    }

    public boolean hasHitObstacle(int x, int y) {
        for (int row = 0; row < currentPiece.length; row++) {
            for (int col = 0; col < currentPiece[row].length; col++) {
                if (currentPiece[row][col] != 0) {
                    try {
                        if (dp_lockfield[currentYPos + row + y][currentXPos + col + x] != 0) return true;
                    } catch (ArrayIndexOutOfBoundsException e) {return true;}
                }
            }
        }
        return false;
    }

    public boolean tetrominoAreaSurroundedByObstacle() {
        int rowCornerMin = 99;
        int colCornerMin = 99;
        int rowCornerMax = -1;
        int colCornerMax = -1;
        for (int row = 0; row < currentPiece.length; row++) {
            for (int col = 0; col < currentPiece[row].length; col++) {
                if (currentPiece[row][col] != 0) {
                    rowCornerMin = Math.min(rowCornerMin, row);
                    colCornerMin = Math.min(colCornerMin, col);
                    rowCornerMax = Math.max(rowCornerMax, row);
                    colCornerMax = Math.max(colCornerMax, col);
                }
            }
        }
        System.out.println(colCornerMin + ", " + rowCornerMin + ", " + colCornerMax + ", " + rowCornerMax);
        for (int row = rowCornerMin; row <= rowCornerMax; row++) {
            for (int col = colCornerMin; col <= colCornerMax; col++) {
                System.out.println("currentPiece[" + row + "][" + col + "] = " + currentPiece[row][col]);
                if (currentPiece[row][col] == 0) {
                    System.out.println("dp_lockfield[" + (currentYPos + row) + "][" + (currentXPos + col) + "] = " + dp_lockfield[currentYPos + row][currentXPos + col]);
                    if (dp_lockfield[currentYPos + row][currentXPos + col] == 0) return false;
                }
            }
        }
        return true;
    }

    public boolean tSpinThreeCornersCheck() {
        int cornerCount = 0;
        System.out.println("corner count : " + cornerCount);
        try {
            if (dp_lockfield[currentYPos][currentXPos] != 0) cornerCount++;
        } catch (ArrayIndexOutOfBoundsException e) {cornerCount++;}
        try {
            if (dp_lockfield[currentYPos+2][currentXPos] != 0) cornerCount++;
        } catch (ArrayIndexOutOfBoundsException e) {cornerCount++;}
        try {
            if (dp_lockfield[currentYPos+2][currentXPos+2] != 0) cornerCount++;
        } catch (ArrayIndexOutOfBoundsException e) {cornerCount++;}
        try {
            if (dp_lockfield[currentYPos][currentXPos+2] != 0) cornerCount++;
        } catch (ArrayIndexOutOfBoundsException e) {cornerCount++;}
        return cornerCount >= 3;
    }

    public int wallKick(int initialRot, int dir) {
        if (currentPieceType == 5) return 1;
        else if (currentPieceType != 4) {
            switch (initialRot) {
                case 0 -> {
                    if (dir == 1) {
                        if (!move(0, 0)) {
                            if (!move(-1, 0)) {
                                if (!move(-1, -1)) {
                                    if (!move(0, 2)) {
                                        if (!move(-1, 2)) return -1;
                                        else return 5;
                                    } else return 4;
                                } else return 3;
                            } else return 2;
                        } else return 1;
                    } else if (dir == -1) {
                        if (!move(0, 0)) {
                            if (!move(1, 0)) {
                                if (!move(1, -1)) {
                                    if (!move(0, 2)) {
                                        if (!move(1, 2)) return -1;
                                        else return 5;
                                    } else return 4;
                                } else return 3;
                            } else return 2;
                        } else return 1;
                    } else return 0;
                }
                case 1 -> {
                    if (dir == 1) {
                        if (!move(0, 0)) {
                            if (!move(1, 0)) {
                                if (!move(1, 1)) {
                                    if (!move(0, -2)) {
                                        if (!move(1, -2)) return -1;
                                        else return 5;
                                    } else return 4;
                                } else return 3;
                            } else return 2;
                        } else return 1;
                    } else if (dir == -1) {
                        if (!move(0, 0)) {
                            if (!move(1, 0)) {
                                if (!move(1, 1)) {
                                    if (!move(0, -2)) {
                                        if (!move(1, -2)) return -1;
                                        else return 5;
                                    } else return 4;
                                } else return 3;
                            } else return 2;
                        } else return 1;
                    } else return 0;
                }
                case 2 -> {
                    if (dir == 1) {
                        if (!move(0, 0)) {
                            if (!move(1, 0)) {
                                if (!move(1, -1)) {
                                    if (!move(0, 2)) {
                                        if (!move(1, 2)) return -1;
                                        else return 5;
                                    } else return 4;
                                } else return 3;
                            } else return 2;
                        } else return 1;
                    } else if (dir == -1) {
                        if (!move(0, 0)) {
                            if (!move(-1, 0)) {
                                if (!move(-1, -1)) {
                                    if (!move(0, 2)) {
                                        if (!move(-1, 2)) return -1;
                                        else return 5;
                                    } else return 4;
                                } else return 3;
                            } else return 2;
                        } else return 1;
                    } else return 0;
                }
                case 3 -> {
                    if (dir == 1) {
                        if (!move(0, 0)) {
                            if (!move(-1, 0)) {
                                if (!move(-1, 1)) {
                                    if (!move(0, -2)) {
                                        if (!move(-1, -2)) return -1;
                                        else return 5;
                                    } else return 4;
                                } else return 3;
                            } else return 2;
                        } else return 1;
                    } else if (dir == -1) {
                        if (!move(0, 0)) {
                            if (!move(-1, 0)) {
                                if (!move(-1, 1)) {
                                    if (!move(0, -2)) {
                                        if (!move(-1, -2)) return -1;
                                        else return 5;
                                    } else return 4;
                                } else return 3;
                            } else return 2;
                        } else return 1;
                    } else return 0;
                }
            }
        }
        else {
            switch (initialRot) {
                case 0 -> {
                    if (dir == 1) {
                        if (!move(0, 0) && !move(-2, 0) && !move(+1, 0) && !move(1, -2) && !move(-2, 1)) return -1;
                    } else if (dir == -1)
                        if (!move(0, 0) && !move(2, 0) && !move(-1, 0) && !move(-1, -2) && !move(2, 1)) return -1;
                }
                case 1 -> {
                    if (dir == 1) {
                        if (!move(0, 0) && !move(-1, 0) && !move(2, 0) && !move(-1, -2) && !move(2, 1)) return -1;
                    } else if (dir == -1)
                        if (!move(0, 0) && !move(2, 0) && !move(-1, 0) && !move(2, -1) && !move(-1, 2)) return -1;
                }
                case 2 -> {
                    if (dir == 1) {
                        if (!move(0, 0) && !move(2, 0) && !move(-1, 0) && !move(2, -1) && !move(-1, 1)) return -1;
                    } else if (dir == -1)
                        if (!move(0, 0) && !move(-2, 0) && !move(1, 0) && !move(-2, -1) && !move(1, 1)) return -1;
                }
                case 3 -> {
                    if (dir == 1) {
                        if (!move(0, 0) && !move(-2, 0) && !move(1, 0) && !move(-2, -1) && !move(1, 2)) return -1;
                    } else if (dir == -1)
                        if (!move(0, 0) && !move(1, 0) && !move(-2, 0) && !move(-1, -2) && !move(2, 1)) return -1;
                }
            }
        }
        if (hasHitObstacle(0, 0)) return -1;
        else return 0;
    }

    public void rotateMatrix(int[][] array, int direction) {
        int size = array.length;
        if (direction == 2) { // 180 rotation
            for (int r = 0; r < 2; r++) {
                for (int i = 0; i < size; i++) {
                    for (int j = i; j < size; j++) {
                        int temp = array[i][j];
                        array[i][j] = array[j][i];
                        array[j][i] = temp;
                    }
                }
                for (int i = 0; i < size; i++) {
                    int low = 0, high = size - 1;
                    while (low < high) {
                        int temp = array[i][low];
                        array[i][low] = array[i][high];
                        array[i][high] = temp;
                        low++;
                        high--;
                    }
                }
            }
            return;
        }
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                int temp = array[i][j];
                array[i][j] = array[j][i];
                array[j][i] = temp;
            }
        }
        if (direction == 1) { //right rotation
            for (int i = 0; i < size; i++) {
                int low = 0, high = size - 1;
                while (low < high) {
                    int temp = array[i][low];
                    array[i][low] = array[i][high];
                    array[i][high] = temp;
                    low++;
                    high--;
                }
            }
        }
        else if (direction == -1) { //left rotation
            for (int i = 0; i < size; i++) {
                int low = 0, high = size - 1;
                while (low < high) {
                    int temp = array[low][i];
                    array[low][i] = array[high][i];
                    array[high][i] = temp;
                    low++;
                    high--;
                }
            }
        }
    }

    public boolean checkSpinIfAny(int initialRot, int dir) {
        int testCase = wallKick(initialRot, dir);
        switch (testCase) {
            case 1 -> {
                if (currentPieceType == 6) {
                    if (tSpinThreeCornersCheck()) {
                        tetrisSounds.playSFX("spin");
                        isSpinClear = true;
                        System.out.println("t-spin!");
                    }
                }
                return true;
            }
            case 2 -> {
                return true;
            }
            case 3 -> {
                if (currentPieceType < 4) {
                    if (tetrominoAreaSurroundedByObstacle()) {
                        tetrisSounds.playSFX("spin");
                        isSpinClear = true;
                        System.out.println("spin!");
                    }
                } else if (currentPieceType == 6) {
                    if (tSpinThreeCornersCheck()) {
                        tetrisSounds.playSFX("spin");
                        isSpinClear = true;
                        System.out.println("t-spin!");
                    }
                }
                return true;
            }
            case 4 -> {
                return true;
            }
            case 5 -> {
                System.out.println("current piece type : " + currentPieceType);
                if (currentPieceType == 6) {
                    if (tSpinThreeCornersCheck()) {
                        tetrisSounds.playSFX("spin");
                        isSpinClear = true;
                        System.out.println("t-spin triple!");
                    }
                }
                return true;
            }
            case 0 -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public void calculateYGapToObstacle() {
        currentYGapToObstacle = 0;
        while (!hasHitObstacle(0, currentYGapToObstacle)) currentYGapToObstacle++;
    }

    public void init_controls() {
        TetrisControls tetrisControls = new TetrisControls(this, tetrisGUI.body);
        tetrisControls.addLoopAction("RIGHT", "moveRight");
        tetrisControls.addLoopAction("LEFT", "moveLeft");
        tetrisControls.addLoopAction("DOWN", "moveDown");
        tetrisControls.addLoopAction("X", "rotateRight");
        tetrisControls.addLoopAction("Z", "rotateLeft");
        tetrisControls.addLoopAction("A", "rotate180");
        tetrisControls.addLoopAction("SPACE", "place");
        tetrisControls.addLoopAction("C", "hold");
        tetrisControls.addLoopAction("ESCAPE", "exit");
        tetrisControls.addLoopAction("S", "sound");
    }

    public void draw_dp_queue() {
        for (int[] ints : dp_queue) {
            Arrays.fill(ints, 0);
        }

        for (int i = 0; i < 5; i++) {
            try {
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < tetrominos[queue.get(i)][row].length; col++) {
                        dp_queue[row + (3 * i)][col] = tetrominos[queue.get(i)][row][col];
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }
        tetrisGUI.tetrisGraphics.repaint();
    }

    public void draw_dp_hold() {
        for (int[] ints : dp_hold) {
            Arrays.fill(ints, 0);
        }

        int gapX = currentHoldPieceType==5 || currentHoldPieceType==0 || currentHoldPieceType==3 ? 1 : 0;
        int gapY = currentHoldPieceType!=4 ? 1 : 0;

        for (int i = 0; i < TetrisContants.TETROMINOS[currentHoldPieceType].length; i++) {
            for (int j = 0; j < TetrisContants.TETROMINOS[currentHoldPieceType][i].length; j++) {
                if (TetrisContants.TETROMINOS[currentHoldPieceType][i][j] != 0) {
                    dp_hold[i+gapY][j+gapX] = TetrisContants.TETROMINOS[currentHoldPieceType][i][j];
                }
            }
        }
        tetrisGUI.tetrisGraphics.repaint();
    }

    public void draw_dp_playfield() {
        calculateYGapToObstacle();
        for (int[] ints : dp_playfield) { //todo rapi2 efficiency set ke 0 tertentu aja
            Arrays.fill(ints, 0);
        }

        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[i].length; j++) {
                if (currentPiece[i][j] != 0) {
                    dp_playfield[currentYPos+i+currentYGapToObstacle-1][currentXPos+j] = 'O';
                    dp_playfield[currentYPos+i][currentXPos+j] = currentPiece[i][j];
                }
            }
        }
        tetrisGUI.tetrisGraphics.repaint();
    }

    public void draw_dp_lockfield() {
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[i].length; j++) {
                if (currentPiece[i][j] != 0) dp_lockfield[currentYPos+i][currentXPos+j] = currentPiece[i][j];
            }
        }
        tetrisGUI.tetrisGraphics.repaint();
    }

    public TetrisApp() {
        tetrisGUI = new TetrisGUI();
        tetrisSpeed = new TetrisSpeed(this);
        tetrisSounds = new TetrisSounds();
        init_controls();

        generateRandomQueue();
        nextPiece();
    }

    public static void main(String[] args) {
        new TetrisApp();
    }

}
