package com.example.music_app1.View;

import static com.example.music_app1.View.PlayMusic_Fragment.totalTime;
import static com.example.music_app1.adapter.MusicAdapter.mediaPlayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.music_app1.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Download_Fragment extends Fragment {
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_, container, false);


        // Tạo danh sách các tệp nhạc từ thư mục "Music"
        File musicDirectory = new File(Environment.getExternalStorageDirectory(), "Music");
        File[] musicFiles = musicDirectory.listFiles();
        List<String> musicList = new ArrayList<>();
        if (musicFiles != null) {
            for (File file : musicFiles) {
                musicList.add(file.getName());
            }
        }

        // Hiển thị danh sách nhạc lên ListView
        ListView listView = view.findViewById(R.id.list_down);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, musicList);
        listView.setAdapter(adapter);

        listView = view.findViewById(R.id.list_down);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy tên bài hát đã chọn
                String selectedMusic = musicList.get(position);

                // Xác định đường dẫn tới bài hát trong thư mục "Music"
                File selectedMusicFile = new File(musicDirectory, selectedMusic);
                Uri musicUri = Uri.fromFile(selectedMusicFile);

                // Phát bài hát
                playMusic(musicUri);
            }
        });

        return view;
    }
    private void playMusic(Uri musicUri) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getContext(), musicUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}