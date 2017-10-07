package com.badass.josh.medicalrecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by timwildauer on 10/7/17.
 */

public class PatientRecordAdapter extends ArrayAdapter<String>
{

    ArrayList<String> recordTypeArray;
    ArrayList<String> recordDescriptionArray;
    ArrayList<String> recordStartDateArray;
    ArrayList<String> recordEndDateArray;

    public PatientRecordAdapter(Context context, ArrayList<String> recordTypeArray, ArrayList<String> recordDescriptionArray,
                                ArrayList<String> recordStartDateArray, ArrayList<String> recordEndDateArray)
    {
        super(context, R.layout.patient_record_layout, recordTypeArray);
        this.recordTypeArray = recordTypeArray;
        this.recordDescriptionArray = recordDescriptionArray;
        this.recordStartDateArray = recordStartDateArray;
        this.recordEndDateArray = recordEndDateArray;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater carListInflater = LayoutInflater.from(getContext());

        View recordListView = carListInflater.inflate(R.layout.patient_record_layout, parent, false);

        String patientRecordTypeString = recordTypeArray.get(position);
        String patientRecordDescriptionString = recordDescriptionArray.get(position);
        String patientRecordStartDateString = recordStartDateArray.get(position);
        String patientRecordEndDateString = recordEndDateArray.get(position);

        TextView recordTypeTV = (TextView) recordListView.findViewById(R.id.patientRecordType);
        TextView recordDescriptionTV = (TextView) recordListView.findViewById(R.id.patientRecordDescription);
        TextView recordStartDateTV = (TextView) recordListView.findViewById(R.id.patientRecordStartDate);
        TextView recordEndDateTV = (TextView) recordListView.findViewById(R.id.patientRecordEndDate);

        recordTypeTV.setText(patientRecordTypeString);
        recordDescriptionTV.setText(patientRecordDescriptionString.substring(0, 15));
        recordStartDateTV.setText(patientRecordStartDateString);
        recordEndDateTV.setText(patientRecordEndDateString);

        return recordListView;
    }

}
