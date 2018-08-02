package com.example.paula.recipeasy.database;

public class RecipeasyDbSchema {

    public static final class LoginTable {
        public static final String NAME = "logins";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
        }
    }

    public static final class RecipeTable {
        public static final String NAME = "recipes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String DURATION = "duration";
            public static final String PORTIONS = "portions";
            public static final String INSTRUCTION = "instruction";
            public static final String QTT_INGREDIENTS = "qtt_ingredients";
            public static final String TYPE = "type";
            public static final String PHOTO = "photo";
        }
    }

    public static final class IngredientTable {
        public static final String NAME = "ingredients";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String INGREDIENT = "ingredient";
        }
    }

    public static final class MeasureTable {
        public static final String NAME = "measures";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String MEASURE = "measure";
        }
    }

    public static final class RecipeIngredientTable {
        public static final String NAME = "recipe_ingredient";

        public static final class Cols{
            public static final String RECIPE_ID = "recipe_id";
            public static final String INGREDIENT_ID = "ingredient_id";
            public static final String MEASURE_ID = "measure_id";
            public static final String QUANTITY = "quantity";
        }
    }

    public static final class FridgeIngredientTable {
        public static final String NAME = "fridge_ingredient";

        public static final class Cols{
            public static final String INGREDIENT_ID = "ingredient_id";
            public static final String MEASURE_ID = "measure_id";
            public static final String QUANTITY = "quantity";
        }
    }

    public static final class RecipesUserTable {
        public static final String NAME = "recipes_user";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String RECIPE_ID = "recipe_id";
            public static final String USER_ID = "user_id";
        }
    }
}
