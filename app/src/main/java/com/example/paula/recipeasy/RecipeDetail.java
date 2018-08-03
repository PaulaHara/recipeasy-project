package com.example.paula.recipeasy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paula.recipeasy.database.LoginLab;
import com.example.paula.recipeasy.database.RecipeIngredientLab;
import com.example.paula.recipeasy.database.RecipeLab;
import com.example.paula.recipeasy.database.RecipeUserLab;
import com.example.paula.recipeasy.models.Login;
import com.example.paula.recipeasy.models.Recipe;
import com.example.paula.recipeasy.models.RecipeIngredient;

import java.util.List;
import java.util.UUID;

public class RecipeDetail extends AppCompatActivity {

    private static final String EXTRA_RECIPE_ID = "recipe_id";
    private static final String EXTRA_LOGIN_ID = "login_id";

    private Recipe mRecipe;
    private Login mLogin;
    private UUID loginId;
    private ShareActionProvider mShareActionProvider;

    public static Intent newIntent(Context packageContext, UUID recipeId, UUID loginId){
        Intent intent = new Intent(packageContext, RecipeDetail.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(EXTRA_LOGIN_ID, loginId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe);

        UUID recipeId = (UUID) getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        loginId = (UUID) getIntent().getSerializableExtra(EXTRA_LOGIN_ID);

        if(recipeId != null) {
            mRecipe = RecipeLab.get(getApplicationContext()).getRecipe(recipeId);
        }
        if(loginId != null) {
            mLogin = LoginLab.get(getApplicationContext()).getLogin(loginId);
        }

        if(mRecipe != null) {
            TextView mRecipeName = findViewById(R.id.recipe_title);
            TextView mDuration = findViewById(R.id.duration);
            TextView mPortions = findViewById(R.id.portion);
            ImageView mRecipeImg = findViewById(R.id.recipe_img);
            TableLayout mTableIngredients = findViewById(R.id.details_table_ingredients_name);
            TextView mInstructions = findViewById(R.id.instructions);
            LinearLayout mReviewStars = findViewById(R.id.review_stars);

            mRecipeName.setText(mRecipe.getName());
            mDuration.setText(mRecipe.getDuration() + " min");
            mPortions.setText(mRecipe.getPortions() + " prt");

            byte[] foodImg = mRecipe.getPhoto();
            Bitmap bitmap = BitmapFactory.decodeByteArray(foodImg, 0, foodImg.length);
            mRecipeImg.setImageBitmap(ImageAndSizeUtils.resize(bitmap, getResources()));

            List<RecipeIngredient> recipeIngr = RecipeLab.get(getApplicationContext()).getIngredientsQttMeasure(mRecipe.getId());
            for (int index = 0; index < recipeIngr.size(); index++) {
                TableRow row = new TableRow(getApplicationContext());
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                TextView text = new TextView(getApplicationContext());
                text.setText(recipeIngr.get(index).getName());
                text.setLayoutParams(new TableRow.LayoutParams(ImageAndSizeUtils.getWidth(150, getResources()),
                        TableRow.LayoutParams.WRAP_CONTENT));
                text.setTextColor(Color.DKGRAY);

                TextView qtt = new TextView(getApplicationContext());
                qtt.setText(recipeIngr.get(index).getQtt() + " " + recipeIngr.get(index).getMeasure());
                qtt.setLayoutParams(new TableRow.LayoutParams(ImageAndSizeUtils.getWidth(70, getResources()),
                        TableRow.LayoutParams.WRAP_CONTENT));
                qtt.setTextColor(Color.DKGRAY);

                row.addView(text);
                row.addView(qtt);
                mTableIngredients.addView(row);
            }

            mInstructions.setText(mRecipe.getInstruction());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_recipe, menu);

        // Share button
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
        //create the sharing intent
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "here goes your share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Subject");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

        //then set the sharingIntent
        mShareActionProvider.setShareIntent(sharingIntent);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean hasRecipe = RecipeUserLab.get(getApplicationContext()).getUserRecipe(mRecipe.getId(), mLogin.getId());

        MenuItem menuItem;
        if(hasRecipe) {
            menuItem = menu.findItem(R.id.save_recipe);
            menuItem.setVisible(false);
        }else{
            menuItem = menu.findItem(R.id.remove_recipe);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_recipe:
                RecipeLab.get(getApplicationContext()).deleteRecipe(mRecipe.getId());
                RecipeIngredientLab.get(getApplicationContext()).deleteRecipeIngre(mRecipe.getId());
                Toast.makeText(getApplicationContext(), "Recipe deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = RecipePagerActivity.newIntent(getApplicationContext(), loginId);
                startActivity(intent);
                return true;
            case R.id.search_recipe:
                Intent searchIntent = SearchRecipeActivity.newIntent(getApplicationContext(), loginId);
                startActivity(searchIntent);
                return true;
            case R.id.save_recipe:
                RecipeUserLab.get(getApplicationContext()).addRecipe(mRecipe.getId(), mLogin.getId());
                Toast.makeText(getApplicationContext(), "Recipe saved!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.remove_recipe:
                RecipeUserLab.get(getApplicationContext()).removeRecipe(mRecipe.getId(), mLogin.getId());
                Toast.makeText(getApplicationContext(), "Recipe removed!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.home:
                Intent homeIntent = RecipePagerActivity.newIntent(getApplicationContext(), loginId);
                startActivity(homeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
