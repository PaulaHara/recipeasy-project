package com.example.paula.recipeasy;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

public class CustomAutoCompleteTextChangedListener implements TextWatcher {

    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        SearchRecipeActivity searchRecipeActivity = ((SearchRecipeActivity) context);

        // query the database based on the user input
        searchRecipeActivity.searchIngredients(userInput.toString());

        // update the adapater
        searchRecipeActivity.getMyAdapter().notifyDataSetChanged();
        searchRecipeActivity.setMyAdapter(new ArrayAdapter<>(searchRecipeActivity,
                android.R.layout.simple_dropdown_item_1line, searchRecipeActivity.getIngredientNames()));
        searchRecipeActivity.getIngredientName().setAdapter(searchRecipeActivity.getMyAdapter());

    }

}