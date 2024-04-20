package com.example.music_app1.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music_app1.View.PlayMusic_Fragment;

public class ViewPageMusicAdapter extends FragmentStateAdapter {
    public ViewPageMusicAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){

            default:
                return new PlayMusic_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
