package com.josh.checkers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rulesBtn = (Button) findViewById(R.id.rulesBtn);
        Button pvpBtn = (Button) findViewById(R.id.pvpBtn);
        Button pvbBtn = (Button) findViewById(R.id.pvbBtn);
        Button bvbBtn = (Button) findViewById(R.id.bvbBtn);

        rulesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRules();
            }
        });

        pvpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPVP();
            }
        });

        pvbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPVB();
            }
        });

    }

    public void openRules() {
        Intent intent = new Intent(this, Rules.class);
        startActivity(intent);
    }

    public void openPVP() {
        Intent intent = new Intent(this, PlayerVsPlayer.class);
        startActivity(intent);
    }

    public void openPVB() {
        Intent intent = new Intent(this, PlayerVsBot.class);
        startActivity(intent);
    }

    public void openBVB() {
        Intent intent = new Intent(this, Rules.class);
        startActivity(intent);
    }
}
