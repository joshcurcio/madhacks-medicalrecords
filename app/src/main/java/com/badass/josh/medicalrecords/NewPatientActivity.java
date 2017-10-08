package com.badass.josh.medicalrecords;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


public class NewPatientActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    EditText patientName;
    EditText patientDOB;
    EditText patientLocation;
    Button newPatientButton;

    Long patientID;
    String patientNameString;
    String patientDOBString;
    String patientLocationString;
    AutoCompleteTextView acTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);

        patientName = (EditText) findViewById(R.id.patientNameEditText);
        patientDOB = (EditText) findViewById(R.id.dateOfBirthEditText);
        // patientLocation = (EditText) findViewById(R.id.locationEditText);

        newPatientButton = (Button) findViewById(R.id.addNewPatientButton);

        patientDOB.setText("2000-01-01");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, Singleton.stringLocations);
        //Find TextView control
        acTextView = (AutoCompleteTextView) findViewById(R.id.locationEditText);
        //Set the number of characters the user must type before the drop down list is shown
        acTextView.setThreshold(0);
        //Set the adapter
        acTextView.setAdapter(adapter);
        acTextView.setText("");


        newPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPatient();
            }
        });

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, NewPatientActivity.this, 2000, 0, 1);
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
        String patientLocationText = acTextView.getText().toString().trim();

        Singleton.LOCATIONS locations = Singleton.LOCATIONS.valueOf(patientLocationText);


        Singleton.patientName = patientNameString;
        Singleton.patientDOB = patientDOBString;
        Singleton.patientName = patientName.getText().toString();
        Singleton.patientDOB = patientDOB.getText().toString();
        Singleton.patientLocation = patientLocationText;



        if (!Singleton.patientName.isEmpty() && !Singleton.patientDOB.isEmpty() && !Singleton.patientLocation.isEmpty())
        {

            int location = locations.getUserLocation();
            Singleton.patientID = WelcomeScreenActivity.maybeDatabase.addNewPatient(Singleton.patientName, Singleton.patientDOB, location);
        }

        goToNewPatientInfo();

    }

    private void goToNewPatientInfo() {
        Intent patientInfoIntent = new Intent(this, PatientProfileActivity.class);
        startActivity(patientInfoIntent);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth)
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
        if (dayOfMonth < 10)
        {

            endDateDay = "0" + dayOfMonth;
        }
        else
        {
            endDateDay = "" + dayOfMonth;
        }

        patientDOBString = endDateYear.substring(endDateYear.length() - 4) + "-" + endDateMonth + "-" + endDateDay;
        patientDOB.setText(patientDOBString);

        Singleton.patientDOB  = patientDOBString;
        patientDOB.setText( Singleton.patientDOB );
    }



}
