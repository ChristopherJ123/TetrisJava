package Tetris;

import java.awt.*;

public class TetrisContants {
    public static final int[][][] TETROMINOS = { //can add custom
            { //0 JTetromino
                    {1,0,0},
                    {1,1,1},
                    {0,0,0}
            },
            { //1 LTtetromino
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
    // TODO fix namings for example ghost piece instead of "outline"
    public static final int[][] TETRIS_AREA = new int[40][10];

    public static final int[][] VISUAL_AREA = new int[40][10];

    public static final int[][] TETROMINO_AREA = new int[15][4];

    public static final int[][] TETROMINO_BOX_AREA = new int[4][4];

    public static final int TIMER_IN_MILLISECONDS = 1000;
    public static final int DAS = 117; //117
    public static final int ARR = 1;
    public static final int SDF = 1;

    //Tetrominos and outline colors
    public static final Color ORANGE = new Color(255, 165, 0);
    public static final Color BLUE = new Color(0, 0, 255);
    public static final Color GREEN = new Color(0, 165, 0);
    public static final Color RED = new Color(255, 0, 0);
    public static final Color TEAL = new Color(0, 255, 255);
    public static final Color YELLOW = new Color(255, 255, 0);
    public static final Color PURPLE = new Color(255, 0, 165);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color GRAY_OUTLINE = new Color(130, 130, 140);

    //GUI Colors
//    public static final Color GUI_TETRISBG = new Color(44, 62, 80);
    public static final Color GUI_TETRISBG = new Color(0.20F, 0.28F, 0.36F, 0.75F);
    public static final Color GUI_MENUBG = new Color(52, 73, 94);
    
}
