package com.example.music_app1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MusicPlayerActivity extends AppCompatActivity {

    private TextView songTitleTextView;
    private ImageView musicIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_playmusic);

        // Ánh xạ các thành phần từ layout
        songTitleTextView = findViewById(R.id.song_title);
        musicIconImageView = findViewById(R.id.music_icon_big);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String songTitle = intent.getStringExtra("SONG_TITLE");
            // Hiển thị tiêu đề bài hát
            songTitleTextView.setText(songTitle);
            // Có thể hiển thị các thông tin khác của bài hát nếu cần
        }

        // Thiết lập sự kiện click cho tiêu đề bài hát
        songTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi tiêu đề bài hát được click
                playMusic();
            }
        });

        // Thiết lập sự kiện click cho hình ảnh bài hát
        musicIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi hình ảnh bài hát được click
                playMusic();
            }
        });

        // Các phần khác của logic và giao diện của trang phát nhạc...
    }

    // Phương thức để phát nhạc
    private void playMusic() {
        // Xử lý để phát nhạc và chuyển qua trang phát nhạc
    }
}
