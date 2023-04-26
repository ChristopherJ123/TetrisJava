package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TetrisGUI extends JFrame implements ActionListener {

    JPanel containerPanel;

    String[] tetrominoTypes = {"LTetromino", "JTetromino", "ZTetromino", "STetromino", "ITetromino", "Tetromino", "TTetromino"};
    String[] colors = {""};

    JButton startButton;

    JPanel tetrisBody;
    JPanel menuBody;
    JPanel tetrisContainer;
    JLabel[][] tetrisBox;

    TetrisGUI() {
        startButton = new JButton("Start");
        startButton.addActionListener(this);

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
                tetrisBox[row][columnInRow].setBorder(BorderFactory.createLineBorder(Color.black, 1));
                tetrisBox[row][columnInRow].setOpaque(true);
                tetrisContainer.add(tetrisBox[row][columnInRow]);
            }
        }
        //Testing Stop//


        tetrisBody = new JPanel();
        tetrisBody.setPreferredSize(new Dimension(340,0));
        tetrisBody.setBackground(new Color(0, 127, 0));
        tetrisBody.setLayout(null);
        tetrisBody.add(tetrisContainer);

        menuBody = new JPanel();
        menuBody.setPreferredSize(new Dimension(240,0));
        menuBody.setBackground(Color.gray);
        menuBody.setLayout(new FlowLayout());
        menuBody.add(startButton);


        this.setTitle("setTitle goes here");
        this.setSize(600, 740);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.BLACK);

        this.add(tetrisBody, BorderLayout.WEST);
        this.add(menuBody, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        new TetrisGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
