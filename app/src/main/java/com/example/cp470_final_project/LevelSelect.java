package com.example.cp470_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class LevelSelect extends AppCompatActivity {

    private Button level_1;
    private Button level_2;
    private Button level_3;
    public int chosenLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        level_1 = findViewById(R.id.level_1_button);
        level_2 = findViewById(R.id.level_2_button);
        level_3 = findViewById(R.id.level_3_button);

        level_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Pass level (1, 2, 3) to GameActivity
                chosenLevel = 1;
                Intent intent = new Intent(LevelSelect.this, GameActivity.class);
                intent.putExtra("Level", chosenLevel);
                startActivity(intent);

            }
        });

        level_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenLevel = 2;
                Intent intent = new Intent(LevelSelect.this, GameActivity.class);
                intent.putExtra("Level", chosenLevel);
                startActivity(intent);
            }
        });

        level_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenLevel = 3;
                Intent intent = new Intent(LevelSelect.this, GameActivity.class);
                intent.putExtra("Level", chosenLevel);
                startActivity(intent);
            }
        });

    }
}