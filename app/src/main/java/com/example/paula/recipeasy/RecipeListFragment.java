package com.example.paula.recipeasy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paula.recipeasy.database.FridgeLab;
import com.example.paula.recipeasy.database.RecipeLab;
import com.example.paula.recipeasy.database.RecipeUserLab;
import com.example.paula.recipeasy.models.Recipe;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class RecipeListFragment extends Fragment {

    private static final String ARG_TYPE_OF_FOOD = "type_of_food";
    private static final String ARG_LOGIN_ID = "login_id";

    private static final String ARG_MEAL = "meal";
    private static final String ARG_DESSERT = "dessert";

    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;
    private String typeOfRecipe;
    private String meal, dessert;
    private UUID loginId;

    public static Fragment newInstance(boolean isMeal, UUID loginId) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_TYPE_OF_FOOD, isMeal);
        args.putString(ARG_LOGIN_ID, loginId.toString());

        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static Fragment newInstance(boolean isMeal, boolean isDessert, UUID loginId) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_MEAL, isMeal);
        args.putBoolean(ARG_DESSERT, isDessert);
        args.putString(ARG_LOGIN_ID, loginId.toString());

        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Serializable sIsMeal = getArguments().getSerializable(ARG_TYPE_OF_FOOD);
        if(sIsMeal != null) {
            boolean isMeal = (boolean) sIsMeal;
            if (isMeal) {
                typeOfRecipe = "MEAL";
            } else {
                typeOfRecipe = "DESSERT";
            }
        }

        Serializable sMeal = getArguments().getSerializable(ARG_MEAL);
        if(sMeal != null) {
            boolean typeMeal = (boolean) sMeal;
            meal = typeMeal ? "MEAL" : null;
        }
        Serializable sDessert = getArguments().getSerializable(ARG_DESSERT);
        if(sDessert != null) {
            boolean typeDessert = (boolean) getArguments().getSerializable(ARG_DESSERT);
            dessert = typeDessert ? "DESSERT" : null;
        }

        Serializable sLoginId = getArguments().getSerializable(ARG_LOGIN_ID);
        if(sLoginId != null){
            loginId = UUID.fromString((String) getArguments().getSerializable(ARG_LOGIN_ID));
        }

    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mRecipeRecyclerView = view.findViewById(R.id.recipe_recycle_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_user_area, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem homeItem = menu.findItem(R.id.home);
        homeItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_recipe:
                Intent intent = SearchRecipeActivity.newIntent(getContext(), loginId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI(){
        List<Recipe> recipes;

        if(typeOfRecipe != null) {
            recipes = RecipeUserLab.get(getActivity()).getUserRecipes(typeOfRecipe, loginId);
        }else{
            if(FridgeLab.get(getActivity()).getFridgeIngredientsQtt() == 0){
                // Search by type
                recipes = RecipeLab.get(getActivity()).getRecipesByType(meal, dessert);
            }else {
                recipes = RecipeLab.get(getActivity()).getRecipeByIngredients(meal, dessert);
            }
        }

        if(mAdapter == null) {
            mAdapter = new RecipeAdapter(recipes, loginId);
            mRecipeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setRecipes(recipes);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private UUID mLoginId;
        private Recipe mRecipe;
        private TextView mTitleTextView;
        private ImageView mRecipeImg;
        private TextView mDuration;
        private TextView mPortion;
        private ImageView mStar5;
        private ImageView mStar4;
        private ImageView mStar3;
        private ImageView mStar2;
        private ImageView mStar1;

        public RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.recipe_title);
            mDuration = itemView.findViewById(R.id.duration);
            mRecipeImg = itemView.findViewById(R.id.recipe_img);
            mPortion = itemView.findViewById(R.id.portion);
            mStar5 = itemView.findViewById(R.id.star_5);
            mStar4 = itemView.findViewById(R.id.star_4);
            mStar3 = itemView.findViewById(R.id.star_3);
            mStar2 = itemView.findViewById(R.id.star_2);
            mStar1 = itemView.findViewById(R.id.star_1);
        }

        public void bind(Recipe recipe, UUID loginId){
            mLoginId = loginId;

            mRecipe = recipe;

            if(mRecipe != null) {
                mTitleTextView.setText(mRecipe.getName());
                mDuration.setText(String.valueOf(mRecipe.getDuration()));
                mPortion.setText(String.valueOf(mRecipe.getPortions()));

                byte[] foodImg = mRecipe.getPhoto();

                Bitmap bitmap = BitmapFactory.decodeByteArray(foodImg, 0, foodImg.length);
                mRecipeImg.setImageBitmap(ImageAndSizeUtils.resize(bitmap, getResources()));
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = RecipeDetail.newIntent(getContext(), mRecipe.getId(), mLoginId);
            startActivity(intent);
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {
        private List<Recipe> mRecipes;
        private UUID mLoginId;

        public RecipeAdapter(List<Recipe> recipes, UUID loginId){
            mRecipes = recipes;
            mLoginId = loginId;
        }

        public void setRecipes(List<Recipe> recipes){
            mRecipes = recipes;
        }

        @NonNull
        @Override
        public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new RecipeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
            Recipe recipe = mRecipes.get(position);
            holder.bind(recipe, mLoginId);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }
    }
}