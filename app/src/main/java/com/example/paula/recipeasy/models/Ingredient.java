package com.example.paula.recipeasy.models;

import java.util.UUID;

public class Ingredient {

    private UUID mId;
    private String mIngredient;

    public Ingredient(){
        this(UUID.randomUUID());
    }

    public Ingredient(UUID id){
        setId(id);
    }

    public Ingredient(String ingredient){
        this(UUID.randomUUID());
        setIngredient(ingredient);
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String mIngredient) {
        this.mIngredient = mIngredient;
    }
}
