package com.example.music_app1.adapter;



import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.MainActivity.mViewPagerMusic;
import static com.example.music_app1.View.PlayMusic_Fragment.PlayPause;
import static com.example.music_app1.View.PlayMusic_Fragment.PlayPause_;
import static com.example.music_app1.View.PlayMusic_Fragment.curentTime;
import static com.example.music_app1.View.PlayMusic_Fragment.imgMusic;
import static com.example.music_app1.View.PlayMusic_Fragment.imgMusic_;
import static com.example.music_app1.View.PlayMusic_Fragment.nameArtist;
import static com.example.music_app1.View.PlayMusic_Fragment.nameArtist_;
import static com.example.music_app1.View.PlayMusic_Fragment.nameMusic;
import static com.example.music_app1.View.PlayMusic_Fragment.nameMusic_;
import static com.example.music_app1.View.PlayMusic_Fragment.pageplaymusic;
import static com.example.music_app1.View.PlayMusic_Fragment.seekBar;
import static com.example.music_app1.View.PlayMusic_Fragment.totalTime;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.example.music_app1.View.DataLocalManager;
import com.example.music_app1.View.MusicDownloader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    public MusicAdapter(List<Music> mListMusic) {
        this.mListMusic = mListMusic;
    }
    public void setData(List<Music> dataList) {
        this.mListMusic = dataList;
        notifyDataSetChanged();
    }

    private List<Music> mListMusic;
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
        holder.tvname.setText(music.getName());
        holder.tvartist.setText(String.valueOf(music.getArtist()));
        Picasso.get().load(music.getImage()).into(holder.imgMusic);

        holder.btndown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo MusicDownloader với context
                MusicDownloader musicDownloader = new MusicDownloader(v.getContext());
                // Gọi phương thức downloadMusic với URL của tập tin và tên tập tin mong muốn
                musicDownloader.downloadMusic(music.getLink(), music.getName()+".mp3",music.getName());
                

            }
        });

        holder.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameMusic.setText(holder.tvname.getText());
                nameMusic_.setText(holder.tvname.getText());
                nameArtist.setText(holder.tvartist.getText());
                nameArtist_.setText(holder.tvartist.getText());
                PlayPause.setImageResource(R.drawable.circle_pause_regular);
                PlayPause_.setImageResource(R.drawable.pause_solid);
                imgMusic.setImageDrawable(holder.imgMusic.getDrawable());
                Animation rotation = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);
                imgMusic.startAnimation(rotation);
                imgMusic_.setImageDrawable(holder.imgMusic.getDrawable());
                Animation rotation1 = AnimationUtils.loadAnimation(imgMusic_.getContext(), R.anim.rotate);
                imgMusic_.startAnimation(rotation1);
                playSound(music.getLink());

                IncreaseListens(music);

                //Màu playmusic
                Bitmap bitmap = ((BitmapDrawable) holder.imgMusic.getDrawable()).getBitmap();
                int averageColor = getAverageColor(bitmap);
                int averageColor_ = getAverageColor_(bitmap);
                int[] colors = {
                        averageColor, // Màu bắt đầu
                        averageColor_ // Màu kết thúc (trong suốt)
                };
                GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                GradientDrawable gradientDrawable = new GradientDrawable(orientation, colors);
                pageplaymusic.setBackground(gradientDrawable);


                // Để vào máy
                DataLocalManager.setNameMusic(music.getName());
                DataLocalManager.setNameArtist(music.getArtist());
                DataLocalManager.setImageMusic(music.getImage());
                DataLocalManager.setLink(music.getLink());
            }
        });
    }



    private File getExternalFilesDir(String directoryMusic) {
        return null;
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
        private final ImageButton btnlike, btndown;

        public MusicViewHolder(@NonNull View itemView){
            super(itemView);
            tvname = itemView.findViewById(R.id.tv_name);
            tvartist = itemView.findViewById(R.id.tv_artist);
            imgMusic = itemView.findViewById(R.id.img_music);
            btnplay = itemView.findViewById(R.id.play);
            btnlike = itemView.findViewById(R.id.heart);
            btndown = itemView.findViewById(R.id.downmusic);
        }
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
                    mediaPlayer.start();
                }
            });
            mediaPlayer.prepareAsync();
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

    private int getAverageColor(Bitmap bitmap) {
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

    private int getAverageColor_(Bitmap bitmap) {
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

    private String millisecondsToTime(int milliseconds) {
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

    //Click view tăng
    public void IncreaseListens (Music music){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("playlist/music");
        music.setListens(music.getListens()+1);
        myRef.child(String.valueOf(music.getId())).updateChildren(music.toMap());


    }

}
