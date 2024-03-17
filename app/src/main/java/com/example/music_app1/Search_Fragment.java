package com.example.music_app1;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.MainActivity.temp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class Search_Fragment extends Fragment {


    private ImageButton imgbtn_quaylai;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        imgbtn_quaylai = view.findViewById(R.id.quaylai);
        imgbtn_quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(temp,false);
            }
        });
        return view;
    }

}