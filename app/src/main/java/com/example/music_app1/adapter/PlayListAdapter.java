package com.example.music_app1.adapter;

import static com.example.music_app1.MainActivity.mViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Model.Music;
import com.example.music_app1.Model.Playlist;
import com.example.music_app1.R;
import com.example.music_app1.View.Playlist_Fragment;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlaylistViewHolder> {


    public PlayListAdapter(List<Playlist> mListPlaylist, Context context) {
        this.context = context;
        this.mListPlaylist = mListPlaylist;

    }

    private List<Playlist> mListPlaylist;
    private Context context;

    @NonNull
    @Override
    public PlayListAdapter.PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = mListPlaylist.get(position);
        if (playlist == null) {
            return;
        }
        holder.playlist_name.setText(playlist.getName());
        if (playlist.getMusic() == null){
            holder.img_playlist.setImageResource(R.drawable.playlist);
        }else {
            Picasso.get().load(playlist.getMusic().get(0).getImage()).into(holder.img_playlist);
        }
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Playlist_Fragment.musicListPlaylist = playlist.getMusic();
                if(Playlist_Fragment.musicAdapter != null){
                    Playlist_Fragment.musicAdapter.setMusicList(playlist.getMusic());
                    Playlist_Fragment.musicAdapter.notifyDataSetChanged();
                    Picasso.get().load(playlist.getMusic().get(0).getImage()).into(Playlist_Fragment.imgMusic);
                }
                mViewPager2.setCurrentItem(6, false);
            }

        });
    }

    @Override
    public int getItemCount() {
        if (mListPlaylist != null) {
            return mListPlaylist.size();
        }
        return 0;
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private final TextView playlist_name;
        private final LinearLayout play;
        private final ImageView img_playlist;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlist_name = itemView.findViewById(R.id.playlist_name);
            img_playlist = itemView.findViewById(R.id.img_playlist);
            play = itemView.findViewById(R.id.play);
        }
    }
}

