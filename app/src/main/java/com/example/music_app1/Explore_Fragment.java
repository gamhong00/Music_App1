package com.example.music_app1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Explore_Fragment extends Fragment {
    private DatabaseReference myFirebaseRef;
    private List<Song> popularSongs;
    private List<Song> newSongs;
    private SongAdapter popularAdapter;
    private SongAdapter newAdapter;
    private static final int MAX_SONGS = 6; // Số lượng bài hát tối đa


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_, container, false);

        // Khởi tạo Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myFirebaseRef = database.getReference("music");

        RecyclerView popularRecyclerView = view.findViewById(R.id.popular_songs_recyclerview);
        popularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularRecyclerView.setHasFixedSize(true);

        popularSongs = new ArrayList<>();
        popularAdapter = new SongAdapter(popularSongs);
        popularRecyclerView.setAdapter(popularAdapter);
        RecyclerView newRecyclerView = view.findViewById(R.id.new_songs_recyclerview);
        newRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newRecyclerView.setHasFixedSize(true);

        newSongs = new ArrayList<>();
        newAdapter = new SongAdapter(newSongs);
        newRecyclerView.setAdapter(newAdapter);

        // Lắng nghe sự thay đổi trong cơ sở dữ liệu Firebase
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                popularSongs.clear();
                newSongs.clear();
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (count >= MAX_SONGS) {
                        break; // Đã đạt tới số lượng bài hát tối đa
                    }
                    Song song = snapshot.getValue(Song.class);
                    if (!isSongExist(song)) {
                        popularSongs.add(song);
                        newSongs.add(song);
                        count++;
                    }
                }
                popularAdapter.notifyDataSetChanged();
                newAdapter.notifyDataSetChanged();


            // Tạo danh sách URL hình ảnh từ 5 bài hát phổ biến
                List<String> imageUrls = new ArrayList<>();
                for (int i = 0; i < Math.min(popularSongs.size(), 5); i++) {
                    imageUrls.add(popularSongs.get(i).getImage());
                }
                TextView viewAllPopularSongs = view.findViewById(R.id.view_all_popular_songs);
                viewAllPopularSongs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Mở màn hình AllSongsActivity để hiển thị tất cả các bài hát phổ biến
                        Intent intent = new Intent(getActivity(), AllSongsActivity.class);
                        intent.putExtra("category", "popular"); // Gửi dữ liệu về loại bài hát (phổ biến)
                        startActivity(intent);
                    }
                });
// Tạo danh sách URL hình ảnh từ 5 bài hát mới
                List<String> newImageUrls = new ArrayList<>();
                for (int i = 0; i < Math.min(newSongs.size(), 5); i++) {
                    newImageUrls.add(newSongs.get(i).getImage());
                }
                TextView viewAllNewSongs = view.findViewById(R.id.view_all_new_songs);
                viewAllNewSongs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Mở màn hình AllSongsActivity để hiển thị tất cả các bài hát mới
                        Intent intent = new Intent(getActivity(), AllSongsActivity.class);
                        intent.putExtra("category", "new"); // Gửi dữ liệu về loại bài hát (mới)
                        startActivity(intent);
                    }
                });

                // Tạo LinearLayoutManager và gắn cho RecyclerView
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                RecyclerView recyclerView = view.findViewById(R.id.banner_recyclerview);
                recyclerView.setLayoutManager(layoutManager);

                // Gắn SnapHelper vào RecyclerView
                SnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(recyclerView);

                // Hiển thị 5 hình ảnh trong banner
                bannerAdapter bannerAdapter = new bannerAdapter(imageUrls);
                recyclerView.setAdapter(bannerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
            }
        });

        return view;
    }

    // Phương thức kiểm tra xem bài hát đã tồn tại trong danh sách chưa
    private boolean isSongExist(Song newSong) {
        for (Song song : popularSongs) {
            if (song.getId() == newSong.getId()) {
                return true;
            }
        }
        return false;
    }
}