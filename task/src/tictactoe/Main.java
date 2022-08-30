package tictactoe;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Game game = new Game();
        final Scanner scanner = new Scanner(System.in);
        String userInput;
        do {
            System.out.print("Input command: ");
            userInput = scanner.nextLine();

            if(userInput.equals("exit")) {
                break;
            }
            final String[] inputs = userInput.split(" ");
            if(inputs.length != 3) {
                System.out.println("Bad parameters!");
                continue;
            }
            final List<String> validPlayers = Arrays.asList("easy", "medium", "hard", "user");
            final boolean start = inputs[0].equals("start");
            final String player1 = inputs[1];
            final String player2 = inputs[2];

            if(!start || !validPlayers.contains(player1) || !validPlayers.contains(player2)) {
                System.out.println("Bad parameters!");
                continue;
            }

            startGame(game, player1, player2);

        } while(!userInput.equals("exit"));
    }

    private static void startGame(Game game, String player1, String player2) {
        do {
            game.makeMovePlayer(player1);
            game.makeMovePlayer(player2);
        } while (game.getGameState().equals(GameState.GAME_NOT_FINISHED));

        //Game finished
        System.out.println(game.getGameState().getState());
    }

}
