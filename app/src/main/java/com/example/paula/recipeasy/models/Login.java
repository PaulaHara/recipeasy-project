package com.example.paula.recipeasy.models;

import java.util.UUID;

public class Login {

    private UUID mId;
    private String username;
    private String password;

    public Login(){
        this(UUID.randomUUID());
    }

    public Login(UUID id){
        mId = id;
    }

    public Login(String username, String password){
        this(UUID.randomUUID());
        setUsername(username);
        setPassword(password);
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
