package com.teckemeyer.recipecostcalculatorv2;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by John on 6/8/2016.
 */
public class Ingredient {

    // Class Members
    private String mName;
    private int mAmount;
    private int mAmountDivider;
    private String mUnit;
    private int mIcon = 1;
    private double mCost = 1f;

    private static final String JSON_INGREDIENT_NAME = "ingredient_name";
    private static final String JSON_INGREDIENT_UNIT = "ingredient_unit";
    private static final String JSON_INGREDIENT_AMOUNT = "ingredient_amount";
    private static final String JSON_INGREDIENT_AMOUNT_DIVIDER = "ingredient_amount_divider";
    private static final String JSON_INGREDIENT_ICON = "ingredient_icon";
    private static final String JSON_INGREDIENT_COST = "ingredient_cost";

    // Constructors

    // Constructor used when called with a JSONObject
    public Ingredient (JSONObject jo) throws JSONException {
        mName = jo.getString(JSON_INGREDIENT_NAME);
        mUnit = jo.getString(JSON_INGREDIENT_UNIT);
        mAmount = jo.getInt(JSON_INGREDIENT_AMOUNT);
        mAmountDivider = jo.getInt(JSON_INGREDIENT_AMOUNT_DIVIDER);
        mIcon = jo.getInt(JSON_INGREDIENT_ICON);
        mCost = jo.getDouble(JSON_INGREDIENT_COST);

    }

    // Default constructor for when we create a note
    public Ingredient () {
        // Nothing to do here
    }


    // Packs up the NOTE members into a JSONObject for serialization
    public JSONObject convertToJSON() throws JSONException {

        JSONObject jo = new JSONObject();

        jo.put(JSON_INGREDIENT_NAME, mName);
        jo.put(JSON_INGREDIENT_ICON, mIcon);
        jo.put(JSON_INGREDIENT_COST, mCost);
        jo.put(JSON_INGREDIENT_UNIT, mUnit);
        jo.put(JSON_INGREDIENT_AMOUNT, mAmount);
        jo.put(JSON_INGREDIENT_AMOUNT_DIVIDER, mAmountDivider);

        return jo;
    }

    public double getCost() {
        return mCost;
    }

    public void setCost(float mCost) {
        this.mCost = mCost;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String mUnit) {
        this.mUnit = mUnit;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int mAmount) {
        this.mAmount = mAmount;
    }

    public int getAmountDivider() {
        return mAmountDivider;
    }

    public void setAmountDivider(int mAmountDivider) {
        this.mAmountDivider = mAmountDivider;
    }
}
