package com.example.minko.mp3cutter.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.minko.mp3cutter.R;
import com.example.minko.mp3cutter.Model.Songs;

import java.io.IOException;
import java.util.Locale;

public class CuttedActivity extends AppCompatActivity {

    private TextView tvTitle;
    private Songs songs;
    private MediaPlayer mPlayer = new MediaPlayer();
    private SeekBar seekBar;
    private Button btnSetAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutted);

        tvTitle = findViewById(R.id.tvTitle);
        seekBar = findViewById(R.id.seekBar);
        btnSetAs = findViewById(R.id.btnSetAs);
        String uriAudio = getIntent().getStringExtra("Uri");

        try {
            mPlayer.setDataSource(uriAudio);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.start();
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(mPlayer.getDuration());
        new CountDownTimer(mPlayer.getDuration(), 250) {
            public void onTick(long millisUntilFinished) {
                seekBar.setProgress(seekBar.getProgress() + 250);
            }
            public void onFinish() {
            }
        }.start();

        btnSetAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_set_as);
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cutted_option, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_share).setVisible(true);
        menu.findItem(R.id.action_home).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                share();
                return true;
            case R.id.action_home:
                Intent mainIntent = new Intent(this, HomeActivity.class);
                this.startActivity(mainIntent);
                return true;
            default:
                return false;
        }
    }

    private void share() {

    }


}
