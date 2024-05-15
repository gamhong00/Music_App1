package com.example.music_app1.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.music_app1.Helper;
import com.example.music_app1.MainActivity;
import com.example.music_app1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateInfomationActivity extends AppCompatActivity {

    private ImageView img_avatar;
    private EditText edt_fullname, edt_infor_user;
    private Button btn_update_profile;
    private ActivityResultLauncher<String> mGalleryLauncher;
    private ProgressDialog progress;

    private Bitmap bitmapImage;
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
            isLoading(true);
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // Đã tải thành công ảnh và nhận được Bitmap
                    // Bạn có thể sử dụng Bitmap ở đây
                    bitmapImage = bitmap;
                    isLoading(false);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    // Xử lý khi tải ảnh thất bại
                    isLoading(false);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    // Khởi tạo trước khi tải ảnh
                    isLoading(false);
                }
            };

            // Tạo yêu cầu tải ảnh từ Picasso
            RequestCreator requestCreator = Picasso.get().load(photoUrl);

            // Gọi phương thức into() để tải ảnh và chuyển đổi thành Bitmap
            requestCreator.into(target);

            if (photoUrl != null) {
                Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(img_avatar);
            } else {
                // Xử lý khi URL ảnh là null
                Glide.with(this).load(R.drawable.avatar_default).error(R.drawable.avatar_default).into(img_avatar);
                Log.e("MyProfile_Fragment", "Photo URL is null");
            }
        } else {
            // Xử lý khi user là null
            Glide.with(this).load(R.drawable.avatar_default).error(R.drawable.avatar_default).into(img_avatar);
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
        bitmapImage= bitmapImageView;
    }

    private void onClickUpdateProfile() {
        if (edt_infor_user.getText().toString() =="" || edt_fullname.getText().toString() =="" || bitmapImage != null) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        String uidUser = Helper.getKeyUidUser(this);
        try {
            isLoading(true);
            updateImage(uidUser);
        }catch (Exception e){
            isLoading(false);
            Log.e("check", e.toString());
            Toast.makeText(this, "Cập nhật thông tin thất bại", Toast.LENGTH_LONG).show();
        }

    }

    private void updateImage(String uidUser){
        // Tải ảnh lên Firebase Storage và cập nhật thông tin người dùng
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            String fileName = "image-" + System.currentTimeMillis() + ".jpg";
            StorageReference imageRef = storageRef.child("avatars/" + fileName);

            imageRef.putBytes(data)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String photoUrl = uri.toString();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(Uri.parse(photoUrl))
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                // Cập nhật thành công
                                                updateUserPremium(this, uidUser, edt_fullname.getText().toString());
                                            } else {
                                                // Xảy ra lỗi khi cập nhật
                                            }
                                        });
                            }))
                    .addOnFailureListener(exception -> {
                        // Xảy ra lỗi khi tải ảnh lên
                    });
        }
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
                    isLoading(false);
                    Toast.makeText(context, "Cập nhật thông tin thành công", Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(UpdateInfomationActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                isLoading(false);
                Toast.makeText(context, "Lỗi cập nhật tên", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void isLoading(Boolean isLoading){
        if(isLoading){
            progress = ProgressDialog.show(this, "", "Please wait...", true);
        }else{
            progress.dismiss();
        }

    }
}