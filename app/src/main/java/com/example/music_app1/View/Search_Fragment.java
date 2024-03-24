package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.MainActivity.temp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;
import com.example.music_app1.api.ApiService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Search_Fragment extends Fragment {
    private ImageButton imgbtn_quaylai;
    private RecyclerView rcvMusic;
    List<Music> mListMusic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rcvMusic = view.findViewById(R.id.rcv_musics);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvMusic.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcvMusic.addItemDecoration(itemDecoration);
        mListMusic = new ArrayList<>();
        callApiGetMusics();

        imgbtn_quaylai = view.findViewById(R.id.quaylai);
        imgbtn_quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(temp,false);
            }
        });


        return view;
    }

    public void callApiGetMusics(){
        ApiService.apiService.getListMusics().enqueue(new Callback<List<Music>>() {
            @Override
            public void onResponse(@NonNull Call<List<Music>> call, @NonNull Response<List<Music>> response) {
                mListMusic = response.body();
                MusicAdapter musicAdapter = new MusicAdapter(mListMusic);
                rcvMusic.setAdapter(musicAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<Music>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "onFaile", Toast.LENGTH_SHORT).show();
            }
        });
    }


}