package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class TetrisGUI extends JFrame implements ActionListener {

    Action rightAction;
    Action leftAction;
    Action downAction;
    Action spaceAction;
    Action rotateClockwiseAction;
    Action rotateAntiClockwiseAction;
    Action exitAction;

    JPanel containerPanel;

    String[] tetrominoTypes = {"LTetromino", "JTetromino", "ZTetromino", "STetromino", "ITetromino", "Tetromino", "TTetromino"};
    String[] colors = {""};

    JButton startButton;

    JLabel scoreLabel;

    JLabel endLabel;

    JPanel body;
    JPanel tetrisBody;
    JPanel menuBody;
    JPanel tetrisContainer;
    JLabel[][] tetrisBox;

    TetrisGUI() {
        scoreLabel = new JLabel("Your score is: 0");

        startButton = new JButton("0");
        startButton.addActionListener(this);
        startButton.setFocusable(false);

        tetrisBox = new JLabel[22][10];

        tetrisContainer = new JPanel();
//        tetrisContainer.setPreferredSize(new Dimension(300,660));
        tetrisContainer.setBounds(20,20,300,660);
        tetrisContainer.setBackground(Color.WHITE);
        tetrisContainer.setLayout(new GridLayout(22, 10));
        for (int row = 0; row < 22; row++) {
            for (int columnInRow = 0; columnInRow < 10; columnInRow++) {
                tetrisBox[row][columnInRow] = new JLabel();
                tetrisBox[row][columnInRow].setOpaque(true);
                tetrisContainer.add(tetrisBox[row][columnInRow]);
            }
        }

        tetrisBody = new JPanel();
        tetrisBody.setPreferredSize(new Dimension(340,0));
        tetrisBody.setBackground(new Color(44, 62, 80));
        tetrisBody.setLayout(null);
        tetrisBody.setFocusable(false);
        tetrisBody.add(tetrisContainer);

        menuBody = new JPanel();
        menuBody.setPreferredSize(new Dimension(240,0));
        menuBody.setBackground(new Color(52, 73, 94));
        menuBody.setLayout(new FlowLayout());
        menuBody.add(scoreLabel);
        menuBody.setFocusable(false);

        body = new JPanel();
        body.setLayout(new BorderLayout());
        body.add(tetrisBody, BorderLayout.WEST);
        body.add(menuBody, BorderLayout.EAST);
        body.setFocusable(true);

        body.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "rightActionKey");
        body.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "leftActionKey");
        body.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "downActionKey");
        body.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "spaceActionKey");
        body.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('x'), "rotateClockwiseActionKey");
        body.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('z'), "rotateAntiClockwiseActionKey");
        body.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "exitActionKey");

        this.add(body);

        this.setUndecorated(true);
        this.setSize(580, 700);
        this.setVisible(true);
        this.setTitle("setTitle goes here");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.BLACK);
    }

    public static void main(String[] args) {
        new TetrisGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
