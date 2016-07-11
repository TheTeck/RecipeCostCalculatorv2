package com.teckemeyer.recipecostcalculatorv2;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

            mIngredientName.setText(mIngredients.get(whichItem).getName());
            String s = "$" + String.format("%.2f", mIngredients.get(whichItem).getCost());
            mIngredientCost.setText(s);
            mIngredientDescription.setText(mIngredients.get(whichItem).getFullDescription());

            return view;
        }
    }
}
