package com.example.music_app1.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music_app1.View.Download_Fragment;
import com.example.music_app1.View.Explore_Fragment;
import com.example.music_app1.View.Library_Fragment;
import com.example.music_app1.View.Notification_fragment;
import com.example.music_app1.View.Playlist_Fragment;
import com.example.music_app1.View.Search_Fragment;
import com.example.music_app1.View.User_Fragment;
import com.example.music_app1.View.Zingchat_Fragment;
import com.example.music_app1.View.favoritesong_Fragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {

            case 0:
                return new Library_Fragment();
            case 1:
                return new Explore_Fragment();
            case 2:
                return new Zingchat_Fragment();
            case 3:
                return new User_Fragment();
            case 4:
                return new Notification_fragment();
            case 5:
                return new Search_Fragment();
            case 6:
                return new Playlist_Fragment();
            case 7:
                return new favoritesong_Fragment();
            case 8:
                return new Download_Fragment();
            default:
                return new Library_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }
}