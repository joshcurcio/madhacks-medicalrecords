package com.badass.josh.medicalrecords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecordDetailsActivity extends AppCompatActivity {


    Long recordID;
    Long patientID;
    String recordType;
    String recordDescription;
    String startDate;
    String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent recordInfo = getIntent();

        recordID = recordInfo.getLongExtra("record_id", 0);
        patientID = recordInfo.getLongExtra("patient_id", 0);
        recordType = recordInfo.getStringExtra("type");
        recordDescription = recordInfo.getStringExtra("description");
        startDate = recordInfo.getStringExtra("start_date");
        endDate = recordInfo.getStringExtra("end_date");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details);
    }










}
