package com.josh.checkers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PlayerVsBot extends AppCompatActivity {
    private int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_vs_bot);
        Button cmd = (Button) findViewById(R.id.cmd);
        cmd.setOnClickListener(new View.OnClickListener() {

            

            @Override
            public void onClick(View v) {
                printBoard();
            }
        });

    }

    public void printBoard() {
        ImageButton button = (ImageButton) findViewById(R.id.zeroOne);
//                ImageButton Btn = (ImageButton) findViewById(R.id.00);
        System.out.println(number);
        switch(number){
            case 0:
                button.setImageResource(R.drawable.checkerpiece_white);
                button.getBackground().setAlpha(0);
                number ++;
                break;
            case 1:
                button.setImageResource(R.drawable.checkerpiece_white_king);
                button.getBackground().setAlpha(0);
                number ++;
                break;
            case 2:
                button.setImageResource(R.drawable.blank);
                button.getBackground().setAlpha(255);
                number ++;
                break;
            case 3:
                button.setImageResource(R.drawable.checkerpiece_white);
                button.getBackground().setAlpha(0);
                number = 1;
                break;
        }
    }
}
