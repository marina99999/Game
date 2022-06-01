public class Start {

    public static void main(String[] args) {
        UserInteraction userInteraction = new UserInteraction();
        String[] userInput = userInteraction.UserInput();
        userInteraction = new UserInteraction(userInput[0]);
        Player player = new Player(userInput[0]);
        //Painter painter = new Painter();
        //InitializeBoard initialize = new InitializeBoard(506, 527, painter);
        Network network = new Network(userInput[1], userInput[2]);
        Game ticTacToe = new Game(network, player, userInteraction);

    }

}
