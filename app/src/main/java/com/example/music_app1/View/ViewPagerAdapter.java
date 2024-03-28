package com.example.music_app1.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){

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
                return new PlayMusic_Fragment();

            default:
                return new Library_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }
}
