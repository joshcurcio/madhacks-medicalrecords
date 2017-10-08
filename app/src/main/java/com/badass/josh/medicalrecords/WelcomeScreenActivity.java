package com.badass.josh.medicalrecords;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.provider.MediaStore;
import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WelcomeScreenActivity extends AppCompatActivity {

    public static DatabaseHelper bigDatabase;
    public static SQLiteDatabase actualDatabase;
    public static DatabaseInfo maybeDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        bigDatabase = DatabaseHelper.getInstance(this);

        openDB();

    }


    public void takePictureButtonPressed(View v) {
        System.out.println("Button press");
        this.dispatchTakePictureIntent();


    }


    static final int REQUEST_IMAGE_CAPTURE = 1;


    String mCurrentPhotoPath;

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

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                galleryAddPic(photoFile);
                goToNewPatient();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                Intent goToNewPatientInfoIntent = new Intent(this, NewPatientActivity.class);
                startActivity(goToNewPatientInfoIntent);

            }
        }

    }

    private void galleryAddPic(File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private void goToNewPatient() {
        Intent goToNewPatientInfoIntent = new Intent(this, NewPatientActivity.class);
        startActivity(goToNewPatientInfoIntent);
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
    }

    private void closeDB()
    {
        maybeDatabase.close();
    }


}