import java.net.Socket;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import javax.swing.JFrame;

public class Game implements Runnable{


        //private final String IP;
    private Painter painter;
    private UserInteraction userInteraction;
    private Player player;
    private Network network;
    private InitializeBoard initializeBoard;
    //private Network network.port;
        private Scanner scanner = new Scanner(System.in);
        //private JFrame frame;
        /*private int width = 506;
        private int height = 527;*/
        private Thread thread;

        //private Painter painter;
        //private Socket socket;
        /*private DataOutputStream dos;
        private DataInputStream dis;

        private ServerSocket serverSocket;*/

        /*private BufferedImage board;
        private BufferedImage redX;
        private BufferedImage blueX;
        private BufferedImage redCircle;
        private BufferedImage blueCircle;*/

        public String[] spaces = new String[9];


        public boolean unableToCommunicateWithOpponent = false;
        public static boolean won = false;
        public boolean enemyWon = false;
        private boolean draw = false;

        public int lengthOfSpace = 160;
        public int errors = 0;
        private int firstSpot = -1;
        private int secondSpot = -1;

        private Font font = new Font("Verdana", Font.BOLD, 32);
        private Font smallerFont = new Font("Verdana", Font.BOLD, 20);
        private Font largerFont = new Font("Verdana", Font.BOLD, 50);

        private String waitingString = "Waiting for another player";
        private String unableToCommunicateWithOpponentString = "Unable to communicate with opponent.";
        private String wonString = "You are the winner!";
        private String enemyWonString = "Opponent is the winner!";
        private String drawString = "The game ended in a draw.";

        private int[][] wins = new int[][] { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, { 0, 4, 8 }, { 2, 4, 6 } };

        /**
         * <pre>
         * 0, 1, 2
         * 3, 4, 5
         * 6, 7, 8
         * </pre>
         */



        /*public Game(InitializeBoard initializeBoard, Network network, Player player, UserInteraction userInteraction, Painter painter) {
            this.player = player;
            this.initializeBoard = initializeBoard;
            this.network = network;
            this.userInteraction = userInteraction;
            this.painter = painter;
            Player player = new Player();
            System.out.println("Please enter your Playername");
            String name = scanner.nextLine();
            player.setName(name);
            System.out.println("Please input the IP: ");
            IP = scanner.nextLine();
            System.out.println("Please input the port: ");
            port = scanner.nextInt();
            while (port < 1 || port > 65535) {
                System.out.println("The port you entered was invalid, please input another port: ");
                port = scanner.nextInt();
            }

            initializeBoard.loadImages();

            painter = new Painter();
            painter.setPreferredSize(new Dimension(width, height));*/

            //if (!connect()) initializeServer();

/*            frame = new JFrame();
            frame.setTitle("Tic-Tac-Toe");
            frame.setContentPane(painter);
            frame.setSize(width, height);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);

            thread = new Thread(this, "TicTacToe");
            thread.start();
        }*/

