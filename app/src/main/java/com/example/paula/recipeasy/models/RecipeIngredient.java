package com.example.paula.recipeasy.models;

public class RecipeIngredient {

    private String name;
    private int qtt;
    private String measure;

    public RecipeIngredient(String name, int qtt, String measure){
        setName(name);
        setQtt(qtt);
        setMeasure(measure);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQtt() {
        return qtt;
    }

    public void setQtt(int qtt) {
        this.qtt = qtt;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
