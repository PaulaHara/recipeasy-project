package com.example.paula.recipeasy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.paula.recipeasy.database.CursorWrapper.LoginCursorWrapper;
import com.example.paula.recipeasy.database.CursorWrapper.RecipeCursorWrapper;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeIngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipesUserTable;
import com.example.paula.recipeasy.models.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeUserLab extends DatabaseActions {

    private static RecipeUserLab sDatabaseActions;

    public static RecipeUserLab get(Context context){
        if(sDatabaseActions == null){
            sDatabaseActions = new RecipeUserLab(context);
        }
        return sDatabaseActions;
    }

    public RecipeUserLab(Context context) {
        super(context);
    }

    private static ContentValues getRecipeUserValues(UUID recipe, UUID user){
        ContentValues values = new ContentValues();
        values.put(RecipesUserTable.Cols.RECIPE_ID, recipe.toString());
        values.put(RecipesUserTable.Cols.USER_ID, user.toString());

        return values;
    }

    private LoginCursorWrapper query(String whereClause, String[] whereArgs){
        Cursor cursor = getDatabase().query(
                RecipesUserTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new LoginCursorWrapper(cursor);
    }

    public void addRecipe(UUID recipe, UUID user){
        ContentValues values = getRecipeUserValues(recipe, user);
        getDatabase().insert(RecipesUserTable.NAME, null, values);
    }

    public void removeRecipe(UUID recipe, UUID user){
        getDatabase().delete(RecipesUserTable.NAME,
                RecipesUserTable.Cols.RECIPE_ID+" = ? AND "+RecipesUserTable.Cols.USER_ID+" = ?",
                new String[] {recipe.toString(), user.toString()});
    }

    private Recipe getRecipe(Cursor cursor){
        String uuidString = cursor.getString(cursor.getColumnIndex(RecipesUserTable.Cols.RECIPE_ID));
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

    public List<Recipe> getUserRecipes(String type, UUID login){
        List<Recipe> recipes = new ArrayList<>();
        Cursor cursor;

        String query = "SELECT * FROM " + RecipeTable.NAME + " AS recipe "
                + "JOIN " + RecipesUserTable.NAME + " AS recipe_user "
                + "ON recipe_user.recipe_id = recipe.uuid "
                + "WHERE recipe_user.user_id = '"+login+"' "
                + "AND recipe.type = '"+type+"'";

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

    public boolean getUserRecipe(UUID recipeId, UUID loginId){
        LoginCursorWrapper cursor = query(
                RecipesUserTable.Cols.RECIPE_ID + " = ? and "
                        + RecipesUserTable.Cols.USER_ID + " = ?",
                new String[] {recipeId.toString(), loginId.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return false;
            }

            cursor.moveToFirst();
            return true;
        } finally {
            cursor.close();
        }
    }
}
