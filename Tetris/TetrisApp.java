package Tetris;

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
            {3,4,5,0,0,0,0,0,0,0},
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

    String[] tetrominoTypes = {"LTetromino", "JTetromino", "ZTetromino", "STetromino", "ITetromino", "Tetromino", "TTetromino"};

    int TEMPTetromino = 2;
    int[][][] tetrominoes = {
            { //0 LTetromino
                    {1,0,0},
                    {1,1,1}
            },
            { //1 JTtetromino
                    {0,0,1},
                    {1,1,1}
            },
            { // DEBUG
                    {2,2,2,2,3},
                    {2,2,2,2,0},
                    {2,2,2,0,0},
                    {2,2,0,0,0},
                    {2,0,0,0,0}
            }
    };

    boolean timerOn = true;

    public void tetrisTimer() throws InterruptedException {
        int y = 0;
        while (timerOn) {
            //Erase tetromino
            for (int row = 0; row < tetrominoes[TEMPTetromino].length; row++) {
                for (int item = 0; item < tetrominoes[TEMPTetromino][row].length; item++) {
                    if (String.valueOf(tetrominoes[TEMPTetromino][row][item]).matches("[1-7]")) {
                        tetrisArea[row+y][item] = 0;
                    }
                }
            }
            //Redraw tetromino + 1
            for (int row = 0; row < tetrominoes[TEMPTetromino].length; row++) {
                for (int item = 0; item < tetrominoes[TEMPTetromino][row].length; item++) {
                    switch (tetrominoes[TEMPTetromino][row][item]) {
                        case 1 -> tetrisArea[row+y+1][item] = 1;
                        case 2 -> tetrisArea[row+y+1][item] = 2;
                        case 3 -> tetrisArea[row+y+1][item] = 3;
                        case 4 -> tetrisArea[row+y+1][item] = 4;
                        case 5 -> tetrisArea[row+y+1][item] = 5;
                        case 6 -> tetrisArea[row+y+1][item] = 6;
                        case 7 -> tetrisArea[row+y+1][item] = 7;
                    }
                }
            }
            y++;
            updateDisplay();
            if (hasHitFloor(y)) break;
            Thread.sleep(1000);
            if (hasHitFloor(y)) break;
        }
    }

    public boolean hasHitFloor(int gap) {
        //Check if hit floor
        for (int row = 0; row < tetrominoes[TEMPTetromino].length; row++) {
            for (int item = 0; item < tetrominoes[TEMPTetromino][row].length; item++) {
                try {
                    if (tetrominoes[TEMPTetromino][row][item] != 0 && tetrominoes[TEMPTetromino][row + 1][item] == 0) {
                        System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino bottom edge because the value below is " + tetrominoes[TEMPTetromino][row + 1][item]);
                        if (tetrisArea[row + 1 + gap][item] != 0) {
                            System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") Has hit floor");
                            System.out.println("The floor is "+"(x:"+(item + 1)+" y:"+(row+2 + 1)+")" + " The floor value is: " + tetrisArea[row + 1][item]);
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") is tetromino bottom edge because the value below is null");
                    if (tetrisArea[row + 1 + gap][item] != 0) {
                        System.out.println("(x:"+(item + 1)+" y:"+(row + 1)+") Has hit floor");
                        System.out.println("The floor is "+"(x:"+(item + 1)+" y:"+(row+2 + 1)+")" + " The floor value is: " + tetrisArea[row + 1 + gap][item]);
                        return true;
                    }
                }
            }
        }
        System.out.println("Has not hit floor");
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

    public TetrisApp() throws InterruptedException {
        tetrisTimer();
    }

    public static void main(String[] args) throws InterruptedException {
        new TetrisApp();
    }

}
