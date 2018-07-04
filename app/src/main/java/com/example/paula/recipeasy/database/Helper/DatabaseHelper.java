package com.example.paula.recipeasy.database.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.paula.recipeasy.database.RecipeasyDbSchema;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.FridgeIngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.IngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.LoginTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.MeasureTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeIngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.RecipeTable;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "recipeBase.db";

    // Create Logins table
    private static final String CREATE_LOGINS = "create table "+ LoginTable.NAME +"("
            +"_id integer primary key autoincrement, "
            +LoginTable.Cols.UUID +", "
            +LoginTable.Cols.USERNAME + ", "
            +LoginTable.Cols.PASSWORD + ")";

    // Create Recipes table
    private static final String CREATE_RECIPES = "create table "+ RecipeTable.NAME +"("
            + "_id integer primary key autoincrement, "
            + RecipeTable.Cols.UUID + ", "
            + RecipeTable.Cols.NAME + ", "
            + RecipeTable.Cols.DURATION + " integer, "
            + RecipeTable.Cols.PORTIONS + " integer, "
            + RecipeTable.Cols.INSTRUCTION + ", "
            + RecipeTable.Cols.QTT_INGREDIENTS + " integer, "
            + RecipeTable.Cols.TYPE + ", "
            + RecipeTable.Cols.PHOTO + ")";

    // Create Ingredients table
    private static final String CREATE_INGREDIENTS = "create table "+ IngredientTable.NAME +"("
            + "_id integer primary key autoincrement, "
            + IngredientTable.Cols.UUID + ", "
            + IngredientTable.Cols.INGREDIENT + ")";

    // Create Measure table
    private static final String CREATE_MEASURE = "create table "+ MeasureTable.NAME + "("
            + "_id integer primary key autoincrement, "
            + MeasureTable.Cols.UUID + ", "
            + MeasureTable.Cols.MEASURE + ")";

    // Create Recipe_Ingredient table
    private static final String CREATE_RECIPE_INGREDIENT = "create table "+ RecipeIngredientTable.NAME +"("
            + "_id integer primary key autoincrement, "
            + RecipeIngredientTable.Cols.QUANTITY + " integer, "
            + RecipeIngredientTable.Cols.MEASURE_ID + " string not null REFERENCES "
            + MeasureTable.NAME + "(uuid), "
            + RecipeIngredientTable.Cols.RECIPE_ID + " string not null REFERENCES "
            + RecipeTable.NAME + "(uuid), "
            + RecipeIngredientTable.Cols.INGREDIENT_ID + " string not null REFERENCES "
            + IngredientTable.NAME + "(uuid))";

    // Create Fridge_Ingredient table
    private static final String CREATE_FRIDGE_INGREDIENTS = "create table "+ FridgeIngredientTable.NAME +"("
            + "_id integer primary key autoincrement, "
            + FridgeIngredientTable.Cols.QUANTITY + " integer, "
            + FridgeIngredientTable.Cols.MEASURE_ID + " string not null REFERENCES "
            + MeasureTable.NAME + "(uuid), "
            + FridgeIngredientTable.Cols.INGREDIENT_ID + " string not null REFERENCES "
            + IngredientTable.NAME + "(uuid))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOGINS);
        db.execSQL(CREATE_RECIPES);
        db.execSQL(CREATE_INGREDIENTS);
        db.execSQL(CREATE_MEASURE);
        db.execSQL(CREATE_RECIPE_INGREDIENT);
        db.execSQL(CREATE_FRIDGE_INGREDIENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeIngredientTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FridgeIngredientTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LoginTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredientTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MeasureTable.NAME);
    }
}
