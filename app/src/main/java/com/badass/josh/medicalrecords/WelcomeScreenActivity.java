package com.badass.josh.medicalrecords;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.badass.josh.medicalrecords.helper.ImageHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeScreenActivity extends Activity implements OnCSTaskCompleted{


    public static DatabaseHelper bigDatabase;
    public static SQLiteDatabase actualDatabase;
    public static DatabaseInfo maybeDatabase;

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
        setContentView(R.layout.activity_welcome_screen);

        bigDatabase = DatabaseHelper.getInstance(this);

        openDB();
        TextView userName = (TextView) findViewById(R.id.textView2);
        userName.setText(Singleton.userEmail);

    }


    public void takePictureButtonPressed(View v) {
        System.out.println("Button press");
        this.dispatchTakePictureIntent();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mImageUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void galleryAddPic(File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mImageUri = Uri.fromFile(f);
        mediaScanIntent.setData(mImageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == REQUEST_TAKE_PHOTO)
        {
            galleryAddPic(photoFile);
            Singleton.mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                    mImageUri, getContentResolver());
            if(Singleton.mBitmap != null) {
                /*ByteArrayOutputStream output = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());*/
                //OnCSTaskCompleted listener = new OnCSTaskCompleted();
                try {
                    CSFaceDetectTask detectTask = new CSFaceDetectTask(this);
                    detectTask.execute();
                } catch (Exception e) {
                    Log.e("ERROR ", "doInBackground: ", e);
                }
            }
        }
    }


    @Override
    public void onDetectCompleted(String result) {
        if (result == "failed") {
            Intent welcomeIntent = new Intent(this, WelcomeScreenActivity.class);
            startActivity(welcomeIntent);
            return;
        }
        try
        {
            JSONArray faceArray = new JSONArray(result);
            JSONObject faceData = faceArray.getJSONObject(0);
            String faceId = faceData.getString("faceId");
            CSFaceIdentifyTask identifyTask = new CSFaceIdentifyTask(this);
            identifyTask.execute(faceId);
        }
        catch(JSONException e)
        {
            //mText.append("\n" + e.toString());
        }
    }

    @Override
    public void onIndentifyCompleted(String personId) {
        //mText.append("\npersonId: " + personId);
        if (personId == ""){
            //mText.append("\nUnidentified Person");
            Intent newPatientIntent = new Intent(this, NewPatientActivity.class);
            startActivity(newPatientIntent);
            return;
        }
        CSFaceGetPersonTask getPersonTask = new CSFaceGetPersonTask(this);
        getPersonTask.execute(personId);
    }

    @Override
    public void onGetPersonCompleted(String personName) {
        boolean isFound = WelcomeScreenActivity.maybeDatabase.getPatientInfo(Integer.parseInt(personName));
        if(isFound){
            Singleton.patientID = Long.parseLong(personName);
            Intent patientInfoIntent = new Intent(this, PatientProfileActivity.class);
            startActivity(patientInfoIntent);
        } else {
            Intent newPatientIntent = new Intent(this, NewPatientActivity.class);
            startActivity(newPatientIntent);
        }
    }

    public class CSFaceGetPersonTask extends AsyncTask<String, Void, String> {
        private OnCSTaskCompleted listener;

        public CSFaceGetPersonTask(OnCSTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String personId = strings[0];
            HttpClient httpclient = new DefaultHttpClient();
            try {
                URIBuilder builder = new URIBuilder(cognitiveServicesBaseUrl + "/persongroups/" + cognitiveServicesPersonGroup + "/persons/" + personId);
                URI uri = builder.build();
                HttpGet request = new HttpGet(uri);
                request.setHeader("Ocp-Apim-Subscription-Key", cognitiveServicesAPIKey);
                HttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }

                JSONObject personData = new JSONObject(result);
                String personName = personData.getString("name");

                return personName;
            } catch (Exception e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            listener.onGetPersonCompleted(result.toString());
        }
    }

    public class CSFaceIdentifyTask extends AsyncTask<String, Void, String> {
        private OnCSTaskCompleted listener;

        public CSFaceIdentifyTask(OnCSTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            HttpClient httpclient = new DefaultHttpClient();
            try {
                URIBuilder builder = new URIBuilder(cognitiveServicesBaseUrl + "/identify");
                URI uri = builder.build();
                HttpPost request = new HttpPost(uri);

                request.setHeader("Content-Type", "application/json");
                request.setHeader("Ocp-Apim-Subscription-Key", cognitiveServicesAPIKey);

                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("personGroupId", cognitiveServicesPersonGroup);
                JSONArray faceIds = new JSONArray();
                faceIds.put(strings[0]);
                jsonRequest.put("faceIds", faceIds);
                request.setEntity(new StringEntity(jsonRequest.toString()));

                HttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }

                JSONArray personData = new JSONArray(result);
                JSONObject faceMatch = personData.getJSONObject(0);
                JSONArray candidates = faceMatch.getJSONArray("candidates");
                JSONObject candidate = candidates.getJSONObject(0);

                String personId = candidate.getString("personId");
                return personId;
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            listener.onIndentifyCompleted(result.toString());
        }
    }

    public class CSFaceDetectTask extends AsyncTask<String, Void, String> {
        private OnCSTaskCompleted listener;
        private String result = "";

        public CSFaceDetectTask(OnCSTaskCompleted listener){
            this.listener=listener;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            try {
                URIBuilder builder = new URIBuilder(cognitiveServicesBaseUrl + "/detect");
                builder.setParameter("returnFaceId", "true");
                builder.setParameter("returnFaceLandmarks", "false");

                URI uri = builder.build();
                HttpPost request = new HttpPost(uri);

                request.setHeader("Content-Type", "application/octet-stream");
                request.setHeader("Ocp-Apim-Subscription-Key", cognitiveServicesAPIKey);

                ByteArrayOutputStream output = new ByteArrayOutputStream();
                Singleton.mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

                request.setEntity(new ByteArrayEntity(output.toByteArray()));

                HttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            listener.onDetectCompleted(result.toString());
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        closeDB();
    }

    private void openDB()
    {
        maybeDatabase = new DatabaseInfo();
        maybeDatabase.open();
        actualDatabase.execSQL(DatabaseInfo.DATABASE_CREATE_PATIENTS_TABLE);
        actualDatabase.execSQL(DatabaseInfo.DATABASE_CREATE_RECORDS_TABLE);
    }



    private void closeDB()
    {
        maybeDatabase.close();
    }

}