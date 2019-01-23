package com.example.minko.mp3cutter.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.minko.mp3cutter.R;
import com.example.minko.mp3cutter.Model.Songs;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MyViewHolder> {

    private List<Songs> songsList;

    public AudioAdapter(List<Songs> songsList) {
        this.songsList = songsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Songs songs = songsList.get(position);
        holder.name.setText(songs.getmSongTitle());
        holder.size.setText(songs.getmSongSize());
        holder.time.setText(songs.getmSongDuration());
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, size, time;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.size);
            time = (TextView) view.findViewById(R.id.time);
        }
    }

}

