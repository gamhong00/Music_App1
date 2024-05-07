package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.MainActivity.temp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class favoritesong_Fragment extends Fragment {

    private ImageButton imgbtnBack;
    private ImageButton imgbtnDown;
    private ImageButton imgbtnSearch;
    private ImageButton imgbtnMore;
    private Button play;
    private RecyclerView rcvFavoritesong;
    private static MusicAdapter mMusicfavoriteAdapter;
    List<Music> mListfavotiteMusic = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritesong, container, false);

        rcvFavoritesong = view.findViewById(R.id.recyclerviewFavoritesong);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvFavoritesong.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcvFavoritesong.addItemDecoration(itemDecoration);
        mMusicfavoriteAdapter = new MusicAdapter(mListfavotiteMusic, getContext());

        rcvFavoritesong.setAdapter(mMusicfavoriteAdapter);
        // Gọi phương thức để lấy danh sách các bài hát yêu thích từ Firebase
        callApiGetFavoritesong();




        imgbtnBack = view.findViewById(R.id.btn_back);
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager2.setCurrentItem(temp, false);
            }
        });
        imgbtnMore = view.findViewById(R.id.btn_more);
        imgbtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị dialog tùy biến, chưa làm dialog nên chưa bỏ dô
            }
        });
        imgbtnSearch = view.findViewById(R.id.image_Search);
        imgbtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager2.setCurrentItem(5, false);
            }
        });
        imgbtnDown = view.findViewById(R.id.btn_down);
        imgbtnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chưa làm gắn down
            }
        });

        return view;
    }

    // Xử lý sự kiện thích bài hát và thêm vào RecyclerView rcvFavoritesong
    private void handleLikeMusic(Music music) {
        // Kiểm tra xem bài hát đã tồn tại trong danh sách yêu thích chưa
        if (!mListfavotiteMusic.contains(music)) {
            // Nếu chưa tồn tại, thêm bài hát vào danh sách yêu thích
            mListfavotiteMusic.add(music);
            // Thông báo cho adapter đã thay đổi dữ liệu
            mMusicfavoriteAdapter.notifyDataSetChanged();
        } else {
            // Nếu đã tồn tại, xóa bài hát khỏi danh sách yêu thích
            mListfavotiteMusic.remove(music);
        }

    }

    // Phương thức để lấy danh sách các bài hát yêu thích từ Firebase
    public void callApiGetFavoritesong() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music");
        Query query = myRef.orderByChild("like");
        Log.d("tag", myRef.toString());
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if (music != null && music.isLike()) { // Kiểm tra nếu thuộc tính "like" là true
                    // Gọi phương thức xử lý sự kiện thích bài hát

                    handleLikeMusic(music);
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
