package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewRecordDetailsActivity extends AppCompatActivity {

    com.example.medicare.db.FirebaseDataOperations firebaseDataOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record_details);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String policyNbr = getIntent().getStringExtra("policyNbr");
       // policyNbr = "9611317679";
        Log.d("MediCare", policyNbr);

        firebaseDataOperations = new com.example.medicare.db.FirebaseDataOperations();

        getDataFromFirebase(policyNbr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected void getDataFromFirebase(String policyNbr) {
        firebaseDataOperations.getPatientDetails(policyNbr, new com.example.medicare.db.GetDataListener() {
            @Override
            public void onStart() {
                Log.d("Medicare", "Firebase Data Fetch Started");
            }

            @Override
            public void onSuccess(com.example.medicare.data.PatientDetails patientDetails) {
                showDataOnUI(patientDetails);
            }

            @Override
            public void onFailure() {
                Log.e("MediCare", "Error fetching the data");
            }
        });
    }

    protected void showDataOnUI(final com.example.medicare.data.PatientDetails patientDetails) {
        Log.d("Medicare", "yes");

        Button viewHistory = findViewById(R.id.viewHistory);
        viewHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewPatientHistory.class);
                intent.putExtra("patientHistory", patientDetails);
                startActivity(intent);
            }
        });
        Button uploadImg = findViewById(R.id.uploadImg);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), uploadImg.class);
              //  intent1.putExtra("patientHistory", patientDetails);
                startActivity(intent1);
            }
        });
        Button addComment = findViewById(R.id.addComment);
        //Button uploadImg = findViewById(R.id.uploadImg);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView policyTextView = findViewById(R.id.policyNbr);
                EditText comment = findViewById(R.id.comment);
                List<com.example.medicare.data.PatientDetailLines> patientDetailLinesList =  patientDetails.getPatientDetails();
                if(patientDetailLinesList == null || patientDetailLinesList.isEmpty()) {
                    patientDetailLinesList = new ArrayList<>();
                }
                com.example.medicare.data.PatientDetailLines patientDetailLines = new com.example.medicare.data.PatientDetailLines();
                patientDetailLines.setComments(comment.getText().toString());
                patientDetailLines.setJoinDate(new Date());
                patientDetailLinesList.add(patientDetailLines);
                patientDetails.setPatientDetails(patientDetailLinesList);
                firebaseDataOperations.updateRecord(patientDetails);

                Toast.makeText(getBaseContext(), "Comment added successfully!!", Toast.LENGTH_LONG).show();
                comment.setText("");
            }
        });

        TextView policyTextView = findViewById(R.id.policyNbr);
        policyTextView.setText(patientDetails.getPolicyNbr());

        TextView firstName = findViewById(R.id.firstNameText);
        firstName.setText(patientDetails.getFirstName());

        TextView lastName = findViewById(R.id.lastNameText);
        lastName.setText(patientDetails.getLastName());

        TextView age = findViewById(R.id.ageText);
        age.setText(patientDetails.getAge().toString());

        TextView height = findViewById(R.id.heightText);
        height.setText(patientDetails.getHeight().toString());

        TextView weight = findViewById(R.id.weightText);
        weight.setText(patientDetails.getWeight().toString());

        TextView sex = findViewById(R.id.sextText);
        sex.setText(patientDetails.getSex());
    }

}
