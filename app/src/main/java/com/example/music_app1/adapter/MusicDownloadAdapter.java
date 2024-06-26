package com.example.music_app1.adapter;



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
import static com.example.music_app1.adapter.MusicAdapter.mediaPlayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
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

import com.example.music_app1.R;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MusicDownloadAdapter extends RecyclerView.Adapter<MusicDownloadAdapter.MusicViewHolder> {

    public MusicDownloadAdapter(List<File> mListMusic) {
        this.mListMusic = mListMusic;
    }


    public void setData(List<File> dataList) {
        this.mListMusic = dataList;
        notifyDataSetChanged();
    }

    private List<File> mListMusic;


    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MusicViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        File music = mListMusic.get(position);
        if (music == null){
            return;
        }
        holder.tvname.setText(music.getName().replace(".mp3", ""));
        holder.imgMusic.setImageBitmap(getAlbumArt(music.getPath()));
        // Kiểm tra xem file MP3 có chứa tag ID3v2 không
        try {
            // Tạo đối tượng Mp3File từ đường dẫn file MP3
            Mp3File mp3file = new Mp3File(music.getPath());
            // Kiểm tra xem file MP3 có chứa tag ID3v2 không
            if (mp3file.hasId3v2Tag()) {
                // Lấy thông tin ID3v2 tag
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                holder.tvartist.setText(id3v2Tag.getArtist());
            } else {
                // File MP3 không chứa tag ID3v2
                holder.tvartist.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                playSound(music.getPath());



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


    // Phương thức để lấy hình từ file mp3
    private Bitmap getAlbumArt(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);

        byte[] art = retriever.getEmbeddedPicture();
        if (art != null) {
            return BitmapFactory.decodeByteArray(art, 0, art.length);
        } else {
            return null;
        }
    }

}
