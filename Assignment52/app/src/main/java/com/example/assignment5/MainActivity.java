package com.example.assignment5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity{

    TextView tv;
    List<Face> fac;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button faceButton = findViewById(R.id.facesButton);

        tv = findViewById(R.id.textView);
        InputStream stream = getResources().openRawResource(R.raw.image02);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        //faceButton.setOnClickListener(this);


        FaceDetector detector = FaceDetection.getClient();

        Task<List<Face>> result = detector.process(image).addOnSuccessListener(
                new OnSuccessListener<List<Face>>(){
                    @Override
                    public void onSuccess(List<Face> faces){

                        fac = faces;
                        if(fac == null) {
                            Log.v("**draw***", "main2 FAXW is null");
                        }
                        FaceView overlay = (FaceView) findViewById(R.id.faceView);
                        overlay.setContent(bitmap, fac);
                        tv.setText(faces.size() + "Faces Seen");
                    }
                }).addOnFailureListener(
                        new OnFailureListener(){
                            @Override
                            public void onFailure(@NonNull Exception e){

                            }
                        }
        );

        //Toast.makeText(this, "There are " +fac+ " faces", Toast.LENGTH_LONG).show();
    }
}