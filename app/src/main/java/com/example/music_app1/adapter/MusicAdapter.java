package com.example.music_app1.adapter;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    public MusicAdapter(List<Music> mListMusic) {
        this.mListMusic = mListMusic;
    }

    private final List<Music> mListMusic;
    private MediaPlayer mediaPlayer;

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = mListMusic.get(position);
        if (music == null){
            return;
        }
        holder.tvname.setText(String.valueOf(music.getName()));
        holder.tvartist.setText(String.valueOf((music.getArtist())));
        Picasso.get().load(music.getImage()).into(holder.imgMusic);
        holder.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(music.getLink());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListMusic != null){
            return mListMusic.size();
        }
        return 0;
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvname, tvartist;
        private final ImageView imgMusic;

        private final LinearLayout btnplay;

        public MusicViewHolder(@NonNull View itemView){
            super(itemView);
            tvname = itemView.findViewById(R.id.tv_name);
            tvartist = itemView.findViewById(R.id.tv_artist);
            imgMusic = itemView.findViewById(R.id.img_music);
            btnplay = itemView.findViewById(R.id.play);
        }
    }
    private void playSound(String link) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        try {
            mediaPlayer.setDataSource(link);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
