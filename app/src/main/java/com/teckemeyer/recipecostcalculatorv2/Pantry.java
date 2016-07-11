package com.teckemeyer.recipecostcalculatorv2;

import java.util.ArrayList;

/**
 * Created by John on 6/9/2016.
 */
public class Pantry {
    private static Pantry ourInstance = new Pantry();

    private ArrayList<Ingredient> mIngredients;

    public static Pantry getInstance() {
        return ourInstance;
    }

    private Pantry() {
        mIngredients = new ArrayList<Ingredient>();
    }

    // Access to the array
    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }
}
