package com.example.cp470_final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GameActivity extends AppCompatActivity {

    private FrameLayout game;
    private ImageButton controlButton;
    private GameControlsFragment controls;
    private Boolean menu;

    private NoteDatabaseHelper datasource;
    private SQLiteDatabase db;
    private String[] columns = {NoteDatabaseHelper.KEY_NOTE};
    private String[] dets = {NoteDatabaseHelper.KEY_ID, NoteDatabaseHelper.KEY_DETAILS};
    Cursor cursor;
    protected static final String ACTIVITY_NAME = "GameActivity";
    DrawerLayout drawerLayout;
    ListView notesList;
    ArrayList<String> notesLog = new ArrayList<String>();
    NoteAdapter noteAdapter;
    Button createButton;
    AlertDialog.Builder enterNote;
    EditText enteredNote;
    String noteS, noteD, currentNote;


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


        ImageButton previous = (ImageButton)findViewById(R.id.gamePrevious);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        datasource = new NoteDatabaseHelper(this);
        db = datasource.getWritableDatabase();

        drawerLayout = findViewById(R.id.notesDrawer);
        notesList = findViewById(R.id.notesListView);
        noteAdapter = new NoteAdapter(this);
        notesList.setAdapter(noteAdapter);

        notesList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                noteS = noteAdapter.getItem(position);
                cursor =  db.rawQuery("select * from " + NoteDatabaseHelper.TABLE_NAME + " where " + NoteDatabaseHelper.KEY_NOTE + "=\"" + noteS + "\"" , null);
                cursor.moveToFirst();
                if (cursor != null && cursor.getCount() > 0){
                    noteD = cursor.getString(cursor.getColumnIndex(NoteDatabaseHelper.KEY_DETAILS));
                }
                Snackbar details = Snackbar.make(findViewById(R.id.notesDrawer), "Note: " + noteS + "\nDetails: " + noteD, Snackbar.LENGTH_LONG);
                details.setAction("Edit Note", new EditNoteListener());
                details.setActionTextColor(Color.CYAN);
                TextView snackbarTextView = (TextView) details.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                snackbarTextView.setMaxLines(5);

                currentNote = noteS;
                details.show();
            }
        });

        notesList.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Define 'where' part of query.
                String selection = NoteDatabaseHelper.KEY_ID + " LIKE ?";
                // Specify arguments in placeholder order.
                String[] selectionArgs = {String.valueOf(position+1)};
                int deletedRows = db.delete(NoteDatabaseHelper.TABLE_NAME, selection, selectionArgs);
                Log.i(ACTIVITY_NAME, "Number deleted:" + deletedRows);

                notesLog.remove(position);
                noteAdapter.notifyDataSetChanged();
                return true;
            }
        });


        //Database - Loading saved notes

        cursor = db.query(NoteDatabaseHelper.TABLE_NAME,
                columns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast() ) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(NoteDatabaseHelper.KEY_NOTE)));
            notesLog.add(cursor.getString(cursor.getColumnIndex(NoteDatabaseHelper.KEY_NOTE)));
            Log.i(ACTIVITY_NAME, "Cursor's  column count =" + cursor.getColumnCount());
            cursor.moveToNext();
        }
        for (int i = 0; i < cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME,"column name "+cursor.getColumnName(i));
        }

        if (notesLog.size() == 0 ){
            String dfault = getString(R.string.noteBlurb);
            notesLog.add(dfault);
            noteAdapter.notifyDataSetChanged();
            Date currentTime = Calendar.getInstance().getTime();

            ContentValues values = new ContentValues();
            values.put(NoteDatabaseHelper.KEY_NOTE, dfault);
            values.put(NoteDatabaseHelper.KEY_DETAILS, currentTime.toString());
            Log.i(ACTIVITY_NAME, "Inserting: " + dfault + currentTime.toString());
            db.insert(NoteDatabaseHelper.TABLE_NAME, null, values);
        }

    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menu_game, m);
        return true;
    }

    public void newNote(View view){
        enterNote = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_custom_dialog, null);
        enterNote.setView(dialogView);
        enterNote.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button - add new note to database + list_view
                enteredNote = dialogView.findViewById(R.id.note);
                notesLog.add(enteredNote.getText().toString());
                noteAdapter.notifyDataSetChanged();

                //Database
                Date currentTime = Calendar.getInstance().getTime();
                ContentValues values = new ContentValues();
                values.put(NoteDatabaseHelper.KEY_NOTE, enteredNote.getText().toString());
                values.put(NoteDatabaseHelper.KEY_DETAILS, currentTime.toString());
                Log.i(ACTIVITY_NAME, "Inserting: " + enteredNote.getText().toString() + currentTime.toString());
                long inserted = db.insert(NoteDatabaseHelper.TABLE_NAME, null, values);
                Log.i(ACTIVITY_NAME, "ID: " + inserted);

            }
        });
        enterNote.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog customDialog = enterNote.create();
        customDialog.show();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.aboutTool:
                Log.d("Toolbar", "About selected");
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
                break;
            case R.id.notesTool:
                Log.d("Toolbar", "Notes selected");
                drawerLayout = findViewById(R.id.notesDrawer);
                notesList = findViewById(R.id.notesListView);
                drawerLayout.openDrawer(notesList);
                break;
            case R.id.helpTool:
                Intent intent = new Intent(GameActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.settingsTool:
                Intent i = new Intent(GameActivity.this, SettingsActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
        cursor.close();
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
            LayoutInflater inflater = GameActivity.this.getLayoutInflater();
            View contentView = inflater.inflate(R.layout.activity_notes_list_row_item, null);

            TextView message = (TextView)contentView.findViewById(R.id.noteText);
            message.setText(getItem(position)); // get the string at position

            if (createButton != null) {
                createButton.setVisibility(View.INVISIBLE);
            }
            createButton = contentView.findViewById(R.id.newNoteButton);

            createButton.setVisibility(View.VISIBLE);
            return contentView;
        }

    }
    public class EditNoteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.i(ACTIVITY_NAME, "Edit note clicked");
            enterNote = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.activity_custom_dialog, null);
            enterNote.setView(dialogView);
            TextView title = dialogView.findViewById(R.id.textView);
            title.setText("Edit Note");
            enteredNote = dialogView.findViewById(R.id.note);
            enteredNote.setText(currentNote);
            enterNote.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button - add new note to database + list_view
                    int index = notesLog.indexOf(currentNote);
                    notesLog.set(index, enteredNote.getText().toString());
                    noteAdapter.notifyDataSetChanged();

                    //Database
                    Date currentTime = Calendar.getInstance().getTime();
                    ContentValues values = new ContentValues();
                    values.put(NoteDatabaseHelper.KEY_NOTE, enteredNote.getText().toString());
                    values.put(NoteDatabaseHelper.KEY_DETAILS, currentTime.toString());
                    String selection = NoteDatabaseHelper.KEY_NOTE + " LIKE ?";
                    String[] selectionArgs = { currentNote };
                    db.update(NoteDatabaseHelper.TABLE_NAME, values, selection, selectionArgs);

                }
            });
            enterNote.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            // Create the AlertDialog
            AlertDialog customDialog = enterNote.create();
            customDialog.show();

        }
    }
}