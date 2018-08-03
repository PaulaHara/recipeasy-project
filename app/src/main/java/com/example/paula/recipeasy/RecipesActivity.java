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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.paula.recipeasy.database.RecipeLab;
import com.example.paula.recipeasy.models.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipesActivity extends AppCompatActivity {

    private final static String EXTRA_IS_MEAL = "is_meal";
    private final static String EXTRA_IS_DESSERT = "is_dessert";
    private final static String EXTRA_LOGIN_ID = "login_id";

    private ViewPager mViewPager;
    private FragmentPagerAdapter adapterViewPager;
    private UUID loginId;

    public static Intent newIntent(Context packageContext, boolean isMeal, boolean isDessert, UUID loginId){
        Intent intent = new Intent(packageContext, RecipesActivity.class);
        intent.putExtra(EXTRA_IS_MEAL, isMeal);
        intent.putExtra(EXTRA_IS_DESSERT, isDessert);
        intent.putExtra(EXTRA_LOGIN_ID, loginId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        boolean isMeal = (boolean) getIntent().getSerializableExtra(EXTRA_IS_MEAL);
        boolean isDessert = (boolean) getIntent().getSerializableExtra(EXTRA_IS_DESSERT);

        loginId = (UUID) getIntent().getSerializableExtra(EXTRA_LOGIN_ID);

        mViewPager = findViewById(R.id.recipes_view_pager);
        adapterViewPager = new RecipesActivity.MyPagerAdapter(getSupportFragmentManager(), isMeal, isDessert, loginId);
        mViewPager.setAdapter(adapterViewPager);

        PagerTabStrip pagerTabStrip = findViewById(R.id.recipes_page_header);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(Color.RED);
        pagerTabStrip.setTextColor(Color.LTGRAY);
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
                Intent home_intent = RecipePagerActivity.newIntent(getApplicationContext(), loginId);
                startActivity(home_intent);
                return true;
            case R.id.search_recipe:
                Intent intent = SearchRecipeActivity.newIntent(getApplicationContext(), loginId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 1;
        private boolean isMeal;
        private boolean isDessert;
        private UUID loginId;

        public MyPagerAdapter(FragmentManager fragmentManager, boolean isMeal, boolean isDessert, UUID loginId) {
            super(fragmentManager);
            this.isMeal = isMeal;
            this.isDessert = isDessert;
            this.loginId = loginId;
        }

        // Returns total number of pages.
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for a particular page.
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return RecipeListFragment.newInstance(isMeal, isDessert, loginId);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(isMeal && !isDessert) {
                return "Meals";
            }else if(isDessert && !isMeal){
                return "Desserts";
            }
            return "Meals and Desserts";
        }
    }
}
