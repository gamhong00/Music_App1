package com.example.music_app1.Model;

import androidx.annotation.NonNull;

import java.util.List;

public class Playlist {
    @NonNull
    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", music=" + music +
                ", name='" + name + '\'' +
                '}';
    }

    private String id;
    private String uid;
    private List<Music> music;
    private String name;

    public Playlist() {
    }

    public Playlist(String id, String uid, List<Music> music, String name) {
        this.id = id;
        this.uid = uid;
        this.music = music;
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Music> getMusic() {
        return music;
    }

    public void setMusic(List<Music> music) {
        this.music = music;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}