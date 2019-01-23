package com.example.minko.mp3cutter.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.minko.mp3cutter.Adapter.VideoHistoryAdapter;
import com.example.minko.mp3cutter.Model.Video;
import com.example.minko.mp3cutter.R;
import com.example.minko.mp3cutter.Utils.SqliteUtils;
import com.example.minko.mp3cutter.videoTrimmer.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class VideoHistoryActivity extends AppCompatActivity implements VideoHistoryAdapter.ItemClickListener {
    int mPos;
    private VideoHistoryAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Video> data;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_history);
        data = new ArrayList<>();
        LayDuLieu();
        recyclerView = findViewById(R.id.rcvvideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new VideoHistoryAdapter(data, this);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private void LayDuLieu() {
        db = SqliteUtils.Docfiledb(SqliteUtils.DATABASE, this);
        Cursor datas = db.query(SqliteUtils.TABLE_VIDEO, null, null, null, null, null, null);
        datas.moveToFirst();
        try {
            do {
                Uri uri = Uri.parse(datas.getString(2));
                File file = new File(uri.getPath());
                if (file.canRead()) {
                    data.add(new Video(datas.getInt(0), datas.getString(1), datas.getString(2)));
                } else {
                    XoaFileRong(datas.getInt(0));
                }

            } while (datas.moveToNext());
        } catch (CursorIndexOutOfBoundsException e) {

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Uri uri = Uri.parse(data.get(position).getUri());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setDataAndType(uri, "video/*");

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_videopayer_found), Toast.LENGTH_SHORT).show();
        }

    }

    private void LongClick(final int posi) {
        final AlertDialog.Builder dialogsave = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialogsave.setMessage(getString(R.string.wantdelete));
        dialogsave.setNegativeButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse(data.get(posi).getUri());
                File file = new File(uri.getPath());
                if (file.delete()) {
                    Toast.makeText(VideoHistoryActivity.this, getString(R.string.done), Toast.LENGTH_SHORT).show();
                    FileUtils.addVideotoVideo(VideoHistoryActivity.this, file.getPath());
                    if (data.size() > 0) {
                        data.clear();
                        LayDuLieu();
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(VideoHistoryActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialogsave.setPositiveButton(getString(R.string.no), null);
        dialogsave.show();
    }

    public void XoaFileRong(Integer id) {
        db.execSQL("DELETE FROM " + SqliteUtils.TABLE_VIDEO + " WHERE Id = '" + id + "'");
    }

    public void onPopUpMenuClickListener(View v, final int position) {
        mPos = position;
        final PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_video_edit:

                        Uri uri = Uri.parse(data.get(position).getUri());
                        Intent intent = new Intent(VideoHistoryActivity.this, VideoTrimActivity.class);
                        intent.putExtra("video", uri.toString());
                        intent.putExtra("info", getMediaDuration(uri));
                        startActivity(intent);

                        break;
                    case R.id.popup_video_detele:
                        LongClick(position);
                        break;
                    case R.id.popup_video_share:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("video/*");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(data.get(position).getUri()));
                        startActivity(Intent.createChooser(share, getString(R.string.sharevs)));
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popupvideo);
        menu.show();
    }

    public int getMediaDuration(Uri uriOfFile) {
        MediaPlayer mp = MediaPlayer.create(this, uriOfFile);
        int duration = mp.getDuration();
        return duration;
    }
}
