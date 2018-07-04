package com.example.paula.recipeasy.database.CursorWrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.paula.recipeasy.database.RecipeasyDbSchema.IngredientTable;
import com.example.paula.recipeasy.database.RecipeasyDbSchema.MeasureTable;
import com.example.paula.recipeasy.models.Ingredient;
import com.example.paula.recipeasy.models.Measure;

import java.util.UUID;

public class MeasureCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public MeasureCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Measure getMeasure(){
        String uuidString = getString(getColumnIndex(MeasureTable.Cols.UUID));
        String name = getString(getColumnIndex(MeasureTable.Cols.MEASURE));

        Measure measure = new Measure(UUID.fromString(uuidString));
        measure.setMeasure(name);

        return measure;
    }
}
