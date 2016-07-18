package com.teckemeyer.recipecostcalculatorv2;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class SelectedRecipe extends AppCompatActivity {

    private TextView mTitle;
    private TextView mCost;
    private ArrayList<IngredientPortion> mIngredients;
    private ListView mSelectedRecipeList;
    private static Recipe mTemp;
    private SelectRecipeAdapter mAdapter;
    private Button mOKButton;
    private ImageView mIVAddIngredient;
    private TextView mTxtAddIngredient;
    private Recipe tempRecipe = new Recipe();

    private Intent intent;

    private Animation mLeftOut;

    private Pantry thePantry = Pantry.getInstance();
    ArrayList<Ingredient> theIngredients = thePantry.getIngredients();

    CurrentRecipe thisRecipe = CurrentRecipe.getInstance();
    ArrayList<IngredientPortion> theRecipe = thisRecipe.getIngredientList();

    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_recipe);

        mAdapter = new SelectRecipeAdapter();

        mTitle = (TextView) findViewById(R.id.txtTitleSelectedRecipe);
        mCost = (TextView) findViewById(R.id.txtCostSelectedRecipe);
        mSelectedRecipeList = (ListView) findViewById(R.id.listViewSeletedRecipe);
        mOKButton = (Button) findViewById(R.id.btnOK);
        mIVAddIngredient = (ImageView) findViewById(R.id.ivAddIngredient);
        mTxtAddIngredient = (TextView) findViewById(R.id.txtAddIngredient);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/AlegreyaSans-Medium.ttf");

        mTitle.setTypeface(typeFace);
        mCost.setTypeface(typeFace);
        mOKButton.setTypeface(typeFace);
        mTxtAddIngredient.setTypeface(typeFace);

        mTitle.setText(mTemp.getName());

        mIngredients = mTemp.getIngredients();

        mSelectedRecipeList.setAdapter(mAdapter);

        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Either click on icon or text will go to new ingredient
        mTxtAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SelectedRecipe.this, AddToRecipe.class);
                startActivity(intent);
            }
        });

        mIVAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SelectedRecipe.this, AddToRecipe.class);
                startActivity(intent);
            }
        });

        // Simple click to view the entry in RecipeBook
        mSelectedRecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EditUsedIngredient.setSelected(mIngredients.get(position));

                intent = new Intent(SelectedRecipe.this, EditUsedIngredient.class);
                startActivity(intent);
            }
        });

        // Handle long clicks on ingredients
        mSelectedRecipeList.setLongClickable(true);

        mSelectedRecipeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Update the total recipe cost after deleting an ingredient
                mTemp.setTotal(mTemp.getTotal() - tempRecipe.calculateIngredientCost(mIngredients.get(position)));
                String s = "$" + String.format("%.2f", mTemp.getTotal());
                mCost.setText(s);

                view.setAnimation(mLeftOut);
                mAdapter.deleteIngredient(position);

                return true;
            }
        });
    }

    protected void onResume() {
        super.onResume();

        mLeftOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);

        // If there is a new ingredient, add it to the full list
        for (IngredientPortion ip : theRecipe) {
            mIngredients.add(ip);
        }

        mTemp.setTotal(0.00);

        // Total up the full list
        for (IngredientPortion ip : mIngredients) {
            mTemp.setTotal(mTemp.getTotal() + tempRecipe.calculateIngredientCost(ip));
        }

        String s = "$" + String.format("%.2f", mTemp.getTotal());
        mCost.setText(s);

        theRecipe.clear();

        mAdapter.update();
    }

    public static void setSelected(Recipe recipe) {
        mTemp = recipe;
    }

    // Inner class to handle the ListView ///////////////////////
    public class SelectRecipeAdapter extends BaseAdapter {

        public void update() {
            notifyDataSetChanged();
        }

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

            mIngredientName.setText(ip.getName());

            String s = "$" + String.format("%.2f", tempRecipe.calculateIngredientCost(ip));
            mIngredientCost.setText(s);
            mIngredientDescription.setText(mIngredients.get(whichItem).getFullDescription());

            return view;
        }

        public void deleteIngredient(int position) {
            mIngredients.remove(position);
            notifyDataSetChanged();
        }
    }


}
