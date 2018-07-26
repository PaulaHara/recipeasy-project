package com.example.paula.recipeasy.database.CursorWrapper;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeTable;
import com.example.paula.recipeasy.models.Recipe;

import java.nio.ByteBuffer;
import java.sql.Blob;
import java.util.UUID;

import static android.graphics.BitmapFactory.decodeByteArray;

public class RecipeCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public RecipeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Recipe getRecipe(){
        String uuidString = getString(getColumnIndexOrThrow(RecipeTable.Cols.UUID));
        String name = getString(getColumnIndexOrThrow(RecipeTable.Cols.NAME));
        float duration = getFloat(getColumnIndexOrThrow(RecipeTable.Cols.DURATION));
        float portions = getFloat(getColumnIndexOrThrow(RecipeTable.Cols.PORTIONS));
        String instruction = getString(getColumnIndexOrThrow(RecipeTable.Cols.INSTRUCTION));
        int qttIngredients = getInt(getColumnIndexOrThrow(RecipeTable.Cols.QTT_INGREDIENTS));
        byte[] photo = getBlob(getColumnIndexOrThrow(RecipeTable.Cols.PHOTO));

        Recipe recipe = new Recipe(UUID.fromString(uuidString));
        recipe.setName(name);
        recipe.setDuration(duration);
        recipe.setPortions(portions);
        recipe.setInstruction(instruction);
        recipe.setQttIngredients(qttIngredients);
        recipe.setPhoto(photo);

        return recipe;
    }
}
