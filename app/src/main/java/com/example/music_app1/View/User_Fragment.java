package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.music_app1.Helper;
import com.example.music_app1.MainActivity;
import com.example.music_app1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class User_Fragment extends Fragment {
    private ImageButton phongtoButton;
    int currentItem = mViewPager2.getCurrentItem();
    private ImageButton btn_notification;

    private ImageButton imgbtn_search;

    LinearLayout linearLayout1, linearLayout2;
    private ImageView avatar_user;
    private TextView name_user, infor_user;

    private String phoneNumber;

    private Button btn_edit_myprofile, btn_sign_out;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_, container, false);
        // Inflate the layout for this fragment

        initUI(view);

        displayUserInfo();

        btn_notification = view.findViewById(R.id.notification);
        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(4, false);
            }
        });


        imgbtn_search = view.findViewById(R.id.search);
        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(5,false);
            }
        });

        LinearLayout btn_layoutUpdatePremium= view.findViewById((R.id.layoutUpdatePremium));

        Boolean isPremium= Helper.getKeyIsPremium(getContext());
        if(!isPremium){
            btn_layoutUpdatePremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), RegisterPremiumActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            btn_layoutUpdatePremium.setVisibility(View.GONE);
        }

        btn_edit_myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(7,false);
            }
        });

        setSignOutClickListener();
        return view;
    }

    private void initUI(View view) {
        avatar_user = view.findViewById(R.id.avatar_user);
        name_user = view.findViewById(R.id.name_user);
        infor_user = view.findViewById(R.id.infor_user);

        btn_edit_myprofile=view.findViewById(R.id.btn_edit_myprofile);
        btn_sign_out=view.findViewById(R.id.btn_sign_out);
    }

    // Hàm để lấy dữ liệu từ Bundle và trả về số điện thoại
    // Hàm để lấy dữ liệu từ Bundle
//    private void getDataFromBundle() {
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            // Lấy số điện thoại từ Bundle
//            phoneNumber = bundle.getString("phone_number");
//        }
//    }

    private void displayUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Xác định cách đăng nhập của người dùng (số điện thoại hoặc email)
            String loginMethod;
            if (user.getPhoneNumber().toString().trim().length()>0) {
                loginMethod = "phone"; // Đăng nhập bằng số điện thoại
            } else {
                loginMethod = "email"; // Đăng nhập bằng email/password
            }

            // Hiển thị thông tin người dùng dựa trên cách đăng nhập
            if (loginMethod.equals("phone")) {
                String phoneNumber = user.getPhoneNumber();

                name_user.setVisibility(getView().GONE);
                // Hiển thị số điện thoại trên giao diện
                infor_user.setText(phoneNumber);

            } else {
                String name= Helper.getKeyName(getContext());
                String email = user.getEmail();
                Uri photoUrl = user.getPhotoUrl();

                if(name == null) {
                    name_user.setVisibility(getView().GONE);
                }
                else{
                    name_user.setVisibility(getView().VISIBLE);
                    name_user.setText(name);
                }
                // Hiển thị email trên giao diện
                infor_user.setText(email);
                Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(avatar_user);

            }
        }
    }

    private void setSignOutClickListener() {
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Helper.clearUser(getContext());
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}