package com.example.music_app1;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static int temp;
    public static ViewPager2 mViewPager2;
    private BottomNavigationView mBottomNavigationView;
    private DatabaseReference myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myFirebaseRef = database.getReference(); // Không truyền đối số nào để truy cập vào cơ sở dữ liệu mặc định

        mViewPager2 = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mViewPager2.setAdapter(adapter);
        mViewPager2.setOffscreenPageLimit(3); // Load trước 3 viewPager
        mViewPager2.setUserInputEnabled(false);

        mBottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_library) {
                    mViewPager2.setCurrentItem(0, false);
                    temp = 0;
                    // Ghi dữ liệu vào Firebase Realtime Database
                    myFirebaseRef.child("selected_item").setValue("library");
                    return true;
                } else if (id == R.id.menu_explore) {
                    mViewPager2.setCurrentItem(1, false);
                    temp = 1;
                    // Ghi dữ liệu vào Firebase Realtime Database
                    myFirebaseRef.child("selected_item").setValue("explore");
                    return true;
                } else if (id == R.id.menu_zingchat) {
                    mViewPager2.setCurrentItem(2, false);
                    temp = 2;
                    // Ghi dữ liệu vào Firebase Realtime Database
                    myFirebaseRef.child("selected_item").setValue("zingchat");
                    return true;
                } else if (id == R.id.menu_user) {
                    mViewPager2.setCurrentItem(3, false);
                    temp = 3;
                    // Ghi dữ liệu vào Firebase Realtime Database
                    myFirebaseRef.child("selected_item").setValue("user");
                    return true;
                }
                return false;
            }
        });
    }
}
