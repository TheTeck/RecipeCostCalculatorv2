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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    // Main members
    BookAdapter bookAdapter;
    Button mIngredientDatabase;
    Button mCreateRecipe;
    Intent intent;
    TextView mBookWarning;
    TextView mYourRecipes;
    ImageView mGreenFlower;
    ImageView mPurpleFlower;
    TextView mSplash;
    TextView mApp;
    TextView mCompany;
    Typeface typeFace;
    ImageView mCredits;
    boolean mUpdated;

    private int mMessageCode;

    private ListView mList;

    private Animation mLeftOut;
    private Animation mFadeOut;
    private Animation mSpin1;
    private Animation mSpin2;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private Pantry thePantry;
    ArrayList<Ingredient> theIngredients;

    // Get access to the RecipeBook ArrayList
    RecipeBook theBook = RecipeBook.getInstance();
    ArrayList<Recipe> theRecipes = theBook.getTheRecipes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind data to widgets
        mIngredientDatabase = (Button) findViewById(R.id.btnIngredientDatabase);
        mCreateRecipe = (Button) findViewById(R.id.btnCreateRecipe);
        mBookWarning = (TextView) findViewById(R.id.txtBookWarning);
        mList = (ListView) findViewById(R.id.listViewBook);
        mYourRecipes = (TextView) findViewById(R.id.textViewYourRecipes);
        mGreenFlower = (ImageView) findViewById(R.id.imageViewFlowerGreen);
        mPurpleFlower = (ImageView) findViewById(R.id.imageViewFlowerPurple);
        mSplash = (TextView) findViewById(R.id.textViewSplash);
        mApp = (TextView) findViewById(R.id.txtApp);
        mCompany = (TextView) findViewById(R.id.txtCompany);
        mCredits = (ImageView) findViewById(R.id.ivCredits);

        prefs = getSharedPreferences("Recipe Cost Calculator", MODE_PRIVATE);
        editor = prefs.edit();

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/AlegreyaSans-Medium.ttf");

        mYourRecipes.setTypeface(typeFace);
        mIngredientDatabase.setTypeface(typeFace);
        mCreateRecipe.setTypeface(typeFace);
        mApp.setTypeface(typeFace);
        mCompany.setTypeface(typeFace);

        mUpdated = prefs.getBoolean("updatedIngredient", false);

        // Converts saved data to new schema
        if (!mUpdated) {
            JSONSerializer mSerializer;
            ArrayList<OldIngredient> ingredientList;
            ArrayList<Ingredient> newList = new ArrayList<>();

            mSerializer = new JSONSerializer("RecipeCostCalculatorData.json", MainActivity.this.getApplicationContext());

            try {
                ingredientList = mSerializer.loadOldPantry();

                // Adds a unique ID value to the converted ingredients
                Calendar cal = Calendar.getInstance();
                long rightNow = cal.getTimeInMillis();

                for (int i = 0; i < ingredientList.size(); i++) {
                    Ingredient conversion = new Ingredient();

                    conversion.setName(ingredientList.get(i).getName());
                    conversion.setAmount(ingredientList.get(i).getAmount());
                    conversion.setAmountDivider(ingredientList.get(i).getAmountDivider());
                    conversion.setCost((float) ingredientList.get(i).getCost());
                    conversion.setUnit(ingredientList.get(i).getUnit());
                    conversion.setIcon(ingredientList.get(i).getIcon());
                    conversion.setYield(100);
                    conversion.setID(rightNow + i);

                    newList.add(conversion);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error converting ingredients!", Toast.LENGTH_LONG).show();
            }

            try {
                mSerializer.savePantry(newList);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error saving converted ingredients!", Toast.LENGTH_LONG).show();
            }

            editor.putBoolean("updatedIngredient", true);
        }

        mMessageCode = prefs.getInt("messageCode", 0);
        editor.putInt("messageCode", mMessageCode);
        editor.apply();

        bookAdapter = new BookAdapter();

        mList.setAdapter(bookAdapter);

        mFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        mSpin1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out_spin);
        mSpin2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out_spin_2);
        mGreenFlower.setAnimation(mSpin1);
        mGreenFlower.startAnimation(mSpin1);
        mPurpleFlower.setAnimation(mSpin2);
        mPurpleFlower.startAnimation(mSpin2);
        mSplash.setAnimation(mFadeOut);
        mSplash.startAnimation(mFadeOut);
        mApp.setAnimation(mFadeOut);
        mApp.startAnimation(mFadeOut);
        mCompany.setAnimation(mFadeOut);
        mCompany.startAnimation(mFadeOut);

        mCreateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (theIngredients.size() > 0) {
                    goToCreateRecipe();
                } else {
                    DialogNoIngredients dialog = new DialogNoIngredients();
                    dialog.show(getFragmentManager(), "No Ingredients");
                }
            }
        });

        mCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Credits.class);
                startActivity(intent);
            }
        });

        // Long click to delete an entry in RecipeBook
        mList.setLongClickable(true);

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                view.setAnimation(mLeftOut);
                bookAdapter.deleteRecipe(position);
                return true;
            }
        });

        // Simple click to view the entry in RecipeBook
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectedRecipe.setSelected(bookAdapter.getFinalList().get(position));

                intent = new Intent(MainActivity.this, SelectedRecipe.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        thePantry = Pantry.getInstance();
        theIngredients = thePantry.getIngredients();

        getIngredients();

        mLeftOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);

        if (bookAdapter.getFinalList().size() == 0 && theRecipes.size() == 0) {
            mBookWarning.setVisibility(View.VISIBLE);
        } else {
            mBookWarning.setVisibility(View.INVISIBLE);
        }

        // There is a saved recipe need to be added to bookAdapter.finalRecipes
        if (theRecipes.size() > 0) {
            bookAdapter.addRecipe(theRecipes.get(0));
            theRecipes.clear();
        }

        bookAdapter.notifyDataSetChanged();

        mMessageCode = prefs.getInt("messageCode", 0);

        if (mMessageCode == 0) {
            DialogTutorial showTutorial = new DialogTutorial();
            showTutorial.show(getFragmentManager(), "Tutorial");
        } else if (mMessageCode == 4) {
            DialogTutorial showTutorial = new DialogTutorial();
            showTutorial.show(getFragmentManager(), "Tutorial");
        } else if (mMessageCode == 8 && bookAdapter.getCount() == 1) {
            DialogTutorial showTutorial = new DialogTutorial();
            showTutorial.show(getFragmentManager(), "Tutorial");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        bookAdapter.saveRecipes();
    }

    // Simply open the IngredientDatabase Activity
    public void goToIngredientDatabase (View v) {
        intent = new Intent(this, IngredientDatabase.class);
        startActivity(intent);
    }

    // Opens the Recipe Creation Activity
    public void goToCreateRecipe() {
        intent = new Intent(this, NewRecipe.class);
        startActivity(intent);
    }

    // Gets the ingredients form memory and puts them into the Pantry
    public void getIngredients() {
        JSONSerializer mSerializer;
        mSerializer = new JSONSerializer("RecipeCostCalculatorData.json", MainActivity.this.getApplicationContext());

        try {
            ArrayList<Ingredient> ingredientList = mSerializer.loadPantry();

            theIngredients.clear();

            for (Ingredient i : ingredientList) {
                theIngredients.add(i);
            }
        } catch (Exception e) {
            // Nothing Here
        }
    }

    // Sort the ArrayList of recipes
    public ArrayList<Recipe> sortRecipes(ArrayList<Recipe> recipes) {

        Collections.sort(recipes, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe lhs, Recipe rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        return recipes;
    }

    // Innerclass to handle the ListView //////////////////////////////////////////////////////
    public class BookAdapter extends BaseAdapter {

        private JSONSerializer mSerializer;
        ArrayList<Recipe> finalRecipes = new ArrayList<>();

        public BookAdapter() {
            mSerializer = new JSONSerializer("NewRecipeBook.json", MainActivity.this.getApplicationContext());

            try {
                finalRecipes = mSerializer.loadBook();
            } catch (Exception e) {
                Log.e("Error loading recipes: ", "" + e);
            }
        }

        public void saveRecipes() {

            try {
                mSerializer.saveBook(finalRecipes);
            } catch (Exception e) {
                Log.e("Error saving recipes: ", "" + e);
            }
        }

        public void addRecipe(Recipe r) {

            finalRecipes.add(r);
            finalRecipes = sortRecipes(finalRecipes);
            notifyDataSetChanged();
        }

        public void deleteRecipe(int n) {
            finalRecipes.remove(n);
            notifyDataSetChanged();
        }

        public ArrayList<Recipe> getFinalList() {
            return finalRecipes;
        }

        @Override
        public int getCount() {
            return finalRecipes.size();
        }

        @Override
        public Recipe getItem(int whichItem) {
            return finalRecipes.get(whichItem);
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
                view = inflater.inflate(R.layout.recipe_list_item, viewGroup, false);
            }

            TextView mRecipeName = (TextView) view.findViewById(R.id.txtRecipeNameView);
            TextView mRecipeCost = (TextView) view.findViewById(R.id.txtRecipeCostView);

            mRecipeName.setTypeface(typeFace);

            // Get temporary Recipe that's a reference to the current view Recipe
            Recipe tempRecipe = finalRecipes.get(whichItem);

            // Set the values to the TextViews
            mRecipeName.setText(tempRecipe.getName());

            tempRecipe.setTotal(0.00);

            ArrayList<IngredientPortion> ipList = tempRecipe.getIngredients();

            for (IngredientPortion ip : ipList) {
                tempRecipe.setTotal(tempRecipe.getTotal() + tempRecipe.calculateIngredientCost(ip));
            }


            String tempString = "$" + String.format("%.2f", tempRecipe.getTotal());
            mRecipeCost.setText(tempString);

            return view;
        }
    }
}
