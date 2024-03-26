package com.example.music_app1.View;

import static com.example.music_app1.adapter.MusicAdapter.mediaPlayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music_app1.R;


public class PlayMusic_Fragment extends Fragment {

    private ImageButton PlayPause;
    public static ImageView imgMusic;
    public static TextView nameMusic;
    public   static TextView nameArtist;

    public static SeekBar seekBar;
    public  static LinearLayout pageplaymusic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_music_, container, false);

        imgMusic = view.findViewById(R.id.img_music);

        nameMusic = view.findViewById(R.id.name_music);

        nameArtist = view.findViewById(R.id.name_artist);

        seekBar = view.findViewById(R.id.seekbar_music);
        pageplaymusic = view.findViewById(R.id.pageplaymusic);
        PlayPause = view.findViewById(R.id.playpause);
        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    PlayPause.setImageResource(R.drawable.circle_play_regular);
                }
                else{
                    mediaPlayer.start();
                    PlayPause.setImageResource(R.drawable.circle_pause_regular);
                }

            }
        });



        return view;
    }
}