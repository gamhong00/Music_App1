package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import androidx.viewpager.widget.ViewPager;

import com.example.music_app1.Model.Music;
import com.example.music_app1.Model.Slide;
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;
import com.example.music_app1.adapter.SlideAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class Explore_Fragment extends Fragment {


    private ImageButton imgbtn_search;
    private RecyclerView rcvMusic;

    private   MusicAdapter  mMusicAdapter;
    List<Music> mListMusic;

    private ViewPager viewPagerSlide;
    private CircleIndicator circleIndicator;
    private SlideAdapter slideAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_, container, false);

        imgbtn_search = view.findViewById(R.id.search);
        rcvMusic = view.findViewById(R.id.rcv_musics);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvMusic.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvMusic.addItemDecoration(itemDecoration);
        mListMusic = new ArrayList<>();

        mMusicAdapter = new MusicAdapter(mListMusic, getContext());
        rcvMusic.setAdapter(mMusicAdapter);
        callApiGetMusics("");

        viewPagerSlide = view.findViewById(R.id.view_pager_slide);
        circleIndicator = view.findViewById(R.id.circle_indicator);
        slideAdapter = new SlideAdapter(getContext(),getListSlide());
        viewPagerSlide.setAdapter(slideAdapter);

        circleIndicator.setViewPager(viewPagerSlide);
        slideAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(5,false);
            }
        });
        return view;
    }
    public void callApiGetMusics(String keyword){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if(music != null){
                    if(music.getName().toLowerCase().contains(keyword)){
                        mListMusic.add(music);
                        mMusicAdapter.notifyDataSetChanged();
                    }
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
    private  List<Slide> getListSlide() {
        List<Slide> slideList = new ArrayList<>();
        slideList.add(new Slide(R.drawable.slide1));
        slideList.add(new Slide(R.drawable.slide2));
        slideList.add(new Slide(R.drawable.slide3));
        return  slideList;
    }
}