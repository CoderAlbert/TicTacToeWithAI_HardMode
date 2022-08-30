package tictactoe;

import java.util.stream.Stream;

public class FieldHelper {
    final String CELL_IS_OCCUPIED = "This cell is occupied! Choose another one!";
    final String ONLY_NUMBERS = "You should enter numbers!";
    final String ONLY_NUMBERS_ONE_TO_THREE = "Coordinates should be from 1 to 3!";

    private FieldHelper(){
    };

    public static String[][] generateNewGameField() {
        String[][] field = new String[3][3];
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                field[row][col] = "_";
            }
        }
        return field;
    }

    public static GameState getGameState(String[][] gameField) {
        final String player1 = "X";
        final String player2 = "O";

        if(playerWon(player1, gameField)) {
            return GameState.X_WINS;
        }
        if(playerWon(player2, gameField)) {
            return GameState.O_WINS;
        }

        if(!gameFinished(gameField)) {
            return GameState.GAME_NOT_FINISHED;
        }

        return GameState.DRAW;
    }

    public static boolean gameFinished(String[][] gameField) {
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                if(gameField[row][col].equals("_")) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean playerWon(String player, String[][] field) {
        final boolean diagonal1 = Stream.of(field[0][0], field[1][1], field[2][2])
                .allMatch(val -> val.equals(player));

        final boolean diagonal2 = Stream.of(field[0][2], field[1][1], field[2][0])
                .allMatch(val -> val.equals(player));

        if(diagonal1 || diagonal2) {
            return true;
        }

        for(int i= 0; i < 3; i++) {
            final boolean playerWonVertical = Stream.of(field[0][i], field[1][i], field[2][i])
                    .allMatch(val -> val.equals(player));


            final boolean playerWonHorizontal = Stream.of(field[i][0], field[i][1], field[i][2])
                    .allMatch(val -> val.equals(player));

            if(playerWonHorizontal || playerWonVertical) {
                return true;
            }
        }

        return false;
    }
}
