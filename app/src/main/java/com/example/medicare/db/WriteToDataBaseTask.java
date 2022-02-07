package com.example.medicare.db;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.medicare.data.PatientDetails;

public class WriteToDataBaseTask extends AsyncTask<PatientDetails, String, String> {

    Context context;

    public WriteToDataBaseTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(PatientDetails... patientDetails) {
        FirebaseDataOperations firebaseDataOperations = new FirebaseDataOperations();
        firebaseDataOperations.insertPatientDetails(patientDetails[0]);
        return "SUCCESS";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context, "Record Inserted Successfully!", Toast.LENGTH_LONG).show();
    }
}
