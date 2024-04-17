package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.music_app1.MainActivity;
import com.example.music_app1.Model.Music;
import com.example.music_app1.Model.Playlist;
import com.example.music_app1.R;
//import com.example.music_app1.adapter.MusicAdapter;
//import com.example.music_app1.adapter.listMusicAdapter;
import com.example.music_app1.adapter.listPlaylistAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Library_Fragment extends Fragment {

    private ImageButton imgbtn_search, add_playlist;
    private RecyclerView recyclerviewPlaylist;
    List<Playlist> mListPlaylist;
    private listPlaylistAdapter mPlaylistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_library_, container, false);

        //
        recyclerviewPlaylist = view.findViewById(R.id.recyclerviewPlaylist1);
        add_playlist = view.findViewById(R.id.add_playlist);

        imgbtn_search = view.findViewById(R.id.search);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewPlaylist.setLayoutManager(linearLayoutManager);
        // hai dong duoi can thi dung, ko thif thoi
        // DividerItemDecoration itemDecoration = new
        // DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        // recyclerviewPlaylist.addItemDecoration(itemDecoration);
        mListPlaylist = new ArrayList<>();

        mPlaylistAdapter = new listPlaylistAdapter(mListPlaylist);
        recyclerviewPlaylist.setAdapter(mPlaylistAdapter);
        callApiGetPlaylists();
        // callApiGetMusics();
        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(5, false);
            }
        });
        // Create AlertDialog (assuming dialog_addplaylist.xml is defined)
        // AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(),
        // R.style.AppTheme_FullscreenDialog);
        // builder.setView(R.layout.dialog_playlist);
        // dialog = builder.create();
        // add_playlist.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View view) {
        // // Trong Library_Fragment, khi người dùng chuyển đến Playlist_Fragment:
        // dialog.show();
        add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo một instance của PlaylistDialogFragment
                PlaylistDialogFragment playlistDialogFragment = new PlaylistDialogFragment();

                // Hiển thị dialog
                playlistDialogFragment.show(getParentFragmentManager(), "playlist_dialog");
                // 1. Thêm Library_Fragment vào Backstack (nếu cần)
                // getFragmentManager().beginTransaction()
                // .replace(R.id.view_pager, new Library_Fragment())
                // .addToBackStack(null)
                // .commit();

                // 2. Khởi tạo Playlist_Fragment
                // Playlist_Fragment playlistFragment = new Playlist_Fragment();

                // 3. Hiển thị Playlist_Fragment
                // getFragmentManager().beginTransaction()
                // .replace(R.id.pageplaylist, playlistFragment)
                // .commit();

            }
        });
        return view;
    }

    // public void callApiGetMusics(){
    // FirebaseDatabase database = FirebaseDatabase.getInstance();
    // DatabaseReference myRef = database.getReference("playlist");
    // Log.d("tagg",myRef.toString());
    // myRef.addChildEventListener(new ChildEventListener() {
    // @Override
    // public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String
    // previousChildName) {
    // Playlist playlist = snapshot.getValue(Playlist.class);
    // if(playlist != null){
    // mListPlaylist.add(playlist);
    // mPlaylistAdapter.notifyDataSetChanged();
    // }
    // }
    //
    // @Override
    // public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
    // previousChildName) {
    //
    // }
    //
    // @Override
    // public void onChildRemoved(@NonNull DataSnapshot snapshot) {
    //
    // }
    //
    // @Override
    // public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String
    // previousChildName) {
    //
    // }
    //
    // @Override
    // public void onCancelled(@NonNull DatabaseError error) {
    // Toast.makeText(getActivity(), "message", Toast.LENGTH_SHORT).show();
    // }
    // });
    // }
    public void callApiGetPlaylists() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("playlist");
        Log.d("tagg", myRef.toString());
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Playlist playlist = snapshot.getValue(Playlist.class);
                if (playlist != null) {
                    mListPlaylist.add(playlist);
                    mPlaylistAdapter.notifyDataSetChanged();
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