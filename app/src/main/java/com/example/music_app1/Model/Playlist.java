package com.example.music_app1.Model;

import java.util.List;

public class Playlist {
    private int id;
    private String idUser;
    private List<Music> music;
    private String name;
    private String image;

    public Playlist() {
    }

    public Playlist(int id, String idUser, List<Music> music, String name, String image) {
        this.id = id;
        this.idUser = idUser;
        this.music = music;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
