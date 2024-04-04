package com.example.music_app1.View;

import static android.view.animation.AnimationUtils.loadAnimation;
import static com.example.music_app1.MainActivity.dpToPx;
import static com.example.music_app1.MainActivity.mBottomNavigationView;
import static com.example.music_app1.MainActivity.mViewPagerMusic;
import static com.example.music_app1.adapter.MusicAdapter.mediaPlayer;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.music_app1.R;



public class PlayMusic_Fragment extends Fragment {


    private RelativeLayout.LayoutParams layoutParams, params;
    public static ImageButton PlayPause;
    public static ImageButton PlayPause_;

    private ImageButton small_viewpage;
    public static ImageView imgMusic, imgMusic_;
    public static TextView nameMusic, nameMusic_;
    public   static TextView nameArtist, nameArtist_;
    public static SeekBar seekBar;
    public  static LinearLayout pageplaymusic, thanh_nhac;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play_music_, container, false);

        imgMusic = view.findViewById(R.id.img_music);
        imgMusic_ = view.findViewById(R.id.img_music_);
        Animation rotation = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);

        nameMusic = view.findViewById(R.id.name_music);
        nameMusic_ = view.findViewById(R.id.name_music_);
        nameArtist = view.findViewById(R.id.name_artist);
        nameArtist_ = view.findViewById(R.id.name_artist_);

        seekBar = view.findViewById(R.id.seekbar_music);
        pageplaymusic = view.findViewById(R.id.pageplaymusic);
        PlayPause = view.findViewById(R.id.playpause);
        PlayPause_ = view.findViewById(R.id.playpause_);

        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    PlayPause.setImageResource(R.drawable.circle_play_regular);
                    PlayPause_.setImageResource(R.drawable.play_solid);
                    Animation currentAnimation = imgMusic.getAnimation();
                }
                else{
                    mediaPlayer.start();
                    PlayPause.setImageResource(R.drawable.circle_pause_regular);
                    PlayPause_.setImageResource(R.drawable.pause_solid);
                    if (imgMusic.getAnimation() == null) {
                        // Lấy trạng thái góc quay từ tag của ImageView
                        Float savedRotation = (Float) imgMusic.getTag();
                        float rotation = savedRotation != null ? savedRotation : 0.0f;
                        // Khởi tạo animation quay với góc quay đã lưu
                        RotateAnimation rotationAnimation = new RotateAnimation(rotation, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotationAnimation.setDuration(20000);
                        rotationAnimation.setRepeatCount(Animation.INFINITE);
                        // Bắt đầu animation
                        imgMusic.startAnimation(rotationAnimation);
                    }
                }
            }
        });


        PlayPause_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    PlayPause_.setImageResource(R.drawable.play_solid);
                    PlayPause.setImageResource(R.drawable.circle_play_regular);
                    Animation currentAnimation = imgMusic_.getAnimation();
                }
                else{
                    mediaPlayer.start();
                    PlayPause_.setImageResource(R.drawable.pause_solid);
                    PlayPause.setImageResource(R.drawable.circle_pause_regular);
                    if (imgMusic_.getAnimation() == null) {
                        // Lấy trạng thái góc quay từ tag của ImageView
                        Float savedRotation = (Float) imgMusic_.getTag();
                        float rotation = savedRotation != null ? savedRotation : 0.0f;
                        // Khởi tạo animation quay với góc quay đã lưu
                        RotateAnimation rotationAnimation = new RotateAnimation(rotation, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotationAnimation.setDuration(20000);
                        rotationAnimation.setRepeatCount(Animation.INFINITE);
                        // Bắt đầu animation
                        imgMusic_.startAnimation(rotationAnimation);
                    }
                }
            }
        });
        thanh_nhac = view.findViewById(R.id.thanh_nhac);
        layoutParams = (RelativeLayout.LayoutParams) mBottomNavigationView.getLayoutParams();
        params = (RelativeLayout.LayoutParams) mViewPagerMusic.getLayoutParams();
        thanh_nhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (layoutParams.bottomMargin > - dpToPx(getActivity(), 81)) {
                    layoutParams.bottomMargin--;
                    mBottomNavigationView.setLayoutParams(layoutParams);
                }

                params.topMargin = - dpToPx(getActivity(), 50);
                params.height = 4000;
                mViewPagerMusic.setLayoutParams(params);
            }
        });

        small_viewpage = view.findViewById(R.id.small_viewpage);
        small_viewpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (layoutParams.bottomMargin < - dpToPx(getActivity(), 0)) {
                    layoutParams.bottomMargin++;
                    mBottomNavigationView.setLayoutParams(layoutParams);
                }
                while(params.height > dpToPx(getActivity(), 50)) {
                    params.height--;
                    mViewPagerMusic.setLayoutParams(params);
                }
            }
        });
        return view;
    }


}