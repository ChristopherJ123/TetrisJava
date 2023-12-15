package Tetris;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TetrisGraphics extends JPanel {

    BufferedImage texturePack;
    BufferedImage tealTetromino;
    BufferedImage blueTetromino;
    BufferedImage orangeTetromino;
    BufferedImage yellowTetromino;
    BufferedImage greenTetromino;
    BufferedImage magentaTetromino;
    BufferedImage redTetromino;
    BufferedImage outlineTetromino;

    int[][] tetrisArea = TetrisContants.TETRIS_AREA;
    int[][] tetrominoArea = TetrisContants.TETROMINO_AREA;
    int[][] tetrominoBoxArea = TetrisContants.TETROMINO_BOX_AREA;

    TetrisGraphics() {
        InputStream istp = getClass().getResourceAsStream("/Assets/Textures/tetromino.png");
        this.setBounds(0, 0, 660, 700);
        this.setOpaque(false); // Supaya ga overlap
        try {
            assert istp != null;
            texturePack = ImageIO.read(istp);
            tealTetromino = texturePack.getSubimage(0,0,30,30);
            blueTetromino = texturePack.getSubimage(30,0,30,30);
            orangeTetromino = texturePack.getSubimage(60,0,30,30);
            yellowTetromino = texturePack.getSubimage(90,0,30,30);
            greenTetromino = texturePack.getSubimage(120,0,30,30);
            magentaTetromino = texturePack.getSubimage(150,0,30,30);
            redTetromino = texturePack.getSubimage(180,0,30,30);
            outlineTetromino = texturePack.getSubimage(210,0,30,30);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        for (int row = 0; row < TetrisContants.TETRIS_AREA.length; row++) {
            for (int columnInRow = 0; columnInRow < TetrisContants.TETRIS_AREA[0].length; columnInRow++) {
                switch (tetrisArea[row][columnInRow]) {
                    case 1 -> g2D.drawImage(orangeTetromino, 30*columnInRow+180,30*row+20, Color.BLACK, null);
                    case 2 -> g2D.drawImage(blueTetromino, 30*columnInRow+180,30*row+20, Color.BLACK, null);
                    case 3 -> g2D.drawImage(greenTetromino, 30*columnInRow+180,30*row+20, Color.BLACK, null);
                    case 4 -> g2D.drawImage(redTetromino, 30*columnInRow+180,30*row+20, Color.BLACK, null);
                    case 5 -> g2D.drawImage(tealTetromino, 30*columnInRow+180,30*row+20, Color.BLACK, null);
                    case 6 -> g2D.drawImage(yellowTetromino, 30*columnInRow+180,30*row+20, Color.BLACK, null);
                    case 7 -> g2D.drawImage(magentaTetromino, 30*columnInRow+180,30*row+20, Color.BLACK, null);
                    case 'O' -> g2D.drawImage(outlineTetromino, 30*columnInRow+180,30*row+20, Color.BLACK, null);
                }
            }
        }

        for (int row = 0; row < TetrisContants.TETROMINO_AREA.length; row++) {
            for (int columnInRow = 0; columnInRow < TetrisContants.TETROMINO_AREA[0].length; columnInRow++) {
                switch (tetrominoArea[row][columnInRow]) {
                    case 1 -> g2D.drawImage(orangeTetromino, 30*columnInRow+520,30*row+50, Color.BLACK, null);
                    case 2 -> g2D.drawImage(blueTetromino, 30*columnInRow+520,30*row+50, Color.BLACK, null);
                    case 3 -> g2D.drawImage(greenTetromino, 30*columnInRow+520,30*row+50, Color.BLACK, null);
                    case 4 -> g2D.drawImage(redTetromino, 30*columnInRow+520,30*row+50, Color.BLACK, null);
                    case 5 -> g2D.drawImage(tealTetromino, 30*columnInRow+520,30*row+50, Color.BLACK, null);
                    case 6 -> g2D.drawImage(yellowTetromino, 30*columnInRow+520,30*row+50, Color.BLACK, null);
                    case 7 -> g2D.drawImage(magentaTetromino, 30*columnInRow+520,30*row+50, Color.BLACK, null);
                }
            }
        }

        for (int row = 0; row < TetrisContants.TETROMINO_BOX_AREA.length; row++) {
            for (int columnInRow = 0; columnInRow < TetrisContants.TETROMINO_BOX_AREA[0].length; columnInRow++) {
                switch (tetrominoBoxArea[row][columnInRow]) {
                    case 1 -> g2D.drawImage(orangeTetromino, 30*columnInRow+20,30*row+50, Color.BLACK, null);
                    case 2 -> g2D.drawImage(blueTetromino, 30*columnInRow+20,30*row+50, Color.BLACK, null);
                    case 3 -> g2D.drawImage(greenTetromino, 30*columnInRow+20,30*row+50, Color.BLACK, null);
                    case 4 -> g2D.drawImage(redTetromino, 30*columnInRow+20,30*row+50, Color.BLACK, null);
                    case 5 -> g2D.drawImage(tealTetromino, 30*columnInRow+20,30*row+50, Color.BLACK, null);
                    case 6 -> g2D.drawImage(yellowTetromino, 30*columnInRow+20,30*row+50, Color.BLACK, null);
                    case 7 -> g2D.drawImage(magentaTetromino, 30*columnInRow+20,30*row+50, Color.BLACK, null);
                }
            }
        }

    }
}
