package com.example.paula.recipeasy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.paula.recipeasy.database.RecipeIngredientLab;
import com.example.paula.recipeasy.database.RecipeLab;
import com.example.paula.recipeasy.models.Recipe;
import com.example.paula.recipeasy.models.RecipeIngredient;

import java.util.List;
import java.util.UUID;

public class RecipeDetail extends AppCompatActivity {

    private static final String EXTRA_RECIPE_ID = "recipe_id";

    private Recipe mRecipe;

    public static Intent newIntent(Context packageContext, UUID id){
        Intent intent = new Intent(packageContext, RecipeDetail.class);
        intent.putExtra(EXTRA_RECIPE_ID, id);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe);

        UUID recipeId = (UUID) getIntent().getSerializableExtra(EXTRA_RECIPE_ID);

        if(recipeId != null) {
            mRecipe = RecipeLab.get(getApplicationContext()).getRecipe(recipeId);
        }

        TextView mRecipeName = findViewById(R.id.recipe_title);
        TextView mDuration = findViewById(R.id.duration);
        TextView mPortions = findViewById(R.id.portion);
        ImageView mRecipeImg = findViewById(R.id.recipe_img);
        TableLayout mTableIngredients = findViewById(R.id.details_table_ingredients_name);
        TextView mInstructions = findViewById(R.id.instructions);
        LinearLayout mReviewStars = findViewById(R.id.review_stars);

        mRecipeName.setText(mRecipe.getName());
        mDuration.setText(mRecipe.getDuration()+" min");
        mPortions.setText(mRecipe.getPortions()+" prt");

        byte[] foodImg = mRecipe.getPhoto();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImg, 0, foodImg.length);
        mRecipeImg.setImageBitmap(ImageAndSizeUtils.resize(bitmap, getResources()));

        List<RecipeIngredient> recipeIngr = RecipeLab.get(getApplicationContext()).getIngredientsQttMeasure(mRecipe.getId());
        for(int index = 0; index < recipeIngr.size(); index++) {
            TableRow row = new TableRow(getApplicationContext());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            TextView text = new TextView(getApplicationContext());
            text.setText(recipeIngr.get(index).getName());
            text.setLayoutParams(new TableRow.LayoutParams(ImageAndSizeUtils.getWidth(150, getResources()),
                    TableRow.LayoutParams.WRAP_CONTENT));
            text.setTextColor(Color.LTGRAY);

            TextView qtt = new TextView(getApplicationContext());
            qtt.setText(recipeIngr.get(index).getQtt() + " " + recipeIngr.get(index).getMeasure());
            qtt.setLayoutParams(new TableRow.LayoutParams(ImageAndSizeUtils.getWidth(70, getResources()),
                    TableRow.LayoutParams.WRAP_CONTENT));
            qtt.setTextColor(Color.LTGRAY);

            row.addView(text);
            row.addView(qtt);
            mTableIngredients.addView(row);
        }

        mInstructions.setText(mRecipe.getInstruction());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_recipe, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.remove_recipe:
                RecipeLab.get(getApplicationContext()).deleteRecipe(mRecipe.getId());
                RecipeIngredientLab.get(getApplicationContext()).deleteRecipeIngre(mRecipe.getId());
                Toast.makeText(getApplicationContext(), "Delete Recipe", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RecipeDetail.this, RecipePagerActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
