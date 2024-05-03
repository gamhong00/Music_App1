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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Helper;
import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.example.music_app1.View.PlayMusic_Fragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {
    public RankAdapter(Context context, List<Integer> dataList, List<Music> mListMusic) {
        this.context= context;
        this.mDataList = dataList;
        this.mListMusic = mListMusic;
    }
    private List<Integer> mDataList;

    private List<Music> mListMusic;

    private Context context;


    @NonNull
    @Override
    public RankAdapter.RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zingchat, parent, false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.RankViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Music music = mListMusic.get(position);
        int number = mDataList.get(position);
        holder.NumberRank.setText(String.valueOf(number));
        if (music == null){
            return;
        }
        if(music.getIsPremium()){
            holder.icon_premium.setVisibility(View.VISIBLE);
        }
        holder.tvname.setText(String.valueOf(music.getName()));
        holder.tvartist.setText(String.valueOf((music.getArtist())));
        Picasso.get().load(music.getImage()).into(holder.imgMusic);

        holder.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music.getIsPremium()){ // nhạc premium
                    Boolean isPremiumUser=  Helper.getKeyIsPremium(v.getContext());
                    if(isPremiumUser){ // người dùng premium
                        PlayMusic_Fragment.mListMusic = mListMusic;
                        PlayMusic_Fragment.position = number-1;
                        PlayMusic_Fragment.playMusic(music);
                    }else{ // người dùng không phải premium
                        Toast.makeText(context, "Hãy nâng cấp lên premium", Toast.LENGTH_SHORT).show();
                    }
                }else{ //nhạc không premium
                    PlayMusic_Fragment.mListMusic = mListMusic;
                    PlayMusic_Fragment.position = number-1;
                    PlayMusic_Fragment.playMusic(music);
                }
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

    public class RankViewHolder extends RecyclerView.ViewHolder {
        private TextView NumberRank;
        private final TextView tvname, tvartist;
        private final  ImageView icon_premium;
        private final ImageView imgMusic;

        private final LinearLayout btnplay;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            NumberRank = itemView.findViewById(R.id.NumberRank);
            tvname = itemView.findViewById(R.id.tv_name);
            icon_premium = itemView.findViewById(R.id.icon_premium);
            tvartist = itemView.findViewById(R.id.tv_artist);
            imgMusic = itemView.findViewById(R.id.img_music);
            btnplay = itemView.findViewById(R.id.play);

        }
    }
}
