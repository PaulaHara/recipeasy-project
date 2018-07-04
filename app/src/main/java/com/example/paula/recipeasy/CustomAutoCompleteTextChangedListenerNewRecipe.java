package com.example.paula.recipeasy;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

public class CustomAutoCompleteTextChangedListenerNewRecipe implements TextWatcher {

    Context context;

    public CustomAutoCompleteTextChangedListenerNewRecipe(Context context){
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

        // if you want to see in the logcat what the user types
        Log.e("CustomAutoComplete", "User input: " + userInput);

        NewRecipeActivity newRecipeActivity = ((NewRecipeActivity) context);

        // query the database based on the user input
        newRecipeActivity.searchIngredients(userInput.toString());

        // update the adapater
        newRecipeActivity.getMyAdapter().notifyDataSetChanged();
        newRecipeActivity.setMyAdapter(new ArrayAdapter<>(newRecipeActivity,
                android.R.layout.simple_dropdown_item_1line, newRecipeActivity.getIngredientNames()));
        newRecipeActivity.getIngredientName().setAdapter(newRecipeActivity.getMyAdapter());

    }

}