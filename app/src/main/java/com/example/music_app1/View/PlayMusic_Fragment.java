package com.example.music_app1.View;

import static android.view.animation.AnimationUtils.loadAnimation;
import static com.example.music_app1.adapter.MusicAdapter.mediaPlayer;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
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
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.music_app1.R;



public class PlayMusic_Fragment extends Fragment {


    private LinearLayout contentLayout;
    private ImageButton thugonButton;
    private ImageButton phongtoButton;
    private ImageButton PlayPause;
    public static ImageView imgMusic;
    public static TextView nameMusic;
    public   static TextView nameArtist;
    public static SeekBar seekBar;
    public  static LinearLayout pageplaymusic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play_music_, container, false);

        final LinearLayout layoutToCollapse = view.findViewById(R.id.pageplaymusic);
        contentLayout = view.findViewById(R.id.content_layout);
        thugonButton = view.findViewById(R.id.thugon);
        phongtoButton = view.findViewById(R.id.phongto);

        // Set initial visibility based on StateUtility
        if (StateUtility.isContentVisible) {
            contentLayout.setVisibility(View.VISIBLE);
            thugonButton.setVisibility(View.VISIBLE);
            phongtoButton.setVisibility(View.GONE);
        } else {
            contentLayout.setVisibility(View.GONE);
            thugonButton.setVisibility(View.GONE);
            phongtoButton.setVisibility(View.VISIBLE);
        }

        thugonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentLayout.setVisibility(View.GONE); // Ẩn content_layout
                thugonButton.setVisibility(View.GONE); // Ẩn thugonButton
                phongtoButton.setVisibility(View.VISIBLE); // Hiện phongtoButton
                StateUtility.isContentVisible = false;
            }
        });

        phongtoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentLayout.setVisibility(View.VISIBLE); // Hiện content_layout
                thugonButton.setVisibility(View.VISIBLE); // Hiện thugonButton
                phongtoButton.setVisibility(View.GONE); // Ẩn phongtoButton
                StateUtility.isContentVisible = true;
            }
        });

        imgMusic = view.findViewById(R.id.img_music);
        Animation rotation = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);

        nameMusic = view.findViewById(R.id.name_music);
        nameArtist = view.findViewById(R.id.name_artist);

        seekBar = view.findViewById(R.id.seekbar_music);
        pageplaymusic = view.findViewById(R.id.pageplaymusic);
        PlayPause = view.findViewById(R.id.playpause);
        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    PlayPause.setImageResource(R.drawable.circle_play_regular);
                    Animation currentAnimation = imgMusic.getAnimation();
                }
                else{
                    mediaPlayer.start();
                    PlayPause.setImageResource(R.drawable.circle_pause_regular);
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

        return view;
    }

}