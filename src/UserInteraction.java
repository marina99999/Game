import java.util.Scanner;

public class UserInteraction {

    private String IP;
    private String name;
    private String port;

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
}
