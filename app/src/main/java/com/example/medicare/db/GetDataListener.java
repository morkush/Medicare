package com.example.medicare.db;

import com.example.medicare.data.PatientDetails;

public interface GetDataListener {

    public void onStart();
    public void onSuccess(PatientDetails patientDetails);
    public void onFailure();
}
