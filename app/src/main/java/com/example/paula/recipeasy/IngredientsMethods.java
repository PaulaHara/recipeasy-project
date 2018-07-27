package com.example.paula.recipeasy;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.paula.recipeasy.database.IngredientLab;
import com.example.paula.recipeasy.database.MeasureLab;
import com.example.paula.recipeasy.models.Ingredient;
import com.example.paula.recipeasy.models.Measure;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class IngredientsMethods {
    public void addIngredient(AutoCompleteTextView mIngredientName, final TableLayout mIngredientTable,
                              Context context, Resources resources, Activity activity){
        String ingredientName = mIngredientName.getText().toString();

        // return if the input fields are blank
        if (TextUtils.isEmpty(ingredientName)) {
            return;
        }

        TableRow row = new TableRow(context);
        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        TextView text = new TextView(context);
        text.setText(ingredientName);
        text.setLayoutParams(new TableRow.LayoutParams(ImageAndSizeUtils.getWidth(110, resources),
                TableRow.LayoutParams.WRAP_CONTENT));
        text.setTextColor(Color.LTGRAY);

        EditText qtt = new EditText(context);
        qtt.setLayoutParams(new TableRow.LayoutParams(ImageAndSizeUtils.getWidth(50, resources),
                TableRow.LayoutParams.WRAP_CONTENT));
        qtt.setTextColor(Color.LTGRAY);

        Spinner measure = new Spinner(context);
        measure.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        ArrayAdapter<Measure> measureAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, getMeasures(activity));
        measure.setAdapter(measureAdapter);
        measure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.LTGRAY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button remove = new Button(context);
        remove.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_remove));
        remove.setLayoutParams(new TableRow.LayoutParams(ImageAndSizeUtils.getWidth(30, resources),
                ImageAndSizeUtils.getWidth(30, resources)));
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow parent = (TableRow) view.getParent();
                mIngredientTable.removeView(parent);
            }
        });

        row.addView(text);
        row.addView(qtt);
        row.addView(measure);
        row.addView(remove);
        mIngredientTable.addView(row);

        mIngredientName.setText("");
    }

    public void addIngredients(Activity activity){
        try (BufferedReader br = new BufferedReader(new FileReader("Ingredients.txt"))) {
            String line;
            IngredientLab ingredientDB = IngredientLab.get(activity);
            while ((line = br.readLine()) != null) {
                ingredientDB.addIngredient(new Ingredient(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Measure> getMeasures(Activity activity){
        MeasureLab measureLab = MeasureLab.get(activity);
        return measureLab.getMeasures();
    }
}
