package com.example.paula.recipeasy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paula.recipeasy.database.FridgeLab;
import com.example.paula.recipeasy.database.IngredientLab;
import com.example.paula.recipeasy.database.MeasureLab;
import com.example.paula.recipeasy.models.Ingredient;
import com.example.paula.recipeasy.models.Measure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchRecipeActivity extends AppCompatActivity {

    private static final String EXTRA_LOGIN_ID = "login_id";

    private AutoCompleteTextView mIngredientName;
    private CheckBox mMealType;
    private CheckBox mDessertType;
    private TableLayout mIngredientTable;
    private Button mSearchBtn;
    private Button mCleanBtn;

    private UUID loginId;

    private List<String> ingredientNames = new ArrayList<>();
    private ArrayAdapter<String> myAdapter;

    private IngredientsMethods ingredientsMethods = new IngredientsMethods();

    public static Intent newIntent(Context packageContext, UUID id){
        Intent intent = new Intent(packageContext, SearchRecipeActivity.class);
        intent.putExtra(EXTRA_LOGIN_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        loginId = (UUID) getIntent().getSerializableExtra(EXTRA_LOGIN_ID);

        mIngredientTable = findViewById(R.id.table_of_ingredients);
        mIngredientName = findViewById(R.id.autoIngredientName);
        mIngredientName.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getIngredientNames());
        mIngredientName.setAdapter(myAdapter);

        mIngredientName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addIngredientToTable();
            }

            private void addIngredientToTable()  {
                ingredientsMethods.addIngredient(mIngredientName, mIngredientTable,
                        getApplicationContext(), getResources(), SearchRecipeActivity.this);
            }
        });

        mMealType = findViewById(R.id.meal_type);
        mDessertType = findViewById(R.id.dessert_type);

        mSearchBtn = findViewById(R.id.search_button);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int childCount = mIngredientTable.getChildCount();

                // Delete everything from the previous fridge
                FridgeLab.get(getApplicationContext()).deleteFridge();
                
                for(int index = 0; index < childCount; index++){
                    TableRow row = (TableRow) mIngredientTable.getChildAt(index);

                    TextView name = (TextView) row.getChildAt(0);
                    EditText quantity = (EditText) row.getChildAt(1);
                    Spinner measure = (Spinner) row.getChildAt(2);
                    String ingredient = name.getText().toString();
                    int qtt = Integer.parseInt(quantity.getText().toString());
                    String meas = measure.getSelectedItem().toString();

                    UUID ingredientId = IngredientLab.get(getApplicationContext()).getIngredient(ingredient.toLowerCase()).getId();
                    UUID measureId = MeasureLab.get(getApplicationContext()).getMeasureByName(meas).getId();
                    FridgeLab.get(getApplicationContext()).addFridge(ingredientId,measureId,qtt);
                }

                Intent intent = RecipesActivity.newIntent(getApplicationContext(),
                        mMealType.isChecked(), mDessertType.isChecked(), loginId);
                startActivity(intent);
            }
        });

        mCleanBtn = findViewById(R.id.clear_button);
        mCleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIngredientTable.removeAllViews();
                mIngredientName.setText("");
                mMealType.setChecked(false);
                mDessertType.setChecked(false);
            }
        });
    }

    public void searchIngredients(String name){
        IngredientLab ingredientDB = IngredientLab.get(SearchRecipeActivity.this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_area, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.search_recipe);
        searchItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Intent home_intent = new Intent(this, RecipePagerActivity.class);
                startActivity(home_intent);
                Toast.makeText(this, "Back home", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
