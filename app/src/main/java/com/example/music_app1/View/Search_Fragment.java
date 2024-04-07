package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.MainActivity.temp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_Fragment extends Fragment {
    private ImageButton imgbtn_quaylai;
    private EditText edt_search;

    private   RecyclerView rcvMusic;

    private   MusicAdapter  mMusicAdapter;
     List<Music> mListMusic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rcvMusic = view.findViewById(R.id.rcv_musics);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvMusic.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcvMusic.addItemDecoration(itemDecoration);
        mListMusic = new ArrayList<>();

        mMusicAdapter = new MusicAdapter(mListMusic);
        rcvMusic.setAdapter(mMusicAdapter);
        callApiGetMusics("");
        imgbtn_quaylai = view.findViewById(R.id.quaylai);
        imgbtn_quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(temp,false);
            }
        });


        edt_search = view.findViewById(R.id.edt_search);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString().toLowerCase());
            }
        });

        return view;
    }
    private void filter(String keyword) {
        List<Music> filteredList = new ArrayList<>();
        for (Music music : mListMusic) {
            if (music.getName().toLowerCase().contains(keyword)) {
                filteredList.add(music);
            }
        }
        mMusicAdapter.setData(filteredList);
    }

    public void callApiGetMusics(String keyword){

        mListMusic.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Music music = dataSnapshot.getValue(Music.class);
                    if (music != null) {
                        mListMusic.add(music);
                    }
                }
                mMusicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "message", Toast.LENGTH_SHORT).show();
            }
        });

    }

}