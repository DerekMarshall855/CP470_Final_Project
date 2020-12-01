package com.example.cp470_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GameActivity extends AppCompatActivity {

    private RelativeLayout game;
    private ImageButton controlButton;
    private GameControlsFragment controls;
    private Boolean menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        menu = false;
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        controls = new GameControlsFragment();

        controlButton = findViewById(R.id.image_button);
        game = findViewById(R.id.game);
        game.addView(new GameView(this, point.x, point.y));


        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (!menu) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.game, controls, null)
                            .addToBackStack("name")
                            .commit();
                    menu = true;
                } else {
                    fragmentManager.popBackStackImmediate();
                    menu = false;
                }

            }
        });

    }
}