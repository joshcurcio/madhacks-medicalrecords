package com.badass.josh.medicalrecords;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;


public class NewPatientActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NewPerson
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

    private String cognitiveServicesBaseUrl = "https://eastus.api.cognitive.microsoft.com/face/v1.0";
    private String cognitiveServicesAPIKey = "d2ea96917bc34094ba1bff98fcb1b1aa";
    private String cognitiveServicesPersonGroup = "madhacks";

    static final int REQUEST_TAKE_PHOTO = 1;
    static File photoFile = null;

    private Uri mImageUri;



    ProgressDialog mProgressDialog;
    String mCurrentPhotoPath;

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
            //TODO add person and train
            CSAddPerson addPerson = new CSAddPerson(this);
            addPerson.execute();

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

    @Override
    public void onAddPersonCompleted(String personId) {
        CSAddPhoto identifyTask = new CSAddPhoto(this);
        identifyTask.execute(personId);
    }

    @Override
    public void onAddFaceCompleted(String result) {
        CSTrainPersonGroup identifyTask = new CSTrainPersonGroup(this);
        identifyTask.execute(result);
    }

    @Override
    public void onTrainPersonGroupCompleted(String result){
        if (result.equals("{}")){

        } else {

        }
    }

    public class CSAddPerson extends AsyncTask<String, Void, String> {
        private NewPerson listener;

        public CSAddPerson(NewPerson listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String personId = strings[0];
            HttpClient httpclient = new DefaultHttpClient();
            try {
                URIBuilder builder = new URIBuilder(cognitiveServicesBaseUrl + "/persongroups/" + cognitiveServicesPersonGroup + "/persons");
                URI uri = builder.build();
                HttpPost request = new HttpPost(uri);
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Ocp-Apim-Subscription-Key", cognitiveServicesAPIKey);
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("name", Singleton.patientID);
                request.setEntity(new StringEntity(jsonRequest.toString()));
                HttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }

                JSONObject personData = new JSONObject(result);
                String personID = personData.getString("personId");

                return personID;
            } catch (Exception e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            listener.onAddPersonCompleted(result.toString());
        }
    }

    public class CSAddPhoto extends AsyncTask<String, Void, String> {
        private NewPerson listener;

        public CSAddPhoto(NewPerson listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String personId = strings[0];
            HttpClient httpclient = new DefaultHttpClient();
            try {
                URIBuilder builder = new URIBuilder(cognitiveServicesBaseUrl + "/persongroups/" + cognitiveServicesPersonGroup + "/persons/" + personId + "/persistedFaces");
                builder.setParameter("personGroupId", cognitiveServicesPersonGroup);
                builder.setParameter("personId", personId);
                URI uri = builder.build();
                HttpPost request = new HttpPost(uri);
                request.setHeader("Content-Type", "application/octet-stream");
                request.setHeader("Ocp-Apim-Subscription-Key", cognitiveServicesAPIKey);
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("url", Singleton.mBitmap.getNinePatchChunk());
                request.setEntity(new StringEntity(jsonRequest.toString()));
                HttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }

                JSONObject personData = new JSONObject(result);
                String persistedFaceId = personData.getString("persistedFaceId");

                return persistedFaceId;
            } catch (Exception e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            listener.onAddFaceCompleted(result.toString());
        }
    }

    public class CSTrainPersonGroup extends AsyncTask<String, Void, String> {
        private NewPerson listener;

        public CSTrainPersonGroup(NewPerson listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String personId = strings[0];
            HttpClient httpclient = new DefaultHttpClient();
            try {
                URIBuilder builder = new URIBuilder(cognitiveServicesBaseUrl + "/persongroups/" + cognitiveServicesPersonGroup + "/train");
                builder.setParameter("personGroupId", cognitiveServicesPersonGroup);
                URI uri = builder.build();
                HttpPost request = new HttpPost(uri);
                request.setHeader("Ocp-Apim-Subscription-Key", cognitiveServicesAPIKey);
                HttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }

                JSONObject personData = new JSONObject(result);
                String personID = personData.toString();
                return personID;
            } catch (Exception e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            listener.onTrainPersonGroupCompleted(result.toString());
        }
    }
}
