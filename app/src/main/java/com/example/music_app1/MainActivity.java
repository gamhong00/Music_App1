package com.example.music_app1;
import static com.example.music_app1.R.id.menu_explore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    public static ViewPager2 mViewPager2;
    private BottomNavigationView mBottomNavigationView;



    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager2 = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mViewPager2.setAdapter(adapter);
        mViewPager2.setOffscreenPageLimit(3); //load trước 3 viewPager

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_library).setChecked(true);
                        break;

                    case 1:
                        mBottomNavigationView.getMenu().findItem(menu_explore).setChecked(true);
                        break;

                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_zingchat).setChecked(true);
                        break;

                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_user).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        mBottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_library) {
                    mViewPager2.setCurrentItem(0,false);
                    return true;
                } else if (id == R.id.menu_explore) {
                    mViewPager2.setCurrentItem(1,false);
                    return true;
                } else if (id == R.id.menu_zingchat) {
                    mViewPager2.setCurrentItem(2,false);
                    return true;
                } else if (id == R.id.menu_user) {
                    mViewPager2.setCurrentItem(3,false);

                    return true;
                }
                return false;
            }
        });

    }
}