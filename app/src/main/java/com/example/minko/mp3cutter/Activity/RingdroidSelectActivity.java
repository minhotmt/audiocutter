/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.minko.mp3cutter.Activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.minko.mp3cutter.Adapter.CustomCursorAdapter;
import com.example.minko.mp3cutter.R;
import com.example.minko.mp3cutter.soundfile.SoundFile;

import java.util.ArrayList;

/**
 * Main screen that shows up when you launch Ringdroid. Handles selecting
 * an audio file or using an intent to record a new one, and then
 * launches RingdroidEditActivity from here.
 */
public class RingdroidSelectActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    // Result codes
    private static final int REQUEST_CODE_EDIT = 1;
    // Context menu
    private static final int CMD_EDIT = 4;
    private static final int CMD_DELETE = 5;
    private static final int CMD_SET_AS_DEFAULT = 6;
    private static final int CMD_SET_AS_CONTACT = 7;
    private static final String[] INTERNAL_COLUMNS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.IS_MUSIC,
            "\"" + MediaStore.Audio.Media.INTERNAL_CONTENT_URI + "\""
    };
    private static final String[] EXTERNAL_COLUMNS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.IS_MUSIC,
            "\"" + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "\""
    };
    private static final int INTERNAL_CURSOR_ID = 0;
    private static final int EXTERNAL_CURSOR_ID = 1;
    private SearchView mFilter;
    //    private SimpleCursorAdapter mAdapter;
    private CustomCursorAdapter mAdapter;
    private boolean mWasGetContentIntent;
    private boolean mShowAll;
    private Cursor mInternalCursor;
    private Cursor mExternalCursor;
    private ListView lvAudio;

    public RingdroidSelectActivity() {
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mShowAll = false;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            showFinalAlert(getResources().getText(R.string.sdcard_readonly));
            return;
        }
        if (status.equals(Environment.MEDIA_SHARED)) {
            showFinalAlert(getResources().getText(R.string.sdcard_shared));
            return;
        }
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            showFinalAlert(getResources().getText(R.string.no_sdcard));
            return;
        }

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.media_select);

        try {

            mAdapter = new CustomCursorAdapter(
                    this,
                    // Use a template that displays a text view
                    R.layout.media_row,
                    null,
                    // Map from database columns...
                    new String[]{
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.SIZE,
                            MediaStore.Audio.Media.DURATION,
                    },
                    // To widget ids in the row layout...
                    new int[]{
                            R.id.row_title,
                            R.id.row_size,
                            R.id.row_duration});

            lvAudio = findViewById(R.id.lv_audio);
            lvAudio.setAdapter(mAdapter);
            lvAudio.setItemsCanFocus(true);
            lvAudio.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startRingdroidEditor();
                }
            });

            mInternalCursor = null;
            mExternalCursor = null;
            getLoaderManager().initLoader(INTERNAL_CURSOR_ID, null, this);
            getLoaderManager().initLoader(EXTERNAL_CURSOR_ID, null, this);

        } catch (SecurityException e) {
            // No permission to retrieve audio?
            Log.e("Ringdroid", e.toString());

            // TODO error 1
        } catch (IllegalArgumentException e) {
            // No permission to retrieve audio?
            Log.e("Ringdroid", e.toString());

            // TODO error 2
        }
