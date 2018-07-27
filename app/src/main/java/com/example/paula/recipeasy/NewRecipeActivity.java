package com.example.paula.recipeasy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paula.recipeasy.database.IngredientLab;
import com.example.paula.recipeasy.database.MeasureLab;
import com.example.paula.recipeasy.database.RecipeIngredientLab;
import com.example.paula.recipeasy.database.RecipeLab;
import com.example.paula.recipeasy.models.Ingredient;
import com.example.paula.recipeasy.models.Measure;
import com.example.paula.recipeasy.models.Recipe;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NewRecipeActivity extends AppCompatActivity {

    private ImageView imageViewFood;
    private EditText edtName, edtDuration, edtPortions, edtInstructions;
    private CheckBox edtMeal, edtDessert;
    private AutoCompleteTextView mIngredientName;
    private TableLayout mIngredientTable;
    private Button btnCreate;

    private List<String> ingredientNames = new ArrayList<>();
    private int qttIngredientsSelected;
    private ArrayAdapter<String> myAdapter;

    private IngredientsMethods ingredientsMethods = new IngredientsMethods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        addIngredients();
        addMeasures();

        imageViewFood = findViewById(R.id.imageViewFood);
        edtName = findViewById(R.id.edtName);
        edtDuration = findViewById(R.id.edtDuration);
        edtPortions = findViewById(R.id.edtPortions);
        edtInstructions = findViewById(R.id.edtInstructions);
        edtMeal = findViewById(R.id.checkBox_meal);
        edtDessert = findViewById(R.id.checkBox_dessert);
        mIngredientName = findViewById(R.id.edtIngredients);
        btnCreate = findViewById(R.id.btnCreate);
        mIngredientTable = findViewById(R.id.table_of_ingredients);

        edtMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtMeal.isChecked()){
                    edtDessert.setChecked(false);
                }
            }
        });

        edtDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtDessert.isChecked()){
                    edtMeal.setChecked(false);
                }
            }
        });

        mIngredientName.addTextChangedListener(new CustomAutoCompleteTextChangedListenerNewRecipe(this));
        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getIngredientNames());
        mIngredientName.setAdapter(myAdapter);

        mIngredientName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                qttIngredientsSelected++;
                addIngredientToTable();
            }

            private void addIngredientToTable()  {
                ingredientsMethods.addIngredient(mIngredientName, mIngredientTable,
                        getApplicationContext(), getResources(), NewRecipeActivity.this);
            }
        });

        imageViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        NewRecipeActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(edtMeal.isChecked() || edtDessert.isChecked()) {
                        String type;
                        if (edtMeal.isChecked()) {
                            type = "MEAL";
                        } else {
                            type = "DESSERT";
                        }

                        Recipe recipe = new Recipe(
                                edtName.getText().toString().trim(),
                                Float.parseFloat(edtDuration.getText().toString().trim()),
                                Float.parseFloat(edtPortions.getText().toString().trim()),
                                edtInstructions.getText().toString().trim(),
                                qttIngredientsSelected,
                                type,
                                imageViewToByte(imageViewFood));
                        Log.i("RecipeListFragment", "New Recipe:" + recipe);
                        RecipeLab.get(getApplicationContext()).addRecipe(recipe);

                        TableRow row = (TableRow) mIngredientTable.getChildAt(0);
                        int childCount = mIngredientTable.getChildCount();
                        String ingredient, meas;
                        int qtt;

                        for (int index = 0; index < childCount; index++) {
                            TextView name = (TextView) row.getChildAt(0);
                            EditText quantity = (EditText) row.getChildAt(1);
                            Spinner measure = (Spinner) row.getChildAt(2);
                            ingredient = name.getText().toString();
                            qtt = Integer.parseInt(quantity.getText().toString());
                            meas = measure.getSelectedItem().toString();

                            UUID ingredientId = IngredientLab.get(getApplicationContext())
                                    .getIngredient(ingredient.toLowerCase()).getId();
                            UUID measureId = MeasureLab.get(getApplicationContext()).getMeasureByName(meas).getId();

                            RecipeIngredientLab.get(getApplicationContext())
                                    .addRecipeIngre(recipe.getId(), ingredientId, measureId, qtt);
                        }

                        Toast.makeText(getApplicationContext(), "Recipe added successfully!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewRecipeActivity.this, RecipePagerActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Error: Recipe must have a type!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception error) {
                    Log.e("Create error", error.getMessage());
                }
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ImageAndSizeUtils.resize(
                ((BitmapDrawable)image.getDrawable()).getBitmap(), Resources.getSystem());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewFood.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_area, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_recipe:
                Intent intent = new Intent(this, SearchRecipeActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Search Recipe", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.home:
                Intent home_intent = new Intent(this, RecipePagerActivity.class);
                startActivity(home_intent);
                Toast.makeText(this, "Back home", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void searchIngredients(String name){
        IngredientLab ingredientDB = IngredientLab.get(NewRecipeActivity.this);

        if(name != null && !name.isEmpty()) {
            List<Ingredient> ingredients = ingredientDB.getIngredientByName(name);

            ingredientNames = new ArrayList<>();

            for (Ingredient ingredient : ingredients) {
                ingredientNames.add(ingredient.getIngredient());
            }
        }
    }

    public AutoCompleteTextView getIngredientName() {
        return mIngredientName;
    }

    public List<String> getIngredientNames() {
        return ingredientNames;
    }

    public ArrayAdapter<String> getMyAdapter() {
        return myAdapter;
    }

    public void setMyAdapter(ArrayAdapter<String> myAdapter) {
        this.myAdapter = myAdapter;
    }

    private void addMeasures(){
        if(MeasureLab.get(getApplicationContext()).getMeasures().isEmpty()) {
            MeasureLab measureLab = MeasureLab.get(NewRecipeActivity.this);
            measureLab.addMeasure(new Measure("unity"));
            measureLab.addMeasure(new Measure("gram"));
            measureLab.addMeasure(new Measure("liter"));
            measureLab.addMeasure(new Measure("kilo"));
        }
    }

    private void addIngredients(){
        if(IngredientLab.get(getApplicationContext()).getIngredients().isEmpty()) {
            ingredientsMethods.addIngredients(NewRecipeActivity.this);
            /*IngredientLab ingredientDB = IngredientLab.get(NewRecipeActivity.this);
            ingredientDB.addIngredient(new Ingredient("egg"));
            ingredientDB.addIngredient(new Ingredient("eggplant"));
            ingredientDB.addIngredient(new Ingredient("beef"));
            ingredientDB.addIngredient(new Ingredient("chicken"));
            ingredientDB.addIngredient(new Ingredient("chocolate"));
            ingredientDB.addIngredient(new Ingredient("carrot"));
            ingredientDB.addIngredient(new Ingredient("cheese"));
            ingredientDB.addIngredient(new Ingredient("avocado"));
            ingredientDB.addIngredient(new Ingredient("pepper"));
            ingredientDB.addIngredient(new Ingredient("salt"));
            ingredientDB.addIngredient(new Ingredient("rice"));
            ingredientDB.addIngredient(new Ingredient("beans"));*/
        }
    }
}
