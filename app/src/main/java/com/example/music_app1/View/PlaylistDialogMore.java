package com.example.music_app1.View;

import android.os.Bundle;
import android.util.Log;
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
import com.example.music_app1.adapter.MusicAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistDialogMore extends DialogFragment {
    private ImageButton btnBack;
    private EditText editNamePlaylist;
    private Switch swRiengTu;
    private Switch swPhatTuanTu;
    private Switch swTuDongTai;

    private Button btn_updatePlaylist;
    private MusicAdapter mMusicAdapter;
    List<Music> mListMusic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_update_playlist, container, false);


        // Tạo reference đến các View trong layout
        btnBack = view.findViewById(R.id.btn_back);
        editNamePlaylist = view.findViewById(R.id.edit_Name_playlist);
        swRiengTu = view.findViewById(R.id.sw_riengtu);
        swPhatTuanTu = view.findViewById(R.id.sw_phattuantu);
        swTuDongTai = view.findViewById(R.id.sw_tudongtai);
        btn_updatePlaylist = view.findViewById(R.id.btn_updatePlaylist);

        // Xử lý sự kiện click cho nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Đóng dialog
                //mViewPager2.setCurrentItem(temp,false);
            }
        });

        // Xử lý sự kiện click cho nút tạo playlist
        // Nhận vị trí của playlist từ Bundle
        // Trong sự kiện click của btn_updatePlaylist
        btn_updatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin playlist từ các trường nhập liệu trong dialog
                String updatedPlaylistName = editNamePlaylist.getText().toString();

                // Lấy playlistId từ Bundle
                Bundle bundle = getArguments();
                if (bundle != null) {
                    String playlistId = bundle.getString("playlistId");

                    if (playlistId != null) {
                        // Tạo một đối tượng Playlist mới với thông tin cập nhật
                        Playlist updatedPlaylist = new Playlist();
                        updatedPlaylist.setName(updatedPlaylistName);
                        Log.d("minh102",getString(getId()));
                        // Cập nhật các trạng thái khác của playlist nếu cần

                        // Tạo một HashMap để chứa dữ liệu cập nhật
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("name", updatedPlaylist.getName());
                        // Đặt các trạng thái khác của playlist vào updateData nếu cần

                        // Thực hiện cập nhật dữ liệu của playlist trong Firebase Realtime Database
                        DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("playlist").child(playlistId);
                        playlistRef.updateChildren(updateData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Xử lý khi cập nhật playlist thành công
                                        dismiss(); // Đóng dialog sau khi cập nhật playlist thành công
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xử lý khi có lỗi xảy ra trong quá trình cập nhật playlist
                                        Toast.makeText(getContext(), "Đã xảy ra lỗi khi cập nhật playlist", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Xử lý khi không có playlistId
                        Toast.makeText(getContext(), "Không tìm thấy playlist cần cập nhật", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        return view;
    }
}
