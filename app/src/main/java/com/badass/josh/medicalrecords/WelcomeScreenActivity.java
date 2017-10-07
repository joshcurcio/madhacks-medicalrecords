package com.badass.josh.medicalrecords;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.provider.MediaStore;
import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
    }

    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_SELECT_IMAGE_IN_ALBUM = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    // The URI of photo taken with camera
    private Uri mUriPhotoTaken;
    public void takePictureButtonPressed(View v) {
        System.out.println("Button press");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            // Save the photo taken to a temporary file.
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File file = File.createTempFile("IMG_", ".jpg", storageDir);
                mUriPhotoTaken = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            } catch (IOException e) {
                //setInfo(e.getMessage());
            }
        }
    }





    // Bring up the camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // What do we do when we get the picture back?
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            System.out.println("requestCode = " + requestCode + "\nresultCode = " + resultCode + "\ndata = " + data);
        }
    }

}

