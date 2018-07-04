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
import android.view.View;

import com.example.paula.recipeasy.database.RecipeLab;
import com.example.paula.recipeasy.models.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipesActivity extends AppCompatActivity {

    private final static String EXTRA_IS_MEAL = "is_meal";
    private final static String EXTRA_IS_DESSERT = "is_dessert";

    private ViewPager mViewPager;
    private FragmentPagerAdapter adapterViewPager;

    public static Intent newIntent(Context packageContext, boolean isMeal, boolean isDessert){
        Intent intent = new Intent(packageContext, RecipesActivity.class);
        intent.putExtra(EXTRA_IS_MEAL, isMeal);
        intent.putExtra(EXTRA_IS_DESSERT, isDessert);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        boolean isMeal = (boolean) getIntent().getSerializableExtra(EXTRA_IS_MEAL);
        boolean isDessert = (boolean) getIntent().getSerializableExtra(EXTRA_IS_DESSERT);

        Log.i("RecipeListFragment", "PagerView:onCreate!");

        mViewPager = findViewById(R.id.recipes_view_pager);
        adapterViewPager = new RecipesActivity.MyPagerAdapter(getSupportFragmentManager(), isMeal, isDessert);
        mViewPager.setAdapter(adapterViewPager);

        PagerTabStrip pagerTabStrip = findViewById(R.id.recipes_page_header);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(Color.RED);
        pagerTabStrip.setTextColor(Color.LTGRAY);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 1;
        private boolean isMeal;
        private boolean isDessert;

        public MyPagerAdapter(FragmentManager fragmentManager, boolean isMeal, boolean isDessert) {
            super(fragmentManager);
            this.isMeal = isMeal;
            this.isDessert = isDessert;
        }

        // Returns total number of pages.
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for a particular page.
        @Override
        public Fragment getItem(int position) {
            Log.i("RecipeListFragment", "RecipeActivity:getItem!");
            switch (position) {
                case 0:
                    return RecipeListFragment.newInstance(isMeal, isDessert);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(isMeal) {
                return "Meals";
            }else if(isDessert){
                return "Desserts";
            }
            return "Meals and Desserts";
        }
    }
}
