package com.example.music_app1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
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
        // Calculate the index of the first song
        int firstSongIndex = position * 2;

        // Bind data for the first song if available
        if (firstSongIndex < songs.size()) {
            Song firstSong = songs.get(firstSongIndex);
            holder.titleTextView1.setText(firstSong.getName());
            holder.artistTextView1.setText(firstSong.getArtist());
            Picasso.get().load(firstSong.getImage()).into(holder.imageView1);

            // Set click listener for the first song
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Convert Song object to JSON string
                    String songJson = new Gson().toJson(firstSong);

                    // Start MusicPlayerActivity and pass the JSON string
                    Intent intent = new Intent(v.getContext(), MusicPlayerActivity.class);
                    intent.putExtra("song", songJson);
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            // If no song available for the first slot, hide views
            holder.titleTextView1.setVisibility(View.GONE);
            holder.artistTextView1.setVisibility(View.GONE);
            holder.imageView1.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null); // Remove click listener
        }

        // Calculate the index of the second song
        int secondSongIndex = firstSongIndex + 1;

        // Bind data for the second song if available
        if (secondSongIndex < songs.size()) {
            Song secondSong = songs.get(secondSongIndex);
            holder.titleTextView2.setText(secondSong.getName());
            holder.artistTextView2.setText(secondSong.getArtist());
            Picasso.get().load(secondSong.getImage()).into(holder.imageView2);

            // Set click listener for the second song
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Convert Song object to JSON string
                    String songJson = new Gson().toJson(secondSong);

                    // Start MusicPlayerActivity and pass the JSON string
                    Intent intent = new Intent(v.getContext(), MusicPlayerActivity.class);
                    intent.putExtra("song", songJson);
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            // If no second song available, hide the corresponding views
            holder.titleTextView2.setVisibility(View.GONE);
            holder.artistTextView2.setVisibility(View.GONE);
            holder.imageView2.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null); // Remove click listener
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