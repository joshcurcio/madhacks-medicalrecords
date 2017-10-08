package com.badass.josh.medicalrecords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends AppCompatActivity
{
    public static DatabaseHelper bigDatabase;
    public static SQLiteDatabase actualDatabase;
    public static DatabaseInfo maybeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        bigDatabase = DatabaseHelper.getInstance(this);

        openDB();

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        closeDB();
    }

    private void openDB()
    {
        maybeDatabase = new DatabaseInfo();
        maybeDatabase.open();
    }

    private void closeDB()
    {
        maybeDatabase.close();
    }

}
