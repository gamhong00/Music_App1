package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.music_app1.DataLocal.DataLocalManager;
import com.example.music_app1.Model.Playlist;
import com.example.music_app1.R;
//import com.example.music_app1.adapter.MusicAdapter;
//import com.example.music_app1.adapter.listMusicAdapter;
import com.example.music_app1.adapter.HistoryAdapter;
import com.example.music_app1.adapter.PlayListAdapter;
import com.example.music_app1.adapter.PlayListDialogAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;
import java.util.List;

public class Library_Fragment extends Fragment{

    private ImageButton imgbtn_search, add_playlist;
    private Button favorite;
    private RecyclerView recyclerviewPlaylist;
    List<Playlist> mListPlaylist = new ArrayList<>();


    private Button taixuong;
    private  RecyclerView recyclerView;

    public static HistoryAdapter historyAdapter;
    public static PlayListDialogAdapter playListDialogAdapter;
    public static PlayListAdapter playListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_library_, container, false);

        recyclerviewPlaylist = view.findViewById(R.id.recyclerviewPlaylist1);
        favorite = view.findViewById(R.id.favorite);
        add_playlist = view.findViewById(R.id.add_playlist);

        imgbtn_search = view.findViewById(R.id.search);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewPlaylist.setLayoutManager(linearLayoutManager);
        playListAdapter = new PlayListAdapter(mListPlaylist,getContext());
        playListDialogAdapter = new PlayListDialogAdapter(mListPlaylist, getContext());
        recyclerviewPlaylist.setAdapter(playListAdapter);
        callApiGetPlaylists();

        taixuong = view.findViewById(R.id.download);

        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(5, false);
            }
        });
        add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo một instance của PlaylistDialogFragment
                PlaylistDialogFragment playlistDialogFragment = new PlaylistDialogFragment();

                // Hiển thị dialog
                playlistDialogFragment.show(getParentFragmentManager(), "playlist_dialog");

            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager2.setCurrentItem(7, false);
            }
        });


        taixuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager2.setCurrentItem(8,false);
            }
        });

        recyclerView = view.findViewById(R.id.history_music);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        historyAdapter = new HistoryAdapter(DataLocalManager.getListMusic(), getContext());

        recyclerView.setAdapter(historyAdapter);

        return view;
    }

    public void callApiGetPlaylists() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("playlist");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = myRef.orderByChild("uid").equalTo(user.getUid());

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Playlist playlist = snapshot.getValue(Playlist.class);

                if (playlist != null) {
                    mListPlaylist.add(playlist);

                }
                playListAdapter.notifyDataSetChanged();
                playListDialogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Playlist playlist = snapshot.getValue(Playlist.class);

                if(mListPlaylist == null || mListPlaylist.isEmpty()){
                    return;
                }
                for (int i = 0; i< mListPlaylist.size(); i++){
                    if(playlist.getId() == mListPlaylist.get(i).getId()){
                        mListPlaylist.set(i, playlist);
                    }
                }
                playListAdapter.notifyDataSetChanged();
                playListDialogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Playlist playlist = snapshot.getValue(Playlist.class);

                if(mListPlaylist == null || mListPlaylist.isEmpty()){
                    return;
                }
                for (int i = 0; i< mListPlaylist.size(); i++){
                    if(playlist.getId() == mListPlaylist.get(i).getId()){
                        mListPlaylist.remove(i);
                    }
                }
                playListAdapter.notifyDataSetChanged();
                playListDialogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "message", Toast.LENGTH_SHORT).show();
            }
        });
    }



}