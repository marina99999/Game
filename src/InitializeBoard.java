import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class InitializeBoard extends JFrame {

    private Painter painter;
    private  int width;
    private int height;
    public BufferedImage board;
    public BufferedImage redX;
    public BufferedImage blueX;
    public BufferedImage redCircle;
    public BufferedImage blueCircle;
    private Player player;

    /**
     * j
     * @param width Spielfeldbreite
     * @param height Spielfeldhöhe
     * @param painter
     * @param player Name des Spielers
     */
    public InitializeBoard(int width, int height, Painter painter, Player player){
        this.width = width;
        this.height = height;
        this.player = player;
        this.painter = painter;
        //painter = new Game.Painter();
        painter.setPreferredSize(new Dimension(width, height));
        setTitle("TicTacToe " + player.getName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(painter);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /**
     * die Bilder für das Spiel werden den Variablen zugewiesen
     */
    public void loadImages() {
        try {
            board = ImageIO.read(getClass().getResourceAsStream("/board.png"));
            redX = ImageIO.read(getClass().getResourceAsStream("/redX.png"));
            redCircle = ImageIO.read(getClass().getResourceAsStream("/redCircle.png"));
            blueX = ImageIO.read(getClass().getResourceAsStream("/blueX.png"));
            blueCircle = ImageIO.read(getClass().getResourceAsStream("/blueCircle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Player getPlayer() {
        return player;
    }
}
