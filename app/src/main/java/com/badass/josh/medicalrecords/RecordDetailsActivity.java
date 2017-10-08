package com.badass.josh.medicalrecords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.badass.josh.medicalrecords.WelcomeScreenActivity.maybeDatabase;

public class RecordDetailsActivity extends AppCompatActivity {

    Long recordID;
    Long patientID;
    String recordType;
    String recordDescription;
    String startDate;
    String endDate;
    String patientName;
    int newRecord;

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

        setContentView(R.layout.activity_record_details);

        // Setting up access to UI elements
        this.saveChangesButton = (Button) this.findViewById(R.id.saveChangesButton);
        this.patientNameEditText = (EditText) this.findViewById(R.id.patientNameEditText);
        this.eventTypeEditText = (EditText) this.findViewById(R.id.eventTypeEditText);
        this.startDateEditText = (EditText) this.findViewById(R.id.startDateEditText);
        this.endDateEditText = (EditText) this.findViewById(R.id.endDateEditText);
        this.descriptionEditText = (EditText) this.findViewById(R.id.descriptionEditText);


        Intent recordInfo = getIntent();

        newRecord = recordInfo.getIntExtra("new_record", 1);
        if (newRecord == 0) {
            recordID = recordInfo.getLongExtra("record_id", 0);
            patientID = recordInfo.getLongExtra("patient_id", 0);
            recordType = recordInfo.getStringExtra("type");
            recordDescription = recordInfo.getStringExtra("description");
            startDate = recordInfo.getStringExtra("start_date");
            endDate = recordInfo.getStringExtra("end_date");
            patientName = recordInfo.getStringExtra("patient_name");

            patientNameEditText.setText(patientName);
            eventTypeEditText.setText(recordType);
            startDateEditText.setText(startDate);
            endDateEditText.setText(endDate);
            descriptionEditText.setText(recordDescription);

        }

        this.saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                recordType = eventTypeEditText.getText().toString();
                recordDescription = descriptionEditText.getText().toString();
                startDate = startDateEditText.getText().toString();
                endDate = endDateEditText.getText().toString();

                if (newRecord == 0) {
                    maybeDatabase.updateRecord(recordID, patientID, recordType, recordDescription, startDate, endDate);
                    System.out.println("updated record");
                }
                else
                {
                    Long result = maybeDatabase.addNewRecord(patientID, recordType, recordDescription, startDate, endDate);
                    System.out.println(result);
                }

                setResult(0);
                finish();
            }
        });


    }










}
