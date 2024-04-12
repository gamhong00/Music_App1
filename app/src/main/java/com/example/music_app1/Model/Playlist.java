package com.example.music_app1.Model;

public class Playlist {

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

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
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
    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
    private int id;
    private String idUser;
    private String music;
    private String name;
    private String image;

    private int view;
}
