package com.example.music_app1;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {

    private TextView songTitleTextView;
    private ImageView musicIconImageView;
    private MediaPlayer mediaPlayer;
    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmusic);

        // Ánh xạ các thành phần giao diện
        songTitleTextView = findViewById(R.id.song_title);
        musicIconImageView = findViewById(R.id.music_icon_big);

        // Lấy dữ liệu bài hát từ intent
        Intent intent = getIntent();
        if (intent != null) {
            String songJson = intent.getStringExtra("song");
            song = new Gson().fromJson(songJson, Song.class);
            if (song != null) {
                // Hiển thị thông tin bài hát
                displaySongInfo(song);
                // Phát nhạc
                String audioUrl = song.getAudioUrl();
                if (audioUrl != null) {
                    playMusicFromUrl(audioUrl);
                } else {
                    Log.e("MusicPlayerActivity", "AudioUrl is null");
                    // Nếu không có URL, bạn có thể phát tệp nhạc từ lưu trữ thiết bị
                    // Ví dụ: playMusicFromFile(song.getFilePath());
                }
            } else {
                Log.e("MusicPlayerActivity", "Song object is null");
                finish(); // Kết thúc activity nếu không có dữ liệu bài hát
            }
        } else {
            Log.e("MusicPlayerActivity", "Intent is null");
            finish(); // Kết thúc activity nếu intent null
        }
    }

    // Phương thức để hiển thị thông tin bài hát
    private void displaySongInfo(@NonNull Song song) {
        songTitleTextView.setText(song.getName());
        Picasso.get().load(song.getImage()).into(musicIconImageView);
    }

    // Phương thức để phát nhạc từ URL
    private void playMusicFromUrl(String audioUrl) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build());
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync(); // Prepare asynchronously
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start(); // Start playing when prepared
                }
            });
        } catch (IOException e) {
            Log.e("MusicPlayerActivity", "Error preparing media player: " + e.getMessage());
            finish(); // Kết thúc activity nếu có lỗi khi chuẩn bị phát nhạc
        }
    }
    // Đảm bảo giải phóng MediaPlayer khi không cần thiết
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
