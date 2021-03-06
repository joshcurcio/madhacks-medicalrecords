package com.badass.josh.medicalrecords;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import static com.badass.josh.medicalrecords.WelcomeScreenActivity.actualDatabase;
import static com.badass.josh.medicalrecords.WelcomeScreenActivity.bigDatabase;


/**
 * Created by timwildauer on 10/7/17.
 */

public class DatabaseInfo
{
    public static final String DATABASE_NAME = "save_starving_kids";

    public static final String DATABASE_TABLE_PATIENTS = "patients";
    public static final String DATABASE_TABLE_PATIENTS_ID_NAME = "idPatients";
    public static final int DATABASE_TABLE_PATIENTS_ID_NUM = 0;
    public static final String DATABASE_TABLE_PATIENTS_NAME_NAME = "name";
    public static final int DATABASE_TABLE_PATIENTS_NAME_NUM = 1;
    public static final String DATABASE_TABLE_PATIENTS_DOB_NAME = "dob";
    public static final int DATABASE_TABLE_PATIENTS_DOB_NUM = 2;
    public static final String DATABASE_TABLE_PATIENTS_LOCATION_NAME = "location";
    public static final int DATABASE_TABLE_PATIENTS_LOCATION_NUM = 3;
    public static final String[] DATABASE_PATIENTS_NAMES = new String[] {DATABASE_TABLE_PATIENTS_ID_NAME, DATABASE_TABLE_PATIENTS_NAME_NAME, DATABASE_TABLE_PATIENTS_DOB_NAME, DATABASE_TABLE_PATIENTS_LOCATION_NAME};

    public static final String DATABASE_CREATE_PATIENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_PATIENTS
                    + " (" + DATABASE_TABLE_PATIENTS_ID_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DATABASE_TABLE_PATIENTS_NAME_NAME + " TEXT NOT NULL, "
                    + DATABASE_TABLE_PATIENTS_DOB_NAME + " TEXT NOT NULL, "
                    + DATABASE_TABLE_PATIENTS_LOCATION_NAME + " INTEGER NOT NULL"
                    + ");";

    public static final String DATABASE_TABLE_RECORDS = "records";
    public static final String DATABASE_TABLE_RECORDS_ID_NAME = "idRecords";
    public static final int DATABASE_TABLE_RECORDS_ID_NUM = 0;
    public static final String DATABASE_TABLE_RECORDS_PATIENT_ID_NAME = "patientID";
    public static final int DATABASE_TABLE_RECORDS_PATIENT_ID_NUM = 1;
    public static final String DATABASE_TABLE_RECORDS_TYPE_NAME = "recordType";
    public static final int DATABASE_TABLE_RECORDS_TYPE_NUM = 2;
    public static final String DATABASE_TABLE_RECORDS_DESCRIPTION_NAME = "description";
    public static final int DATABASE_TABLE_RECORDS_DESCRIPTION_NUM = 3;
    public static final String DATABASE_TABLE_RECORDS_DATE_START_NAME = "dateStart";
    public static final int DATABASE_TABLE_RECORDS_START_DATE_NUM = 4;
    public static final String DATABASE_TABLE_RECORDS_DATE_END_NAME = "dateEnd";
    public static final int DATABASE_TABLE_RECORDS_END_DATE_NUM = 5;
    public static final String[] DATABASE_RECORDS_NAMES = new String[] {DATABASE_TABLE_RECORDS_ID_NAME, DATABASE_TABLE_RECORDS_PATIENT_ID_NAME, DATABASE_TABLE_RECORDS_TYPE_NAME, DATABASE_TABLE_RECORDS_DESCRIPTION_NAME, DATABASE_TABLE_RECORDS_DATE_START_NAME, DATABASE_TABLE_RECORDS_DATE_END_NAME};

    public static final String DATABASE_CREATE_RECORDS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_RECORDS
                    + " (" + DATABASE_TABLE_RECORDS_ID_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DATABASE_TABLE_RECORDS_PATIENT_ID_NAME + " INTEGER NOT NULL, "
                    + DATABASE_TABLE_RECORDS_TYPE_NAME + " TEXT NOT NULL, "
                    + DATABASE_TABLE_RECORDS_DESCRIPTION_NAME + " TEXT NOT NULL, "
                    + DATABASE_TABLE_RECORDS_DATE_START_NAME + " INTEGER NOT NULL, "
                    + DATABASE_TABLE_RECORDS_DATE_END_NAME + " INTEGER DEFAULT 1"
                    + ");";


