package com.example.music_app1.View;

import static com.example.music_app1.MainActivity.mViewPager2;
import static com.example.music_app1.MainActivity.temp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.music_app1.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class Search_Fragment extends Fragment {

    private ImageView img_music;
    private ImageButton imgbtn_quaylai, imgbtn_timkiem;
    private TextView tv_nameMusic, tv_nameCasi;
    private EditText edt_search;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        imgbtn_quaylai = view.findViewById(R.id.quaylai);
        imgbtn_quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(temp,false);
            }
        });
        tv_nameMusic = view.findViewById(R.id.tv_nameMusic);
        tv_nameCasi = view.findViewById(R.id.tv_nameCasi);
        img_music = view.findViewById(R.id.img_music);
        imgbtn_timkiem = view.findViewById(R.id.btn_search);
        edt_search = view.findViewById(R.id.edt_search);
        imgbtn_timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  nameMusic = edt_search.getText().toString();
                getMusic(nameMusic);
            }
        });



        return view;
    }

    private void getMusic (String musicName){
        String uri = "https://ac.zingmp3.vn/v1/web/featured?query=" + musicName;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String imgmusic = response.getJSONObject("data").getJSONArray("items").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("thumb");
                    Picasso.get().load(imgmusic).into(img_music);
                    String title = response.getJSONObject("data").getJSONArray("items").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("title");
                    tv_nameMusic.setText(title);
                    String artist = response.getJSONObject("data").getJSONArray("items").getJSONObject(0).getJSONArray("items").getJSONObject(0).getJSONArray("artists").getJSONObject(0).getString("name");
                    tv_nameCasi.setText(artist);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Enter music", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}