package com.teckemeyer.recipecostcalculatorv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NewRecipe extends AppCompatActivity {

    private RecipeAdapter mRecipeAdapter;
    private ListView mListView;
    private TextView mTotalCost;
    private Button mBtnSubmit;
    private TextView mName;
    private TextView mNameWarning;
    private TextView txtName;
    private TextView txtTotal;
    private TextView txtAddToRecipe;
    private int mMessageCode;

    Typeface typeFace;

    private Animation mLeftOut;

    SharedPreferences prefs;

    private Pantry thePantry = Pantry.getInstance();
    ArrayList<Ingredient> theIngredients = thePantry.getIngredients();

    ArrayList<IngredientPortion> recipeList;
    ArrayList<Recipe> theRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        // Get access to the RecipeBook ArrayList
        RecipeBook theBook = RecipeBook.getInstance();
        theRecipes = theBook.getTheRecipes();

        prefs = getSharedPreferences("Recipe Cost Calculator", MODE_PRIVATE);



        // Load the current Recipe if it exists
        CurrentRecipe mCurrentRecipe = CurrentRecipe.getInstance();
        recipeList = mCurrentRecipe.getIngredientList();

        mTotalCost = (TextView) findViewById(R.id.txtTotalCostValue);
        mListView = (ListView) findViewById(R.id.listViewRecipeIngredients);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmitRecipe);
        mName = (TextView) findViewById(R.id.editRecipeName);
        mNameWarning = (TextView) findViewById(R.id.txtRecipeWarning);
        txtName = (TextView) findViewById(R.id.textView2);
        txtAddToRecipe = (TextView) findViewById(R.id.txtAddToRecipe);
        txtTotal = (TextView) findViewById(R.id.txtTotalCost);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/AlegreyaSans-Medium.ttf");

        mBtnSubmit.setTypeface(typeFace);
        mTotalCost.setTypeface(typeFace);
        txtName.setTypeface(typeFace);
        txtAddToRecipe.setTypeface(typeFace);
        txtTotal.setTypeface(typeFace);


        mRecipeAdapter = new RecipeAdapter();

        mListView.setAdapter(mRecipeAdapter);

        // Handle the Submit button
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipeList.size() > 0) {

                    boolean error = false;

                    if (mName.getText().toString().trim().equals("") || mName.getText().toString().trim().length() == 0) {
                        mNameWarning.setVisibility(View.VISIBLE);
                        error = true;
                    }

                    if (!error) {
                        Recipe thisRecipe = new Recipe();
                        if (mName.getText().length() > 25) {
                            String theName = mName.getText().toString();
                            mName.setText(theName.substring(0,24));
                        }

                        thisRecipe.setName(mName.getText().toString().trim().toUpperCase());

                        float recipeTotal = 0;
                        for (IngredientPortion ip : recipeList) {
                            recipeTotal += ip.getCost();
                            IngredientPortion tempIp = ip;
                            thisRecipe.getIngredients().add(tempIp);
                        }
                        thisRecipe.setTotal(recipeTotal);

                        // Add this finished recipe to the RecipeBook
                        theRecipes.add(thisRecipe);

                        Toast.makeText(NewRecipe.this, "" + thisRecipe.getName() + " added to your recipes", Toast.LENGTH_SHORT).show();

                        // Then clear the current recipe and go back to main page
                        recipeList.clear();
                        mName.setText("");

                        finish();
                    }

                } else { // No recipe to save
                    mName.setText("");
                    finish();
                }
            }
        });

        // Handle long clicks on ingredients
        mListView.setLongClickable(true);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                view.setAnimation(mLeftOut);
                mRecipeAdapter.deleteIngredient(position);

                for (IngredientPortion ip : recipeList) {
                    ip.setCost(mRecipeAdapter.calculateIngredientCost(ip));
                }

                String newTotal = "$" + String.format("%.2f", getTotalCost(recipeList));
                mTotalCost.setText(newTotal);

                return true;
            }
        });
    }

    public ArrayList<IngredientPortion> sortList(ArrayList<IngredientPortion> list) {

        Collections.sort(list, new Comparator<IngredientPortion>() {
            @Override
            public int compare(IngredientPortion ip1, IngredientPortion ip2) {
                return ip1.getName().compareTo(ip2.getName());
            }
        });

        return list;
    }

    public void showTutorial() {
        if (mMessageCode == 5) {
            DialogTutorial showTutorial = new DialogTutorial();
            showTutorial.show(getFragmentManager(), "Tutorial");
        } else if (mMessageCode == 7 && mRecipeAdapter.getCount() == 1) {
            DialogTutorial showTutorial = new DialogTutorial();
            showTutorial.show(getFragmentManager(), "Tutorial");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLeftOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);

        recipeList = sortList(recipeList);

        if (recipeList.size() > 0) {

            for (IngredientPortion ip : recipeList) {
                ip.setCost(mRecipeAdapter.calculateIngredientCost(ip));
            }

            mTotalCost.setText("$" + String.format("%.2f", getTotalCost(recipeList)));

        } else {
            mTotalCost.setText("$0.00");
        }

        mRecipeAdapter.notifyDataSetChanged();

        mMessageCode = prefs.getInt("messageCode", -1);
        showTutorial();
    }

    public float getTotalCost(ArrayList<IngredientPortion> items) {

        float total = 0;

        for (IngredientPortion ip : items) {
            total += ip.getCost();
        }

        return total;
    }

    public void addToNewRecipe(View view) {
        Intent intent = new Intent(this, AddToRecipe.class);
        startActivity(intent);
    }

    public String displayDivision(int input) {
        String output;

        if (input == 1) {
            output = "";
        } else {
            output = "/" + input;
        }

        return output;
    }


    // Innerclass to handle the ListView ///////////////////////////////////////////////
    public class RecipeAdapter extends BaseAdapter {

        ArrayList<IngredientPortion> recipeList;

        public RecipeAdapter() {
            // Load the current Recipe if it exists
            CurrentRecipe mCurrentRecipe = CurrentRecipe.getInstance();
            recipeList = mCurrentRecipe.getIngredientList();
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

        public void deleteIngredient(int position) {
            recipeList.remove(position);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return recipeList.size();
        }

        @Override
        public IngredientPortion getItem(int whichItem) {
            return recipeList.get(whichItem);
        }

        @Override
        public long getItemId(int whichItem) {
            return whichItem;
        }

        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup) {

            int mIcon;
            float mCost;
            String mFullDescription;

            // If view has not been inflated yet
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.recipe_ingredient_entry, viewGroup, false);
            }

            IngredientPortion temp = recipeList.get(whichItem);

            Ingredient thisIngredient = null;

            // Find the appropriate Ingredient from the pantry for this IngredientPortion
            for (Ingredient anIngredient : theIngredients) {
                if (anIngredient.getID() == temp.getIngredientID()) {
                    thisIngredient = anIngredient;
                    break;
                }
            }

            ImageView ivIcon = (ImageView) view.findViewById(R.id.imageViewIngredientIcon);
            TextView txtName = (TextView) view.findViewById(R.id.txtThisIngredient);
            TextView txtDescription = (TextView) view.findViewById(R.id.txtIngredientDescription);

            txtDescription.setTypeface(typeFace);
            txtName.setTypeface(typeFace);

            if (thisIngredient != null) {
                mIcon = thisIngredient.getIcon();

                // Display the correct icon for each ingredient
                switch (mIcon) {
                    case 1:
                        ivIcon.setImageResource(R.drawable.ic_meat);
                        break;
                    case 2:
                        ivIcon.setImageResource(R.drawable.ic_vegetables);
                        break;
                    case 3:
                        ivIcon.setImageResource(R.drawable.ic_fruit);
                        break;
                    case 4:
                        ivIcon.setImageResource(R.drawable.ic_cheese);
                        break;
                    case 5:
                        ivIcon.setImageResource(R.drawable.ic_spice);
                        break;
                    case 6:
                        ivIcon.setImageResource(R.drawable.ic_other);
                        break;
                }

                mCost = calculateIngredientCost(temp);

                mFullDescription = "" + temp.getUsedAmount() + displayDivision(temp.getUsedAmountDivider())
                        + " " + temp.getUsedUnit() + " at $" + String.format("%.2f", mCost);

                txtName.setText(thisIngredient.getName());
                txtDescription.setText(mFullDescription);
            }

            return view;
        }
    }
}
