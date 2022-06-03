import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class Painter extends JPanel implements MouseListener {
    private Network network;
    private Painter painter;
    private Game game;
    private static final long serialVersionUID = 1L;

    public Painter(Network network, Game game) {
        this.game = game;
        this.network = network;
        setFocusable(true);
        requestFocus();
        setBackground(Color.WHITE);
        //setPreferredSize(new Dimension(506, 527));
        addMouseListener(this);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (network.accepted) {
            if (network.isYourTurn() && !game.unableToCommunicateWithOpponent && !game.won && !game.enemyWon) {
                int x = e.getX() / game.getCalculateBoard().getLengthOfSpace();
                int y = e.getY() / game.getCalculateBoard().getLengthOfSpace();
                y *= 3;
                int position = x + y;

                if (game.spaces[position] == null) {
                    if (!network.isCircle()) game.spaces[position] = "X";
                    else game.spaces[position] = "O";
                    network.setYourTurn(false);
                    repaint();
                    Toolkit.getDefaultToolkit().sync();

                    try {
                        network.getDataOutputStream().writeInt(position);
                        network.getDataOutputStream().flush();
                    } catch (IOException e1) {
                        game.errors++;
                        e1.printStackTrace();
                    }

                    System.out.println("DATA WAS SENT");
                    game.checkForWin();
                    game.checkForDraw();

                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
