package com.example.music_app1.View;



import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_app1.Model.Music;
import com.example.music_app1.Model.Playlist;
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;
import com.example.music_app1.adapter.PlayListAdapter;

import java.util.ArrayList;
import java.util.List;


public class Playlist_Fragment extends Fragment  {
    public static MusicAdapter musicAdapter;
    public static List<Music> musicListPlaylist;
    RecyclerView recyclerviewPlaylist;
    public Playlist_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_playlist_, container, false);
        recyclerviewPlaylist = view.findViewById(R.id.recyclerviewPlaylist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewPlaylist.setLayoutManager(linearLayoutManager);
        musicAdapter = new MusicAdapter(musicListPlaylist, getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerviewPlaylist.addItemDecoration(itemDecoration);

        // Inflate the layout for this fragment
        recyclerviewPlaylist.setAdapter(musicAdapter);
        return view;

    }
}