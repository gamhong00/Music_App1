package com.example.music_app1;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Library_Fragment extends Fragment {

    // @Override
    // public void onAttach(@NonNull Context context) {
    // super.onAttach(context);
    // Log.e("tincoder", "Fragment 1");
    // }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_, container, false);
    }
}
