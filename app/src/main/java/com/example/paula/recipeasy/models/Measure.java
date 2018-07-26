package com.example.paula.recipeasy.models;

import java.util.UUID;

public class Measure {

    private UUID mId;
    private String mMeasure;

    public Measure(){
        this(UUID.randomUUID());
    }

    public Measure(UUID id){
        setId(id);
    }

    public Measure(String measure){
        this(UUID.randomUUID());
        setMeasure(measure);
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String measure) {
        mMeasure = measure;
    }

    @Override
    public String toString() {
        return getMeasure();
    }
}
