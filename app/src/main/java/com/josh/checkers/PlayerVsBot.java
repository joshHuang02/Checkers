package com.josh.checkers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerVsBot extends AppCompatActivity {
    CheckerBoard gameBoard = new CheckerBoard();

    private int number = 0;
    private boolean wantHelp = false;
    private boolean somePieceMoved = false;
    private String initialCoordinates = null;
    private View currentBtnView;

    private int stackCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_vs_bot);

        // none of the buttons are defined or have onClick because I set their 'onClick' property
        Switch help = (Switch) findViewById(R.id.helpSwitch);

        // makes new board and display it
        gameBoard.newBoard();
        printBoard(gameBoard);

        // help switch
        help.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wantHelp = isChecked;
                printBoard(gameBoard);
            }
        });
    }

    public void move(CheckerBoard checkerBoard) {
        TextView notificationTextView = (TextView) findViewById(R.id.notificationTextView);
        TextView turnTextView = (TextView) findViewById(R.id.turnTextView);
        ImageButton piece = (ImageButton) findViewById(currentBtnView.getId());

        String tag = piece.getTag().toString();

        // when you click on a piece, moveOwnPiece checks if the piece you clicked is on the correct turn
        // ex moveOwnPiece if you click on a white piece on black turn
        boolean moveOwnPiece;
        boolean squareIsNull;

        if (checkerBoard.getBoard()[tag.charAt(0) - '0'][tag.charAt(1) - '0'] != null) {
            moveOwnPiece = ('B' == checkerBoard.getBoard()[tag.charAt(0) - '0'][tag.charAt(1) - '0'].charAt(0) && checkerBoard.getBlackTurn())
                    || ('W' == checkerBoard.getBoard()[tag.charAt(0) - '0'][tag.charAt(1) - '0'].charAt(0) && !checkerBoard.getBlackTurn());
            squareIsNull = false;
        } else {
            moveOwnPiece = false;
            squareIsNull = true;
        }

        if (initialCoordinates == null) { // when click on actual piece for first time
            // for some reason putting this if statement as an "&&" in the above statement breaks the app
            if (moveOwnPiece) initialCoordinates = tag;
        } else if (moveOwnPiece) { // when click on own piece again, switches initial coordinate
            if (tag.equals(initialCoordinates)) {
                initialCoordinates = null;
            } else {
                initialCoordinates = tag;
            }
        } else if (squareIsNull) { // at this stage piece definitely moves b/c there is initial coordinate
            String coordinates = initialCoordinates + tag;
            // if a piece actually moved
            if (checkerBoard.movePiece(coordinates.charAt(0) - '0', coordinates.charAt(1) - '0', coordinates.charAt(2) - '0', coordinates.charAt(3) - '0')) {
                checkerBoard.setBlackTurn(!checkerBoard.getBlackTurn());
                somePieceMoved = true;
            } else {
                somePieceMoved = false;
            }
            initialCoordinates = null;
        }

        // tells player whose turn it is
        if (checkerBoard.getBlackTurn()) {
            turnTextView.setText("Black Turn");
            // This is when the bot starts calculating its next move
            // when turn is black and a piece has moved, avoids user clicking random squares and restarting bot process
            if (somePieceMoved) {
                printBoard(gameBoard);
                findBestMove(gameBoard);
            }
        } else {
            turnTextView.setText("White Turn");
        }

        // win message
        if (checkerBoard.getBlackPiecesCount() == 0) {
            notificationTextView.setText(getString(R.string.winMsg, "WHITE"));
        } else if (checkerBoard.getWhitePieceCount() == 0) {
            notificationTextView.setText(getString(R.string.winMsg, "BLACK"));
        } else {
            // set error or help text on the screen
            notificationTextView.setText(checkerBoard.errMsg);
        }

        printBoard(gameBoard);
    }

    /* Recursive function to find the best move by predicting every move possible for the rest of the game
     * if no single best move is found, will return a random move
     */
    public String[] findBestMove(CheckerBoard startingBoard) {
        ArrayList<String> possibleBlackMoves = startingBoard.allPossibleMoves();
        String randomBlackMove = possibleBlackMoves.get((int) (Math.random() * possibleBlackMoves.size()) );
        String[] result = {"not found", randomBlackMove};
        System.out.println(startingBoard.getBlackTurn());

        for (String blackMove : possibleBlackMoves) {
            CheckerBoard predictBlackMoves = startingBoard;
            predictBlackMoves.movePiece(Integer.parseInt(String.valueOf(blackMove.charAt(0))), Integer.parseInt(String.valueOf(blackMove.charAt(1))),
                    Integer.parseInt(String.valueOf(blackMove.charAt(2))), Integer.parseInt(String.valueOf(blackMove.charAt(3))));

            stackCount = 0;
            ArrayList<String> possibleWhiteMoves = predictBlackMoves.allPossibleMoves();
            for (int j = 1; j <= possibleWhiteMoves.size(); j++) {
                CheckerBoard predictWhiteMoves = predictBlackMoves;

                predictWhiteMoves.movePiece(Integer.parseInt(String.valueOf(possibleWhiteMoves.get(j).charAt(0))), Integer.parseInt(String.valueOf(possibleWhiteMoves.get(j).charAt(1))),
                        Integer.parseInt(String.valueOf(possibleWhiteMoves.get(j).charAt(2))), Integer.parseInt(String.valueOf(possibleWhiteMoves.get(j).charAt(3))));

                if (predictWhiteMoves.getBlackPiecesCount() == 0) {
                    result[0] = "found";
                    result[1] = blackMove;
                    System.out.println(blackMove + " " + randomBlackMove);
                    return result;
//                } else if (j == possibleWhiteMoves.size()) {
////                    return result;
                } else {
                    stackCount++;
//                    System.out.println(stackCount);
                    try
                    {
                        Thread.sleep(1000);
                        printBoard(startingBoard);
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }

                    String[] addStack = findBestMove(predictWhiteMoves);
                    if (addStack[0].equals("found")) return addStack;
                }
            } // end white prediction loop
        } // end black prediction loop

        System.out.println("not found: "+result[1]);
        return result;
    }

    public void printBoard(CheckerBoard printedBoard) {
        TextView blackPiecesTextView = (TextView) findViewById(R.id.blackPiecesTextView);
        TextView whitePiecesTextView = (TextView) findViewById(R.id.whitePiecesTextView);
        String[][] board = printedBoard.getBoard();
        ImageButton[] allPieces = getAllPieces();
        ArrayList<String> allPossibleFinalCoordinates = printedBoard.allPossibleFinalCoordinates();
        ArrayList<String> allPossibleMoves = printedBoard.allPossibleMoves();
        ArrayList<String> piecePossibleFinalCoordinates = new ArrayList<>();
        if (initialCoordinates != null && wantHelp)
            piecePossibleFinalCoordinates = printedBoard.piecePossibleMoves(initialCoordinates.charAt(0) - '0', initialCoordinates.charAt(1) - '0');

        blackPiecesTextView.setText(getString(R.string.blackPieceCount, printedBoard.getBlackPiecesCount()));
        whitePiecesTextView.setText(getString(R.string.whitePieceCount, printedBoard.getWhitePieceCount()));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (ImageButton btn : allPieces) {
                    String coordinate = i + "" + j; // empty string in middle makes sure "0" doesn't get dropped
                    String tag = btn.getTag().toString(); // coordinate tag given to each button

                    // when the button tag matches the board coordinate
                    if (tag.equals(coordinate)) {
                        // set of if-else for the values of each square
                        // note: cannot use switch(board[i][j]) because board[i][j] can be null
                        if ("BLACK".equals(board[i][j])) {
                            btn.setImageResource(R.drawable.checkerpiece_black);
                            btn.getBackground().setAlpha(0);
                        } else if ("WHITE".equals(board[i][j])) {
                            btn.setImageResource(R.drawable.checkerpiece_white);
                            btn.getBackground().setAlpha(0);
                        } else if ("BKING".equals(board[i][j])) {
                            btn.setImageResource(R.drawable.checkerpiece_black_king);
                            btn.getBackground().setAlpha(0);
                        } else if ("WKING".equals(board[i][j])) {
                            btn.setImageResource(R.drawable.checkerpiece_white_king);
                            btn.getBackground().setAlpha(0);
                        } else {
                            btn.setImageDrawable(null);
                            btn.getBackground().setAlpha(0);
                        }
                        if (tag.equals(initialCoordinates)) {
                            btn.getBackground().setAlpha(255);
                        }

                        if (wantHelp) {
                            // mark all possible moves
                            if (allPossibleFinalCoordinates.contains(coordinate)) {
                                btn.setImageResource(R.drawable.green_box);
                                btn.getBackground().setAlpha(0);
                                // mark possible moves for the move selected
                                if (allPossibleMoves.contains(initialCoordinates + coordinate) && piecePossibleFinalCoordinates.contains(coordinate)) {
                                    btn.setImageResource(R.drawable.green_square);
                                    btn.getBackground().setAlpha(0);
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    // returns an array of all piece ImageButtons
    private ImageButton[] getAllPieces() {
        ImageButton allPieces[] = new ImageButton[32];
        allPieces[0] = (ImageButton) findViewById(R.id.piece);
        allPieces[1] = (ImageButton) findViewById(R.id.piece1);
        allPieces[2] = (ImageButton) findViewById(R.id.piece2);
        allPieces[3] = (ImageButton) findViewById(R.id.piece3);
        allPieces[4] = (ImageButton) findViewById(R.id.piece4);
        allPieces[5] = (ImageButton) findViewById(R.id.piece5);
        allPieces[6] = (ImageButton) findViewById(R.id.piece6);
        allPieces[7] = (ImageButton) findViewById(R.id.piece7);
        allPieces[8] = (ImageButton) findViewById(R.id.piece8);
        allPieces[9] = (ImageButton) findViewById(R.id.piece9);
        allPieces[10] = (ImageButton) findViewById(R.id.piece10);
        allPieces[11] = (ImageButton) findViewById(R.id.piece11);
        allPieces[12] = (ImageButton) findViewById(R.id.piece12);
        allPieces[13] = (ImageButton) findViewById(R.id.piece13);
        allPieces[14] = (ImageButton) findViewById(R.id.piece14);
        allPieces[15] = (ImageButton) findViewById(R.id.piece15);
        allPieces[16] = (ImageButton) findViewById(R.id.piece16);
        allPieces[17] = (ImageButton) findViewById(R.id.piece17);
        allPieces[18] = (ImageButton) findViewById(R.id.piece18);
        allPieces[19] = (ImageButton) findViewById(R.id.piece19);
        allPieces[20] = (ImageButton) findViewById(R.id.piece20);
        allPieces[21] = (ImageButton) findViewById(R.id.piece21);
        allPieces[22] = (ImageButton) findViewById(R.id.piece22);
        allPieces[23] = (ImageButton) findViewById(R.id.piece23);
        allPieces[24] = (ImageButton) findViewById(R.id.piece24);
        allPieces[25] = (ImageButton) findViewById(R.id.piece25);
        allPieces[26] = (ImageButton) findViewById(R.id.piece26);
        allPieces[27] = (ImageButton) findViewById(R.id.piece27);
        allPieces[28] = (ImageButton) findViewById(R.id.piece28);
        allPieces[29] = (ImageButton) findViewById(R.id.piece29);
        allPieces[30] = (ImageButton) findViewById(R.id.piece30);
        allPieces[31] = (ImageButton) findViewById(R.id.piece31);
        return allPieces;
    }

    // allows a piece to be moved without user clicking on anything
    public void moveBtn(View v) {
        currentBtnView = v;
        move(gameBoard);
    }

    public void returnHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
