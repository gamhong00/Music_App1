package com.example.music_app1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AllSongsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private DatabaseReference myFirebaseRef;
    private List<Song> allSongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);

        // Khởi tạo Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myFirebaseRef = database.getReference("music");

        recyclerView = findViewById(R.id.all_songs_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SongAdapter(allSongs);
        recyclerView.setAdapter(adapter);

        // Lắng nghe sự kiện thêm mới dữ liệu vào Firebase
        myFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Lấy dữ liệu từ dataSnapshot và thêm vào danh sách bài hát của bạn
                Song song = dataSnapshot.getValue(Song.class);
                allSongs.add(song);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Xử lý khi có sự thay đổi trong dữ liệu của một bài hát
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Xử lý khi một bài hát được xóa khỏi danh sách
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Xử lý khi một bài hát được di chuyển trong danh sách
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
            }
        });
    }
}

