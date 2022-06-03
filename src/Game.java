import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;

public class Game implements Runnable {

    private Painter painter;
    private UserInteraction userInteraction;
    private Player player;
    private Network network;
    private InitializeBoard initializeBoard;
    private CalculateBoard calculateBoard = new CalculateBoard();
    //private Scanner scanner = new Scanner(System.in);

    private Thread thread;

    public String[] spaces = new String[9];


    public boolean unableToCommunicateWithOpponent = false;
    public static boolean won = false;
    public boolean enemyWon = false;
    private boolean draw = false;

    //public int lengthOfSpace = 160;
    public int errors = 0;


    private Font font = new Font("Verdana", Font.BOLD, 32);
    private Font smallerFont = new Font("Verdana", Font.BOLD, 20);
    private Font largerFont = new Font("Verdana", Font.BOLD, 50);

    /** Spielfeld nach Zahlen sortiert
     *0, 1, 2
     *3, 4, 5
     *6, 7, 8
     */
    private int[][] winsSize3 = new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};


    public Game(Network network, Player player, UserInteraction userInteraction) {
        this.player = player;
        this.network = network;
        this.painter = new Painter(network, this);
        this.initializeBoard = new InitializeBoard(506, 527, painter, player);
        this.userInteraction = userInteraction;
        //this.painter = painter;

        initializeBoard.loadImages();

        thread = new Thread(this, "TicTacToe");
        thread.start();
    }

    /**
     * solange es keine Verbindung gibt, wird die Methode listenForServerRequest ausgeführt
     */
    public void run() {
        while (true) {
            tick();
            painter.repaint();

            if (!network.isCircle() && !network.accepted) {
                network.listenForServerRequest();
            }

        }
    }

    public void render(Graphics g) {
        g.drawImage(initializeBoard.board, 0, 0, null);
        if (unableToCommunicateWithOpponent) {
            g.setColor(Color.ORANGE);
            g.setFont(smallerFont);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int stringWidth = g2.getFontMetrics().stringWidth(userInteraction.getUnableToCommunicateWithOpponentString());
            g.drawString(userInteraction.getUnableToCommunicateWithOpponentString(), initializeBoard.getWidth() / 2 - stringWidth / 2, initializeBoard.getHeight() / 2);
            return;
        }

        if (network.accepted) {
            for (int i = 0; i < spaces.length; i++) {
                if (spaces[i] != null) {
                    if (spaces[i].equals("X")) {
                        if (network.isCircle()) {
                            g.drawImage(initializeBoard.redX, calculateBoard.calculateXCoordinate(i), calculateBoard.calculateYCoordinate(i), null);
                        } else {
                            g.drawImage(initializeBoard.blueX, calculateBoard.calculateXCoordinate(i), calculateBoard.calculateYCoordinate(i), null);
                        }
                    } else if (spaces[i].equals("O")) {
                        if (network.isCircle()) {
                            g.drawImage(initializeBoard.blueCircle, calculateBoard.calculateXCoordinate(i), calculateBoard.calculateYCoordinate(i), null);
                        } else {
                            g.drawImage(initializeBoard.redCircle, calculateBoard.calculateXCoordinate(i), calculateBoard.calculateYCoordinate(i), null);
                        }
                    }
                }
            }
            if (won || enemyWon) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(10));
                g.setColor(Color.BLACK);
                g.drawLine(calculateBoard.calculateXOneCoordinate(), calculateBoard.calculateYOneCoordinate(), calculateBoard.calculateXTwoCoordinate(), calculateBoard.calculateYTwoCoordinate());

                g.setColor(Color.GREEN);
                g.setFont(largerFont);
                if (won) {
                    int stringWidth = g2.getFontMetrics().stringWidth(userInteraction.getWonString());
                    g.drawString(userInteraction.getWonString(), initializeBoard.getWidth() / 2 - stringWidth / 2, initializeBoard.getHeight() / 2);
                } else if (enemyWon) {
                    int stringWidth = g2.getFontMetrics().stringWidth(userInteraction.getEnemyWonString());
                    g.drawString(userInteraction.getEnemyWonString(), initializeBoard.getWidth() / 2 - stringWidth / 2, initializeBoard.getHeight() / 2);
                }
            }
            if (draw) {
                Graphics2D g2 = (Graphics2D) g;
                g.setColor(Color.BLACK);
                g.setFont(largerFont);
                int stringWidth = g2.getFontMetrics().stringWidth(userInteraction.getDrawString());
                g.drawString(userInteraction.getEnemyWonString(), initializeBoard.getWidth() / 2 - stringWidth / 2, initializeBoard.getHeight() / 2);
            }
        } else {
            g.setColor(Color.ORANGE);
            g.setFont(font);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int stringWidthPlayer = g2.getFontMetrics().stringWidth(initializeBoard.getName());
            g.drawString(initializeBoard.getPlayer().getName(), initializeBoard.getWidth() / 2 - stringWidthPlayer / 2, initializeBoard.getHeight()/4);

            int stringWidth = g2.getFontMetrics().stringWidth(userInteraction.getWaitingString());
            g.drawString(userInteraction.getWaitingString(), initializeBoard.getWidth() / 2 - stringWidth / 2, initializeBoard.getHeight() / 2);
        }

    }

    public void tick() {
        if (errors >= 10) unableToCommunicateWithOpponent = true;

        if (!network.isYourTurn() && !unableToCommunicateWithOpponent) {
            try {
                int space = network.getDataInputStream().readInt();
                if (network.isCircle()) spaces[space] = "X";
                else spaces[space] = "O";
                checkForEnemyWin();
                checkForDraw();
                network.setYourTurn(true);
            } catch (IOException e) {
                e.printStackTrace();
                errors++;
            }
        }
    }

    /**
     * Überprüfung ob ein SPieler gewonnen hat
     */
    public void checkForWin() {
        for (int[] ints : winsSize3) {
            if (network.isCircle()) {
                if (spaces[ints[0]] =="O" && spaces[ints[1]] =="O" && spaces[ints[2]]=="O") {
                    calculateBoard.setFirstSpot(ints[0]);
                    calculateBoard.setSecondSpot(ints[2]);
                    won = true;
                }
            } else {
                if (spaces[ints[0]] =="X" && spaces[ints[1]] =="X" && spaces[ints[2]] == "X") {
                    calculateBoard.setFirstSpot(ints[0]);
                    calculateBoard.setSecondSpot(ints[2]);
                    won = true;
                }
            }
        }
    }

    public void checkForEnemyWin() {
        for (int i = 0; i < winsSize3.length; i++) {
            if (network.isCircle()) {
                if (spaces[winsSize3[i][0]] == "X" && spaces[winsSize3[i][1]] =="X" && spaces[winsSize3[i][2]] == "X") {
                    calculateBoard.setFirstSpot(winsSize3[i][0]);
                    calculateBoard.setSecondSpot(winsSize3[i][2]);
                    enemyWon = true;
                }
            } else {
                if (spaces[winsSize3[i][0]] =="O" && spaces[winsSize3[i][1]]== "O" && spaces[winsSize3[i][2]] =="O") {
                    calculateBoard.setFirstSpot(winsSize3[i][0]);
                    calculateBoard.setSecondSpot(winsSize3[i][2]);
                    enemyWon = true;
                }
            }
        }
    }

    public void checkForDraw() {
        for (String space : spaces) {
            if (space == null) {
                return;
            }
        }
        draw = true;
    }

    public CalculateBoard getCalculateBoard() {
        return calculateBoard;
    }
}
