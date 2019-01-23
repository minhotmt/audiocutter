package com.example.minko.mp3cutter.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.minko.mp3cutter.Adapter.VideosAdapter;
import com.example.minko.mp3cutter.Model.Video;
import com.example.minko.mp3cutter.R;
import com.example.minko.mp3cutter.videoTrimmer.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class VideoSelectActivity extends AppCompatActivity implements VideosAdapter.ItemClickListener {
    int mPos;
    Uri uri;
    private VideosAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Video> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_select);

        data = new ArrayList<>();
        data = getAllMedia();
        recyclerView = findViewById(R.id.rcvvideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new VideosAdapter(data, this);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        startTrimActivity(Uri.fromFile(new File(data.get(position).getUri())));
    }

    public ArrayList<Video> getAllMedia() {
        ArrayList<Video> vd = new ArrayList<>();
        String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        try {
            cursor.moveToFirst();
            do {
                vd.add(new Video(cursor.getPosition(), cursor.getString(1), cursor.getString(0)));
            } while (cursor.moveToNext());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vd;
    }

    public void onPopUpMenuClickListener(View view, int position) {
        File file = new File(data.get(position).getUri());
        uri = Uri.fromFile(file);
        mPos = position;
        final PopupMenu menu = new PopupMenu(this, view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_video_edit:
                        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
                        startTrimActivity(uri);
                        break;
                    case R.id.popup_video_share:
//                        Intent share = new Intent(Intent.ACTION_SEND);
//                        share.setType("video/*");
//                        share.putExtra(Intent.EXTRA_STREAM, uri);
//                        startActivity(Intent.createChooser(share, getString(R.string.sharevs)));
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popupvideolocal);
        menu.show();

    }

    public int getMediaDuration(Uri uriOfFile) {
        MediaPlayer mp = MediaPlayer.create(this, uriOfFile);
        int duration;
        try {
            duration = mp.getDuration();
        } catch (NullPointerException e) {
            duration = 0;
        }

        return duration;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.edit_options, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Pick a video");
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void startTrimActivity(@NonNull final Uri uri) {
        Intent intent = new Intent(getApplicationContext(), VideoTrimActivity.class);
        intent.putExtra("video", FileUtils.getPath(getApplicationContext(), uri));
        intent.putExtra("info", getMediaDuration(uri));
        startActivity(intent);

    }

}
