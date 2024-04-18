package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import com.example.music_app1.R;

public class Library_Fragment extends Fragment {

    private ImageButton imgbtn_search;
    private Button taixuong;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_library_, container, false);



        imgbtn_search = view.findViewById(R.id.search);
        taixuong = view.findViewById(R.id.download);

        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(5,false);
            }
        });

        taixuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager2.setCurrentItem(6,false);
            }
        });


        return view;
    }


}