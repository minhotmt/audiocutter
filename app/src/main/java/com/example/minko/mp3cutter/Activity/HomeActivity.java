package com.example.minko.mp3cutter.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.example.minko.mp3cutter.R;

public class HomeActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;
    private CardView btnCut, btnMerge, btnConvert, btnCutVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        btnCut = findViewById(R.id.btn_cut);
        btnCutVideo = findViewById(R.id.btn_cut_video);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        }

        btnCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RingdroidSelectActivity.class);
                startActivity(intent);
            }
        });
        btnCutVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoSelectActivity.class);
                startActivity(intent);
            }
        });


    }
}
