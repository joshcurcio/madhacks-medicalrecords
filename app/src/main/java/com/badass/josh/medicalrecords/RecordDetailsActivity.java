package com.badass.josh.medicalrecords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RecordDetailsActivity extends AppCompatActivity {

    Long recordID;
    Long patientID;
    String recordType;
    String recordDescription;
    String startDate;
    String endDate;

    // UI elements
    Button saveChangesButton;
    EditText patientNameEditText;
    EditText eventTypeEditText;
    EditText startDateEditText;
    EditText endDateEditText;
    EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent recordInfo = getIntent();

        recordID = recordInfo.getLongExtra("record_id", 0);
        patientID = recordInfo.getLongExtra("patient_id", 0);
        recordType = recordInfo.getStringExtra("type");
        recordDescription = recordInfo.getStringExtra("description");
        startDate = recordInfo.getStringExtra("start_date");
        endDate = recordInfo.getStringExtra("end_date");

        // Setting up access to UI elements
        this.saveChangesButton = (Button) this.findViewById(R.id.saveChangesButton);
        this.patientNameEditText = (EditText) this.findViewById(R.id.patientNameEditText);
        this.eventTypeEditText = (EditText) this.findViewById(R.id.eventTypeEditText);
        this.startDateEditText = (EditText) this.findViewById(R.id.startDateEditText);
        this.endDateEditText = (EditText) this.findViewById(R.id.endDateEditText);
        this.descriptionEditText = (EditText) this.findViewById(R.id.descriptionEditText);




        setContentView(R.layout.activity_record_details);
    }










}
