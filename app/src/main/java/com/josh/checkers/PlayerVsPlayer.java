package com.josh.checkers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class PlayerVsPlayer extends AppCompatActivity {
    Checkers checkers = new Checkers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_vs_player);

        Button moveBtn = (Button) findViewById(R.id.moveBtn);
        final Button returnBtn = (Button) findViewById(R.id.returnBtn);

        checkers.newBoard();
        printVisual();

        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView notificationTextView = (TextView) findViewById(R.id.notificationTextView);
                notificationTextView.setText("");
                playGame();
                EditText coordinateEditText = (EditText) findViewById(R.id.coordinateEditText);
                coordinateEditText.getText().clear();
                checkers.errMsg = "";
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnHome();
            }
        });
    }

    public void playGame() {
        TextView notificationTextView = (TextView) findViewById(R.id.notificationTextView);
        EditText coordinates = (EditText) findViewById(R.id.coordinateEditText);

        // while none of the scores are 0, run the game, prints the board at the the end
        // of each loop
        if (Math.min(checkers.getBlackPiecesCount(), checkers.getWhitePieceCount()) != 0) {
            String move = coordinates.getText().toString();
            // switch to see what the input string is
            switch (move) {
                // restarts the game
                case "restart":
//                    System.out.println("Game restarted. ");
                    notificationTextView.setText(getString(R.string.restart));
                    checkers.newBoard();
                    break;
                // type "help" to see all your possible moves
                case "help":
//                    System.out.print("You have these possible move(s): ");
//                    System.out.println(Arrays.toString(checkers.allPossibleMoves().toArray()));\
                    String possibleMovesMsg = getString(R.string.possibleMoves, checkers.allPossibleMoves().toString());
                    notificationTextView.setText(possibleMovesMsg);
                    break;
                // default is when you give a coordinate
                default:
                    // coordinates can only be number and be only 4 characters long
                    if (!move.matches("[0-9]+") || move.length() != 4) {
//                        System.out.println("Invalid Coordinates");
                        notificationTextView.setText(R.string.invalCoor);
                    } else {
                        // call move piece. If move piece returns true, then switch turns
                        // Note: will not switch turns when another jump is possible
                        if (checkers.movePiece((int) move.charAt(0) - 48, (int) move.charAt(1) - 48, (int) move.charAt(2) - 48,
                                (int) move.charAt(3) - 48)) {
                            checkers.setBlackTurn(!checkers.getBlackTurn());
                        }
                        if (checkers.errMsg.length() != 0)
                            notificationTextView.setText(checkers.errMsg);
                        // when a piece reach the other side of the board, make it a king
                        for (int i = 0; i < 8; i++) {
                            if ("WHITE".equals(checkers.getBoard()[0][i]))
                                checkers.getBoard()[0][i] = "WKING";
                            if ("BLACK".equals(checkers.getBoard()[7][i]))
                                checkers.getBoard()[7][i] = "BKING";
                        }
                    }
                    break;
            }
            if (checkers.getBlackTurn()) {
//                System.out.print("BLACK Turn: ");
                coordinates.setHint("BLACK Turn");
            } else {
//                System.out.print("WHITE Turn: ");
                coordinates.setHint("WHITE Turn");
            }
            Log.d("boolean", checkers.getBlackTurn() + "");
            printVisual();
        }
        // when a side runs out of pieces, the other side wins, also ends the program
        if (checkers.getBlackPiecesCount() == 0) {
//            System.out.println("WHITE wins!
            notificationTextView.setText(getString(R.string.winMsg, "WHITE"));
        } else if (checkers.getWhitePieceCount() == 0) {
//            System.out.println("BLACK wins!");
            notificationTextView.setText(getString(R.string.winMsg, "BLACK"));
        }
    }

    public void printVisual() {
        TextView blackPiecesTextView = (TextView) findViewById(R.id.blackPiecesTextView);
        TextView whitePiecesTextView = (TextView) findViewById(R.id.whitePiecesTextView);
        TextView boardTextView = (TextView) findViewById(R.id.boardTextView);

        blackPiecesTextView.setText(getString(R.string.blackPieceCount, checkers.getBlackPiecesCount()));
        whitePiecesTextView.setText(getString(R.string.whitePieceCount, checkers.getWhitePieceCount()));
        boardTextView.setText(checkers.printBoard());
    }

    public void returnHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
