package com.example.music_app1;

public class Playlist {

    private String name;
    private String imageUrl; // Optional
    private String creator;
    private List<Song> songs;

    public Playlist(String name, String creator) {
        this.name = name;
        this.creator = creator;
        this.songs = new ArrayList<>();
    }

    public Playlist(String name, String creator, String imageUrl) {
        this(name, creator);
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public void removeSong(Song song) {
        this.songs.remove(song);
    }

    public int getNumberOfSongs() {
        return songs.size();
    }

    // You can add other methods as needed,
    // like calculating total playlist duration
}

