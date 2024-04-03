package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.R;

public class Playlist_Fragment extends Fragment {

    int currentItem = mViewPager2.getCurrentItem();
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
    public static RecyclerView recyclerviewPlaylist;
    public static RecyclerView recyclerviewBaihatthinhhanh;
    public static Button btnThembai;

    public  static LinearLayout pageplaylist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                imgbtnBack = view.findViewById(R.id.menu_library);
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

        recyclerviewPlaylist = view.findViewById(R.id.recyclerviewPlaylist);

        recyclerviewBaihatthinhhanh = view.findViewById(R.id.recyclerviewBaihatthinhhanh);

        return view;
    }
}
