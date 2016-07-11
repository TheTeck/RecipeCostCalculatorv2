package com.teckemeyer.recipecostcalculatorv2;

import java.util.ArrayList;

/**
 * Created by John on 6/12/2016.
 */
public class CurrentRecipe {

    private ArrayList<IngredientPortion> theList = new ArrayList<>();

    private static CurrentRecipe ourInstance = new CurrentRecipe();

    public static CurrentRecipe getInstance() {
        return ourInstance;
    }

    private CurrentRecipe() {
    }

    public ArrayList<IngredientPortion> getIngredientList() {
        return theList;
    }
}
