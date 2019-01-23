package com.example.minko.mp3cutter.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.minko.mp3cutter.Adapter.AudioAdapter;
import com.example.minko.mp3cutter.R;
import com.example.minko.mp3cutter.RecyclerTouchListener;
import com.example.minko.mp3cutter.Model.Songs;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;
    private ArrayList<Songs> songsArrayList;
    private RecyclerView recyclerView;
    private AudioAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songsArrayList = new ArrayList<>();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            getArrSong();
        }


        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new AudioAdapter(songsArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Songs songs = songsArrayList.get(position);
                Intent intent = new Intent(getApplicationContext(), RingdroidSelectActivity.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(), songs.getmSongTitle(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getArrSong();
                }
                break;

            default:
                break;
        }
    }

    public ArrayList<Songs> getArrSong() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songSize = songCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);

            do {
                long currentId = songCursor.getLong(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentDuration = songCursor.getString(songDuration);
                String currentSize = songCursor.getString(songSize);
                songsArrayList.add(new Songs(currentId, currentTitle, currentArtist, currentDuration, currentSize));
            } while (songCursor.moveToNext());
        }

        return songsArrayList;
    }
}
