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

    /**
     * UserInput liest die Nutzereingabe
     * @return ein Array, welches die drei Eingaben beinhaltet
     */
    public String[] UserInput(){
        int portInteger = 0;
        System.out.println("Please enter your Playername");
        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();
        System.out.println("Please input the IP: ");
        try {
            String IP = scanner.nextLine();
        }
        catch (Exception e) {
            e.getMessage();
        }

        System.out.println("Please input the port: ");
        portInteger = scanner.nextInt();

        while (portInteger < 1 || portInteger > 65535) {
            System.out.println("The port you entered was invalid, please input another port: ");
            portInteger = scanner.nextInt();
        }
        port = String.valueOf(portInteger);
        return new String[]{name, IP, port};
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
