package com.badass.josh.medicalrecords;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


public class NewPatientActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    EditText patientName;
    EditText patientDOB;
    EditText patientLocation;
    Button newPatientButton;

    String patientNameString;
    String patientDOBString;
    String patientLocationString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);

        patientName = (EditText) findViewById(R.id.patientNameEditText);
        patientDOB = (EditText) findViewById(R.id.dateOfBirthEditText);
        patientLocation = (EditText) findViewById(R.id.locationEditText);

        newPatientButton = (Button) findViewById(R.id.addNewPatientButton);

        newPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPatient();
            }
        });

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, NewPatientActivity.this, 01, 01, 01);
        patientDOB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datePickerDialog.show();
            }
        });

    }

    private void createNewPatient()
    {
        patientNameString = patientName.getText().toString().trim();
        patientDOBString = patientDOB.getText().toString().trim();
        patientLocationString = patientLocation.getText().toString().trim();

        if (!patientNameString.isEmpty() && !patientDOBString.isEmpty() && !patientLocationString.isEmpty())
        {
            int location = Integer.parseInt(patientLocationString);
            MainActivity.maybeDatabase.addNewPatient(patientNameString, patientDOBString, location);
        }

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        int day = dayOfMonth + 1;
        patientDOBString = year + "-" + month + "-" + day;
        patientDOB.setText(patientDOBString);
    }
}
