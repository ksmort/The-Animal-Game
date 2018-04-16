package com.animalgame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.animalgame.animal.Animal;
import com.animalgame.theanimalgame.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimalDatabaseAdapter {
    private AnimalDatabaseHelper mDbHelper;

    public AnimalDatabaseAdapter(Context context)
    {
        mDbHelper = new AnimalDatabaseHelper(context);
    }

    public long addAnimal(Context context, Animal animal) {

        long animal_Id = -1;
        if (validateParameter(animal.animalName)) {

            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Animal.KEY_animalName, animal.animalName);
            values.put(Animal.KEY_pictureFilename, animal.pictureFilename);
            values.put(Animal.KEY_fact, animal.fact);

            // Inserting Row
            animal_Id = db.insert(Animal.TABLE, null, values);

            db.close(); // Closing database connection
            if (animal_Id < 0) {
                Toast.makeText(context, R.string.add_animal_error_message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Added animal " + animal.animalName, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, R.string.missing_mandatory_parameters, Toast.LENGTH_SHORT).show();
        }
        return animal_Id;
    }

    public long deleteAnimal(Context context, int animalId) {
        int returnId = -1;
        if (animalId > 0) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            // It's a good practice to use parameter ?, instead of concatenate string
            returnId = db.delete(Animal.TABLE, Animal.KEY_ID + "= ?", new String[]{String.valueOf(animalId)});
            db.close(); // Closing database connection

            if (returnId < 0) {
                Toast.makeText(context, R.string.delete_animal_error_message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Deleted animal.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, R.string.missing_mandatory_parameters, Toast.LENGTH_SHORT).show();
        }
        return returnId;
    }

    public long updateAnimal(Context context, Animal animal) {
        long resultId = -1;
        String animalName = animal.animalName;
        if (validateParameter(animalName) && animal.animal_ID > 0) {
            Animal currentAnimal = getAnimalByName(animal.animalName);
            //make sure that name is not already played
            if (currentAnimal.animal_ID == animal.animal_ID) {
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Animal.KEY_ID, animal.animal_ID);
                values.put(Animal.KEY_animalName, animalName);
                values.put(Animal.KEY_pictureFilename, animal.pictureFilename);
                values.put(Animal.KEY_fact, animal.fact);

                // It's a good practice to use parameter ?, instead of concatenate string
                resultId = db.update(Animal.TABLE, values, Animal.KEY_ID + "= ?", new String[]{String.valueOf(animal.animal_ID)});
                db.close(); // Closing database connection
                if (resultId < 0) {
                    Toast.makeText(context, R.string.update_animal_error_message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Updated animal " + animalName, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, R.string.update_animal_name_already_used, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, R.string.missing_mandatory_parameters, Toast.LENGTH_SHORT).show();
        }

        return resultId;

    }

    public ArrayList<HashMap<String, String>> getAnimalList() {
        //Open connection to read only
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Animal.KEY_ID + "," +
                Animal.KEY_animalName +
                " FROM " + Animal.TABLE +
                " ORDER BY " + Animal.KEY_animalName + " COLLATE NOCASE ASC";

        //Student student = new Student();
        ArrayList<HashMap<String, String>> animalList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> animal = new HashMap<>();
                animal.put("id", cursor.getString(cursor.getColumnIndex(Animal.KEY_ID)));
                animal.put("name", cursor.getString(cursor.getColumnIndex(Animal.KEY_animalName)));
                animalList.add(animal);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return animalList;
    }
    public Animal getAnimalByName(String animalName){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Animal.KEY_ID + "," +
                Animal.KEY_animalName + "," +
                Animal.KEY_pictureFilename + "," +
                Animal.KEY_fact +
                " FROM " + Animal.TABLE
                + " WHERE " +
                Animal.KEY_animalName + "='" + animalName + "'";// It's a good practice to use parameter ?, instead of concatenate string

        Animal animal = new Animal();

        Cursor cursor = db.rawQuery(selectQuery, null );

        if (cursor.moveToFirst()) {
            do {
                animal.animal_ID =cursor.getInt(cursor.getColumnIndex(Animal.KEY_ID));
                animal.animalName =cursor.getString(cursor.getColumnIndex(Animal.KEY_animalName));
                animal.pictureFilename  =cursor.getString(cursor.getColumnIndex(Animal.KEY_pictureFilename));
                animal.fact =cursor.getString(cursor.getColumnIndex(Animal.KEY_fact));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return animal;
    }

    public boolean checkIfAnimalExists(String animalName) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  EXISTS (SELECT * FROM " +
                Animal.TABLE + " WHERE " + Animal.KEY_animalName +
                "='" + animalName + "' LIMIT 1)";
        try {
            Cursor cursor = db.rawQuery(selectQuery, null );
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) == 1) {
                    cursor.close();
                    db.close();
                    return true;
                } else {
                    cursor.close();
                    db.close();
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean validateParameter(String animalName) {
        return !(animalName == null || animalName.isEmpty());
    }
}
