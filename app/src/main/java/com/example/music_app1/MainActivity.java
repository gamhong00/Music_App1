package com.example.music_app1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_app1.DataLocal.DataLocalManager;
import com.example.music_app1.adapter.SlideAdapter;
import com.example.music_app1.adapter.ViewPageMusicAdapter;

import com.example.music_app1.View.User_Fragment;
import com.example.music_app1.adapter.ViewPagerAdapter;
import com.example.music_app1.View.favoritesong_Fragment;
//import com.example.music_app1.adapter.PlaylistAdapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    public static int temp;
    public static ViewPager2 mViewPager2;
    public static ViewPager2 mViewPagerMusic;
    public static BottomNavigationView mBottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataLocalManager.init(getApplicationContext());

        mViewPager2 = findViewById(R.id.view_pager);
        mViewPagerMusic = findViewById(R.id.view_pager_music);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mViewPager2.setAdapter(adapter);

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
                    mViewPager2.setCurrentItem(0, false);
                    temp = 0;

                    return true;
                } else if (id == R.id.menu_explore) {
                    mViewPager2.setCurrentItem(1, false);
                    temp = 1;
                    return true;
                } else if (id == R.id.menu_zingchat) {
                    mViewPager2.setCurrentItem(2, false);
                    temp = 2;
                    return true;
                } else if (id == R.id.menu_user) {
                    mViewPager2.setCurrentItem(3, false);
                    temp = 3;
                    return true;
                }

                return false;
            }
        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdView mAdView = findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);

                InterstitialAd.load(MainActivity.this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                                // Hiển thị quảng cáo
                                interstitialAd.show(MainActivity.this);
                                // Ẩn AdView nếu tắt quảng cáo
                                mAdView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d("TAG", "Interstitial ad failed to load: " + loadAdError.getMessage());

                            }
                        });

            }

        });
    }

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics());
    }
}