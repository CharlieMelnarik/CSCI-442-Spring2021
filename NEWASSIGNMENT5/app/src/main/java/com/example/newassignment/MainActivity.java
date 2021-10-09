package com.example.newassignment;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.face.Face;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.ImageProxy;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import java.io.InputStream;


import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    TextView tv;
    List<Face> fac;

    private Talk tts;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.Start_Button);
        startButton.setOnClickListener(this);


        tts = Talk.getInstance(this);
        tts.start();

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Start_Button:

                tv = findViewById(R.id.TextView);
                InputStream stream = getResources().openRawResource(R.raw.download);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                InputImage image = InputImage.fromBitmap(bitmap, 0);


                FaceDetector detector = FaceDetection.getClient();

                Task<List<Face>> result = detector.process(image).addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {

                                fac = faces;
                                if (fac == null) {
                                    Log.v("**draw***", "main2 FAXW is null");
                                }
                                FaceView overlay = (FaceView) findViewById(R.id.faceView);
                                overlay.setContent(bitmap, fac);
                                if (faces.size() == 1) {
                                    tv.setText(faces.size() + " face seen");
                                } else {
                                    tv.setText(faces.size() + " faces seen");
                                }

                                tts.run((String) tv.getText());

                            }
                        }).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }
                );
        }
    }
}
