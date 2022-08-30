package tictactoe;

public enum GameState {
    GAME_NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins");

    private String state;

    GameState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
