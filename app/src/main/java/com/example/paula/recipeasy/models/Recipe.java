package com.example.paula.recipeasy.models;

import java.util.UUID;

public class Recipe {

    private UUID mId;
    private String mName;
    private float mDuration;
    private float mPortions;
    private String mInstruction;
    private int mQttIngredients;
    private String mType;
    private byte[] mPhoto;

    public Recipe(){
        this(UUID.randomUUID());
    }

    public Recipe(UUID id){
        mId = id;
    }

    public Recipe(String name, float duration, float portions, String instruction, int qttIngredients,
                  String type, byte[] photo){
        this(UUID.randomUUID());
        setName(name);
        setDuration(duration);
        setPortions(portions);
        setInstruction(instruction);
        setQttIngredients(qttIngredients);
        setType(type);
        setPhoto(photo);
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public float getDuration() {
        return mDuration;
    }

    public void setDuration(float duration) {
        this.mDuration = duration;
    }

    public float getPortions() {
        return mPortions;
    }

    public void setPortions(float portions) {
        this.mPortions = portions;
    }

    public String getInstruction() {
        return mInstruction;
    }

    public void setInstruction(String instruction) {
        this.mInstruction = instruction;
    }

    public int getQttIngredients() {
        return mQttIngredients;
    }

    public void setQttIngredients(int qttIngredients) {
        this.mQttIngredients = qttIngredients;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public byte[] getPhoto() {
        return mPhoto;
    }

    public void setPhoto(byte[] photo) {
        mPhoto = photo;
    }

    @Override
    public String toString() {
        return getId()+","+getName()+","+getDuration()+","+getPortions()+","+getInstruction()+","+getType()+","+getPhoto();
    }
}
