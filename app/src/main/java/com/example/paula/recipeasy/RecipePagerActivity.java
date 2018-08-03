package com.example.paula.recipeasy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.paula.recipeasy.database.RecipeLab;
import com.example.paula.recipeasy.database.RecipeUserLab;
import com.example.paula.recipeasy.models.Recipe;

import java.util.List;
import java.util.UUID;

public class RecipePagerActivity extends AppCompatActivity {

    private static final String EXTRA_LOGIN_ID = "login_id";

    private ViewPager mViewPager;
    private List<Recipe> mRecipes;
    private FragmentPagerAdapter adapterViewPager;
    private FloatingActionButton mNewRecipe;
    private UUID loginId;

    public static Intent newIntent(Context packageContext, UUID id){
        Intent intent = new Intent(packageContext, RecipePagerActivity.class);
        intent.putExtra(EXTRA_LOGIN_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_pager);

        loginId = (UUID) getIntent().getSerializableExtra(EXTRA_LOGIN_ID);

        mRecipes = RecipeUserLab.get(this).getUserRecipes("MEAL", loginId);
        //mRecipes = RecipeLab.get(this).getRecipes();

        mViewPager = findViewById(R.id.recipe_view_pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), mRecipes, loginId);
        mViewPager.setAdapter(adapterViewPager);

        mNewRecipe = findViewById(R.id.addNewRecipe);
        mNewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = NewRecipeActivity.newIntent(getApplicationContext(), loginId);
                startActivity(intent);
            }
        });

        PagerTabStrip pagerTabStrip = findViewById(R.id.recipe_page_header);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(Color.RED);
        pagerTabStrip.setTextColor(Color.DKGRAY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        return true;
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
        List<Recipe> mRecipes;
        UUID mLoginId;

        public MyPagerAdapter(FragmentManager fragmentManager, List<Recipe> recipes, UUID loginId) {
            super(fragmentManager);
            mRecipes = recipes;
            mLoginId = loginId;
        }

        // Returns total number of pages.
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for a particular page.
        @Override
        public Fragment getItem(int position) {
            Log.i("RecipeListFragment", "PagerView:getItem!");
            switch (position) {
                case 0:
                    return RecipeListFragment.newInstance(true, mLoginId);
                case 1:
                    return RecipeListFragment.newInstance(false, mLoginId);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "Meals";
            }
            return "Desserts";
        }

    }
}
