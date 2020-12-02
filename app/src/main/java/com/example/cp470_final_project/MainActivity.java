package com.example.cp470_final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.media.MediaPlayer;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "MainActivity";
    public static MediaPlayer bgm;
    DrawerLayout drawerLayout;
    ListView notesList;
    ArrayList<String> notesLog = new ArrayList<String>();
    NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        ImageButton help = (ImageButton)findViewById(R.id.Help);
        Button play = (Button)findViewById(R.id.Play);
        ImageButton settings = (ImageButton)findViewById(R.id.Settings);
        bgm = MediaPlayer.create(MainActivity.this,R.raw.bgm);
        bgm.setLooping(true);
        bgm.start();

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LevelSelect.class);
                startActivity(intent);
            }
        });

        drawerLayout = findViewById(R.id.notesDrawer);
        notesList = findViewById(R.id.notesListView);
        noteAdapter = new NoteAdapter(this);
        notesList.setAdapter(noteAdapter);

    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menu_main, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.help_action:
                Log.d("Toolbar", "Help selected");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.about);
                builder.setMessage(R.string.appInfo);
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                //break;
            case R.id.notesTool:
                Log.d("Toolbar", "Notes selected");
                drawerLayout = findViewById(R.id.notesDrawer);
                notesList = findViewById(R.id.notesListView);
                drawerLayout.openDrawer(notesList);
        }
        return true;
    }

    protected void onStart() {
        super.onStart();
        bgm.start();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onResume() {
        super.onResume();
        bgm.start();
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
        bgm.stop();
        Log.i(ACTIVITY_NAME, "In onDestroy()");

    }
    private class NoteAdapter extends ArrayAdapter<String> {
        public NoteAdapter(Context ctx){
            super(ctx,0);
        }

        public int getCount(){
            return notesLog.size();
        }

        public String getItem(int position){
            return notesLog.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            View contentView = inflater.inflate(R.layout.activity_notes_list_row_item, null);

            TextView message = (TextView)contentView.findViewById(R.id.noteText);
            message.setText(getItem(position)); // get the string at position
            return contentView;
        }

    }
}