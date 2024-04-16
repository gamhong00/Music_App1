package com.example.music_app1.View;

import android.content.Context;

public class DataLocalManager {
    private static final String MUSIC_NAME = "MUSIC_NAME";
    private static final String ARTIST_NAME = "ARTIST_NAME";
    private static final String IMAGE_MUSIC = "IMAGE_MUSIC";
    private static final String LINK_MUSIC = "LINK_MUSIC";
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

}
