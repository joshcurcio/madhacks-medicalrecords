package com.badass.josh.medicalrecords;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by linseb325 on 10/7/17.
 */

public class Singleton {
    public static boolean isNew = true;
    public static long patientID = 0;
    public static String patientName = "";
    public static String patientDOB = "";
    public static String patientLocation = "";

    public static Bitmap mBitmap;

    public static enum LOCATIONS {
        MADISON (0),
        MEQUON (1);

        private final int userLocation;
        LOCATIONS (int location){
            userLocation = location;
        }
        public int getUserLocation(){
            return userLocation;
        }


    }

    public static String[] stringLocations = new String[] {"MADISON", "MEQUON"};
}
