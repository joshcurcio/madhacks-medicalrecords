package com.badass.josh.medicalrecords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {
    public static DatabaseHelper bigDatabase;
    public static SQLiteDatabase actualDatabase;
    public static DatabaseInfo maybeDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
