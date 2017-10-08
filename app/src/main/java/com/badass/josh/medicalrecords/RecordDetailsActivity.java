package com.badass.josh.medicalrecords;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import static com.badass.josh.medicalrecords.WelcomeScreenActivity.maybeDatabase;

public class RecordDetailsActivity extends AppCompatActivity
{

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
    protected void onCreate(Bundle savedInstanceState)
    {
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
        else
        {
            patientNameEditText.setText(Singleton.patientName);
            eventTypeEditText.setText("");
            startDateEditText.setText("");
            endDateEditText.setText("");
            descriptionEditText.setText("");
        }

        final DatePickerDialog startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String startDateYear, startDateMonth, startDateDay;

                startDateYear = "0000" + year;

                if (month < 10) {

                    startDateMonth = "0" + month;
                }
                else
                {
                    startDateMonth = "" + month;
                }
                if (day < 10)
                {

                    startDateDay = "0" + day;
                }
                else
                {
                    startDateDay = "" + day;
                }

                startDate = startDateYear.substring(startDateYear.length() - 4) + "-" + startDateMonth + "-" + startDateDay;
                startDateEditText.setText(startDate);

            }
        }, 2000, 0, 1);
        startDateEditText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                int year, month, day;
                if (startDate != null)
                {
                    year = Integer.parseInt(startDate.substring(0, 4));
                    month = Integer.parseInt(startDate.substring(5, 7)) - 1;
                    day = Integer.parseInt(startDate.substring(8));

                }
                else
                {
                    Calendar today = Calendar.getInstance();
                    year = today.get(Calendar.YEAR);
                    month = today.get(Calendar.MONTH);
                    day = today.get(Calendar.DAY_OF_MONTH);

                }
                startDatePickerDialog.show();
                startDatePickerDialog.updateDate(year, month, day);

            }
        });
        final DatePickerDialog endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String endDateYear, endDateMonth, endDateDay;

                endDateYear = "0000" + year;

                if (month < 10)
                {

                    endDateMonth = "0" + month;
                }
                else
                {
                    endDateMonth = "" + month;
                }
                if (day < 10)
                {

                    endDateDay = "0" + day;
                }
                else
                {
                    endDateDay = "" + day;
                }

                endDate = endDateYear.substring(endDateYear.length() - 4) + "-" + endDateMonth + "-" + endDateDay;
                endDateEditText.setText(endDate);

            }
        }, 2000, 0, 1);
        endDateEditText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int year, month, day;
                if (endDate != null)
                {
                    year = Integer.parseInt(endDate.substring(0, 4));
                    month = Integer.parseInt(endDate.substring(5, 7)) - 1;
                    day = Integer.parseInt(endDate.substring(8));

                }
                else
                {
                    Calendar today = Calendar.getInstance();
                    year = today.get(Calendar.YEAR);
                    month = today.get(Calendar.MONTH);
                    day = today.get(Calendar.DAY_OF_MONTH);


                }

                endDatePickerDialog.show();
                endDatePickerDialog.updateDate(year, month, day);


            }
        });


        this.saveChangesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                recordType = eventTypeEditText.getText().toString();
                recordDescription = descriptionEditText.getText().toString();
                startDate = startDateEditText.getText().toString();
                endDate = endDateEditText.getText().toString();

                if (newRecord == 0)
                {
                    System.out.println("trying to update");
                    maybeDatabase.updateRecord(recordID, patientID, recordType, recordDescription, startDate, endDate);
                    System.out.println("updated record");
                }
                else
                {
                    Long result = maybeDatabase.addNewRecord(recordType, recordDescription, startDate, endDate);
                    System.out.println(result);
                }

                setResult(0);
                finish();
            }
        });


    }

}
