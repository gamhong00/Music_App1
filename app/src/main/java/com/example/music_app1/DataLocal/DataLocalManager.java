package com.example.music_app1.DataLocal;

import android.content.Context;

import com.example.music_app1.Model.Music;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataLocalManager {
    private static final String MUSIC_NAME = "MUSIC_NAME";
    private static final String ARTIST_NAME = "ARTIST_NAME";
    private static final String IMAGE_MUSIC = "IMAGE_MUSIC";
    private static final String LINK_MUSIC = "LINK_MUSIC";
    private static final String BACKGROUND_COLOR = "BACKGROUND_COLOR";

    private static final String PREF_OBJECT_USER = "PREF_OBJECT_USER";
    private static final String PREF_LIST_MUSIC = "PREF_LIST_MUSIC";
    private static DataLocalManager instance;

    private MySharePreferences mySharePreferences;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.mySharePreferences = new MySharePreferences(context);
    }

    public static DataLocalManager getInstance() {
        if(instance == null ){
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setNameMusic(String name){
        DataLocalManager.getInstance().mySharePreferences.putStringValue(MUSIC_NAME, name);
    }
    public static String getNameMusic(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue(MUSIC_NAME);
    }

    public static void setNameArtist(String name){
        DataLocalManager.getInstance().mySharePreferences.putStringValue(ARTIST_NAME, name);
    }
    public static String getNameArtist(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue(ARTIST_NAME);
    }

    public static void setImageMusic(String name){
        DataLocalManager.getInstance().mySharePreferences.putStringValue(IMAGE_MUSIC, name);
    }
    public static String getImageMusic(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue(IMAGE_MUSIC);
    }

    public static void setLink(String name){
        DataLocalManager.getInstance().mySharePreferences.putStringValue(LINK_MUSIC, name);
    }
    public static String getLink(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue(LINK_MUSIC);
    }

    public static void setMusic(Music music){
        Gson gson = new Gson();
        String strJsonMusic = gson.toJson(music);
        DataLocalManager.getInstance().mySharePreferences.putStringValue(PREF_OBJECT_USER, strJsonMusic);
    }

    public static Music getMusic() {
            String strJsonMusic = DataLocalManager.getInstance().mySharePreferences.getStringValue(PREF_OBJECT_USER);
            Gson gson = new Gson();
            Music music = gson.fromJson(strJsonMusic, Music.class);
            return music;
    }

    public  static  void setListMusic(List<Music> listMusic) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(listMusic).getAsJsonArray();
        String strJsonArray = jsonArray.toString();
        DataLocalManager.getInstance().mySharePreferences.putStringValue(PREF_LIST_MUSIC, strJsonArray);
    }

    public  static  List<Music> getListMusic() {
        String strJsonArray = DataLocalManager.getInstance().mySharePreferences.getStringValue(PREF_LIST_MUSIC);
        List<Music> listMusic = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(strJsonArray);
            JSONObject jsonObject;
            Music music;
            Gson gson = new Gson();
            for (int i =0; i<jsonArray.length();i++) {
                jsonObject = jsonArray.getJSONObject(i);
                music = gson.fromJson(jsonObject.toString(), Music.class);
                listMusic.add(music);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  listMusic;
    }


}
