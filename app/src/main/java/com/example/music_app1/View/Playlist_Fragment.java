package com.example.music_app1.View;



import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.MainActivity.temp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app1.Model.Music;
import com.example.music_app1.Model.Playlist;
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;
import com.example.music_app1.adapter.PlayListAdapter;
import com.example.music_app1.adapter.PlayListDialogAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class Playlist_Fragment extends Fragment  {
    public static MusicAdapter musicAdapter;
    public static List<Music> musicListPlaylist;
    private ImageButton imgbtnBack;
    private ImageButton imgbtnMore;
    public static ImageView imgMusic;
    public static TextView textPlaylist;
    public static TextView textUser;
    public static int p;

    RecyclerView recyclerviewPlaylist;
    public Playlist_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_playlist_, container, false);
        recyclerviewPlaylist = view.findViewById(R.id.recyclerviewPlaylist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewPlaylist.setLayoutManager(linearLayoutManager);
        musicAdapter = new MusicAdapter(musicListPlaylist, getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerviewPlaylist.addItemDecoration(itemDecoration);
        callApiGetDataPlaylist();
        imgMusic = view.findViewById(R.id.imageMusic);
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
                        playlistsRef.orderByKey().limitToFirst(positionToDelete).addListenerForSingleValueEvent(new ValueEventListener() {
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

        // Inflate the layout for this fragment
        recyclerviewPlaylist.setAdapter(musicAdapter);
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
                        textUser.setText(playlist.getUid());
                        String imageUrl = playlist.getMusic().get(0).getImage();
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
}