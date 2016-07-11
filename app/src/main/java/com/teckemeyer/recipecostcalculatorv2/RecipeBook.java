package com.teckemeyer.recipecostcalculatorv2;

import java.util.ArrayList;

/**
 * Created by John on 6/14/2016.
 */
public class RecipeBook {

    private ArrayList<Recipe> theRecipes = new ArrayList<>();

    private static RecipeBook ourInstance = new RecipeBook();

    public static RecipeBook getInstance() {
        return ourInstance;
    }

    private RecipeBook() {
    }

    public ArrayList<Recipe> getTheRecipes() {
        return theRecipes;
    }
}
