package com.example.music_app1.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.example.music_app1.View.PlayMusic_Fragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {
    public RankAdapter(List<Integer> dataList, List<Music> mListMusic) {
        this.mDataList = dataList;
        this.mListMusic = mListMusic;
    }
    private List<Integer> mDataList;

    private List<Music> mListMusic;




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
        holder.tvname.setText(String.valueOf(music.getName()));
        holder.tvartist.setText(String.valueOf((music.getArtist())));
        Picasso.get().load(music.getImage()).into(holder.imgMusic);

        holder.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic_Fragment.mListMusic = mListMusic;
                PlayMusic_Fragment.position = number-1;
                PlayMusic_Fragment.playMusic(music);
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
        private final ImageView imgMusic;

        private final LinearLayout btnplay;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            NumberRank = itemView.findViewById(R.id.NumberRank);
            tvname = itemView.findViewById(R.id.tv_name);
            tvartist = itemView.findViewById(R.id.tv_artist);
            imgMusic = itemView.findViewById(R.id.img_music);
            btnplay = itemView.findViewById(R.id.play);

        }
    }
}
