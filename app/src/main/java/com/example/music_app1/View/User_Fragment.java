package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    private TextView name_user, email_user;

    private Button btn_sign_out;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_, container, false);
        // Inflate the layout for this fragment

        initUI(view);
        showUserInformation();

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

        setSignOutClickListener();
        return view;
    }

    private void initUI(View view) {
        avatar_user = view.findViewById(R.id.avatar_user);
        name_user = view.findViewById(R.id.name_user);
        email_user = view.findViewById(R.id.email_user);

        btn_sign_out=view.findViewById(R.id.btn_sign_out);
    }

    private void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            return;

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name == null) {
            name_user.setVisibility(getView().GONE);
        }
        else{
            name_user.setVisibility(getView().VISIBLE);
            name_user.setText(name);
        }

        email_user.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(avatar_user);
    }

    private void setSignOutClickListener() {
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}