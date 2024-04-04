package com.example.music_app1.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPageMusicAdapter extends FragmentStateAdapter {
    public ViewPageMusicAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){

            case 0:
                return new PlayMusic_Fragment();
            default:
                return new PlayMusic_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
