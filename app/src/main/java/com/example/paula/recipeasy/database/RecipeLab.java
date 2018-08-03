package com.example.paula.recipeasy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.paula.recipeasy.database.CursorWrapper.MeasureCursorWrapper;
import com.example.paula.recipeasy.database.CursorWrapper.RecipeCursorWrapper;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.FridgeIngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.IngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.MeasureTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeIngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeTable;
import com.example.paula.recipeasy.models.Recipe;
import com.example.paula.recipeasy.models.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeLab extends DatabaseActions {

    private static RecipeLab sDatabaseActions;

    public static RecipeLab get(Context context){
        if(sDatabaseActions == null){
            sDatabaseActions = new RecipeLab(context);
        }
        return sDatabaseActions;
    }

    public RecipeLab(Context context) {
        super(context);
    }

    private static ContentValues getRecipeValues(Recipe recipe){
        ContentValues values = new ContentValues();
        values.put(RecipeTable.Cols.UUID, recipe.getId().toString());
        values.put(RecipeTable.Cols.NAME, recipe.getName());
        values.put(RecipeTable.Cols.DURATION, recipe.getDuration());
        values.put(RecipeTable.Cols.PORTIONS, recipe.getPortions());
        values.put(RecipeTable.Cols.INSTRUCTION, recipe.getInstruction());
        values.put(RecipeTable.Cols.QTT_INGREDIENTS, recipe.getQttIngredients());
        values.put(RecipeTable.Cols.TYPE, recipe.getType());
        values.put(RecipeTable.Cols.PHOTO, recipe.getPhoto());

        return values;
    }

    private RecipeCursorWrapper queryRecipe(String whereClause, String[] whereArgs){
        Cursor cursor = getDatabase().query(
                RecipeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new RecipeCursorWrapper(cursor);
    }

    public void addRecipe(Recipe recipe){
        ContentValues values = getRecipeValues(recipe);

        getDatabase().insert(RecipeTable.NAME, null, values);
    }

    public void deleteRecipe(UUID id){
        String uuidString = id.toString();

        getDatabase().delete(RecipeTable.NAME, RecipeTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public void updateRecipe(Recipe recipe){
        String uuidString = recipe.getId().toString();
        ContentValues values = getRecipeValues(recipe);

        getDatabase().update(RecipeTable.NAME, values, RecipeTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public Recipe getRecipeByName(String name){
        RecipeCursorWrapper cursor = queryRecipe(
                RecipeTable.Cols.NAME + " = ? ",
                new String[] {name}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getRecipe();
        } finally {
            cursor.close();
        }
    }

    public List<Recipe> getRecipeByType(String type){
        List<Recipe> recipes = new ArrayList<>();

        RecipeCursorWrapper cursor = queryRecipe(
                RecipeTable.Cols.TYPE + " = ? ",
                new String[] {type}
        );

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                recipes.add(cursor.getRecipe());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return recipes;
    }

    public List<Recipe> getRecipeByIngredients(String meal, String dessert){
        List<Recipe> recipes = new ArrayList<>();
        Cursor cursor;

        String searchByType = "";
        if(meal != null && dessert == null) {
            searchByType = "recipe.type = '"+meal+"' AND ";
        }else if(meal == null && dessert != null) {
            searchByType = "recipe.type = '"+dessert+"' AND ";
        }

        String query = "SELECT * FROM " + RecipeTable.NAME + " AS recipe "
                + "WHERE "+searchByType+"recipe.qtt_ingredients = ("
                + "SELECT count(*) FROM " + RecipeIngredientTable.NAME + " AS recipe_ingr "
                + "JOIN " + FridgeIngredientTable.NAME + " AS fridge_ingr ON "
                + "fridge_ingr.ingredient_id = recipe_ingr.ingredient_id "
                + "AND recipe_ingr.quantity <= fridge_ingr.quantity "
                + "WHERE recipe.uuid = recipe_ingr.recipe_id)";

        cursor = getDatabase().rawQuery(query, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                recipes.add(getRecipe(cursor));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return recipes;
    }

    public Recipe getRecipe(UUID id){
        RecipeCursorWrapper cursor = queryRecipe(
                RecipeTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getRecipe();
        } finally {
            cursor.close();
        }
    }

    public List<Recipe> getRecipes(){
        List<Recipe> recipes = new ArrayList<>();

        RecipeCursorWrapper cursor = queryRecipe(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                recipes.add(cursor.getRecipe());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return recipes;
    }

    public List<RecipeIngredient> getIngredientsQttMeasure(UUID recipeId){
        List<RecipeIngredient> recipeIngr = new ArrayList<>();
        Cursor cursor;

        String query = "SELECT ingr.ingredient, recipeIngr.quantity, meas.measure FROM "
                + RecipeIngredientTable.NAME + " AS recipeIngr "
                + "JOIN " + RecipeTable.NAME + " AS recipe ON "
                + "recipe.uuid = recipeIngr.recipe_id "
                + "JOIN " + IngredientTable.NAME + " AS ingr ON "
                + "ingr.uuid = recipeIngr.ingredient_id "
                + "JOIN " + MeasureTable.NAME + " AS meas ON "
                + "meas.uuid = recipeIngr.measure_id "
                + "WHERE recipe.uuid = '"+recipeId+"'";

        cursor = getDatabase().rawQuery(query, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                recipeIngr.add(getRecipeIngr(cursor));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return recipeIngr;
    }

    private RecipeIngredient getRecipeIngr(Cursor cursor){
        String name = cursor.getString(cursor.getColumnIndex("ingredient"));
        String measure = cursor.getString(cursor.getColumnIndex("measure"));
        int qtt = cursor.getInt(cursor.getColumnIndex("quantity"));

        RecipeIngredient recipeIngr = new RecipeIngredient(name, qtt, measure);

        return recipeIngr;
    }

    private Recipe getRecipe(Cursor cursor){
        String uuidString = cursor.getString(cursor.getColumnIndex(RecipeTable.Cols.UUID));
        String name = cursor.getString(cursor.getColumnIndex(RecipeTable.Cols.NAME));
        float duration = cursor.getFloat(cursor.getColumnIndex(RecipeTable.Cols.DURATION));
        float portions = cursor.getFloat(cursor.getColumnIndex(RecipeTable.Cols.PORTIONS));
        String instruction = cursor.getString(cursor.getColumnIndex(RecipeTable.Cols.INSTRUCTION));
        int qttIngredients = cursor.getInt(cursor.getColumnIndex(RecipeTable.Cols.QTT_INGREDIENTS));
        byte[] photo = cursor.getBlob(cursor.getColumnIndex(RecipeTable.Cols.PHOTO));

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