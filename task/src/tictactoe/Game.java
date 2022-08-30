package tictactoe;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    final String CELL_IS_OCCUPIED = "This cell is occupied! Choose another one!";
    final String ONLY_NUMBERS = "You should enter numbers!";
    final String ONLY_NUMBERS_ONE_TO_THREE = "Coordinates should be from 1 to 3!";
    private final Scanner scanner = new Scanner(System.in);
    private String[][] gameField;
    private GameState gameState;
    private static Map<Integer, int[]> coordsMapper = new HashMap<>();

    public GameState getGameState() {
        return gameState;
    }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    static {
        coordsMapper.put(0, new int[] {0, 0});
        coordsMapper.put(1, new int[] {0, 1});
        coordsMapper.put(2, new int[] {0, 2});
        coordsMapper.put(3, new int[] {1, 0});
        coordsMapper.put(4, new int[] {1, 1});
        coordsMapper.put(5, new int[] {1, 2});
        coordsMapper.put(6, new int[] {2, 0});
        coordsMapper.put(7, new int[] {2, 1});
        coordsMapper.put(8, new int[] {2, 2});
    }

    public Game() {
        gameField = FieldHelper.generateNewGameField();
        gameState = GameState.GAME_NOT_FINISHED;
    }

    private GameState setPlayerToCoords(String coords) {
        final int[] rowAndCol = tryGetRowAndCol(coords);
        if(rowAndCol == null) {
            return null;
        }
        final int row = rowAndCol[0] - 1;
        final int col = rowAndCol[1] - 1;
        gameField[row][col] = getCurrentPlayer();

        return FieldHelper.getGameState(gameField);
    }

    public void makeMovePlayer(String player) {
        switch(player) {
            case "user": {
                this.makeUserMove();
                break;
            }
            case "easy": {
                this.makeEasyLevelMove();
                break;
            }
            case "medium": {
                this.makeMediumLevelMove();
                break;
            }
            case "hard": {
                this.makeHardLevelMove();
                break;
            }
        }
    }

    public void makeUserMove() {
        printField();
        GameState gameState;
        do {
            System.out.print("Enter the coordinates:");
            final String coords = scanner.nextLine();
            gameState = setPlayerToCoords(coords);
            if(gameState != null) {
                printField();
                System.out.println(gameState.getState());
            }
        } while(gameState == null);

        setGameState(gameState);
    }

    public void makeEasyLevelMove() {
        if(!FieldHelper.getGameState(gameField).equals(GameState.GAME_NOT_FINISHED)) {
            return;
        }
        System.out.println("Making move level \"easy\"");
        makeRandomStep();
        printField();

        setGameState(FieldHelper.getGameState(gameField));
    }

    /**
     * logic:
     * 1. try to win. If there are two in row set it to win
     * 2. check if the enemy can win with next step block it
     * 3. make random move
     */
    public void makeMediumLevelMove() {
        if(!FieldHelper.getGameState(gameField).equals(GameState.GAME_NOT_FINISHED)) {
            return;
        }
        System.out.println("Making move level \"medium\"");
        final String computer = getCurrentPlayer();

        final boolean madeStepToWin = foundPositionToWin(computer);
        if(!madeStepToWin) {
            tryBlockOrRandomStep();
        }

        printField();
        setGameState(FieldHelper.getGameState(gameField));
    }

    /**
     * Call MiniMaxAlgorithmn
     */
    private void makeHardLevelMove() {
        if(!FieldHelper.getGameState(gameField).equals(GameState.GAME_NOT_FINISHED)) {
            return;
        }
        System.out.println("Making move level \"hard\"");
        final String currentPlayer = getCurrentPlayer();
        final String opponent = currentPlayer.equals("X") ? "O" : "X";
        MiniMaxHardMode hardMode = new MiniMaxHardMode(currentPlayer, opponent);
        MiniMaxHardMode.Move bestMove = hardMode.findBestMove(gameField);
        gameField[bestMove.row][bestMove.col] = currentPlayer;

        printField();
        setGameState(FieldHelper.getGameState(gameField));
    }

    private String getCurrentPlayer() {
        final List<String> fieldAsList = Arrays.stream(gameField)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        final long amountOfEmptyPositions = fieldAsList.stream()
                .filter(val -> val.equals("_"))
                .count();

        return amountOfEmptyPositions % 2 == 0 ? "O" : "X";
    }

    private int[] tryGetRowAndCol(String coords) {
        if(coords.matches("[a-zA-Z]+")) {
            System.out.println(ONLY_NUMBERS);
            return null;
        }
        final String[] twoCoords = coords.split(" ");
        for(String coordinate : twoCoords) {
            if(coordinate.matches("[a-zA-Z]+")) {
                System.out.println(ONLY_NUMBERS);
                return null;
            }
        }

        final int row = Integer.parseInt(twoCoords[0]);
        final int col = Integer.parseInt(twoCoords[1]);

        if(row > 3 || row < 1 || col > 3 || col < 1 ) {
            System.out.println(ONLY_NUMBERS_ONE_TO_THREE);
            return null;
        }

        if(!isPositionValid(row, col)) {
            System.out.println(CELL_IS_OCCUPIED);
            return null;
        }

        return new int[] {row, col};
    }

    private boolean isPositionValid(int row, int col) {
        return gameField[row - 1][col - 1].equals("_");
    }

    private void makeRandomStep() {
        final Random rand = new Random();
        boolean isPositionValid = false;
        int row, col;
        do{
            final int coordinate = rand.nextInt(9);
            row = coordsMapper.get(coordinate)[0];
            col = coordsMapper.get(coordinate)[1];
            isPositionValid = isPositionValid(row + 1, col + 1);
        } while(!isPositionValid);

        String computer = getCurrentPlayer();
        gameField[row][col] = computer;
    }

    private void tryBlockOrRandomStep() {
        final String enemyPlayer = getCurrentPlayer().equals("X") ? "O" : "X";
        final boolean haveBlockedEnemy = foundPositionToWin(enemyPlayer);
        if(!haveBlockedEnemy) {
            makeRandomStep();
        }
    }

    private boolean foundPositionToWin(String player) {
        String[][] copyField = copyField();
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                final String currentPos = gameField[row][col];
                if(currentPos.equals("_")) {
                    copyField[row][col] = player;
                    final boolean playerWon = FieldHelper.playerWon(player, copyField);
                    //update fields and return
                    if(playerWon) {
                        gameField[row][col] = getCurrentPlayer();
                        return true;
                    } else { // reset
                        copyField = copyField();
                    }
                }
            }
        }
        FieldHelper.playerWon(player, gameField);

        return false;
    }

    private String[][] copyField() {
        String[][] copy = new String[gameField.length][gameField[0].length];
        for(int row = 0; row < gameField.length; row++) {
            System.arraycopy(gameField[row], 0, copy[row], 0, gameField[row].length);
        }
        return copy;
    }

    public void printField() {
        System.out.println("---------");
        for(int row = 0; row < 3; row++) {
            System.out.print("| ");
            for(int col = 0; col < 3; col++) {
                String gameValue = gameField[row][col].equals("_") ? " " : gameField[row][col];
                System.out.print(gameValue + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }
}
