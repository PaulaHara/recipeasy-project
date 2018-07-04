package com.example.paula.recipeasy.database;

import android.content.ContentValues;
import android.content.Context;

import com.example.paula.recipeasy.database.RecipeasyDbSchema.FridgeIngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeIngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeTable;

import java.util.UUID;

public class RecipeIngredientLab extends DatabaseActions {

    private static RecipeIngredientLab sDatabaseActions;

    public static RecipeIngredientLab get(Context context){
        if(sDatabaseActions == null){
            sDatabaseActions = new RecipeIngredientLab(context);
        }
        return sDatabaseActions;
    }

    public RecipeIngredientLab(Context context) {
        super(context);
    }

    private static ContentValues getRecipeIngredientValues(UUID recipe, UUID ingredient, UUID measure, int qtt){
        ContentValues values = new ContentValues();
        values.put(RecipeIngredientTable.Cols.RECIPE_ID, recipe.toString());
        values.put(RecipeIngredientTable.Cols.INGREDIENT_ID, ingredient.toString());
        values.put(RecipeIngredientTable.Cols.MEASURE_ID, measure.toString());
        values.put(RecipeIngredientTable.Cols.QUANTITY, qtt);

        return values;
    }

    public void addRecipeIngre(UUID recipe, UUID ingredient, UUID measure, int qtt){
        ContentValues values = getRecipeIngredientValues(recipe, ingredient, measure, qtt);
        getDatabase().insert(RecipeIngredientTable.NAME, null, values);
    }

    public void deleteRecipeIngre(UUID recipe){
        getDatabase().delete(RecipeIngredientTable.NAME,
                RecipeIngredientTable.Cols.RECIPE_ID+" = '"+recipe+"'", null);
    }
}
