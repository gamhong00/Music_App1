package com.example.music_app1;

import static com.example.music_app1.MainActivity.mViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.music_app1.View.RegisterPremiumActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class UpdateInfomationActivity extends AppCompatActivity {

    private ImageView img_avatar;
    private EditText edt_fullname, edt_infor_user;
    private Button btn_update_profile;
    private ActivityResultLauncher<String> mGalleryLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_infomation);
        initUI();
        setUserInformation();
        initListener();

        mGalleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                if (bitmap != null) {
                                    setBitmapImageView(bitmap);
                                } else {
                                    Log.e("MyProfile_Fragment", "Bitmap is null");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    private void initUI(){
        img_avatar = findViewById(R.id.img_avatar);
        edt_fullname = findViewById(R.id.edt_full_name);
        edt_infor_user = findViewById(R.id.edt_infor_user);
        btn_update_profile = findViewById(R.id.btn_update_profile);
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = Helper.getKeyName(this);
        if (user != null) {
            String email = user.getEmail();
            String phoneNumber = user.getPhoneNumber();
            if (email != null) {
                edt_infor_user.setText(email);
            } else if (phoneNumber != null) {
                edt_infor_user.setText(phoneNumber);
            }
            edt_fullname.setText(name);
            Uri photoUrl = user.getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(img_avatar);
            } else {
                // Xử lý khi URL ảnh là null
                Log.e("MyProfile_Fragment", "Photo URL is null");
            }
        } else {
            // Xử lý khi user là null
            Log.e("MyProfile_Fragment", "User is null");
        }
    }

    private void initListener() {
        img_avatar.setOnClickListener(v -> openGallery());
        btn_update_profile.setOnClickListener(v -> onClickUpdateProfile());
        ImageView backUpdateInfomation= findViewById(R.id.backUpdateInfomation);

        backUpdateInfomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void openGallery() {
        mGalleryLauncher.launch("image/*");
    }

    public void setBitmapImageView(Bitmap bitmapImageView){
        img_avatar.setImageBitmap(bitmapImageView);
    }

    private void onClickUpdateProfile() {
        String uidUser = Helper.getKeyUidUser(this);
        updateUserPremium(this, uidUser, edt_fullname.getText().toString());
    }

    private void updateUserPremium(Context context, String valueUID, String name){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        Query query = usersRef.orderByChild("uid").equalTo(valueUID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userID = userSnapshot.getKey();
                    DatabaseReference userRef = usersRef.child(userID);

                    // Cập nhật tên mới
                    userRef.child("name").setValue(name);
                    Boolean isPremium= Helper.getKeyIsPremium(context);
                    Helper.saveUser(context, valueUID, name, isPremium);
                    Toast.makeText(context, "Cập nhật Thông tin thành công", Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(UpdateInfomationActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Lỗi cập nhật premium", Toast.LENGTH_LONG).show();
            }
        });
    }
}