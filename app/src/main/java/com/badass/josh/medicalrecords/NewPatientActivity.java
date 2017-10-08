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
        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.locationEditText);
        //Set the number of characters the user must type before the drop down list is shown
        acTextView.setThreshold(0);
        //Set the adapter
        acTextView.setAdapter(adapter);


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
        patientLocationString = patientLocation.getText().toString().trim();

        Singleton.patientName = patientNameString;
        Singleton.patientDOB = patientDOBString;
        Singleton.patientLocation = patientLocationString;
        Singleton.patientName = patientName.getText().toString();
        Singleton.patientDOB = patientDOB.getText().toString();
        Singleton.patientLocation = patientLocation.getText().toString();



        if (!Singleton.patientName.isEmpty() && !Singleton.patientDOB.isEmpty() && !Singleton.patientLocation.isEmpty())
        {
            int location = Integer.parseInt(Singleton.patientLocation);
            Singleton.patientID = WelcomeScreenActivity.maybeDatabase.addNewPatient(Singleton.patientName, Singleton.patientDOB, location);
        }

        goToNewPatientInfo();

    }

    private void goToNewPatientInfo() {
        Intent patientInfoIntent = new Intent(this, PatientProfileActivity.class);
        startActivity(patientInfoIntent);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        int day = dayOfMonth + 1;
        Singleton.patientDOB  = year + "-" + month + "-" + day;
        patientDOB.setText( Singleton.patientDOB );
    }



}
