package com.example.music_app1;


import static com.example.music_app1.Nofication.CHANNEL_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Helper;
import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.example.music_app1.DataLocal.MusicDownloader;

import com.example.music_app1.View.Library_Fragment;
import com.example.music_app1.View.PlayMusic_Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> implements Filterable {

    public MusicAdapter(List<Music> mListMusic, Context context) {
        this.context = context;
        this.mListMusic = mListMusic;
        OldmListMusic = mListMusic;
    }
    public static Music musicglobal;
    private List<Music> mListMusic;
    private List<Music> OldmListMusic;
    public static MediaPlayer mediaPlayer;
    private  Context context;

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MusicViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Music music = mListMusic.get(position);
        if (music == null) {
            return;
        }
        if(music.getIsPremium()){
            holder.icon_premium.setVisibility(View.VISIBLE);
        }
        holder.tvname.setText(music.getName());
        holder.tvartist.setText(String.valueOf(music.getArtist()));
        Picasso.get().load(music.getImage()).into(holder.imgMusic);
        holder.btnellipsis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(music, context);
            }
        });


        holder.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic_Fragment.mListMusic = mListMusic;
                PlayMusic_Fragment.position = position;
                PlayMusic_Fragment.playMusic(music);
                sendNotificationMedia(music);
            }
        });
        holder.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music.getIsPremium()){ // nhạc premium
                    Boolean isPremiumUser=  Helper.getKeyIsPremium(v.getContext());
                    if(isPremiumUser){ // người dùng premium
                        PlayMusic_Fragment.mListMusic = mListMusic;
                        PlayMusic_Fragment.position = position;
                        PlayMusic_Fragment.playMusic(music);
                    }else{ // người dùng không phải premium
                        Toast.makeText(context, "Hãy nâng cấp lên premium", Toast.LENGTH_SHORT).show();
                    }
                }else{ //nhạc không premium
                    PlayMusic_Fragment.mListMusic = mListMusic;
                    PlayMusic_Fragment.position = position;
                    PlayMusic_Fragment.playMusic(music);
                }
            }
        });
    }

    private void sendNotificationMedia(Music music) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_small_music)
                .setSubText(music.getName())
                .setContentTitle(music.getName())
                .setContentText(music.getArtist())
                .addAction(R.drawable.backward_step_solid, "backward", null)
                .addAction(R.drawable.pause_solid, "pause", null)
                .addAction(R.drawable.forward_step_solid, "next", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(mediaSessionCompat.getSessionToken())).build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        managerCompat.notify(1, notification);
    }

    @Override
    public int getItemCount() {
        if(mListMusic != null){
            return mListMusic.size();
        }
        return 0;
    }

    public void addMusic(Music music) {
        mListMusic.add(music);
    }


    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvname, tvartist;
        private final ImageView imgMusic;
        private final  ImageView icon_premium;

        private final LinearLayout btnplay;
        private final ImageButton  btnellipsis ;

        public MusicViewHolder(@NonNull View itemView){
            super(itemView);
            tvname = itemView.findViewById(R.id.tv_name);
            icon_premium = itemView.findViewById(R.id.icon_premium);
            tvartist = itemView.findViewById(R.id.tv_artist);
            imgMusic = itemView.findViewById(R.id.img_music);
            btnplay = itemView.findViewById(R.id.play);
            btnellipsis = itemView.findViewById(R.id.ellipsis);

        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    mListMusic = OldmListMusic;
                }
                else {
                    List<Music> list = new ArrayList<>();
                    for (Music music : OldmListMusic){
                        if (music.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(music);
                        }
                    }
                    mListMusic = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mListMusic;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListMusic = (List<Music>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    private static void showDialog(Music music, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout);
        LinearLayout dowloadMusic = dialog.findViewById(R.id.layoutdowload);
        dowloadMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicDownloader musicDownloader = new MusicDownloader(v.getContext());
//                // Gọi phương thức downloadMusic với URL của tập tin và tên tập tin mong muốn
                musicDownloader.downloadMusic(music.getLink(), music.getName()+".mp3", music.getArtist());
            }
        });
        LinearLayout likeMusic = dialog.findViewById(R.id.layoutLike);

        likeMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra music có tồn tại hay không
                if (music != null) {
                    // Truy cập vào đối tượng DatabaseReference tương ứng với bài hát được thích
                    DatabaseReference musicRef = FirebaseDatabase.getInstance().getReference("music").child(String.valueOf(music.getId()));

                    // Kiểm tra trạng thái like hiện tại của bài hát
                    musicRef.child("like").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Boolean isLiked = dataSnapshot.getValue(Boolean.class);

                            // Nếu bài hát chưa được yêu thích (null hoặc false)
                            if (isLiked == null || !isLiked) {
                                // Cập nhật trường like của bài hát thành true
                                musicRef.child("like").setValue(true);
                                // Hiển thị thông báo cho người dùng
                                Toast.makeText(v.getContext(), "Bài hát đã được thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                            } else {
                                // Nếu bài hát đã được yêu thích (true)
                                // Cập nhật trường like của bài hát thành false
                                musicRef.child("like").setValue(false);
                                // Hiển thị thông báo cho người dùng
                                Toast.makeText(v.getContext(), "Bài hát đã được xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                            }

                            // Đóng dialog
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý khi có lỗi xảy ra
                        }
                    });
                }
            }
        });

        LinearLayout addPlaylist = dialog.findViewById(R.id.layoutAddplaylist);
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showDialogPlaylist(music,context);

            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public static void showDialogPlaylist(Music music, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_layout_playlist);
        RecyclerView rcvplaylist = dialog.findViewById(R.id.recyclerviewPlaylist1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvplaylist.setLayoutManager(linearLayoutManager);
        rcvplaylist.setAdapter(Library_Fragment.playListDialogAdapter);
        musicglobal = music;
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void setMusicList(List<Music> musicList){
        this.mListMusic = musicList;
    }

}