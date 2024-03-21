package com.example.music_app1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Playlist_Fragment extends Fragment {
    View view;
    ListView lvplaylist;
    TextView txttitlelaylist, txtxemplaylist
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_library_, container,false);
        lvplaylist = view.findViewById(R.id.listviewplaylist);
        txttitlelaylist = view.findViewById(R.id.textviewbaihatgoiy);
//        Getdata();
        return view;
    }

}

