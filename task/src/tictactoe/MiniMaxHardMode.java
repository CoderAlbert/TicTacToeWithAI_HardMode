package tictactoe;

public class MiniMaxHardMode {
    static class Move {
        int row, col;
    };

    private String player, opponent;

    public MiniMaxHardMode(String player, String oppenent) {
        this.player = player;
        this.opponent = oppenent;
    }

    private boolean isMovesLeft(String[][] board) {
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                if(board[row][col].equals("_")) {
                    return true;
                }
            }
        }

        return false;
    }

    // This is the evaluation function as discussed
    // in the previous article ( http://goo.gl/sJgv68 )
    private int evaluate(String[][] board) {
        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++) {
            if (board[row][0].equals(board[row][1]) &&
                    board[row][1].equals(board[row][2])) {
                if (board[row][0].equals(player)) {
                    return +10;
                }

                else if (board[row][0].equals(opponent)) {
                    return -10;
                }
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++) {
            if (board[0][col].equals(board[1][col]) &&
                    board[1][col].equals(board[2][col])) {
                if (board[0][col].equals(player)) {
                    return +10;
                } else if (board[0][col].equals(opponent)) {
                    return -10;
                }
            }
        }

        // Checking for Diagonals for X or O victory.
        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            if (board[0][0].equals(player)) {
                return +10;
            } else if (board[0][0].equals(opponent)) {
                return -10;
            }
        }

        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            if (board[0][2].equals(player)) {
                return +10;
            }

            else if (board[0][2].equals(opponent)) {
                return -10;
            }
        }

        // Else if none of them have won then return 0
        return 0;
    }

    // This is the minimax function. It considers all
    // the possible ways the game can go and returns
    // the value of the board
    private int minimax(String[][] board, int depth, Boolean isMax) {
        int score = evaluate(board);

        // If Maximizer has won the game
        // return his/her evaluated score
        if (score == 10) {
            return score;
        }

        // If Minimizer has won the game
        // return his/her evaluated score
        if (score == -10) {
            return score;
        }

        // If there are no more moves and
        // no winner then it is a tie
        if (!isMovesLeft(board)) {
            return 0;
        }

        // If this maximizer's move
        if (isMax) {
            int best = -1000;

            // Traverse all cells
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (board[i][j].equals("_")) {
                        // Make the move
                        board[i][j] = player;

                        // Call minimax recursively and choose
                        // the maximum value
                        best = Math.max(best, minimax(board,
                                depth + 1, !isMax));

                        // Undo the move
                        board[i][j] = "_";
                    }
                }
            }
            return best;
        }

        // If this minimizer's move
        else {
            int best = 1000;

            // Traverse all cells
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    // Check if cell is empty
                    if (board[row][col].equals("_")) {
                        // Make the move
                        board[row][col] = opponent;

                        // Call minimax recursively and choose
                        // the minimum value
                        best = Math.min(best, minimax(board, depth + 1, !isMax));

                        // Undo the move
                        board[row][col] = "_";
                    }
                }
            }

            return best;
        }
    }

    // This will return the best possible
    // move for the player
    public Move findBestMove(String[][] board) {
        int bestVal = -1000;
        Move bestMove = new Move();
        bestMove.row = -1;
        bestMove.col = -1;

        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                // Check if cell is empty
                if (board[row][col].equals("_")) {
                    // Make the move
                    board[row][col] = player;

                    // compute evaluation function for this
                    // move.
                    int moveVal = minimax(board, 0, false);

                    // Undo the move
                    board[row][col] = "_";

                    // If the value of the current move is
                    // more than the best value, then update
                    // best/
                    if (moveVal > bestVal) {
                        bestMove.row = row;
                        bestMove.col = col;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove;
    }
}
