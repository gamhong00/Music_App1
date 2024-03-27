package com.example.music_app1.api;

import com.example.music_app1.Model.Music;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    ApiService apiService = new Retrofit.Builder().baseUrl("https://api.8dzj9bzycq.workers.dev/").addConverterFactory(GsonConverterFactory.create(gson)).build().create(ApiService.class);
    @GET("music")
    Call<List<Music>> getListMusics();

}
