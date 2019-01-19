package com.example.minko.mp3cutter.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.minko.mp3cutter.R;
import com.example.minko.mp3cutter.WaveformView;

public class EditActivity extends AppCompatActivity {

    private WaveformView waveformView;
    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        waveformView = findViewById(R.id.waveform);
        tvTest = findViewById(R.id.tvTest);

    }

}
