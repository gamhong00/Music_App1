package com.example.music_app1.Model;

public class User {
    public String uid;

    public String name;
    public boolean isPremium;

    public User(){}

    public User(String uid, String name, boolean isPremium){
        this.uid= uid;
        this.name= name;
        this.isPremium= isPremium;
    }
}
