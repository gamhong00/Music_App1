package com.example.music_app1.View;

import static com.example.music_app1.View.PlayMusic_Fragment.totalTime;
import static com.example.music_app1.adapter.MusicAdapter.mediaPlayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicDownloadAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Download_Fragment extends Fragment {
    private RecyclerView rcv;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private List<String> mp3Files = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_, container, false);


        // Tạo danh sách các tệp nhạc từ thư mục "Music"
        File musicDirectory = new File(Environment.getExternalStorageDirectory(), "Music");
        File[] musicFiles = musicDirectory.listFiles();
        List<File> musicList = new ArrayList<>();
        if (musicFiles != null) {
            musicList.addAll(Arrays.asList(musicFiles));
        }
        MusicDownloadAdapter adapter1 = new MusicDownloadAdapter(musicList);
        // Hiển thị danh sách nhạc lên ListView
        rcv = view.findViewById(R.id.list_down);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(itemDecoration);
        rcv.setAdapter(adapter1);

        return view;
    }

}