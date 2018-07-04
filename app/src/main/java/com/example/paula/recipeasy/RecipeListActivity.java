package com.example.paula.recipeasy;

import android.support.v4.app.Fragment;

import com.example.paula.recipeasy.models.Login;
import com.example.paula.recipeasy.models.Recipe;

public class RecipeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }

}
