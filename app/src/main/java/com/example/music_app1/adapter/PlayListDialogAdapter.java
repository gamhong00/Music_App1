package com.example.music_app1.adapter;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.View.Library_Fragment.historyAdapter;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Model.Music;
import com.example.music_app1.Model.Playlist;
import com.example.music_app1.R;
import com.example.music_app1.View.Playlist_Fragment;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class PlayListDialogAdapter extends RecyclerView.Adapter<PlayListDialogAdapter.PlaylistViewHolder> {
    public PlayListDialogAdapter(List<Playlist> mListPlaylist, Context context) {
        this.context = context;
        this.mListPlaylist = mListPlaylist;

    }

    private List<Playlist> mListPlaylist;
    private Context context;


    @NonNull
    @Override
    public PlayListDialogAdapter.PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = mListPlaylist.get(position);
        if (playlist == null) {
            return;
        }
        holder.playlist_name.setText(playlist.getName());
        if (playlist.getMusic() == null){
            holder.img_playlist.setImageResource(R.drawable.playlist);
        }else {
            Picasso.get().load(playlist.getMusic().get(0).getImage()).into(holder.img_playlist);
        }
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference playlistRef = database.getReference("playlist").child(playlist.getId());
                DatabaseReference musicRef = playlistRef.child("music");

                // Lấy số lượng bài hát hiện có trong danh sách
                musicRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long numberOfSongs = snapshot.getChildrenCount();

                        // Tạo một bài hát mới với số thứ tự tiếp theo
                        String musicId = String.valueOf(numberOfSongs); // Sử dụng số thứ tự làm khóa
                        DatabaseReference newSongRef = musicRef.child(musicId);

                        // Thiết lập dữ liệu cho bài hát mới
                        newSongRef.setValue(MusicAdapter.musicglobal, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error != null) {
                                    // Xử lý khi có lỗi xảy ra
                                } else {
                                    Playlist_Fragment.musicAdapter.addMusic(MusicAdapter.musicglobal);
                                    Playlist_Fragment.musicAdapter.notifyDataSetChanged();
                                    Toast.makeText(context, "Add zô rồi nè ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mListPlaylist != null) {
            return mListPlaylist.size();
        }
        return 0;
    }


    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private final TextView playlist_name;
        private final LinearLayout play;
        private final ImageView img_playlist;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlist_name = itemView.findViewById(R.id.playlist_name);
            img_playlist = itemView.findViewById(R.id.img_playlist);
            play = itemView.findViewById(R.id.play);
        }
    }
}

