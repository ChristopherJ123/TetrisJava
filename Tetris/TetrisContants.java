package Tetris;

import java.awt.*;

public class TetrisContants {
    public static final int[][][] TETROMINOES = {
            { //0 LTetromino
                    {1,0,0},
                    {1,1,1},
                    {0,0,0}
            },
            { //1 JTtetromino
                    {0,0,2},
                    {2,2,2},
                    {0,0,0}
            },
            { //2 ZTetromino
                    {0,3,3},
                    {3,3,0},
                    {0,0,0}
            },
            { //3 STetromino
                    {4,4,0},
                    {0,4,4},
                    {0,0,0}
            },
            { //4 ITetromino
                    {0,0,0,0},
                    {5,5,5,5},
                    {0,0,0,0},
                    {0,0,0,0}
            },
            { //5 OTtetromino
                    {6,6,},
                    {6,6,}
            },
            { //6 TTtetromino
                    {0,7,0},
                    {7,7,7},
                    {0,0,0}
            }
    };

    public static final Color ORANGE = new Color(255, 165, 0);
    public static final Color BLUE = new Color(0, 0, 255);
    public static final Color GREEN = new Color(0, 165, 0);
    public static final Color RED = new Color(255, 0, 0);
    public static final Color TEAL = new Color(0, 255, 255);
    public static final Color YELLOW = new Color(255, 255, 0);
    public static final Color PURPLE = new Color(255, 0, 165);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color GRAY_OUTLINE = new Color(70, 80, 90);
}
