package com.example.music_app1.adapter;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.View.PlayMusic_Fragment.imgMusic;
import static com.example.music_app1.View.PlayMusic_Fragment.nameArtist;
import static com.example.music_app1.View.PlayMusic_Fragment.nameMusic;
import static com.example.music_app1.View.PlayMusic_Fragment.pageplaymusic;
import static com.example.music_app1.View.PlayMusic_Fragment.seekBar;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    public MusicAdapter(List<Music> mListMusic) {
        this.mListMusic = mListMusic;
    }

    private final List<Music> mListMusic;
    public static MediaPlayer mediaPlayer;

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = mListMusic.get(position);
        if (music == null){
            return;
        }
        holder.tvname.setText(String.valueOf(music.getName()));
        holder.tvartist.setText(String.valueOf((music.getArtist())));
        Picasso.get().load(music.getImage()).into(holder.imgMusic);
        holder.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(6,false);
                playSound(music.getLink());
                nameMusic.setText(holder.tvname.getText());
                nameArtist.setText(holder.tvartist.getText());
                imgMusic.setImageDrawable(holder.imgMusic.getDrawable());
                Animation rotation = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);
                imgMusic.startAnimation(rotation);

                load_seekbar();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListMusic != null){
            return mListMusic.size();
        }
        return 0;
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvname, tvartist;
        private final ImageView imgMusic;

        private final LinearLayout btnplay;

        public MusicViewHolder(@NonNull View itemView){
            super(itemView);
            tvname = itemView.findViewById(R.id.tv_name);
            tvartist = itemView.findViewById(R.id.tv_artist);
            imgMusic = itemView.findViewById(R.id.img_music);
            btnplay = itemView.findViewById(R.id.play);
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

    private void load_seekbar(){
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
        mHandler.postDelayed(mRunnable,1000);
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
