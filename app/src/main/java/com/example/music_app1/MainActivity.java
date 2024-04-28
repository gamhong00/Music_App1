package com.example.music_app1;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.example.music_app1.DataLocal.DataLocalManager;
import com.example.music_app1.adapter.ViewPageMusicAdapter;

import com.example.music_app1.adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static int temp;
    public static ViewPager2 mViewPager2;
    public static ViewPager2 mViewPagerMusic;
    public static BottomNavigationView mBottomNavigationView;
    public static Context main;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DataLocalManager.init(getApplicationContext());
        main = this;

        mViewPager2 = findViewById(R.id.view_pager);
        mViewPagerMusic = findViewById(R.id.view_pager_music);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mViewPager2.setAdapter(adapter);
         //load trước 3 viewPager
        mViewPager2.setUserInputEnabled(false);



        ViewPageMusicAdapter adapter1 = new ViewPageMusicAdapter(getSupportFragmentManager(), getLifecycle());
        mViewPagerMusic.setAdapter(adapter1);
        mViewPagerMusic.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                ViewGroup.LayoutParams layoutParams = mViewPagerMusic.getLayoutParams();
                layoutParams.height += positionOffsetPixels;
                mViewPagerMusic.setLayoutParams(layoutParams);
            }
        });

        mBottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.menu_library) {
                    mViewPager2.setCurrentItem(0,false);
                    temp = 0;

                    return true;
                } else if (id == R.id.menu_explore) {
                    mViewPager2.setCurrentItem(1,false);
                    temp = 1;
                    return true;
                } else if (id == R.id.menu_zingchat) {
                    mViewPager2.setCurrentItem(2,false);
                    temp = 2;
                    return true;
                } else if (id == R.id.menu_user) {
                    mViewPager2.setCurrentItem(3,false);
                    temp = 3;
                    return true;
                }

                return false;
            }
        });
    }
    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }




}