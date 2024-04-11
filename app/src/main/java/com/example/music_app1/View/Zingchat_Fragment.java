package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;
import com.example.music_app1.adapter.RankAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Zingchat_Fragment extends Fragment {

    private ImageButton imgbtn_search;
    private RecyclerView rcvMusic, recyclerView;
    private RankAdapter mRankAdapter;
    List<Music> mListMusic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zingchat_, container, false);
        mListMusic = new ArrayList<>();

        imgbtn_search = view.findViewById(R.id.search);
        recyclerView = view.findViewById(R.id.rcv_number_rank);
        List<Integer> dataList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            dataList.add(i);
        }
        mRankAdapter = new RankAdapter(dataList, mListMusic);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecoration1 = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration1);
        recyclerView.setAdapter(mRankAdapter);
        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(5,false);
            }
        });


        callApiGetMusics("");
        return view;
    }
    public void callApiGetMusics(String keyword){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music");
        Query query = myRef.orderByChild("listens").limitToLast(10);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if(music != null){
                    mListMusic.add(music);
                    Collections.sort(mListMusic, new Comparator<Music>() {
                        @Override
                        public int compare(Music m1, Music m2) {
                            return Integer.compare(m2.getListens(), m1.getListens());
                        }
                    });
                    mRankAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if(music != null || mListMusic == null || mListMusic.isEmpty()){
                    return;
                }

                for (int i = 0; i< mListMusic.size(); i++){
                    if(music.getId() == mListMusic.get(i).getId()){
                        mListMusic.set(i, music);
                    }
                }
                Collections.sort(mListMusic, new Comparator<Music>() {
                    @Override
                    public int compare(Music m1, Music m2) {
                        return Integer.compare(m2.getListens(), m1.getListens());
                    }
                });
                mRankAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}