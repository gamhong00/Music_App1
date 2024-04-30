package com.example.music_app1.adapter;


import static com.example.music_app1.Nofication.CHANNEL_ID;
import static com.example.music_app1.View.Library_Fragment.historyAdapter;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.DataLocal.DataLocalManager;
import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.example.music_app1.DataLocal.MusicDownloader;

import com.example.music_app1.View.PlayMusic_Fragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MusicViewHolder> {

    public HistoryAdapter(List<Music> mListMusic, Context context) {
        this.context = context;
        this.mListMusic = mListMusic;
    }

    private List<Music> mListMusic;
    private Context context;

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new MusicViewHolder(view);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Music music = mListMusic.get(position);
        if (music == null) {
            return;
        }
        holder.tvname.setText(music.getName());
        Picasso.get().load(music.getImage()).into(holder.imgMusic);
        holder.imgMusic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog(music,context);
                return true;
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvname;
        private final ImageView imgMusic;
        private final LinearLayout btnplay;
        public MusicViewHolder(@NonNull View itemView){
            super(itemView);
            tvname = itemView.findViewById(R.id.tv_name);
            imgMusic = itemView.findViewById(R.id.img_music);
            btnplay = itemView.findViewById(R.id.play);
        }
    }



    private static void showDialog(Music music, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_layout_history);
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
                dialog.dismiss();
            }
        });
        LinearLayout addPlaylist = dialog.findViewById(R.id.layoutAddplaylist);
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        LinearLayout deleteHistory = dialog.findViewById(R.id.layoutdeletehistory);
        deleteHistory.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {

                List<Music> mMusicHistory = DataLocalManager.getListMusic();
                for (int i = 0; i< mMusicHistory.size(); i++){
                    if(music.getId() == mMusicHistory.get(i).getId()){
                        mMusicHistory.remove(i);
                    }
                }

                DataLocalManager.setListMusic(mMusicHistory);
                Toast.makeText(context,"Xóa Nghe gần đây", Toast.LENGTH_SHORT).show();
                historyAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
