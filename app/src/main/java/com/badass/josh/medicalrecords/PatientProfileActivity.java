package com.badass.josh.medicalrecords;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


public class PatientProfileActivity extends AppCompatActivity {


    int newRecord;

    List<Long> recordIDArray = new ArrayList<>();
    Long patientID;
    List<String> recordTypeArray = new ArrayList<>();
    List<String> recordDescriptionArray = new ArrayList<>();
    List<String> recordStartDateArray = new ArrayList<>();
    List<String> recordEndDateArray = new ArrayList<>();

    TextView patientNameTV;
    TextView patientDOBTV;
    TextView patientLocationTV;
    Button addEventButton;

    ListAdapter patientRecordAdapter;
    ListView recordsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        patientNameTV = (TextView) findViewById(R.id.nameTextView);
        patientLocationTV = (TextView) findViewById(R.id.locationTextView);
        patientDOBTV = (TextView) findViewById(R.id.birthDateTextView);

        setView();
    }

    private void moveToRecordInfo(int i)
    {
        Intent moveToRecordInfo = new Intent(this, RecordDetailsActivity.class);

        moveToRecordInfo.putExtra("new_record", newRecord);
        if (newRecord == 0) {
            Singleton.isNew = true;
            moveToRecordInfo.putExtra("record_id", recordIDArray.get(i));
            moveToRecordInfo.putExtra("patient_id", Singleton.patientID);
            moveToRecordInfo.putExtra("patient_name", Singleton.patientName);
            moveToRecordInfo.putExtra("type", recordTypeArray.get(i));
            moveToRecordInfo.putExtra("description", recordDescriptionArray.get(i));
            moveToRecordInfo.putExtra("start_date", recordStartDateArray.get(i));
            moveToRecordInfo.putExtra("end_date", recordEndDateArray.get(i));
        } else {
            Singleton.isNew = false;
        }
        startActivityForResult(moveToRecordInfo, 0);

    }

    private void getPatientRecords()
    {
        Cursor c = WelcomeScreenActivity.maybeDatabase.returnAllRecords();

        recordIDArray.clear();
        recordTypeArray.clear();
        recordDescriptionArray.clear();
        recordStartDateArray.clear();
        recordEndDateArray.clear();

        if (c.moveToNext())
        {
            do {

                Long recordID = c.getLong(DatabaseInfo.DATABASE_TABLE_RECORDS_ID_NUM);
                String recordType = c.getString(DatabaseInfo.DATABASE_TABLE_RECORDS_TYPE_NUM);
                String recordDescription = c.getString(DatabaseInfo.DATABASE_TABLE_RECORDS_DESCRIPTION_NUM);
                String recordStartDate = c.getString(DatabaseInfo.DATABASE_TABLE_RECORDS_START_DATE_NUM);
                String recordEndDate = c.getString(DatabaseInfo.DATABASE_TABLE_RECORDS_END_DATE_NUM);

                recordIDArray.add(recordID);
                recordTypeArray.add(recordType);
                recordDescriptionArray.add(recordDescription);
                recordStartDateArray.add(recordStartDate);
                recordEndDateArray.add(recordEndDate);

                System.out.println(recordEndDate);


            }
            while (c.moveToNext());

        }
        c.close();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        /*setContentView(R.layout.activity_patient_profile);
        getPatientRecords(patientID);
        recordsListView.setAdapter(patientRecordAdapter);
        recordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                newRecord = 0;
                moveToRecordInfo(i);
            }
        });*/
        setView();
        patientNameTV.setText(Singleton.patientName);


    }


    public void setView()
    {
        patientNameTV.setText(Singleton.patientName);
        System.out.println(patientNameTV.getText().toString());
        patientDOBTV.setText(Singleton.patientDOB);
        patientLocationTV.setText(Singleton.patientLocation);

        getPatientRecords();
        addEventButton = (Button) findViewById(R.id.newEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                newRecord = 1;
                moveToRecordInfo(0);
            }
        });

        patientRecordAdapter = new PatientRecordAdapter(this, recordTypeArray, recordDescriptionArray,
                recordStartDateArray, recordEndDateArray);
        recordsListView = (ListView) this.findViewById(R.id.recordsListView);
        recordsListView.setAdapter(patientRecordAdapter);
        recordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                newRecord = 0;
                moveToRecordInfo(i);
            }
        });

    }

}
