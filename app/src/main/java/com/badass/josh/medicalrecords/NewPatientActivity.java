package com.badass.josh.medicalrecords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewPatientActivity extends AppCompatActivity {

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
    }

    private void createNewPatient()
    {
        patientNameString = patientName.getText().toString().trim();
        patientDOBString = patientDOB.getText().toString().trim();
        patientLocationString = patientLocation.getText().toString().trim();


    }


}
