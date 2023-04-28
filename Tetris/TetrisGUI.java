package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TetrisGUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    Action rightAction;
    Action leftAction;
    Action downAction;
    Action spaceAction;
    Action rotateClockwiseAction;
    Action rotateAntiClockwiseAction;
    Action exitAction;

    JButton startButton;

    JLabel scoreLabel;

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
        tetrisBody.setBackground(TetrisContants.GUI_TETRISBG);
        tetrisBody.setLayout(null);
        tetrisBody.setFocusable(false);
        tetrisBody.add(tetrisContainer);

        menuBody = new JPanel();
        menuBody.setPreferredSize(new Dimension(240,0));
        menuBody.setBackground(TetrisContants.GUI_MENUBG);
        menuBody.setLayout(new FlowLayout());
        menuBody.add(scoreLabel);
        menuBody.setFocusable(false);

        body = new JPanel();
        body.setLayout(new BorderLayout());
        body.add(tetrisBody, BorderLayout.WEST);
        body.add(menuBody, BorderLayout.EAST);
        body.addMouseListener(this);
        body.addMouseMotionListener(this);
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
