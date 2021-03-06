package com.example.medicare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.medicare.data.PatientDetails;
import com.example.medicare.db.FirebaseDataOperations;
import com.example.medicare.db.GetDataListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeScanActivity extends AppCompatActivity {

    SurfaceView cameraPreview;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int requestCameraPermissionId = 1001;
    boolean alreadyScanned = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alreadyScanned = false;
        setContentView(R.layout.activity_barcode_scan);
        scanBarcode();
    }


    @Override
    protected void onResume() {
        super.onResume();
        alreadyScanned = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        alreadyScanned = false;
    }

    public void scanBarcode() {
        cameraPreview = findViewById(R.id.surfaceView);

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).setRequestedPreviewSize(640, 480).setFacing(CameraSource.CAMERA_FACING_BACK).build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BarcodeScanActivity.this, new String[]{Manifest.permission.CAMERA}, requestCameraPermissionId);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    Log.e("MediHelp", e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0 && !alreadyScanned) {
                    alreadyScanned = true;

                    String barcodeValue = qrCodes.valueAt(0).displayValue;
                    Log.d("MediHelp", barcodeValue);

                    renderNextScreen(barcodeValue);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case requestCameraPermissionId:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        Log.e("MediHelp", e.getMessage());
                    }
                }
                break;
        }
    }

    public void renderNextScreen(final String barcodeValue) {
        FirebaseDataOperations firebaseDataOperations = new FirebaseDataOperations();
        firebaseDataOperations.getPatientDetails(barcodeValue, new GetDataListener() {
            @Override
            public void onStart() {
                Log.d("Medicare", "Firebase Data Fetch Started");
            }

            @Override
            public void onSuccess(PatientDetails patientDetails) {
                Intent intent = new Intent(getApplicationContext(), ViewRecordDetailsActivity.class);
                intent.putExtra("policyNbr", patientDetails.getPolicyNbr());
                startActivity(intent);
            }

            @Override
            public void onFailure() {
                Intent intent = new Intent(getApplicationContext(), NewRecordActivity.class);
                intent.putExtra("policyNbr", barcodeValue);
                startActivity(intent);
            }
        });
    }
}

