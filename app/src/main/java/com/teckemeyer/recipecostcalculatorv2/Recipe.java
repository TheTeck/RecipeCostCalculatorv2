package com.teckemeyer.recipecostcalculatorv2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by John on 6/12/2016.
 */
public class Recipe {

    private String mName;
    private double mTotal;
    private int mSize;
    ArrayList<IngredientPortion> mIngredients;

    private static final String JSON_RECIPE_NAME = "title";
    private static final String JSON_RECIPE_COST = "total";
    private static final String JSON_RECIPE_SIZE = "size";
    private static final String JSON_IP_NAME = "ip_name";
    private static final String JSON_IP_DESCRIPTION = "ip_description";
    private static final String JSON_IP_COST = "ip_cost";
    private static final String JSON_IP_ID ="ip_id";
    private static final String JSON_IP_UNIT = "ip_unit";
    private static final String JSON_IP_AMOUNT = "ip_amount";
    private static final String JSON_IP_DIVIDER = "ip_divider";

    // Constructors ////////////

    // Default
    public Recipe() {
        mSize = 0;
        mIngredients = new ArrayList<>();
    }

    // Constructor with an JSON object
    public Recipe(JSONObject jo) throws JSONException {
        mIngredients = new ArrayList<>();

        mName = jo.getString(JSON_RECIPE_NAME);
        mTotal = jo.getDouble(JSON_RECIPE_COST);
        mSize = jo.getInt(JSON_RECIPE_SIZE);

        // Loops through each ingredient of the recipe
        for (int i = 0; i < mSize; i++) {
            IngredientPortion temp = new IngredientPortion();

            temp.setName(jo.getString(JSON_IP_NAME + i));
            temp.setCost((float) jo.getDouble(JSON_IP_COST + i));
            temp.setFullDescription(jo.getString(JSON_IP_DESCRIPTION + i));
            temp.setIngredientID(jo.getLong(JSON_IP_ID + i));
            temp.setUsedUnit(jo.getString(JSON_IP_UNIT + i));
            temp.setUsedAmount(jo.getInt(JSON_IP_AMOUNT + i));
            temp.setUsedAmountDivider(jo.getInt(JSON_IP_DIVIDER +i));

            mIngredients.add(temp);
        }

    }

    // Packs up the Recipe members into a JSONObject for serialization
    public JSONObject convertToJSON() throws JSONException {

        JSONObject jo = new JSONObject();
        mSize = mIngredients.size();

        jo.put(JSON_RECIPE_NAME, mName);
        jo.put(JSON_RECIPE_COST, mTotal);
        jo.put(JSON_RECIPE_SIZE, mSize);

        int looper = 0;

        // Loops through each ingredient of the recipe
        for (IngredientPortion ip : mIngredients) {
            jo.put(JSON_IP_NAME + looper, ip.getName());
            jo.put(JSON_IP_COST + looper, ip.getCost());
            jo.put(JSON_IP_DESCRIPTION + looper, ip.getFullDescription());
            jo.put(JSON_IP_ID + looper, ip.getIngredientID());
            jo.put(JSON_IP_UNIT + looper, ip.getUsedUnit());
            jo.put(JSON_IP_AMOUNT + looper, ip.getUsedAmount());
            jo.put(JSON_IP_DIVIDER + looper, ip.getUsedAmountDivider());

            looper++;
        }

        return jo;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public double getTotal() {
        return mTotal;
    }

    public void setTotal(double total) {
        this.mTotal = total;
    }

    public ArrayList<IngredientPortion> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<IngredientPortion> mIngredients) {
        this.mIngredients = mIngredients;
    }
}

