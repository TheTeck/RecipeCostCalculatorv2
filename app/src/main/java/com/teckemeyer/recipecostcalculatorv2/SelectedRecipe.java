package com.teckemeyer.recipecostcalculatorv2;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectedRecipe extends AppCompatActivity {

    private TextView mTitle;
    private TextView mCost;
    private ArrayList<IngredientPortion> mIngredients;
    private ListView mSelectedRecipeList;
    private static Recipe mTemp;
    private SelectRecipeAdapter mAdapter;
    private Button mOKButton;

    private Pantry thePantry = Pantry.getInstance();
    ArrayList<Ingredient> theIngredients = thePantry.getIngredients();

    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_recipe);

        mAdapter = new SelectRecipeAdapter();

        mIngredients = new ArrayList<>();

        mTitle = (TextView) findViewById(R.id.txtTitleSelectedRecipe);
        mCost = (TextView) findViewById(R.id.txtCostSelectedRecipe);
        mSelectedRecipeList = (ListView) findViewById(R.id.listViewSeletedRecipe);
        mOKButton = (Button) findViewById(R.id.btnOK);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/AlegreyaSans-Medium.ttf");

        mTitle.setTypeface(typeFace);
        mCost.setTypeface(typeFace);
        mOKButton.setTypeface(typeFace);

        mTitle.setText(mTemp.getName());

        String s = "$" + String.format("%.2f", mTemp.getTotal());
        mCost.setText(s);

        mIngredients = mTemp.getIngredients();

        mSelectedRecipeList.setAdapter(mAdapter);

        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void setSelected(Recipe recipe) {
        mTemp = recipe;
    }

    // Inner class to handle the ListView ///////////////////////
    public class SelectRecipeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mIngredients.size();
        }

        @Override
        public IngredientPortion getItem(int whichItem) {
            return mIngredients.get(whichItem);
        }

        @Override
        public long getItemId(int whichItem) {
            return whichItem;
        }

        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup) {

            // If view has not been inflated yet
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.selected_recipe_ingredient, viewGroup, false);
            }

            TextView mIngredientName = (TextView) view.findViewById(R.id.txtIngredientName);
            TextView mIngredientCost = (TextView) view.findViewById(R.id.txtIngredientCost);
            TextView mIngredientDescription = (TextView) view.findViewById(R.id.txtIngredientDescription);

            mIngredientName.setTypeface(typeFace);
            mIngredientCost.setTypeface(typeFace);
            mIngredientDescription.setTypeface(typeFace);

            IngredientPortion ip = mIngredients.get(whichItem);
            Ingredient thisIngredient = null;

            for (Ingredient i : theIngredients) {
                if (i.getID() == ip.getIngredientID()) {
                    thisIngredient = i;
                    break;
                }
            }

            mIngredientName.setText(thisIngredient.getName());

            String s = "$" + String.format("%.2f", calculateIngredientCost(mIngredients.get(whichItem)));
            mIngredientCost.setText(s);
            mIngredientDescription.setText(mIngredients.get(whichItem).getFullDescription());

            return view;
        }
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
