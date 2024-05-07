package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.music_app1.Helper;
import com.example.music_app1.MainActivity;
import com.example.music_app1.Model.User;
import com.example.music_app1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class MyProfile_Fragment extends Fragment {
    private ImageView img_avatar;
    private EditText edt_fullname, edt_infor_user;
    private Button btn_update_profile;
    private ActivityResultLauncher<String> mGalleryLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile_, container, false);
        initUI(view);
        setUserInformation();
        initListener();

        mGalleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
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

        return view;
    }

    private void initUI(View view){
        img_avatar = view.findViewById(R.id.img_avatar);
        edt_fullname = view.findViewById(R.id.edt_full_name);
        edt_infor_user = view.findViewById(R.id.edt_infor_user);
        btn_update_profile = view.findViewById(R.id.btn_update_profile);
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = Helper.getKeyName(getContext());
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
                Glide.with(requireActivity()).load(photoUrl).error(R.drawable.avatar_default).into(img_avatar);
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
    }

    private void openGallery() {
        mGalleryLauncher.launch("image/*");
    }

    public void setBitmapImageView(Bitmap bitmapImageView){
        img_avatar.setImageBitmap(bitmapImageView);
    }

    private void onClickUpdateProfile() {
        String uidUser = Helper.getKeyUidUser(getContext());
        updateUserPremium(getContext(), uidUser, edt_fullname.getText().toString());
        mViewPager2.setCurrentItem(4, false);
    }

    private void updateUserPremium(Context context,String valueUID, String name){
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Lỗi cập nhật premium", Toast.LENGTH_LONG).show();
            }
        });
    }

}
