package com.example.medicare;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewRecordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);

        String policyNbr = getIntent().getStringExtra("policyNbr");
        TextView policyTextView = findViewById(R.id.policyNbr);
        policyTextView.setText(policyNbr);

        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewRecordActivity.this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        sex = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void addPatientDetails(View view) {

        TextView policyTextView = findViewById(R.id.policyNbr);
        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText age = findViewById(R.id.age);
        EditText height =  findViewById(R.id.height);
        EditText weight =  findViewById(R.id.weight);
        EditText comment = findViewById(R.id.comment);

        com.example.medicare.data.PatientDetails patientDetails = new com.example.medicare.data.PatientDetails();
        patientDetails.setPolicyNbr(policyTextView.getText().toString());
        patientDetails.setFirstName(firstName.getText().toString());
        patientDetails.setLastName(lastName.getText().toString());
        patientDetails.setAge(Integer.parseInt(age.getText().toString()));
        patientDetails.setWeight(Integer.parseInt(weight.getText().toString()));
        patientDetails.setHeight(Integer.parseInt(height.getText().toString()));
        patientDetails.setSex(sex);

        com.example.medicare.data.PatientDetailLines patientDetailLines = new com.example.medicare.data.PatientDetailLines();
        patientDetailLines.setJoinDate(new Date());
        patientDetailLines.setComments(comment.getText().toString());

        List<com.example.medicare.data.PatientDetailLines> patientDetailLinesList = new ArrayList<>();
        patientDetailLinesList.add(patientDetailLines);
        patientDetails.setPatientDetails(patientDetailLinesList);

        new com.example.medicare.db.WriteToDataBaseTask(getApplicationContext()).execute(patientDetails);
    }


}
