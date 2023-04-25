import javax.swing.*;
import java.awt.*;

public class TetrisGUI extends JFrame {

    JPanel containerPanel;
    char[][] tetrisArea = {
            {0,0,0,0,0,0,0,3,0,0}, //tetrisArea[row][column]
            {0,0,0,0,0,2,0,3,0,0},
            {0,1,0,1,0,0,0,3,0,0},
            {0,1,1,1,0,2,0,0,0,0},
            {0,1,0,1,0,2,0,3,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {4,4,4,5,5,5,6,6,7,7},
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
    };

    String[] tetrominoTypes = {"LTetromino", "JTetromino", "ZTetromino", "STetromino", "ITetromino", "Tetromino", "TTetromino"};
    String[] colors = {""};
    Color ORANGE = new Color(255, 165, 0);
    Color BLUE = new Color(0, 0, 255);
    Color GREEN = new Color(0, 165, 0);
    Color RED = new Color(255, 0, 0);
    Color TEAL = new Color(0, 255, 255);
    Color YELLOW = new Color(255, 255, 0);
    Color PURPLE = new Color(255, 0, 165);



    int[][] LTetromino = {
            {1,0,0},
            {1,1,1}
    };

    JPanel tetrisBody;
    JPanel tetrisContainer;
    JLabel[][] tetrisBox;

    TetrisGUI() {
        tetrisBox = new JLabel[22][10];

        tetrisContainer = new JPanel();
//        tetrisContainer.setPreferredSize(new Dimension(300,660));
        tetrisContainer.setBounds(20,20,300,660);
        tetrisContainer.setBackground(Color.WHITE);
        tetrisContainer.setLayout(new GridLayout(22, 10));
        // testing stuffs //
        for (int row = 0; row < 22; row++) {
            for (int columnInRow = 0; columnInRow < 10; columnInRow++) {
                tetrisBox[row][columnInRow] = new JLabel();
                tetrisBox[row][columnInRow].setBackground(Color.GRAY);
                tetrisBox[row][columnInRow].setBorder(BorderFactory.createLineBorder(Color.black, 1));
                tetrisBox[row][columnInRow].setOpaque(false);
                tetrisContainer.add(tetrisBox[row][columnInRow]);
            }
        }
        for (int row = 0; row < 22; row++) {
            for (int columnInRow = 0; columnInRow < 10; columnInRow++) {
                switch (tetrisArea[row][columnInRow]) {
                    case 1 -> {
                        tetrisBox[row][columnInRow].setBackground(ORANGE);
                        tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 2 -> {
                        tetrisBox[row][columnInRow].setBackground(BLUE);
                        tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 3 -> {
                        tetrisBox[row][columnInRow].setBackground(GREEN);
                        tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 4 -> {
                        tetrisBox[row][columnInRow].setBackground(RED);
                        tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 5 -> {
                        tetrisBox[row][columnInRow].setBackground(TEAL);
                        tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 6 -> {
                        tetrisBox[row][columnInRow].setBackground(YELLOW);
                        tetrisBox[row][columnInRow].setOpaque(true);
                    }
                    case 7 -> {
                        tetrisBox[row][columnInRow].setBackground(PURPLE);
                        tetrisBox[row][columnInRow].setOpaque(true);
                    }
                }
            }
        }
        //Testing Stop//


        tetrisBody = new JPanel();
        tetrisBody.setPreferredSize(new Dimension(340,0));
        tetrisBody.setBackground(new Color(0, 127, 0));
        tetrisBody.setLayout(null);
        tetrisBody.add(tetrisContainer);

        this.setTitle("setTitle goes here");
        this.setSize(600, 740);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.WHITE);

        this.add(tetrisBody, BorderLayout.WEST);
    }

    public static void main(String[] args) {
        new TetrisGUI();
    }
}
