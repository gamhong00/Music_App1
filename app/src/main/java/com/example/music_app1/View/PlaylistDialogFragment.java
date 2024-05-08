package com.example.music_app1.View;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.music_app1.Model.Music;
import com.example.music_app1.Model.Playlist;
import com.example.music_app1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PlaylistDialogFragment extends DialogFragment {
    private ImageButton btnBack;
    private EditText editNamePlaylist;
    private Switch swRiengTu;
    private Switch swPhatTuanTu;
    private Switch swTuDongTai;
    private Button createPlaylistButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_playlist, container, false);

        // Tạo reference đến các View trong layout
        btnBack = view.findViewById(R.id.btn_back);
        editNamePlaylist = view.findViewById(R.id.edit_Name_playlist);
        swRiengTu = view.findViewById(R.id.sw_riengtu);
        swPhatTuanTu = view.findViewById(R.id.sw_phattuantu);
        swTuDongTai = view.findViewById(R.id.sw_tudongtai);
        createPlaylistButton = view.findViewById(R.id.create_playlist_button);

        // Xử lý sự kiện click cho nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Đóng dialog
                //mViewPager2.setCurrentItem(temp,false);
            }
        });

        // Xử lý sự kiện click cho nút tạo playlist
        createPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy tên playlist từ EditText
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String playlistName = editNamePlaylist.getText().toString();

                // Tạo một playlist mới
                Playlist newPlaylist = new Playlist();// Có thể tự động tạo hoặc tăng tự động theo thứ tự
                newPlaylist.setUid(user.getUid()); // Lấy từ dữ liệu người dùng đăng nhập hoặc thông tin người dùng
                newPlaylist.setName(playlistName);
                 // Có thể để trống hoặc thêm sau khi tải ảnh lên
                List<Music> music = null;
                // Tạo danh sách trống cho các bài hát (nếu cần)
                newPlaylist.setMusic(music); // Hoặc null tùy thuộc vào yêu cầu của ứng dụng

                // Lưu playlist lên Firebase Realtime Database
                DatabaseReference playlistsRef = FirebaseDatabase.getInstance().getReference("playlist");
                String playlistId = playlistsRef.push().getKey(); // Tạo một id mới cho playlist
                newPlaylist.setId(playlistId);
                playlistsRef.child(playlistId).setValue(newPlaylist)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Xử lý khi lưu playlist thành công
                                dismiss(); // Đóng dialog sau khi tạo playlist thành công


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý khi có lỗi xảy ra trong quá trình lưu playlist
                                dismiss(); // Đóng dialog sau khi xảy ra lỗi
                                Toast.makeText(getActivity(), "Đã xảy ra lỗi khi tạo playlist", Toast.LENGTH_SHORT).show();
                            }
                        });
                // Sự kiện lắng nghe khi thêm playlist mới
                playlistsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                        String playlistId = dataSnapshot.getKey();
                        // Sử dụng playlistId ở đây để thực hiện các hoạt động liên quan đến playlist mới được thêm vào
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

                    }
                    // Các phương thức khác của ChildEventListener
                });
            }
        });
                return view;
    }
}

