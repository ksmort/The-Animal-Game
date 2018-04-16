package com.animalgame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.animalgame.animal.Animal;
import com.animalgame.theanimalgame.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AnimalDatabaseHelper extends SQLiteOpenHelper {

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 8;
    private static final String FILENAME = "AnimalDatabase.txt";
    private static final String DB_NAME = "animal.db";
   private final Context mContext;

    AnimalDatabaseHelper(Context context ) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        String CREATE_TABLE_ANIMAL = "CREATE TABLE IF NOT EXISTS " + Animal.TABLE  + "("
                + Animal.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Animal.KEY_animalName + " TEXT COLLATE NOCASE, "
                + Animal.KEY_fact + " TEXT COLLATE NOCASE, "
                + Animal.KEY_pictureFilename + " TEXT COLLATE NOCASE, UNIQUE ( " + Animal.KEY_animalName + " )" +
                "ON CONFLICT FAIL)";

        db.execSQL(CREATE_TABLE_ANIMAL);
        populateDatabase(db, FILENAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Animal.TABLE);

//        // Create tables again
        onCreate(db);
        populateDatabase(db, FILENAME);

    }

    private void populateDatabase(SQLiteDatabase database, String filename) {
        String line;
        try {
            InputStream is = mContext.getResources().getAssets().open(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                ContentValues values = new ContentValues();
                values.put(Animal.KEY_animalName, line);
                values.put(Animal.KEY_pictureFilename, "");
                values.put(Animal.KEY_fact, "");

                // Inserting Row
                database.insert(Animal.TABLE, null, values);

            }
            br.close();
        } catch (Exception e) {
            Toast.makeText(mContext, R.string.load_database_error, Toast.LENGTH_SHORT).show();
        }
    }
}
