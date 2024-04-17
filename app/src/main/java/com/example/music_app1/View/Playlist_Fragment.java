package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.MainActivity.temp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Model.Music;
import com.example.music_app1.Model.Playlist;
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Playlist_Fragment extends Fragment {

    private ImageButton imgbtnBack;
    private ImageButton imgbtnMore;
    public static ImageView imgMusic;
    public static TextView textPlaylist;
    public static TextView textUser;
    public static TextView textSobaihat;
    public static TextView textSophut;
    public ImageButton imgbtnDown;
    public Button btnPhatngaunhien;
    public ImageButton imgbtnPlus;

    public static Button btnThembai;

    public  static LinearLayout pageplaylist;
    private RecyclerView rcvPlaylist;

    private RecyclerView rcvPlaylistgoiy;
    private MusicAdapter mMusicAdapter;
    List<Music> mListMusic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        rcvPlaylist = view.findViewById(R.id.recyclerviewBaihatthinhhanh);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvPlaylist.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcvPlaylist.addItemDecoration(itemDecoration);
        mListMusic = new ArrayList<>();

        mMusicAdapter = new MusicAdapter(mListMusic);
        rcvPlaylist.setAdapter(mMusicAdapter);
        callApiGetBaihatthinhhanh();


        imgbtnBack = view.findViewById(R.id.imagebntBack);
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager2.setCurrentItem(temp,false);
            }
        });
        imgbtnMore = view.findViewById(R.id.imagebntMore);
        imgbtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiển thị dialog tùy biến, chưa làm dialog nên chưa bỏ dô

            }
        });
        imgMusic = view.findViewById(R.id.imageMusic);
        Animation rotation = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);
        textPlaylist = view.findViewById(R.id.textPlaylist);

        textUser = view.findViewById(R.id.textUser);

        textSobaihat = view.findViewById(R.id.textSobaihat);

        textSophut = view.findViewById(R.id.textSophut);

        imgbtnDown = view.findViewById(R.id.imagebntDown);


        btnPhatngaunhien = view.findViewById(R.id.bntPhatngaunhien);

        imgbtnPlus = view.findViewById(R.id.imagebntPlus);


        return view;
    }
    public void callApiGetBaihatthinhhanh(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("playlist/Music");
        Log.d("tag",myRef.toString());
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if(music != null){
                    mListMusic.add(music);
                    mMusicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

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
    public void callApiGetPlaylist(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music");
        Log.d("tag",myRef.toString());
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if(music != null){
                    mListMusic.add(music);
                    mMusicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

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
