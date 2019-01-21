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

public class CustomCursorAdapter extends SimpleCursorAdapter {

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

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView title = view.findViewById(R.id.tvName);
        TextView size = view.findViewById(R.id.tvSize);
        TextView time = view.findViewById(R.id.tvTime);

        int Title_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int Size_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
        int Duration_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

        title.setText(cursor.getString(Title_index));

        int sizeAudio = Integer.parseInt(cursor.getString(Size_index));
        int durationAudio = Integer.parseInt(cursor.getString(Duration_index));

        size.setText(cursor.getString(Size_index));
        time.setText(cursor.getString(Duration_index));


    }

}