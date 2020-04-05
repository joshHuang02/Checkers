package com.josh.checkers;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class CheckerBoard {
    // board elements
    private String[][] board = new String[8][8];
    // count of pieces remaining on the board for each side
    private int blackPieceCount = 12;
    private int whitePieceCount = 12;
    // this keeps track of turns to determine possible moves
    private boolean blackTurn = true;
    // the entire board in one string to be printed
    private String boardVisual = "";
    // error message in case there is one, wasn't feeling getters and setters so its public
    public String errMsg = "";

    public boolean movePiece(int x0, int y0, int xf, int yf)  {
        // checks if the initial square is correct color based on what turn it is
        if (!(("BLACK".equals(board[x0][y0]) && blackTurn) || ("BKING".equals(board[x0][y0]) && blackTurn)
                || ("WHITE".equals(board[x0][y0]) && !blackTurn) || ("WKING".equals(board[x0][y0]) && !blackTurn))) {
//            System.out.println("Only BLACK pieces can be moved on BLACK Turn and only WHITE pieces can be moved on WHITE Turn");
            this.errMsg = "Only BLACK pieces can be moved on BLACK Turn and only WHITE pieces can be moved on WHITE Turn";
            return false;
        }
        ArrayList<String> allPossibleMoves = allPossibleMoves();
        // makes the coordinates into string for matching
        String moveCoordinates = Integer.toString(x0) + Integer.toString(y0) + Integer.toString(xf) + Integer.toString(yf);
        // if move is a step (move by one square) and is a possible move
        if (Math.abs(xf - x0) <= 1 && Math.abs(yf - y0) <= 1 && allPossibleMoves.contains(moveCoordinates)) {
            board[xf][yf] = board[x0][y0];
            board[x0][y0] = null;
            return true;
            // else if move is a jump (over opponent piece) and is a possible move
        } else if (allPossibleMoves.contains(moveCoordinates)) {
            board[xf][yf] = board[x0][y0];
            board[x0][y0] = null;
            board[(x0 + xf) / 2][(y0 + yf) / 2] = null; // makes the piece that was jumped over into null
            // decrease opponent piece count by one
            if (blackTurn) {
                whitePieceCount--;
            } else {
                blackPieceCount--;
            }

            // else if final coordinates are out of bounds
        } else {
//            System.out.println("Illegal move, type 'help' for all possible moves");
            this.errMsg = "Illegal move, type 'help' for all possible moves";
            return false;
        }
        // implements rule that jumps must be chained if another jump is possible
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && availableJumps(i, j).size() != 0) {
                    return false; // returning false will NOT switch turns
                }
            }
        }
        return true; // returning true will switch turns
    }

    // finds all possible moves of every one of your piece based on turn
    public ArrayList<String> allPossibleMoves() {
        // possibleMoves stores all possible moves as String coordinates (includes
        // initial and final coordinates)
        ArrayList<String> possibleMoves = new ArrayList<>();
        // for loop every square to see if there is any available jumps
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ArrayList<String> availableJumps = availableJumps(i, j);
                if (board[i][j] != null && availableJumps.size() != 0) {
                    for (String coordiate : availableJumps) {
                        possibleMoves.add(Integer.toString(i) + Integer.toString(j) + coordiate);
                    }
                }
            }
        }
        // if there is no jumps (because you must jump if there is a possible jump)
        if (possibleMoves.size() == 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ArrayList<String> availableMoves = availableSteps(i, j);
                    if (board[i][j] != null && availableMoves.size() != 0) {
                        for (String coordiate : availableMoves) {
                            possibleMoves.add(Integer.toString(i) + Integer.toString(j) + coordiate);
                        }
                    }
                }
            }
        }
        // reverses the order of the help array to make it more readable for black side
        if (blackTurn)
            Collections.reverse(possibleMoves);
        return possibleMoves;
    }

    // all possible steps (move by one square) from initial coordinates
    public ArrayList<String> availableSteps(int x0, int y0) {
        ArrayList<String> availableMoves = new ArrayList<>();
        // if is black turn and initial square is black piece
        if (blackTurn && ("BLACK".equals(board[x0][y0]) || "BKING".equals(board[x0][y0]))) {
            // checks the left side move
            if (x0 < 7 && y0 > 0 && board[x0 + 1][y0 - 1] == null)
                availableMoves.add(Integer.toString(x0 + 1) + Integer.toString(y0 - 1));
            // check right side move
            if (x0 < 7 && y0 < 7 && board[x0 + 1][y0 + 1] == null)
                availableMoves.add(Integer.toString(x0 + 1) + Integer.toString(y0 + 1));
            // if piece if a king then check backwards moves
            if ("BKING".equals(board[x0][y0])) {
                // check left side
                if (x0 > 0 && y0 > 0 && board[x0 - 1][y0 - 1] == null)
                    availableMoves.add(Integer.toString(x0 - 1) + Integer.toString(y0 - 1));
                // check right side
                if (x0 > 0 && y0 < 7 && board[x0 - 1][y0 + 1] == null)
                    availableMoves.add(Integer.toString(x0 - 1) + Integer.toString(y0 + 1));
            }
        } else if (!blackTurn && ("WHITE".equals(board[x0][y0]) || "WKING".equals(board[x0][y0]))) {
            if (x0 > 0 && y0 > 0 && board[x0 - 1][y0 - 1] == null)
                availableMoves.add(Integer.toString(x0 - 1) + Integer.toString(y0 - 1));
            if (x0 > 0 && y0 < 7 && board[x0 - 1][y0 + 1] == null)
                availableMoves.add(Integer.toString(x0 - 1) + Integer.toString(y0 + 1));
            if ("WKING".equals(board[x0][y0])) {
                if (x0 < 7 && y0 > 0 && board[x0 + 1][y0 - 1] == null)
                    availableMoves.add(Integer.toString(x0 + 1) + Integer.toString(y0 - 1));
                if (x0 < 7 && y0 < 7 && board[x0 + 1][y0 + 1] == null)
                    availableMoves.add(Integer.toString(x0 + 1) + Integer.toString(y0 + 1));
            }
        }
        return availableMoves;
    }

    // all possible jumps (jump over opponent piece) from initial coordinates
    public ArrayList<String> availableJumps(int x0, int y0) {
        ArrayList<String> availableJumps = new ArrayList<>();
        if (blackTurn) {
            if ("BLACK".equals(board[x0][y0]) || "BKING".equals(board[x0][y0])) {
                if (x0 < 6 && y0 > 1 && ("WHITE".equals(board[x0 + 1][y0 - 1]) || "WKING".equals(board[x0 + 1][y0 - 1]))
                        && board[x0 + 2][y0 - 2] == null)
                    availableJumps.add(Integer.toString(x0 + 2) + Integer.toString(y0 - 2));

                if (x0 < 6 && y0 < 6 && ("WHITE".equals(board[x0 + 1][y0 + 1]) || "WKING".equals(board[x0 + 1][y0 + 1]))
                        && board[x0 + 2][y0 + 2] == null)
                    availableJumps.add(Integer.toString(x0 + 2) + Integer.toString(y0 + 2));
            }
            if ("BKING".equals(board[x0][y0])) {
                if (x0 > 1 && y0 > 1 && ("WHITE".equals(board[x0 - 1][y0 - 1]) || "WKING".equals(board[x0 - 1][y0 - 1]))
                        && board[x0 - 2][y0 - 2] == null)
                    availableJumps.add(Integer.toString(x0 - 2) + Integer.toString(y0 - 2));

                if (x0 > 1 && y0 < 6 && ("WHITE".equals(board[x0 - 1][y0 + 1]) || "WKING".equals(board[x0 - 1][y0 + 1]))
                        && board[x0 - 2][y0 + 2] == null)
                    availableJumps.add(Integer.toString(x0 - 2) + Integer.toString(y0 + 2));
            }
        } else {
            if ("WHITE".equals(board[x0][y0]) || "WKING".equals(board[x0][y0])) {
                if (x0 > 1 && y0 > 1 && ("BLACK".equals(board[x0 - 1][y0 - 1]) || "BKING".equals(board[x0 - 1][y0 - 1]))
                        && board[x0 - 2][y0 - 2] == null)
                    availableJumps.add(Integer.toString(x0 - 2) + Integer.toString(y0 - 2));
                if (x0 > 1 && y0 < 6 && ("BLACK".equals(board[x0 - 1][y0 + 1]) || "BKING".equals(board[x0 - 1][y0 + 1]))
                        && board[x0 - 2][y0 + 2] == null)
                    availableJumps.add(Integer.toString(x0 - 2) + Integer.toString(y0 + 2));
            }
            if ("WKING".equals(board[x0][y0])) {
                if (x0 < 6 && y0 > 1 && ("BLACK".equals(board[x0 + 1][y0 - 1]) || "BKING".equals(board[x0 + 1][y0 - 1]))
                        && board[x0 + 2][y0 - 2] == null)
                    availableJumps.add(Integer.toString(x0 + 2) + Integer.toString(y0 - 2));
                if (x0 < 6 && y0 < 6 && ("BLACK".equals(board[x0 + 1][y0 + 1]) || "BKING".equals(board[x0 + 1][y0 + 1]))
                        && board[x0 + 2][y0 + 2] == null)
                    availableJumps.add(Integer.toString(x0 + 2) + Integer.toString(y0 + 2));
            }
        }
        return availableJumps;
    }

    public String printBoard() {
        System.out.println("     0     1     2     3     4     5     6     7   ");
        System.out.println("  _________________________________________________");
        this.boardVisual = "     0     1     2     3     4     5     6     7   \n  _________________________________________________\n";
        int rowNum = 0;
        for (String[] row : board) {
            System.out.println("  |     |     |     |     |     |     |     |     |");
            boardVisual += "  |     |     |     |     |     |     |     |     |\n";
            System.out.print(rowNum + " ");
            boardVisual += rowNum + " ";
            for (String s : row) {
                if (s == null) {
                    System.out.print("|     ");
                    boardVisual += "|     ";
                } else {
                    System.out.print("|" + s + "");
                    boardVisual += "|" + s;
                }
            }
            System.out.print("|");
            boardVisual += "|";
            if (rowNum == 3) {
                System.out.print(" BLACK Pieces: " + blackPieceCount);
            } else if (rowNum == 4) {
                System.out.print(" WHITE Pieces: " + whitePieceCount);
            }
            System.out.println("\n  |_____|_____|_____|_____|_____|_____|_____|_____|");
            boardVisual += "\n  |_____|_____|_____|_____|_____|_____|_____|_____|\n";
            rowNum++;
        }
        Log.d("board", "printed");
        return boardVisual;
    }

    public void newBoard() {
        for (int i = 1; i < 8; i += 2) {
            board[0][i] = "BLACK";
            board[2][i] = "BLACK";
            board[3][i] = null;
            board[4][i] = null;
        }
        for (int i = 0; i < 8; i += 2) {
            board[1][i] = "BLACK";
            board[3][i] = null;
            board[4][i] = null;
        }
        for (int i = 0; i < 8; i += 2) {
            board[5][i] = "WHITE";
            board[7][i] = "WHITE";
        }
        for (int i = 1; i < 8; i += 2) {
            board[6][i] = "WHITE";
        }
        blackPieceCount = 12;
        whitePieceCount = 12;
        blackTurn = true;
    }

    public int getBlackPiecesCount() {
        return this.blackPieceCount;
    }

    public int getWhitePieceCount() {
        return this.whitePieceCount;
    }

    public boolean getBlackTurn() {
        return this.blackTurn;
    }

    public String[][] getBoard() {
        return this.board;
    }

    public void setBlackTurn(boolean blackTurn) {
        this.blackTurn = blackTurn;
    }
}
