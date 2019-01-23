package com.example.minko.mp3cutter.Adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.minko.mp3cutter.Activity.VideoHistoryActivity;
import com.example.minko.mp3cutter.Model.Video;
import com.example.minko.mp3cutter.R;
import com.example.minko.mp3cutter.Ringdroid.Utils;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by HP 6300 Pro on 5/8/2018.
 */

public class VideoHistoryAdapter extends RecyclerView.Adapter<VideoHistoryAdapter.ViewHolder> {

    private ArrayList<Video> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private VideoHistoryActivity context;


    public VideoHistoryAdapter(ArrayList<Video> list, VideoHistoryActivity context) {
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public VideoHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_videos_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHistoryAdapter.ViewHolder holder, int position) {
        Uri uri = Uri.parse(list.get(position).getUri());
        String path = uri.getPath();
        File file = new File(path);
        Glide.with(context).load(path)
                .skipMemoryCache(true)
                .dontAnimate()
                .placeholder(R.drawable.default_art_video)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.thumb);
        holder.name.setText(file.getName());
        holder.duration.setText(Utils.makeShortTimeString(context.getApplicationContext(), context.getMediaDuration(uri) / 1000));
        Float s = ((Float.valueOf(file.length()) / (1024 * 1024)) * 100);
        s = Float.valueOf(s.intValue()) / 100;
        holder.size.setText(s + " MB");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView thumb;
        private TextView name, size, duration;
        private ImageView mPopUpMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            name = itemView.findViewById(R.id.name);
            size = itemView.findViewById(R.id.size);
            mPopUpMenu = itemView.findViewById(R.id.overflow);
            duration = itemView.findViewById(R.id.video_duration);

            mPopUpMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.onPopUpMenuClickListener(view, getAdapterPosition());
                }
            });
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            mPopUpMenu.performClick();
            return true;
        }
    }
}
