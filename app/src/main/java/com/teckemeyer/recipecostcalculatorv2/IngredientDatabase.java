package com.teckemeyer.recipecostcalculatorv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class IngredientDatabase extends AppCompatActivity {

    private ImageView mAddIcon;
    private TextView mAddText;
    private ListView mList;
    private Animation mLeftOut;
    private Button mReturn;
    private int mMessageCode;
    private Intent intent;

    Typeface typeFace;

    IngredientAdapter mIngredientAdapter;

    SharedPreferences prefs;

    private Pantry thePantry = Pantry.getInstance();
    ArrayList<Ingredient> theIngredients = thePantry.getIngredients();

    public void showTutorial() {
        if (mMessageCode == 1) {
            DialogTutorial showTutorial = new DialogTutorial();
            showTutorial.show(getFragmentManager(), "Tutorial");
        } else if (mMessageCode == 3 && mIngredientAdapter.getCount() == 1) {
            DialogTutorial showTutorial = new DialogTutorial();
            showTutorial.show(getFragmentManager(), "Tutorial");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIngredientAdapter.saveIngredients();
        mIngredientAdapter.updateIngredients();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLeftOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);

        if (theIngredients.size() - 1 == mIngredientAdapter.getCount()) {
            mIngredientAdapter.addIngredient(theIngredients.get(theIngredients.size()-1));
        }

        mIngredientAdapter.notifyDataSetChanged();

        mMessageCode = prefs.getInt("messageCode", 0);

        showTutorial();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_database);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/AlegreyaSans-Medium.ttf");

        mAddIcon = (ImageView) findViewById(R.id.imageViewAdd);
        mAddText = (TextView) findViewById(R.id.txtCreateIngredient);
        mList = (ListView) findViewById(R.id.listView);
        mReturn = (Button) findViewById(R.id.btnReturn);

        mIngredientAdapter = new IngredientAdapter();
        mList.setAdapter(mIngredientAdapter);

        prefs = getSharedPreferences("Recipe Cost Calculator", MODE_PRIVATE);

        mAddText.setTypeface(typeFace);
        mReturn.setTypeface(typeFace);


        /* Handle long click on list items
        mList.setLongClickable(true);

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.startAnimation(mLeftOut);
                mIngredientAdapter.deleteIngredient(position);
                return true;
            }
        }); */

        // Handle click on list item to edit ingredient
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditIngredient.setSelected(mIngredientAdapter.getFinalList().get(position));
                Toast.makeText(IngredientDatabase.this, "EditIngredient.setSelected() called with (" + position + ")", Toast.LENGTH_SHORT).show();

                intent = new Intent(IngredientDatabase.this, EditIngredient.class);
                startActivity(intent);
            }
        });

        // Both the icon and the text with create a dialog
        mAddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(IngredientDatabase.this, NewIngredient.class);
                startActivity(intent);
            }
        });

        mAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(IngredientDatabase.this, NewIngredient.class);
                startActivity(intent);
            }
        });

        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void saveNewIngredient(Ingredient i) {
        mIngredientAdapter.addIngredient(i);
        mIngredientAdapter.saveIngredients();
    }

    public ArrayList<Ingredient> sortIngredients(ArrayList<Ingredient> ingredients) {

        Collections.sort(ingredients, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient i1, Ingredient i2) {
                return i1.getName().compareTo(i2.getName());
            }
        });

        return ingredients;
    }

    // Inner class to help bind data to listView ////////////////////////////////
    public class IngredientAdapter extends BaseAdapter {

        private JSONSerializer mSerializer;
        ArrayList<Ingredient> ingredientList = new ArrayList<>();

        // Constructor to load ingredients from memory
        public IngredientAdapter() {
            mSerializer = new JSONSerializer("RecipeCostCalculatorData.json", IngredientDatabase.this.getApplicationContext());

            try {
                ingredientList = mSerializer.loadPantry();

                // Clears and updates the Pantry with the latest ingredientList
                if (ingredientList.size() > 0) {
                    theIngredients.clear();
                    for (Ingredient i : ingredientList) {
                        theIngredients.add(i);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(IngredientDatabase.this, "Error loading ingredients!", Toast.LENGTH_LONG).show();
            }
        }

        public ArrayList<Ingredient> getFinalList() {
            return ingredientList;
        }

        public void updateIngredients() {
            theIngredients.clear();

            for (Ingredient i : ingredientList) {
                theIngredients.add(i);
            }
        }

        public void saveIngredients() {
            try {
                mSerializer.savePantry(ingredientList);
            } catch (Exception e) {
                Toast.makeText(IngredientDatabase.this, "Error saving ingredients!", Toast.LENGTH_LONG).show();
            }
        }

        // Removes an ingredient from Pantry at position i
        public void deleteIngredient(int i) {
            ingredientList.remove(i);

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return ingredientList.size();
        }

        @Override
        public Ingredient getItem(int whichItem) {
            return ingredientList.get(whichItem);
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
                view = inflater.inflate(R.layout.pantry_entry, viewGroup, false);
            }

            Ingredient temp = ingredientList.get(whichItem);

            TextView txtName = (TextView) view.findViewById(R.id.txtName);
            TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            ImageView imageViewIcon = (ImageView) view.findViewById(R.id.imageViewIcon);

            txtName.setTypeface(typeFace);
            txtDescription.setTypeface(typeFace);

            switch (temp.getIcon()) {
                case 1:
                    imageViewIcon.setImageResource(R.drawable.ic_meat);
                    break;
                case 2:
                    imageViewIcon.setImageResource(R.drawable.ic_vegetables);
                    break;
                case 3:
                    imageViewIcon.setImageResource(R.drawable.ic_fruit);
                    break;
                case 4:
                    imageViewIcon.setImageResource(R.drawable.ic_cheese);
                    break;
                case 5:
                    imageViewIcon.setImageResource(R.drawable.ic_spice);
                    break;
                case 6:
                    imageViewIcon.setImageResource(R.drawable.ic_other);
                    break;
            }

            String tempCost = String.format("%.2f", temp.getCost() / (temp.getYield() / 100f));
            String tempString = "" + temp.getAmount() + "  " + temp.getUnit()
                    + " @ $" + tempCost + "   (" + temp.getYield() + "% yield)";

            txtName.setText(temp.getName());
            txtDescription.setText(tempString);

            return view;
        }

        // Adds an ingredient to Pantry
        public void addIngredient(Ingredient i) {

            ingredientList.add(i);
            ingredientList = sortIngredients(ingredientList);
            notifyDataSetChanged();
        }
    }
}
