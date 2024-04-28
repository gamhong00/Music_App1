package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.MainActivity.temp;
import static com.example.music_app1.adapter.listPlaylistAdapter.p;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Model.Music;
import com.example.music_app1.Model.Playlist;
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;
import com.example.music_app1.adapter.listPlaylistAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Playlist_Fragment extends Fragment{

    private ImageButton imgbtnBack;
    private ImageButton imgbtnMore;
    public static ImageView imgMusic;
    public static TextView textPlaylist;
    public static TextView textUser;
    public ImageButton imgbtnDown;
    public Button btnPhatngaunhien;
    public ImageButton imgbtnPlus;

    public static Button btnThembai;

    public  static LinearLayout pageplaylist;
    private RecyclerView rcvPlaylist;

    private RecyclerView rcvPlaylistthinhhanh;
    private MusicAdapter mMusicAdapter;
    private listPlaylistAdapter mlistPlaylistAdapter;
    List<Music> mListMusicPlaylist;
    List<Music> mListMusicThinhhanh;
    List<Playlist> mListPlaylist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        // rcv cho playlist
        rcvPlaylist = view.findViewById(R.id.recyclerviewPlaylist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvPlaylist.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcvPlaylist.addItemDecoration(itemDecoration);
        mListMusicPlaylist = new ArrayList<>();
        mMusicAdapter = new MusicAdapter(mListMusicPlaylist);
        rcvPlaylist.setAdapter(mMusicAdapter);
        callApiGetPlaylist();

        // rcv cho list thinhhanh
        rcvPlaylistthinhhanh = view.findViewById(R.id.recyclerviewBaihatthinhhanh);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity()); // Tạo một LinearLayoutManager mới
        rcvPlaylistthinhhanh.setLayoutManager(linearLayoutManager2); // Gán LinearLayoutManager mới
        rcvPlaylistthinhhanh.addItemDecoration(itemDecoration);
        mListMusicThinhhanh = new ArrayList<>();
        mMusicAdapter = new MusicAdapter(mListMusicThinhhanh);
        rcvPlaylistthinhhanh.setAdapter(mMusicAdapter); // Sử dụng cùng một adapter không có vấn đề
        callApiGetBaihatthinhhanh();

//        mListPlaylist = new ArrayList<>();
//        mlistPlaylistAdapter = new listPlaylistAdapter(mListPlaylist);
        callApiGetDataPlaylist();

        imgbtnBack = view.findViewById(R.id.imagebntBack);
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager2.setCurrentItem(temp,false);
            }
        });
        imgbtnMore = view.findViewById(R.id.imagebntMore);
        imgbtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(R.layout.dialog_more_playlist);
                TextView txt_updatePlaylist = bottomSheetDialog.findViewById(R.id.txt_updatePlaylist);
                txt_updatePlaylist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Tạo một instance của PlaylistDialogFragment
                        PlaylistDialogMore playlistDialogMore = new PlaylistDialogMore();

                        // Hiển thị dialog
                        playlistDialogMore.show(getParentFragmentManager(), "dialog_update_playlist");
                    }
                });
                TextView txt_deletePlaylist = bottomSheetDialog.findViewById(R.id.txt_deletePlaylist);

                txt_deletePlaylist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Lấy vị trí của playlist được chọn
                        int positionToDelete = p /* Xác định vị trí của playlist cần xóa */;

                        // Lấy ID của playlist cần xóa
                        DatabaseReference playlistsRef = FirebaseDatabase.getInstance().getReference("playlist");
                        playlistsRef.orderByKey().limitToFirst(positionToDelete + 1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String playlistId = null;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    playlistId = snapshot.getKey();
                                }

                                if (playlistId != null) {
                                    // Xóa playlist khỏi Firebase Realtime Database
                                    playlistsRef.child(playlistId).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Xử lý khi xóa playlist thành công
                                                    bottomSheetDialog.dismiss(); // Đóng dialog sau khi xóa playlist thành công
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Xử lý khi có lỗi xảy ra trong quá trình xóa playlist
                                                    Toast.makeText(getContext(), "Đã xảy ra lỗi khi xóa playlist", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    // Xử lý khi không tìm thấy playlist
                                    Toast.makeText(getContext(), "Không tìm thấy playlist cần xóa", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu
                                Toast.makeText(getContext(), "Đã xảy ra lỗi khi đọc dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                bottomSheetDialog.show();
            }
        });
        imgMusic = view.findViewById(R.id.imageMusic);

        textPlaylist = view.findViewById(R.id.textPlaylist);

        textUser = view.findViewById(R.id.textUser);

        imgbtnDown = view.findViewById(R.id.imagebntDown);


        btnPhatngaunhien = view.findViewById(R.id.bntPhatngaunhien);

        imgbtnPlus = view.findViewById(R.id.imagebntPlus);


        return view;
    }
    public void callApiGetDataPlaylist() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("playlist");
        String position = String.valueOf(p);
        DatabaseReference playlistRef = myRef.child(position);

        playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lấy thông tin của playlist từ DataSnapshot
                    Playlist playlist = snapshot.getValue(Playlist.class);
                    if (playlist != null) {
                        // Hiển thị thông tin của playlist
                        textPlaylist.setText(playlist.getName());
                        textUser.setText(playlist.getIdUser());
                        String imageUrl = playlist.getImage();
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get().load(imageUrl).into(imgMusic);
                        }
                    }
                } else {
                    // Xử lý khi không có dữ liệu tại vị trí "playlist/0"
                    Log.d("tag", "Không tìm thấy playlist tại vị trí 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy cập dữ liệu
                Toast.makeText(getActivity(), "message", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void callApiGetBaihatthinhhanh(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music");
        Log.d("tag",myRef.toString());
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if(music != null){
                    mListMusicThinhhanh.add(music);
                    mMusicAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "message", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void callApiGetPlaylist(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("playlist/0/Music");
        Log.d("tag",myRef.toString());
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if(music != null){
                    mListMusicPlaylist.add(music);
                    mMusicAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "message", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
