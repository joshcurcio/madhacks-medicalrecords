package com.badass.josh.medicalrecords;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import static com.badass.josh.medicalrecords.MainActivity.maybeDatabase;

public class PatientProfileActivity extends AppCompatActivity {
    List<Long> recordIDArray;
    Long patientID;
    List<String> recordTypeArray;
    List<String> recordDescriptionArray;
    List<String> recordStartDateArray;
    List<String> recordEndDateArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);


        Intent hello = getIntent();

        patientID = hello.getLongExtra("patient_id", 0);
        getPatientRecords(patientID);


        ListAdapter patientRecordAdapter = new PatientRecordAdapter(this, recordTypeArray, recordDescriptionArray,
                recordStartDateArray, recordEndDateArray);
        ListView recordsListView = (ListView) this.findViewById(R.id.recordsListView);
        recordsListView.setAdapter(patientRecordAdapter);
        recordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                moveToRecordInfo(i);
            }
        });

    }

    private void moveToRecordInfo(int i)
    {
        Intent moveToRecordInfo = new Intent(this, RecordDetailsActivity.class);

        moveToRecordInfo.putExtra("record_id", recordIDArray.get(i));
        moveToRecordInfo.putExtra("patient_id", patientID);
        moveToRecordInfo.putExtra("type", recordTypeArray.get(i));
        moveToRecordInfo.putExtra("description", recordDescriptionArray.get(i));
        moveToRecordInfo.putExtra("start_date", recordStartDateArray.get(i));
        moveToRecordInfo.putExtra("end_date", recordEndDateArray.get(i));

        startActivity(moveToRecordInfo);

    }

    private void getPatientRecords(Long patientIDNum)
    {
        Cursor c = maybeDatabase.returnAllRecords(patientIDNum);

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

            }
            while (c.moveToNext());

        }
        c.close();
    }
}
