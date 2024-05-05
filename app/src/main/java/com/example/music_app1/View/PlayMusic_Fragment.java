package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.dpToPx;
import static com.example.music_app1.MainActivity.mBottomNavigationView;
import static com.example.music_app1.MainActivity.mViewPagerMusic;

import static com.example.music_app1.View.Library_Fragment.historyAdapter;
import static com.example.music_app1.adapter.MusicAdapter.mediaPlayer;

import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.music_app1.DataLocal.DataLocalManager;
import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;


public class PlayMusic_Fragment extends Fragment {

    public static List<Music> mListMusic;
    public static int position;

    private RelativeLayout.LayoutParams layoutParams, params;
    public static ImageButton PlayPause;
    public static ImageButton PlayPause_;

    private ImageButton small_viewpage;
    public static ImageView imgMusic, imgMusic_;
    public static TextView nameMusic, nameMusic_;
    public   static TextView nameArtist, nameArtist_;
    public static SeekBar seekBar;
    public  static LinearLayout pageplaymusic, thanh_nhac;
    public static TextView curentTime, totalTime;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play_music_, container, false);
        curentTime = view.findViewById(R.id.curentTime);
        totalTime = view.findViewById(R.id.totalTime);

        imgMusic = view.findViewById(R.id.img_music);

        imgMusic_ = view.findViewById(R.id.img_music_);


        nameMusic = view.findViewById(R.id.name_music);

        nameMusic_ = view.findViewById(R.id.name_music_);


        nameArtist = view.findViewById(R.id.name_artist);

        nameArtist_ = view.findViewById(R.id.name_artist_);


        seekBar = view.findViewById(R.id.seekbar_music);
        pageplaymusic = view.findViewById(R.id.pageplaymusic);
        PlayPause = view.findViewById(R.id.playpause);
        PlayPause_ = view.findViewById(R.id.playpause_);
        if(DataLocalManager.getMusic() == null){

        }else {
            Picasso.get().load(DataLocalManager.getMusic().getImage()).into(imgMusic);
            Picasso.get().load(DataLocalManager.getMusic().getImage()).into(imgMusic_);
            nameMusic.setText(DataLocalManager.getMusic().getName());
            nameMusic_.setText(DataLocalManager.getMusic().getName());
            nameArtist.setText(DataLocalManager.getMusic().getArtist());
            nameArtist_.setText(DataLocalManager.getMusic().getArtist());
            playSound(DataLocalManager.getLink());
            Animation rotation = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);
            Glide.with(this)
                    .asBitmap()
                    .load(DataLocalManager.getMusic().getImage())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                            int averageColor = getAverageColor(resource);
                            int averageColor_ = getAverageColor_(resource);
                            int[] colors = {
                                    averageColor, // Màu bắt đầu
                                    averageColor_ // Màu kết thúc (trong suốt)
                            };
                            GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                            GradientDrawable gradientDrawable = new GradientDrawable(orientation, colors);
                            pageplaymusic.setBackground(gradientDrawable);

                        }
                    });

        }
        Animation rotation = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);

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
                        Animation rotation1 = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);
                        imgMusic.startAnimation(rotation1);
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
                        Animation rotation1 = AnimationUtils.loadAnimation(imgMusic_.getContext(), R.anim.rotate);
                        imgMusic_.startAnimation(rotation1);
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
        view.findViewById(R.id.nextMusic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == mListMusic.size() - 1){
                    position = 0;
                }else{
                    position++;
                }
                Music music = mListMusic.get(position);
                playMusic(music);
            }
        });
        view.findViewById(R.id.previousMusic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    position = mListMusic.size() - 1;
                    playMusic(mListMusic.get(position));
                }else {
                    position--;
                    playMusic(mListMusic.get(position));
                }

            }
        });
        return view;
    }
    private void playSound(String link) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(link);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    totalTime.setText(millisecondsToTime(mediaPlayer.getDuration()));
                    load_seekbar();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load_seekbar(){
        seekBar.setMax(mediaPlayer.getDuration());
        Handler mHandler = new Handler();
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                // Lấy vị trí hiện tại của MediaPlayer
                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                // Cập nhật seekbar
                seekBar.setProgress(mCurrentPosition);
                curentTime.setText(millisecondsToTime(mCurrentPosition));
                // Lập lại việc cập nhật sau 1 giây
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.postDelayed(mRunnable,1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Nếu người dùng thay đổi vị trí seekbar, chuyển đến vị trí tương ứng trong bài hát
                    mediaPlayer.seekTo(progress);
                    curentTime.setText(millisecondsToTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.postDelayed(mRunnable, 1000);
            }
        });

    }
    public static String millisecondsToTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        int hours = (milliseconds / (1000 * 60 * 60)) % 24;

        String timeString;
        if (hours > 0) {
            timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeString = String.format("%02d:%02d", minutes, seconds);
        }
        return timeString;
    }

    public static void playMusic(Music music){
        nameMusic.setText(music.getName());
        nameMusic_.setText(music.getName());
        nameArtist.setText(music.getArtist());
        nameArtist_.setText(music.getArtist());
        PlayPause.setImageResource(R.drawable.circle_pause_regular);
        PlayPause_.setImageResource(R.drawable.pause_solid);
        Picasso.get().load(music.getImage()).into(imgMusic);
        Animation rotation = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);
        imgMusic.startAnimation(rotation);
        Picasso.get().load(music.getImage()).into(imgMusic_);
        Animation rotation1 = AnimationUtils.loadAnimation(imgMusic_.getContext(), R.anim.rotate);
        imgMusic_.startAnimation(rotation1);
        //Màu playmusic
        Glide.with(pageplaymusic.getContext())
                .asBitmap()
                .load(music.getImage())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                        int averageColor = getAverageColor(resource);
                        int averageColor_ = getAverageColor_(resource);
                        int[] colors = {
                                averageColor, // Màu bắt đầu
                                averageColor_ // Màu kết thúc (trong suốt)
                        };
                        GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                        GradientDrawable gradientDrawable = new GradientDrawable(orientation, colors);
                        pageplaymusic.setBackground(gradientDrawable);

                    }
                });
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(music.getLink());
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    totalTime.setText(millisecondsToTime(mediaPlayer.getDuration()));
                    load_seekbar();
                    mediaPlayer.start();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Để vào máy

        DataLocalManager.setMusic(music);


        List<Music> mMusicHistory = DataLocalManager.getListMusic();
        mMusicHistory.add(0,music);
            for (int i = 1; i< mMusicHistory.size(); i++){
                if(music.getId() == mMusicHistory.get(i).getId()){
                    mMusicHistory.remove(i);
                }
            }

        DataLocalManager.setListMusic(mMusicHistory);
        historyAdapter.setMusicList(mMusicHistory);
        historyAdapter.notifyDataSetChanged();

    }

    public static int getAverageColor(Bitmap bitmap) {
        int width = bitmap.getWidth()/2;
        int height = bitmap.getHeight()/2;
        int pixelCount = width * height;
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;

        for (int pixel : pixels) {
            redSum += (pixel >> 16) & 0xFF;
            greenSum += (pixel >> 8) & 0xFF;
            blueSum += pixel & 0xFF;
        }

        int averageRed = redSum / pixelCount;
        int averageGreen = greenSum / pixelCount;
        int averageBlue = blueSum / pixelCount;

        return 0xFF000000 | (averageRed << 16) | (averageGreen << 8) | averageBlue;
    }

    public static int getAverageColor_(Bitmap bitmap) {
        int width = bitmap.getWidth()/2;
        int height = bitmap.getHeight()/2;
        int pixelCount = width * height;
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, width, width, height, width, height);

        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;

        for (int pixel : pixels) {
            redSum += (pixel >> 16) & 0xFF;
            greenSum += (pixel >> 8) & 0xFF;
            blueSum += pixel & 0xFF;
        }

        int averageRed = redSum / pixelCount;
        int averageGreen = greenSum / pixelCount;
        int averageBlue = blueSum / pixelCount;

        return 0xFF000000 | (averageRed << 16) | (averageGreen << 8) | averageBlue;
    }



    //Click view tăng
    public static void IncreaseListens (Music music){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music");
        music.setListens(music.getListens()+1);
        myRef.child(String.valueOf(music.getId())).updateChildren(music.toMap());
    }
}