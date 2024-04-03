package com.example.music_app1.adapter;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.View.PlayMusic_Fragment.imgMusic;
import static com.example.music_app1.View.PlayMusic_Fragment.nameArtist;
import static com.example.music_app1.View.PlayMusic_Fragment.nameMusic;
import static com.example.music_app1.View.PlayMusic_Fragment.seekBar;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;


public class PlaylistAdapter extends RecyclerView.Adapter <PlaylistAdapter.PlaylistViewHolder> {
    public PlaylistAdapter(List<Music> mListMusic) {
        this.mListMusic = mListMusic;
    }

    private final List<Music> mListMusic;
    public static MediaPlayer mediaPlayer;

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_playlist, parent, false);
        return new PlaylistAdapter.PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Music music = mListMusic.get(position);
        if (music == null) {
            return;
        }
        holder.textPlaylist.setText(String.valueOf(music.getName()));
        Picasso.get().load(music.getImage()).into(holder.imgMusic);
        holder.btnPhatngaunhien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(6, false);
                playSound(music.getLink());
                nameMusic.setText(holder.textPlaylist.getText());
                nameArtist.setText(holder.textArtist.getText());
                imgMusic.setImageDrawable(holder.imgMusic.getDrawable());
                Animation rotation = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);
                imgMusic.startAnimation(rotation);

                load_seekbar();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListMusic != null) {
            return mListMusic.size();
        }
        return 0;
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private ImageButton imgbtnBack;
        private ImageButton imgbtnMore;
        public ImageView imgMusic;
        public TextView textArtist;
        public TextView textPlaylist;
        public TextView textUser;
        public TextView textSobaihat;
        public TextView textSophut;
        public ImageButton imgbtnDown;
        public Button btnPhatngaunhien;
        public ImageButton imgbtnPlus;
        public RecyclerView recyclerviewPlaylist;
        public RecyclerView recyclerviewBaihatthinhhanh;
        public Button btnThembai;
        public LinearLayout pageplaylist;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            imgbtnBack = itemView.findViewById(R.id.imagebntBack);
            imgMusic = itemView.findViewById(R.id.imageMusic);
            textPlaylist = itemView.findViewById(R.id.textPlaylist);
            textArtist = itemView.findViewById(R.id.tv_artist);
            textUser = itemView.findViewById(R.id.textUser);
            textSobaihat = itemView.findViewById(R.id.textSobaihat);
            textSophut = itemView.findViewById(R.id.textSophut);
            imgbtnDown = itemView.findViewById(R.id.imagebntDown);
            btnPhatngaunhien = itemView.findViewById(R.id.bntPhatngaunhien);
            imgbtnPlus = itemView.findViewById(R.id.imagebntPlus);
            recyclerviewPlaylist = itemView.findViewById(R.id.recyclerviewPlaylist);
            recyclerviewBaihatthinhhanh = itemView.findViewById(R.id.recyclerviewBaihatthinhhanh);
        }
    }

    private void playSound(String link) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        try {
            mediaPlayer.setDataSource(link);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load_seekbar() {
        seekBar.setMax(mediaPlayer.getDuration());

        Handler mHandler = new Handler();
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                // Lấy vị trí hiện tại của MediaPlayer
                int mCurrentPosition = mediaPlayer.getCurrentPosition();

                // Cập nhật seekbar
                seekBar.setProgress(mCurrentPosition);

                // Lập lại việc cập nhật sau 1 giây
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Nếu người dùng thay đổi vị trí seekbar, chuyển đến vị trí tương ứng trong bài hát
                    mediaPlayer.seekTo(progress);
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
        mediaPlayer.start();
    }
}

