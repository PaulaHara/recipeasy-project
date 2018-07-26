package com.example.paula.recipeasy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.paula.recipeasy.database.CursorWrapper.IngredientCursorWrapper;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.IngredientTable;
import com.example.paula.recipeasy.models.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngredientLab extends DatabaseActions {

    private static IngredientLab sDatabaseActions;

    public static IngredientLab get(Context context){
        if(sDatabaseActions == null){
            sDatabaseActions = new IngredientLab(context);
        }
        return sDatabaseActions;
    }

    public IngredientLab(Context context) {
        super(context);
    }

    private static ContentValues getIngredientValues(Ingredient ingredient){
        ContentValues values = new ContentValues();
        values.put(IngredientTable.Cols.UUID, ingredient.getId().toString());
        values.put(IngredientTable.Cols.INGREDIENT, ingredient.getIngredient().toString());

        return values;
    }

    private IngredientCursorWrapper queryIngredient(String whereClause, String[] whereArgs){
        Cursor cursor = getDatabase().query(
                IngredientTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new IngredientCursorWrapper(cursor);
    }

    public void addIngredient(Ingredient ingredient){
        ContentValues values = getIngredientValues(ingredient);

        getDatabase().insert(IngredientTable.NAME, null, values);
    }

    public void deleteIngredient(UUID id){
        String uuidString = id.toString();

        getDatabase().delete(IngredientTable.NAME, IngredientTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public void updateIngredient(Ingredient ingredient){
        String uuidString = ingredient.getId().toString();
        ContentValues values = getIngredientValues(ingredient);

        getDatabase().update(IngredientTable.NAME, values, IngredientTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public List<Ingredient> getIngredientByName(String name){
        List<Ingredient> ingredients = new ArrayList<>();

        String sql = "SELECT ingredient FROM ingredients WHERE ingredient LIKE '%"+name.toLowerCase()+"%'";
        Cursor cursor = getDatabase().rawQuery(sql, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String ingredientName = cursor.getString(cursor.getColumnIndex(IngredientTable.Cols.INGREDIENT));
                ingredients.add(new Ingredient(ingredientName));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return ingredients;
    }

    public Ingredient getIngredient(UUID id){
        IngredientCursorWrapper cursor = queryIngredient(
                IngredientTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getIngredient();
        } finally {
            cursor.close();
        }
    }

    public Ingredient getIngredient(String name){
        IngredientCursorWrapper cursor = queryIngredient(
                IngredientTable.Cols.INGREDIENT + " = ?",
                new String[] {name}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getIngredient();
        } finally {
            cursor.close();
        }
    }

    public List<Ingredient> getIngredients(){
        List<Ingredient> ingredients = new ArrayList<>();

        IngredientCursorWrapper cursor = queryIngredient(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                ingredients.add(cursor.getIngredient());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return ingredients;
    }
}
