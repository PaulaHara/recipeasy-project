package com.example.paula.recipeasy.database;

import android.content.ContentValues;
import android.content.Context;

import com.example.paula.recipeasy.database.RecipeasyDbSchema.FridgeIngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeTable;

import java.util.UUID;

public class FridgeLab extends DatabaseActions {

    private static FridgeLab sDatabaseActions;

    public static FridgeLab get(Context context){
        if(sDatabaseActions == null){
            sDatabaseActions = new FridgeLab(context);
        }
        return sDatabaseActions;
    }

    public FridgeLab(Context context) {
        super(context);
    }

    private static ContentValues getRecipeValues(UUID ingredient, UUID measure, int qtt){
        ContentValues values = new ContentValues();
        values.put(FridgeIngredientTable.Cols.INGREDIENT_ID, ingredient.toString());
        values.put(FridgeIngredientTable.Cols.MEASURE_ID, measure.toString());
        values.put(FridgeIngredientTable.Cols.QUANTITY, qtt);

        return values;
    }

    public void addFridge(UUID ingredient, UUID measure, int qtt){
        ContentValues values = getRecipeValues(ingredient, measure, qtt);
        getDatabase().insert(FridgeIngredientTable.NAME, null, values);
    }

    public void deleteFridge(){
        getDatabase().delete(FridgeIngredientTable.NAME, null, null);
    }
}
