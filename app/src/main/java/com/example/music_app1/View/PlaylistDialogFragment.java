package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.MainActivity.temp;

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
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;
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
    private MusicAdapter mMusicAdapter;
    List<Music> mListMusic;
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
                String playlistName = editNamePlaylist.getText().toString();

                // Lấy trạng thái của các switch
                boolean isPrivate = swRiengTu.isChecked();
                boolean isSequential = swPhatTuanTu.isChecked();
                boolean isAutoDownload = swTuDongTai.isChecked();

                // Thực hiện các thao tác cần thiết với thông tin playlist như lưu vào cơ sở dữ liệu
                // ...
                // Gọi hàm thêm playlist vào cơ sở dữ liệu
                //addPlaylistToDatabase(playlistName, isPrivate, isSequential, isAutoDownload);

                // Sau khi thực hiện xong, đóng dialog
                dismiss();
            }
        });

        return view;
    }
    // xử lý dialog full màn hình
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullscreenDialogTheme);
//    }

    public void callApiGetMusics(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music");
        Log.d("tag",myRef.toString());
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if(music != null){
                    mListMusic.add(music);
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

