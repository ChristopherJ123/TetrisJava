package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;

public class TetrisGUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    Action rightAction;
    Action leftAction;
    Action downAction;
    Action spaceAction;
    Action rotateClockwiseAction;
    Action rotateAntiClockwiseAction;
    Action holdAction;
    Action exitAction;

    Font customFont;
    Font customFontSmall;

    JButton startButton;

    JLabel staticLabel;
    JLabel scoreLabel;

    JLayeredPane body;

    JPanel tetrominoHoldBody;
    JPanel tetrisBody;
    JPanel menuBody;
    JPanel tetrominoHoldContainer;
    JPanel tetrisContainer;
    JPanel tetrisGraphics;
    JPanel tetrominoContainer;
    JLabel[][] tetrominoHoldBox;
    JLabel[][] tetrisBox;
    JLabel[][] tetrominoBox;

    TetrisGUI() {
        InputStream is = getClass().getResourceAsStream("/Assets/Font/HunDIN1451.ttf");
        try { //src StackOverflow
            //create the font to use. Specify the size!
            assert is != null;
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
            customFontSmall = customFont.deriveFont(13f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        scoreLabel = new JLabel("score: 0");
        scoreLabel.setBounds(20, 180, 150, 40);
        scoreLabel.setFont(customFont);
        scoreLabel.setForeground(Color.WHITE);

        startButton = new JButton("Clear Memory");
        startButton.addActionListener(this);
        startButton.setFocusable(false);
        startButton.setBounds(0,320,130,20);

        tetrominoHoldBox = new JLabel[4][4];
        tetrisBox = new JLabel[22][10];
        tetrominoBox = new JLabel[15][4];

        tetrominoHoldContainer = new JPanel();
        tetrominoHoldContainer.setBounds(20,50,120,120);
        tetrominoHoldContainer.setBackground(TetrisContants.BLACK);

        tetrisContainer = new JPanel();
        tetrisContainer.setBounds(20, 20, 300, 600);
        tetrisContainer.setBackground(TetrisContants.BLACK);

        tetrominoContainer = new JPanel();
        tetrominoContainer.setBounds(20, 50, 120, 450);
        tetrominoContainer.setBackground(TetrisContants.BLACK);

        tetrominoHoldBody = new JPanel();
        tetrominoHoldBody.setBounds(0,70,160,337);
        tetrominoHoldBody.setOpaque(true);
        tetrominoHoldBody.setBackground(TetrisContants.GUI_MENUBG);
        tetrominoHoldBody.setLayout(null);
        staticLabel = new JLabel("hold tetromino");
        staticLabel.setFont(customFont);
        staticLabel.setBounds(20,20,160,20);
        staticLabel.setForeground(Color.WHITE);
        tetrominoHoldBody.add(tetrominoHoldContainer);
        tetrominoHoldBody.add(staticLabel);
        tetrominoHoldBody.add(scoreLabel);
        staticLabel = new JLabel("controls:");
        staticLabel.setFont(customFont);
        staticLabel.setBounds(20,220,160,20);
        staticLabel.setForeground(Color.WHITE);
        tetrominoHoldBody.add(staticLabel);
        staticLabel = new JLabel("movements = arrow keys");
        staticLabel.setFont(customFontSmall);
        staticLabel.setBounds(20,240,160,20);
        staticLabel.setForeground(Color.WHITE);
        tetrominoHoldBody.add(staticLabel);
        staticLabel = new JLabel("space");
        staticLabel.setFont(customFontSmall);
        staticLabel.setBounds(20,253,160,20);
        staticLabel.setForeground(Color.WHITE);
        tetrominoHoldBody.add(staticLabel);
        staticLabel = new JLabel("rotations = X,Y");
        staticLabel.setFont(customFontSmall);
        staticLabel.setBounds(20,271,160,20);
        staticLabel.setForeground(Color.WHITE);
        tetrominoHoldBody.add(staticLabel);
        staticLabel = new JLabel("hold = C");
        staticLabel.setFont(customFontSmall);
        staticLabel.setBounds(20,289,160,20);
        staticLabel.setForeground(Color.WHITE);
        tetrominoHoldBody.add(staticLabel);
        staticLabel = new JLabel("exit = ESCAPE");
        staticLabel.setFont(customFontSmall);
        staticLabel.setBounds(20,307,160,20);
        staticLabel.setForeground(Color.WHITE);
        tetrominoHoldBody.add(staticLabel);

        tetrisBody = new JPanel();
        tetrisBody.setBounds(160,70,340,640);
        tetrisBody.setBackground(TetrisContants.GUI_TETRISBG);
        tetrisBody.setLayout(null);
        tetrisBody.add(tetrisContainer);

        menuBody = new JPanel();
        menuBody.setBounds(500,70,160,520);
        menuBody.setBackground(TetrisContants.GUI_MENUBG);
        menuBody.setLayout(null);
        staticLabel = new JLabel("next tetromino");
        staticLabel.setFont(customFont);
        staticLabel.setBounds(20,20,160,20);
        staticLabel.setForeground(Color.WHITE);
        menuBody.add(tetrominoContainer);
        menuBody.add(staticLabel);

        tetrisGraphics = new TetrisGraphics();

        body = new JLayeredPane();
        body.setLayout(null);
        body.add(tetrisBody, Integer.valueOf(0));
        body.add(menuBody, Integer.valueOf(0));
        body.add(tetrominoHoldBody, Integer.valueOf(0));
        body.add(tetrisGraphics, Integer.valueOf(1));
        body.addMouseListener(this);
        body.addMouseMotionListener(this);
        body.setFocusable(true);
        body.setBackground(new Color(0,0,0,0.5F));
        body.setOpaque(false); // false

        this.add(body);

        this.setUndecorated(true);
        this.setBackground(new Color(0,0,0,0));
        this.setSize(660, 710);
        this.setVisible(true);
        this.setTitle("setTitle goes here");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setLayout(null);
    }

    public static void main(String[] args) {
        new TetrisGUI();
    }

    Point pressedPoint;
    Rectangle frameBounds;

    private void moveJFrame(MouseEvent event) {
        Point endPoint = event.getPoint();

        int xDiff = endPoint.x - pressedPoint.x;
        int yDiff = endPoint.y - pressedPoint.y;
        frameBounds.x += xDiff;
        frameBounds.y += yDiff;
        this.setBounds(frameBounds);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            System.out.println("cleared");
            System.gc();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.frameBounds = this.getBounds();
        this.pressedPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        moveJFrame(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        moveJFrame(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
