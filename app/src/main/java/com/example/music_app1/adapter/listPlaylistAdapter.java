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
import android.util.Log;
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
import com.example.music_app1.Model.Playlist;
import com.example.music_app1.R;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;


public class listPlaylistAdapter extends RecyclerView.Adapter<listPlaylistAdapter.PlaylistViewHolder> {
    public listPlaylistAdapter(List<Playlist> mListPlaylist) {
        this.mListPlaylist = mListPlaylist;
    }

    private final List<Playlist> mListPlaylist;
    public static int p;
    // Interface để gửi vị trí item được nhấn đến MainActivity

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_userplaylist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = mListPlaylist.get(position);
        if (playlist == null){
            return;
        }
        holder.tvname.setText(String.valueOf(playlist.getName()));
        holder.tvidUser.setText(String.valueOf((playlist.getIdUser())));
        Picasso.get().load(playlist.getImage()).into(holder.imgMusic);
        Log.e("minh",String.valueOf(position));

        // Thêm listener cho nút phát
        holder.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gửi vị trí của item được nhấn đến MainActivity
                int po = holder.getAdapterPosition();
                if (mViewPager2 != null) {
                    mViewPager2.setCurrentItem(6, false);
                    p = po;
                    Log.e("position",String.valueOf(p));
                    mViewPager2.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListPlaylist != null){
            return mListPlaylist.size();
        }
        return 0;
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private TextView tvname, tvidUser;
        private ImageView imgMusic;
        private LinearLayout btnplay;


        public PlaylistViewHolder(@NonNull View itemView){
            super(itemView);
            tvname = itemView.findViewById(R.id.tv_name);
            tvidUser = itemView.findViewById(R.id.idUser);
            imgMusic = itemView.findViewById(R.id.img_music);
            btnplay = itemView.findViewById(R.id.playlistUser);

        }

    }



}
