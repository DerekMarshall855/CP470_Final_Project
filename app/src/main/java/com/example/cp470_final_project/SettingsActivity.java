package com.example.cp470_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private final static String ACTIVITY_NAME = "SettingsActivity";
    private SeekBar volumeBar = null;
    ImageButton skin1, skin2, skin3;
    String skinSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox sfx = (CheckBox)findViewById(R.id.checkBox);
        ImageButton previous = (ImageButton)findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        volumeBar = (SeekBar)findViewById(R.id.seekBar2);



        sfx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    //sfx is on
                }
                else{
                    //sfx is off
                }
            }
        });

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = (float)(1 - (Math.log(100 - (progress)) / Math.log(100)));
                MainActivity.bgm.setVolume(volume, volume);
                MainActivity.bgm.start();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        //Skins
        SharedPreferences sharedPref = getSharedPreferences("Skin", 0);
        final SharedPreferences.Editor editor = sharedPref.edit();
        skinSelected = sharedPref.getString("Skin", "pirate");
        skin1 = findViewById(R.id.pirate1);
        skin2 = findViewById(R.id.pirate2);
        skin3 = findViewById(R.id.pirate3);

        final Toast toast = Toast.makeText(this, R.string.skinChange, Toast.LENGTH_SHORT);
        View toastView = toast.getView();
        toastView.getBackground().setColorFilter(getResources().getColor(R.color.ic_launcher_background), PorterDuff.Mode.SRC_IN);

        skin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skinSelected = "pirate";
                Log.i(ACTIVITY_NAME, "User skin: " + skinSelected);
                editor.putString("Skin", skinSelected);
                editor.commit();
                toast.show();
            }
        });
        skin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skinSelected = "pirate2";
                Log.i(ACTIVITY_NAME, "User skin: " + skinSelected);
                editor.putString("Skin", skinSelected);
                editor.commit();
                toast.show();
            }
        });
        skin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skinSelected = "pirate3";
                Log.i(ACTIVITY_NAME, "User skin: " + skinSelected);
                editor.putString("Skin", skinSelected);
                editor.commit();
                toast.show();
            }
        });

    }


    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}