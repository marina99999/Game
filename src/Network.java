import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Network {
    public boolean accepted = false;
    private String port;
    private String IP;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private boolean yourTurn = false;
    public boolean circle = true;
    private ServerSocket serverSocket;

    public Network(String IP, String port) {
        this.port = port;
        this.IP = IP;
        if (!connect()) initializeServer();
    }

    public void listenForServerRequest() {
        Socket socket = null;
        try {
            socket = serverSocket.accept();
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
            accepted = true;
            System.out.println("CLIENT HAS REQUESTED TO JOIN, AND WE HAVE ACCEPTED");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean connect() {

        try {
            int portInteger = Integer.parseInt(port);
            this.socket = new Socket(IP, portInteger);
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
            this.accepted = true;
        } catch (IOException e) {
            System.out.println("Unable to connect to the address: " + IP + ":" + port + " | Starting a server");
            return false;
        }
        System.out.println("Successfully connected to the server.");
        return true;
    }

    private void initializeServer() {
        try {
            this.serverSocket = new ServerSocket(Integer.parseInt(port), 8, InetAddress.getByName(IP));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.yourTurn = true;
        this.circle = false;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public boolean isCircle() {
        return circle;
    }

    public void setCircle(boolean circle) {
        this.circle = circle;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public DataInputStream getDis() {
        return dis;
    }
}
