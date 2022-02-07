package com.example.medicare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicare.data.PatientDetailLines;
import com.example.medicare.data.PatientDetails;

import java.util.List;

public class ViewPatientHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_history);

        PatientDetails patientDetails = (PatientDetails) getIntent().getSerializableExtra("patientHistory");

        showDataOnUI(patientDetails.getPatientDetails());
    }

    protected void showDataOnUI(List<PatientDetailLines> patientDetails) {
        RecyclerView recyclerView =  findViewById(R.id.rv);

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        CustomRecyclerViewAdapter adapter = new CustomRecyclerViewAdapter(patientDetails);
        recyclerView.setAdapter(adapter);
    }
}
