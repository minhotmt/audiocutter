package com.example.minko.mp3cutter.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.minko.mp3cutter.R;
import com.example.minko.mp3cutter.Utils.SqliteUtils;
import com.example.minko.mp3cutter.videoTrimmer.HgLVideoTrimmer;
import com.example.minko.mp3cutter.videoTrimmer.interfaces.OnHgLVideoListener;
import com.example.minko.mp3cutter.videoTrimmer.interfaces.OnTrimVideoListener;
import com.example.minko.mp3cutter.videoTrimmer.utils.FileUtils;

public class VideoTrimActivity extends AppCompatActivity implements OnTrimVideoListener, OnHgLVideoListener {

    private HgLVideoTrimmer mVideoTrimmer;
    private ProgressDialog mProgressDialog;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_trim);
        getSupportActionBar().hide();
        Intent extraIntent = getIntent();
        String path = "";
        int maxDuration = 10;

        if (extraIntent != null) {
            path = extraIntent.getStringExtra("video");
            maxDuration = extraIntent.getIntExtra("info", 10);
        }

        //setting progressbar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.trimming_progress));

        mVideoTrimmer = findViewById(R.id.timeLine);
        if (mVideoTrimmer != null) {


            /**
             * get total duration of video file
             */
            Log.e("tg", "maxDuration = " + maxDuration);
            //mVideoTrimmer.setMaxDuration(maxDuration);
            mVideoTrimmer.setMaxDuration(maxDuration);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setOnHgLVideoListener(this);
            //mVideoTrimmer.setDestinationPath("/storage/emulated/0/DCIM/CameraCustom/");
            mVideoTrimmer.setVideoURI(Uri.parse(path));
            mVideoTrimmer.setVideoInformationVisibility(true);
        }

        db = SqliteUtils.Docfiledb(SqliteUtils.DATABASE, this);


    }

    @Override
    public void onVideoPrepared() {

    }

    @Override
    public void onTrimStarted() {
        mProgressDialog.show();
    }

    @Override
    public void getResult(final Uri uri) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoTrimActivity.this, getString(R.string.video_saved_at, uri.getPath()), Toast.LENGTH_LONG).show();
            }
        });

        try {

            ContentValues values = new ContentValues();
            values.put(SqliteUtils.F_URI, uri.toString());
            values.put(SqliteUtils.F_NAME, uri.toString());
            db.insert(SqliteUtils.TABLE_VIDEO, null, values);

            FileUtils.addVideotoVideo(this, uri.getPath());
//            startActivity(new Intent(VideoTrimActivity.this, VideoHistoryActivity.class));
            finish();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void cancelAction() {
        mProgressDialog.cancel();
        mVideoTrimmer.destroy();
        finish();
    }

    @Override
    public void onError(final String message) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoTrimActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(VideoTrimActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


}
