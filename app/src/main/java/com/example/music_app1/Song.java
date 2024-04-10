package com.example.music_app1;

public class Song {
    private String name;
    private String artist;
    private String link;
    private String image;
    private int listens;
    private int id;

    public Song() {
        // Empty constructor needed for Firebase
    }

    public Song(String name, String artist, String link, String image, int listens, int id) {
        this.name = name;
        this.artist = artist;
        this.link = link;
        this.image = image;
        this.listens = listens;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getListens() {
        return listens;
    }

    public void setListens(int listens) {
        this.listens = listens;
    }

    public int getId() {
        return id;
    }
}
