package com.josh.checkers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import java.util.*;

public class PlayerVsBot extends AppCompatActivity implements View.OnClickListener {
    private int number = 0;
    private boolean wantHelp = false;
    CheckerBoard checkerBoard = new CheckerBoard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_vs_bot);

        Switch help = (Switch) findViewById(R.id.helpSwitch);

        checkerBoard.newBoard();
        printBoard();

        help.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wantHelp = isChecked;
                printBoard();
            }
        });

        // sends all click listener to onClick;
//        piece.setOnClickListener(this);


    }

    public void printBoard() {
        String[][] board = checkerBoard.getBoard();
        ImageButton[] allPieces = getAllPieces();
        ArrayList<String> allPossibleFinalCoordinates = checkerBoard.allPossibleMoves();
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

                        if (wantHelp) {
                            if (allPossibleFinalCoordinates.contains(coordinate)) {
                                btn.setImageResource(R.drawable.possible_move);
                                btn.getBackground().setAlpha(100);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        printBoard();
    }

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

    public void test(View v) {
        ImageButton button = (ImageButton) findViewById(v.getId());
        String coordinate = "01";
        if (coordinate.equals(v.getTag())) {
            switch (number) {
                case 0:
                    button.setImageResource(R.drawable.checkerpiece_white);
                    button.getBackground().setAlpha(0);
                    number++;
                    break;
                case 1:
                    button.setImageResource(R.drawable.checkerpiece_white_king);
                    button.getBackground().setAlpha(0);
                    number++;
                    break;
                case 2:
                    button.setImageResource(R.drawable.blank);
                    button.getBackground().setAlpha(255);
                    number++;
                    break;
                case 3:
                    button.setImageResource(R.drawable.checkerpiece_white);
                    button.getBackground().setAlpha(0);
                    number = 1;
                    break;
            }
        }
    }
}
