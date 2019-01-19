package com.example.minko.mp3cutter.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minko.mp3cutter.R;

public class LoadingActivity extends AppCompatActivity {

    private Button btnAdd, btnCancel;
    private ProgressBar progressBar;
    private TextView tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        int value = 0;
        btnAdd = findViewById(R.id.btnAddProgress);
        btnCancel = findViewById(R.id.btnCancel);
        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tvProgress);
        progressBar.setProgress(value);
        tvProgress.setText(String.valueOf(progressBar.getProgress())+" %");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(progressBar.getProgress()+5);
                if (progressBar.getProgress() == 100){
                    Toast.makeText(getApplicationContext(), "Finished",Toast.LENGTH_LONG).show();
                }
                tvProgress.setText(String.valueOf(progressBar.getProgress())+" %");
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
