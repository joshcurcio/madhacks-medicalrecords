package com.badass.josh.medicalrecords;

/**
 * Created by Josh on 10/8/2017.
 */

public interface OnCSTaskCompleted{
    void onDetectCompleted(String result);
    void onIndentifyCompleted(String personId);
    void onGetPersonCompleted(String personName);
}
