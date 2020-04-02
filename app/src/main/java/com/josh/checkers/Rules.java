package com.josh.checkers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Rules extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        Button returnBtn = (Button) findViewById(R.id.returnBtn);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPVP();
            }
        });
    }

    public void openPVP() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
