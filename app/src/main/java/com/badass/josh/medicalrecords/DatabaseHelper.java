package com.badass.josh.medicalrecords;

/**
 * Created by Josh on 10/7/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper
{

    /*
     * This method is the one that should be called to create the DB. It first checks to see if the database has been
     * created, and if it hasn't, it gets the app context and creates the DB there instead of in only one context.
     * If not for this, we would have to recreate the DB every time we go to a new screen.
     */
    private static DatabaseHelper savedInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (savedInstance == null) {
            savedInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return savedInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DatabaseInfo.DATABASE_NAME, null, DatabaseInfo.DATABASE_VERSION);
        Log.d("db", "database created");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseInfo.DATABASE_CREATE_PATIENTS_TABLE);
        db.execSQL(DatabaseInfo.DATABASE_CREATE_RECORDS_TABLE);
        Log.d("db", "created tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = 0; i < DatabaseInfo.DATABASE_TABLE_LIST.length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseInfo.DATABASE_TABLE_LIST[i]);
        }
        onCreate(db);
    }

}