    public Game(Network network, Player player, UserInteraction userInteraction) {
        this.player = player;
        this.network = network;
        this.painter = new Painter(network, this);
        this.initializeBoard = new InitializeBoard(506, 527, painter);
        this.userInteraction = userInteraction;
        this.painter = painter;
            /*Player player = new Player();
            System.out.println("Please enter your Playername");
            String name = scanner.nextLine();
            player.setName(name);
            System.out.println("Please input the IP: ");
            IP = scanner.nextLine();
            System.out.println("Please input the port: ");
            port = scanner.nextInt();
            while (port < 1 || port > 65535) {
                System.out.println("The port you entered was invalid, please input another port: ");
                port = scanner.nextInt();
            }*/

        initializeBoard.loadImages();

/*            painter = new Painter();
            painter.setPreferredSize(new Dimension(width, height));*/

        //if (!network.connect()) network.initializeServer();

/*            frame = new JFrame();
            frame.setTitle("Tic-Tac-Toe");
            frame.setContentPane(painter);
            frame.setSize(width, height);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);*/

        thread = new Thread(this, "TicTacToe");
        thread.start();
    }

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
                g.setColor(Color.RED);
                g.setFont(smallerFont);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int stringWidth = g2.getFontMetrics().stringWidth(unableToCommunicateWithOpponentString);
                g.drawString(unableToCommunicateWithOpponentString, initializeBoard.getWidth() / 2 - stringWidth / 2, initializeBoard.getHeight() / 2);
                return;
            }

            if (network.accepted) {
                for (int i = 0; i < spaces.length; i++) {
                    if (spaces[i] != null) {
                        if (spaces[i].equals("X")) {
                            if (network.isCircle()) {
                                g.drawImage(initializeBoard.redX, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                            } else {
                                g.drawImage(initializeBoard.blueX, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                            }
                        } else if (spaces[i].equals("O")) {
                            if (network.isCircle()) {
                                g.drawImage(initializeBoard.blueCircle, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                            } else {
                                g.drawImage(initializeBoard.redCircle, (i % 3) * lengthOfSpace + 10 * (i % 3), (int) (i / 3) * lengthOfSpace + 10 * (int) (i / 3), null);
                            }
                        }
                    }
                }
                if (won || enemyWon) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(10));
                    g.setColor(Color.BLACK);
                    g.drawLine(firstSpot % 3 * lengthOfSpace + 10 * firstSpot % 3 + lengthOfSpace / 2, (int) (firstSpot / 3) * lengthOfSpace + 10 * (int) (firstSpot / 3) + lengthOfSpace / 2, secondSpot % 3 * lengthOfSpace + 10 * secondSpot % 3 + lengthOfSpace / 2, (int) (secondSpot / 3) * lengthOfSpace + 10 * (int) (secondSpot / 3) + lengthOfSpace / 2);

                    g.setColor(Color.RED);
                    g.setFont(largerFont);
                    if (won) {
                        int stringWidth = g2.getFontMetrics().stringWidth(wonString);
                        g.drawString(wonString, initializeBoard.getWidth() / 2 - stringWidth / 2, initializeBoard.getHeight() / 2);
                    } else if (enemyWon) {
                        int stringWidth = g2.getFontMetrics().stringWidth(enemyWonString);
                        g.drawString(enemyWonString, initializeBoard.getWidth() / 2 - stringWidth / 2, initializeBoard.getHeight() / 2);
                    }
                }
                if (draw) {
                    Graphics2D g2 = (Graphics2D) g;
                    g.setColor(Color.BLACK);
                    g.setFont(largerFont);
                    int stringWidth = g2.getFontMetrics().stringWidth(drawString);
                    g.drawString(drawString, initializeBoard.getWidth() / 2 - stringWidth / 2, initializeBoard.getHeight() / 2);
                }
            } else {
                g.setColor(Color.RED);
                g.setFont(font);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int stringWidth = g2.getFontMetrics().stringWidth(waitingString);
                g.drawString(waitingString, initializeBoard.getWidth() / 2 - stringWidth / 2, initializeBoard.getHeight() / 2);
            }

        }

        public void tick() {
            if (errors >= 10) unableToCommunicateWithOpponent = true;

            if (!network.isYourTurn() && !unableToCommunicateWithOpponent) {
                try {
                    int space = network.getDis().readInt();
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

        public void checkForWin() {
            for (int i = 0; i < wins.length; i++) {
                if (network.isCircle()) {
                    if (spaces[wins[i][0]] == "O" && spaces[wins[i][1]] == "O" && spaces[wins[i][2]] == "O") {
                        firstSpot = wins[i][0];
                        secondSpot = wins[i][2];
                        won = true;
                    }
                } else {
                    if (spaces[wins[i][0]] == "X" && spaces[wins[i][1]] == "X" && spaces[wins[i][2]] == "X") {
                        firstSpot = wins[i][0];
                        secondSpot = wins[i][2];
                        won = true;
                    }
                }
            }
        }

        public void checkForEnemyWin() {
            for (int i = 0; i < wins.length; i++) {
                if (network.isCircle()) {
                    if (spaces[wins[i][0]] == "X" && spaces[wins[i][1]] == "X" && spaces[wins[i][2]] == "X") {
                        firstSpot = wins[i][0];
                        secondSpot = wins[i][2];
                        enemyWon = true;
                    }
                } else {
                    if (spaces[wins[i][0]] == "O" && spaces[wins[i][1]] == "O" && spaces[wins[i][2]] == "O") {
                        firstSpot = wins[i][0];
                        secondSpot = wins[i][2];
                        enemyWon = true;
                    }
                }
            }
        }

        public void checkForDraw() {
            for (int i = 0; i < spaces.length; i++) {
                if (spaces[i] == null) {
                    return;
                }
            }
            draw = true;
        }

        /*private void listenForServerRequest() {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                accepted = true;
                System.out.println("CLIENT HAS REQUESTED TO JOIN, AND WE HAVE ACCEPTED");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean connect() {
            try {
                socket = new Socket(IP, port);
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                accepted = true;
            } catch (IOException e) {
                System.out.println("Unable to connect to the address: " + IP + ":" + port + " | Starting a server");
                return false;
            }
            System.out.println("Successfully connected to the server.");
            return true;
        }

        private void initializeServer() {
            try {
                serverSocket = new ServerSocket(port, 8, InetAddress.getByName(IP));
            } catch (Exception e) {
                e.printStackTrace();
            }
            yourTurn = true;
            circle = false;
        }*/

        /*private void loadImages() {
            try {
                board = ImageIO.read(getClass().getResourceAsStream("/board.png"));
                redX = ImageIO.read(getClass().getResourceAsStream("/redX.png"));
                redCircle = ImageIO.read(getClass().getResourceAsStream("/redCircle.png"));
                blueX = ImageIO.read(getClass().getResourceAsStream("/blueX.png"));
                blueCircle = ImageIO.read(getClass().getResourceAsStream("/blueCircle.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        //@SuppressWarnings("unused")
        //public static void main(String[] args) {
            //Game ticTacToe = new Game();
        //}

        /*private class Painter extends JPanel implements MouseListener {
            private static final long serialVersionUID = 1L;

            public Painter() {
                setFocusable(true);
                requestFocus();
                setBackground(Color.WHITE);
                addMouseListener(this);
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                render(g);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (accepted) {
                    if (yourTurn && !unableToCommunicateWithOpponent && !won && !enemyWon) {
                        int x = e.getX() / lengthOfSpace;
                        int y = e.getY() / lengthOfSpace;
                        y *= 3;
                        int position = x + y;

                        if (spaces[position] == null) {
                            if (!circle) spaces[position] = "X";
                            else spaces[position] = "O";
                            yourTurn = false;
                            repaint();
                            Toolkit.getDefaultToolkit().sync();

                            try {
                                dos.writeInt(position);
                                dos.flush();
                            } catch (IOException e1) {
                                errors++;
                                e1.printStackTrace();
                            }

                            System.out.println("DATA WAS SENT");
                            checkForWin();
                            checkForTie();

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
*/

}
