package com.example.music_app1;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private List<Song> songs;

    public SongAdapter(List<Song> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        // Calculate index of the first song in the pair
        int index = position * 2;

        // Bind data for the first song
        if (index < songs.size()) {
            Song firstSong = songs.get(index);
            holder.titleTextView1.setText(firstSong.getName());
            holder.artistTextView1.setText(firstSong.getArtist());
            Picasso.get().load(firstSong.getImage()).into(holder.imageView1);
        } else {
            // If no song available for the first slot, hide views
            holder.titleTextView1.setVisibility(View.GONE);
            holder.artistTextView1.setVisibility(View.GONE);
            holder.imageView1.setVisibility(View.GONE);
        }

        // Bind data for the second song
        index++;
        if (index < songs.size()) {
            Song secondSong = songs.get(index);
            holder.titleTextView2.setText(secondSong.getName());
            holder.artistTextView2.setText(secondSong.getArtist());
            Picasso.get().load(secondSong.getImage()).into(holder.imageView2);
        } else {
            // If no song available for the second slot, hide views
            holder.titleTextView2.setVisibility(View.GONE);
            holder.artistTextView2.setVisibility(View.GONE);
            holder.imageView2.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) songs.size() / 2);
    }


    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView1, titleTextView2, artistTextView1, artistTextView2;
        ImageView imageView1, imageView2;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView1 = itemView.findViewById(R.id.titleTextView1);
            titleTextView2 = itemView.findViewById(R.id.titleTextView2);
            artistTextView1 = itemView.findViewById(R.id.artistTextView1);
            artistTextView2 = itemView.findViewById(R.id.artistTextView2);
            imageView1 = itemView.findViewById(R.id.imageView1);
            imageView2 = itemView.findViewById(R.id.imageView2);
        }
    }
}