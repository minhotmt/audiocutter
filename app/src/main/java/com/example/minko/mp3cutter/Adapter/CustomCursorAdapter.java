package com.example.minko.mp3cutter.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.minko.mp3cutter.R;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class CustomCursorAdapter extends SimpleCursorAdapter {

    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;
    private final LayoutInflater inflater;
    private Context mContext;
    private Context appContext;
    private int layout;
    private Cursor cr;

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.layout = layout;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.cr = c;
    }

    public String getFileSize(int size) {

        final double length = size;

        if (length > MiB) {
            return "Size: " + format.format(length / MiB) + " MiB -";
        }
        if (length > KiB) {
            return "Size: " + format.format(length / KiB) + " KiB -";
        }
        return "Size: " + format.format(length) + " B -";
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView title = view.findViewById(R.id.tvName);
        TextView size = view.findViewById(R.id.tvSize);
        TextView icTitle = view.findViewById(R.id.ic_title);

        int Title_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int Size_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
        int Duration_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

        icTitle.setText(String.valueOf(cursor.getString(Title_index).charAt(0)).toUpperCase());
        title.setText(cursor.getString(Title_index) + ".mp3");

        int sizeAudio = Integer.parseInt(cursor.getString(Size_index));
        int durationAudio = Integer.parseInt(cursor.getString(Duration_index));
        String test = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(durationAudio),
                TimeUnit.MILLISECONDS.toSeconds(durationAudio) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationAudio))
        );
        size.setText(getFileSize(sizeAudio) + " Time: " + test);
    }

}