//        registerForContextMenu(getListView());

    }

    /**
     * Called with an Activity we started with an Intent returns.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent dataIntent) {
        if (requestCode != REQUEST_CODE_EDIT) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        setResult(RESULT_OK, dataIntent);
        //finish();  // TODO(nfaralli): why would we want to quit the app here?
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_option, menu);

//        mFilter = (SearchView) menu.findItem(R.id.action_search_filter).getActionView();
//        if (mFilter != null) {
//            mFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                public boolean onQueryTextChange(String newText) {
//                    refreshListView();
//                    return true;
//                }
//
//                public boolean onQueryTextSubmit(String query) {
//                    refreshListView();
//                    return true;
//                }
//            });
//        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Pick a file");
//        menu.findItem(R.id.action_about).setVisible(true);
//        menu.findItem(R.id.action_record).setVisible(true);
        // TODO(nfaralli): do we really need a "Show all audio" item now?
//        menu.findItem(R.id.action_show_all_audio).setVisible(true);
//        menu.findItem(R.id.action_show_all_audio).setEnabled(!mShowAll);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_about:
//                RingdroidEditActivity.onAbout(this);
//                return true;
//            case R.id.action_record:
//                onRecord();
//                return true;
//            case R.id.action_show_all_audio:
//                mShowAll = true;
//                refreshListView();
//                return true;
            default:
                return false;
        }
    }


    private void showFinalAlert(CharSequence message) {
        new AlertDialog.Builder(RingdroidSelectActivity.this)
                .setTitle(getResources().getText(R.string.alert_title_failure))
                .setMessage(message)
                .setPositiveButton(
                        R.string.alert_ok_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                finish();
                            }
                        })
                .setCancelable(false)
                .show();
    }

    private void startRingdroidEditor() {
        Cursor c = mAdapter.getCursor();
        int dataIndex = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        String filename = c.getString(dataIndex);
        try {
            Intent intent = new Intent(getApplicationContext(), RingdroidEditActivity.class);
            intent.putExtra("was_get_content_intent", mWasGetContentIntent);
            intent.putExtra("mFileName", Uri.parse(filename).toString());
            startActivity(intent);

        } catch (Exception e) {
            Log.e("Ringdroid", "Couldn't start editor");
        }
    }

    private void refreshListView() {
        mInternalCursor = null;
        mExternalCursor = null;
        Bundle args = new Bundle();
        args.putString("filter", mFilter.getQuery().toString());
        getLoaderManager().restartLoader(INTERNAL_CURSOR_ID, args, this);
        getLoaderManager().restartLoader(EXTERNAL_CURSOR_ID, args, this);
    }

    /* Implementation of LoaderCallbacks.onCreateLoader */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ArrayList<String> selectionArgsList = new ArrayList<String>();
        String selection;
        Uri baseUri;
        String[] projection;

        switch (id) {
            case INTERNAL_CURSOR_ID:
                baseUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
                projection = INTERNAL_COLUMNS;
                break;
            case EXTERNAL_CURSOR_ID:
                baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                projection = EXTERNAL_COLUMNS;
                break;
            default:
                return null;
        }

        if (mShowAll) {
            selection = "(_DATA LIKE ?)";
            selectionArgsList.add("%");
        } else {
            selection = "(";
            for (String extension : SoundFile.getSupportedExtensions()) {
                selectionArgsList.add("%." + extension);
                if (selection.length() > 1) {
                    selection += " OR ";
                }
                selection += "(_DATA LIKE ?)";
            }
            selection += ")";

            selection = "(" + selection + ") AND (_DATA NOT LIKE ?)";
            selectionArgsList.add("%espeak-data/scratch%");
        }

        String filter = args != null ? args.getString("filter") : null;
        if (filter != null && filter.length() > 0) {
            filter = "%" + filter + "%";
            selection =
                    "(" + selection + " AND " +
                            "((TITLE LIKE ?) OR (ARTIST LIKE ?) OR (ALBUM LIKE ?)))";
            selectionArgsList.add(filter);
            selectionArgsList.add(filter);
            selectionArgsList.add(filter);
        }

        String[] selectionArgs =
                selectionArgsList.toArray(new String[selectionArgsList.size()]);
        return new CursorLoader(
                this,
                baseUri,
                projection,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        );
    }

    /* Implementation of LoaderCallbacks.onLoadFinished */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case INTERNAL_CURSOR_ID:
                mInternalCursor = data;
                break;
            case EXTERNAL_CURSOR_ID:
                mExternalCursor = data;
                break;
            default:
                return;
        }
        // TODO: should I use a mutex/synchronized block here?
        if (mInternalCursor != null && mExternalCursor != null) {
            Cursor mergeCursor = new MergeCursor(new Cursor[]{mInternalCursor, mExternalCursor});
            mAdapter.swapCursor(mergeCursor);
        }
    }

    /* Implementation of LoaderCallbacks.onLoaderReset */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }
}
