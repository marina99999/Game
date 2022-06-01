import java.util.Scanner;

public class UserInteraction {

    private String IP;
    private String name;
    private String port;
    private String waitingString = "Waiting for another player";
    private String unableToCommunicateWithOpponentString = "Unable to communicate with the enemy.";
    private String wonString = "You are the winner!";
    private String enemyWonString = "Enemy is the winner!";
    private String drawString = "The game ended in a draw.";

    public UserInteraction(){
    }

    public UserInteraction(String name){
        this.name = name;
    }

    public String[] UserInput(){
        System.out.println("Please enter your Playername");
        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();
        System.out.println("Please input the IP: ");
        String IP = scanner.nextLine();
        System.out.println("Please input the port: ");
        port = scanner.nextLine();
        int portInteger = Integer. valueOf(port);
        while (portInteger < 1 || portInteger > 65535) {
            System.out.println("The port you entered was invalid, please input another port: ");
            portInteger = scanner.nextInt();
        }
        port = String.valueOf(portInteger);
        return new String[]{name, IP, port};
    }

    public String getIP() {
        return IP;
    }

    public String getWaitingString() {
        return waitingString;
    }

    public String getUnableToCommunicateWithOpponentString() {
        return unableToCommunicateWithOpponentString;
    }

    public String getWonString() {
        return wonString;
    }

    public String getEnemyWonString() {
        return enemyWonString;
    }

    public String getDrawString() {
        return drawString;
    }
}
