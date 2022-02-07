package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

    }
    public void meth1(View view)
    {
        startActivity(new Intent(getApplicationContext(),BarcodeScanActivity.class));
    }
    public void meth2(View view)
    {
        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
    }
}
