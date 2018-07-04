package com.example.paula.recipeasy.database.CursorWrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.paula.recipeasy.database.RecipeasyDbSchema.IngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeTable;
import com.example.paula.recipeasy.models.Ingredient;
import com.example.paula.recipeasy.models.Recipe;

import java.util.UUID;

public class IngredientCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public IngredientCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Ingredient getIngredient(){
        String uuidString = getString(getColumnIndex(IngredientTable.Cols.UUID));
        String name = getString(getColumnIndex(IngredientTable.Cols.INGREDIENT));

        Ingredient ingredient = new Ingredient(UUID.fromString(uuidString));
        ingredient.setIngredient(name);

        return ingredient;
    }
}
