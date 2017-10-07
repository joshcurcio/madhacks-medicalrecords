package com.badass.josh.medicalrecords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PatientProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        ListAdapter patientRecordAdapter = new PatientRecordAdapter();
        ListView recordsListView = (ListView) this.findViewById(R.id.recordsListView);
        recordsListView.setAdapter(patientRecordAdapter);

    }
}
