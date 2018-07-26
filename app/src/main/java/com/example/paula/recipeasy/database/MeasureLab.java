package com.example.paula.recipeasy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.paula.recipeasy.database.CursorWrapper.IngredientCursorWrapper;
import com.example.paula.recipeasy.database.CursorWrapper.MeasureCursorWrapper;
import com.example.paula.recipeasy.database.CursorWrapper.RecipeCursorWrapper;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.IngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.MeasureTable;
import com.example.paula.recipeasy.models.Ingredient;
import com.example.paula.recipeasy.models.Measure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MeasureLab extends DatabaseActions {

    private static MeasureLab sDatabaseActions;

    public static MeasureLab get(Context context){
        if(sDatabaseActions == null){
            sDatabaseActions = new MeasureLab(context);
        }
        return sDatabaseActions;
    }

    public MeasureLab(Context context) {
        super(context);
    }

    private static ContentValues getMeasureValues(Measure measure){
        ContentValues values = new ContentValues();
        values.put(MeasureTable.Cols.UUID, measure.getId().toString());
        values.put(MeasureTable.Cols.MEASURE, measure.getMeasure().toString());

        return values;
    }

    private MeasureCursorWrapper queryMeasure(String whereClause, String[] whereArgs){
        Cursor cursor = getDatabase().query(
                MeasureTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new MeasureCursorWrapper(cursor);
    }

    public void addMeasure(Measure measure){
        ContentValues values = getMeasureValues(measure);

        getDatabase().insert(MeasureTable.NAME, null, values);
    }

    public void deleteMeasure(UUID id){
        String uuidString = id.toString();

        getDatabase().delete(MeasureTable.NAME, MeasureTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public void updateIngredient(Measure measure){
        String uuidString = measure.getId().toString();
        ContentValues values = getMeasureValues(measure);

        getDatabase().update(MeasureTable.NAME, values, MeasureTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public Measure getMeasure(UUID id){
        MeasureCursorWrapper cursor = queryMeasure(
                MeasureTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getMeasure();
        } finally {
            cursor.close();
        }
    }

    public Measure getMeasureByName(String name){
        MeasureCursorWrapper cursor = queryMeasure(
                MeasureTable.Cols.MEASURE + " = ? ",
                new String[] {name}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getMeasure();
        } finally {
            cursor.close();
        }
    }

    public List<Measure> getMeasures(){
        List<Measure> measures = new ArrayList<>();

        MeasureCursorWrapper cursor = queryMeasure(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                measures.add(cursor.getMeasure());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return measures;
    }
}
