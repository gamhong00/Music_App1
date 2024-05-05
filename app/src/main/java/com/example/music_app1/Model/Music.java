package com.example.music_app1.Model;

import java.util.HashMap;
import java.util.Map;

public class Music {
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getListens() {
        return listens;
    }

    public void setListens(int listens) {
        this.listens = listens;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    private int id;
    private String name;
    private String artist;
    private String image;
    private String link;

    private int listens;
    private boolean like;



    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("listens", listens);
        return result;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", listens=" + listens +
                '}';
    }
}
