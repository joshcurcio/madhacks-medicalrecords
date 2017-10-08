package com.badass.josh.medicalrecords;

/**
 * Created by Josh on 10/8/2017.
 */

public interface NewPerson {
    void onAddPersonCompleted(String result);
    void onAddFaceCompleted(String personId);
    void onTrainPersonGroupCompleted(String status);
}
