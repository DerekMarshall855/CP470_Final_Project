package com.example.cp470_final_project;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteDatabaseHelper extends SQLiteOpenHelper {
    public static String ACTIVITY_NAME = "NoteDatabaseHelper";
    public static String DATABASE_NAME = "Notes.db";
    public static int VERSION_NUM = 1;
    public static final String KEY_ID = "_id";
    public static final String KEY_NOTE = "NOTE";
    public static final String KEY_DETAILS = "DETAILS";
    public static final String TABLE_NAME = "TableOfNotes";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "(" + KEY_ID
            + " integer primary key autoincrement, " + KEY_NOTE
            + " text not null, " + KEY_DETAILS
            + " text not null );";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        Log.i(ACTIVITY_NAME,"Calling onCreate");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public NoteDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
}