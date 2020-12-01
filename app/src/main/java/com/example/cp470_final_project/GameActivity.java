package com.example.cp470_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
    private GameControlsFragment controls;
    private Boolean menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);

        menu = false;
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        controls = new GameControlsFragment();
        Bundle bundle = this.getIntent().getExtras();

        //controlButton = findViewById(R.id.image_button);
        game = findViewById(R.id.game);
        game.addView(new GameView(this, point.x, point.y));
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!menu) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            controls.setArguments(bundle);
            ft.replace(R.id.game, controls, null);
            ft.addToBackStack("name");
            ft.commit();
            menu = true;
        } else {
            fragmentManager.popBackStackImmediate();
            menu = false;
        }


        //controlButton.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View v) {


     //       }
      //  });

    }
}