    public static final String DATABASE_VIEW_MAINTENANCE = "maintenance_records";

    public static final int DATABASE_VERSION = 5;

    public static final String[] DATABASE_TABLE_LIST = {DATABASE_TABLE_PATIENTS, DATABASE_TABLE_RECORDS};



    public long addNewPatient(String name, String DOB, int location)
    {
        ContentValues newPatientValues = new ContentValues();
        newPatientValues.put(DATABASE_TABLE_PATIENTS_NAME_NAME, name);
        newPatientValues.put(DATABASE_TABLE_PATIENTS_DOB_NAME, DOB);
        newPatientValues.put(DATABASE_TABLE_PATIENTS_LOCATION_NAME, location);

        return actualDatabase.insert(DATABASE_TABLE_PATIENTS, null, newPatientValues);
    }

    public long addNewRecord(String type, String description, String startDate, String endDate)
    {
        ContentValues newPatientValues = new ContentValues();
        newPatientValues.put(DATABASE_TABLE_RECORDS_PATIENT_ID_NAME, Singleton.patientID); //patientID);
        newPatientValues.put(DATABASE_TABLE_RECORDS_TYPE_NAME, type);
        newPatientValues.put(DATABASE_TABLE_RECORDS_DESCRIPTION_NAME, description);
        newPatientValues.put(DATABASE_TABLE_RECORDS_DATE_START_NAME, startDate);
        newPatientValues.put(DATABASE_TABLE_RECORDS_DATE_END_NAME, endDate);
        return actualDatabase.insert(DATABASE_TABLE_RECORDS, null, newPatientValues);
    }

    public Cursor returnAllPatients()
    {
        Cursor c = actualDatabase.query(true, DATABASE_TABLE_PATIENTS, DATABASE_PATIENTS_NAMES, null, null, null, null, null, null);
        return c;
    }

    public Cursor returnAllRecords()
    {
        Cursor killMeNow = actualDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE_RECORDS + " WHERE " + DATABASE_TABLE_RECORDS_PATIENT_ID_NAME + " = " + Integer.parseInt(Long.toString(Singleton.patientID)), null);
        return killMeNow;
    }

    public boolean getPatientInfo(int patientID)
    {
        Cursor patientInfoCursor = actualDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE_PATIENTS + " WHERE " + DATABASE_TABLE_PATIENTS_ID_NAME + " = " + patientID, null);
        if (patientInfoCursor.moveToFirst())
        {
            Singleton.patientName = patientInfoCursor.getString(DATABASE_TABLE_PATIENTS_NAME_NUM);
            Singleton.patientDOB = patientInfoCursor.getString(DatabaseInfo.DATABASE_TABLE_PATIENTS_DOB_NUM);

            String userLoc = Singleton.stringLocations[Integer.parseInt(patientInfoCursor.getString(DatabaseInfo.DATABASE_TABLE_PATIENTS_LOCATION_NUM))];
            Singleton.patientLocation = userLoc;
            return true;
        }
        else
        {
            return false;
        }

    }

    public void updateRecord(Long recordID, Long patientID, String newType, String newDescription, String newStartDate, String newEndDate)
    {
        ContentValues newContentValues = new ContentValues();
        //newContentValues.put(DATABASE_TABLE_RECORDS_ID_NAME, recordID);
        newContentValues.put(DATABASE_TABLE_RECORDS_PATIENT_ID_NAME, patientID);
        newContentValues.put(DATABASE_TABLE_RECORDS_TYPE_NAME, newType);
        newContentValues.put(DATABASE_TABLE_RECORDS_DESCRIPTION_NAME, newDescription);
        newContentValues.put(DATABASE_TABLE_RECORDS_DATE_START_NAME, newStartDate);
        newContentValues.put(DATABASE_TABLE_RECORDS_DATE_END_NAME, newEndDate);

        actualDatabase.update(DATABASE_TABLE_RECORDS, newContentValues, DATABASE_TABLE_RECORDS_ID_NAME + "=?", new String[] {"" + recordID});// + "=" + recordID, null);
    }


    public void open()
    {
        actualDatabase = bigDatabase.getWritableDatabase();
    }

    public void close()
    {
        bigDatabase.close();
    }
}