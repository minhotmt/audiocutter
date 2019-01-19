package com.example.minko.mp3cutter.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.minko.mp3cutter.R;
import com.example.minko.mp3cutter.Songs;

import java.io.IOException;

public class CuttedActivity extends AppCompatActivity {

    private TextView tvTitle;
    private Songs songs;
    private ProgressBar progressBar;
    private MediaPlayer mPlayer = new MediaPlayer();
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutted);

        tvTitle = findViewById(R.id.tvTitle);
        seekBar = findViewById(R.id.seekBar);
        String a = getIntent().getStringExtra("Uri");

        try {
            mPlayer.setDataSource(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.start();
        final SeekBar progress = (SeekBar) findViewById(R.id.seekBar);
        progress.setMax(mPlayer.getDuration());
        new CountDownTimer(mPlayer.getDuration(), 250) {
            public void onTick(long millisUntilFinished) {
                progress.setProgress(progress.getProgress() + 250);
            }

            public void onFinish() {
            }
        }.start();
//        mPlayer.start();

    }

}
