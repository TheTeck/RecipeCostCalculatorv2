package com.teckemeyer.recipecostcalculatorv2;

import android.util.Log;

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

    private Pantry thePantry = Pantry.getInstance();
    ArrayList<Ingredient> theIngredients = thePantry.getIngredients();

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

    public float calculateIngredientCost(IngredientPortion ip) {

        Ingredient thisIngredient = null;

        // Find the appropriate Ingredient from the pantry for this IngredientPortion
        for (Ingredient anIngredient : theIngredients) {
            if (anIngredient.getID() == ip.getIngredientID()) {
                thisIngredient = anIngredient;
                break;
            }
        }

        if ( thisIngredient != null) {
            // Calculates ingredients based in units
            if (ip.getUsedUnit().equals("units")) {
                // Cost per unit
                float initialCost = ((float) thisIngredient.getCost() / ((float) thisIngredient.getYield() / 100f)) / thisIngredient.getAmount();
                float newCost = initialCost * ip.getUsedAmount() / ip.getUsedAmountDivider();

                return newCost;

                // Calculates based on weight (imperial)
            } else if (ip.getUsedUnit().equals("ounces") || ip.getUsedUnit().equals("pounds")) {
                int divisor;
                // Cost per ounce
                if (thisIngredient.getUnit().equals("ounces")) {
                    divisor = 1;
                } else {
                    divisor = 16;
                }
                float initialCost = ((float) thisIngredient.getCost() / ((float) thisIngredient.getYield() / 100f)) / (divisor * thisIngredient.getAmount());

                int multiplier;
                if (ip.getUsedUnit().equals("ounces")) {
                    multiplier = 1;
                } else {
                    multiplier = 16;
                }
                float newCost = initialCost * ip.getUsedAmount() * multiplier / ip.getUsedAmountDivider();

                return newCost;

                // Calculates based on weight (metric)
            } else if (ip.getUsedUnit().equals("milligrams") || ip.getUsedUnit().equals("grams") || ip.getUsedUnit().equals("kilograms")) {
                int divisor;
                // Cost per gram
                if (thisIngredient.getUnit().equals("milligrams") || thisIngredient.getUnit().equals("grams")) {
                    divisor = 1;
                } else {
                    divisor = 1000;
                }

                float initialCost = ((float) thisIngredient.getCost() / ((float) thisIngredient.getYield() / 100f)) / (divisor * thisIngredient.getAmount());

                if (thisIngredient.getUnit().equals("milligrams")) {
                    initialCost *= 1000;
                }

                Log.i("Initial Cost", String.valueOf(((float) thisIngredient.getCost() / ((float) thisIngredient.getYield() / 100f)) / (divisor * thisIngredient.getAmount())));

                int multiplier;
                if (ip.getUsedUnit().equals("milligrams") || ip.getUsedUnit().equals("grams")) {
                    multiplier = 1;
                } else {
                    multiplier = 1000;
                }
                float newCost = initialCost * ip.getUsedAmount() * multiplier / ip.getUsedAmountDivider();

                if (ip.getUsedUnit().equals("milligrams")) {
                    newCost /= 1000;
                }

                if (newCost <= 0.01f) {
                    newCost = 0.01f;
                }

                Log.i("New Cost", String.valueOf(initialCost * ip.getUsedAmount() * multiplier / ip.getUsedAmountDivider()));

                return newCost;

                // Calculates bases on volume
            } else {
                float divisor;
                switch (thisIngredient.getUnit()) {
                    case "liquid ounces":
                        divisor = 1;
                        break;
                    case "teaspoons":
                        divisor = 0.166667f;
                        break;
                    case "tablespoons":
                        divisor = 0.5f;
                        break;
                    case "cups":
                        divisor = 8;
                        break;
                    case "pints":
                        divisor = 16;
                        break;
                    case "quarts":
                        divisor = 32;
                        break;
                    case "milliliters":
                        divisor = 0.033814f;
                        break;
                    case "liters":
                        divisor = 33.814f;
                        break;
                    case "half-gallons":
                        divisor = 64;
                        break;
                    case "gallons":
                        divisor = 128;
                        break;
                    default:
                        divisor = 1;
                        break;
                }
                float initialCost = ((float) thisIngredient.getCost() / ((float) thisIngredient.getYield() / 100f)) / (divisor * thisIngredient.getAmount());
                Log.i("Initial Cost", String.valueOf(((float) thisIngredient.getCost() / ((float) thisIngredient.getYield() / 100f)) / (divisor * thisIngredient.getAmount())));

                float multiplier;
                switch (ip.getUsedUnit()) {
                    case "liquid ounces":
                        multiplier = 1;
                        break;
                    case "teaspoons":
                        multiplier = 0.166667f;
                        break;
                    case "tablespoons":
                        multiplier = 0.5f;
                        break;
                    case "cups":
                        multiplier = 8.11537f;
                        break;
                    case "pints":
                        multiplier = 16;
                        break;
                    case "quarts":
                        multiplier = 32;
                        break;
                    case "milliliters":
                        multiplier = 0.033814f;
                        break;
                    case "liters":
                        multiplier = 33.814f;
                        break;
                    case "half-gallons":
                        multiplier = 64;
                        break;
                    case "gallons":
                        multiplier = 128;
                        break;
                    default:
                        multiplier = 1;
                        break;
                }
                float newCost = initialCost * ip.getUsedAmount() * multiplier / ip.getUsedAmountDivider();

                Log.i("New Cost", String.valueOf(initialCost * ip.getUsedAmount() * multiplier / ip.getUsedAmountDivider()));

                return newCost;
            }
        }
        return 0.00f;
    }
}

