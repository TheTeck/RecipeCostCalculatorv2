package com.teckemeyer.recipecostcalculatorv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddToRecipe extends AppCompatActivity {

    private Button btnSubmit;
    private Spinner spinner1;
    private Spinner spinner2;
    private String mUnitUsed;
    private String mAmountDividerUsed;
    private EditText mAmountUsed;
    private Ingredient mIngredientUsed;
    private TextView mAddToRecipeTitle;

    private ListView listIngredients;
    private PantryAdapter pantryAdapter;
    private ArrayAdapter<String> spinnerAdapter;
    private String tempString;
    private int mMessageCode;

    Typeface typeFace;

    private Pantry thePantry = Pantry.getInstance();
    ArrayList<Ingredient> theIngredients = thePantry.getIngredients();

    SharedPreferences prefs;

    @Override
    protected void onResume() {
        super.onResume();

        mMessageCode = prefs.getInt("messageCode", -1);

        if (mMessageCode == 6) {
            DialogTutorial showTutorial = new DialogTutorial();
            showTutorial.show(getFragmentManager(), "Tutorial");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_recipe);

        btnSubmit = (Button) findViewById(R.id.btnSubmitAddToRecipe);
        spinner1 = (Spinner) findViewById(R.id.spinnerAmountUsed);
        spinner2 = (Spinner) findViewById(R.id.spinnerUnitUsed);
        mAmountUsed = (EditText) findViewById(R.id.editAmountUsed);
        mAddToRecipeTitle = (TextView) findViewById(R.id.textAddToRecipeTitle);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/AlegreyaSans-Medium.ttf");

        btnSubmit.setTypeface(typeFace);
        mAddToRecipeTitle.setTypeface(typeFace);

        mIngredientUsed = new Ingredient();

        listIngredients = (ListView) findViewById(R.id.listViewDatabase);

        pantryAdapter = new PantryAdapter();

        listIngredients.setAdapter(pantryAdapter);

        mIngredientUsed.setIcon(theIngredients.get(0).getIcon());
        mIngredientUsed.setName(theIngredients.get(0).getName());
        mIngredientUsed.setAmount(theIngredients.get(0).getAmount());
        mIngredientUsed.setAmountDivider(theIngredients.get(0).getAmountDivider());
        mIngredientUsed.setUnit(theIngredients.get(0).getUnit());
        mIngredientUsed.setCost((float) theIngredients.get(0).getCost());
        mIngredientUsed.setYield(theIngredients.get(0).getYield());

        prefs = getSharedPreferences("Recipe Cost Calculator", MODE_PRIVATE);

        // Sets the appropriate spinner for the ingredient selected
        tempString = theIngredients.get(0).getUnit().trim();

        if (tempString.equals("units")) {
            spinnerAdapter = new ArrayAdapter<>(AddToRecipe.this, R.layout.spinner_units, getResources().getStringArray(R.array.units));
        } else if (tempString.equals("ounces") || tempString.equals("pounds")){
            spinnerAdapter = new ArrayAdapter<>(AddToRecipe.this, R.layout.spinner_units, getResources().getStringArray(R.array.weight));
        } else if (tempString.equals("milligrams") || tempString.equals("grams") || tempString.equals("kilograms")) {
            spinnerAdapter = new ArrayAdapter<>(AddToRecipe.this, R.layout.spinner_units, getResources().getStringArray(R.array.weight_metric));
        } else {
            spinnerAdapter = new ArrayAdapter<>(AddToRecipe.this, R.layout.spinner_units, getResources().getStringArray(R.array.volume));
        }
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();

        // Submit the current ingredient usage to the CurrentRecipe singleton
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Convert the values
                int mIntAmountUsed = Integer.parseInt(mAmountUsed.getText().toString().trim());
                int mADU; //Amount Divider Used
                String dividerView = "";

                switch (mAmountDividerUsed) {
                    case "1":
                        mADU = 1;
                        break;
                    case "1/2":
                        mADU = 2;
                        break;
                    case "1/3":
                        mADU = 3;
                        break;
                    case "1/4":
                        mADU = 4;
                        break;
                    case "1/6":
                        mADU = 6;
                        break;
                    case "1/8":
                        mADU = 8;
                        break;
                    default:
                        mADU = 1;
                        break;
                }

                IngredientPortion useIngredient = new IngredientPortion();
                useIngredient.setUsedAmount(mIntAmountUsed);
                useIngredient.setUsedAmountDivider(mADU);
                useIngredient.setUsedUnit(mUnitUsed);
                useIngredient.setIngredient(mIngredientUsed);
                useIngredient.setName(mIngredientUsed.getName());

                if (mADU != 1) {
                    dividerView = "/" + mADU;
                }
                useIngredient.setFullDescription("" + mIntAmountUsed + dividerView + " " + mUnitUsed);

                CurrentRecipe thisRecipe = CurrentRecipe.getInstance();
                ArrayList<IngredientPortion> theRecipe = thisRecipe.getIngredientList();

                theRecipe.add(useIngredient);

                finish();
            }
        });

        // handle the clicks on the listView
        listIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIngredientUsed.setIcon(theIngredients.get(position).getIcon());
                mIngredientUsed.setName(theIngredients.get(position).getName());
                mIngredientUsed.setAmount(theIngredients.get(position).getAmount());
                mIngredientUsed.setAmountDivider(theIngredients.get(position).getAmountDivider());
                mIngredientUsed.setUnit(theIngredients.get(position).getUnit());
                mIngredientUsed.setCost((float) theIngredients.get(position).getCost());
                mIngredientUsed.setYield(theIngredients.get(position).getYield());

                // Update the spinner with appropriate entries
                tempString = mIngredientUsed.getUnit().trim();

                if (tempString.equals("units")) {
                    spinnerAdapter = new ArrayAdapter<>(AddToRecipe.this, R.layout.spinner_units, getResources().getStringArray(R.array.units));
                } else if (tempString.equals("ounces") || tempString.equals("pounds")) {
                    spinnerAdapter = new ArrayAdapter<>(AddToRecipe.this, R.layout.spinner_units, getResources().getStringArray(R.array.weight));
                } else if (tempString.equals("milligrams") || tempString.equals("grams") || tempString.equals("kilograms")) {
                    spinnerAdapter = new ArrayAdapter<>(AddToRecipe.this, R.layout.spinner_units, getResources().getStringArray(R.array.weight_metric));
                }else {
                    spinnerAdapter = new ArrayAdapter<>(AddToRecipe.this, R.layout.spinner_units, getResources().getStringArray(R.array.volume));
                }
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();

                pantryAdapter.setSelected(position);
                pantryAdapter.notifyDataSetChanged();
            }
        });

        // Handle the amount divider used spinner
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#ee5959"));
                mAmountDividerUsed = String.valueOf(spinner1.getSelectedItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing Yet
            }
        });

        // Handle the unit used spinner
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUnitUsed = String.valueOf(spinner2.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing Yet
            }
        });
    }

    // Innerclass to help bind the ingredient data to ListView ////////////////////////////////////
    public class PantryAdapter extends BaseAdapter {

        List<Ingredient> listIngredients = theIngredients;
        int mSelected;

        public PantryAdapter() {
            mSelected = 0;
        }

        public void setSelected(int pos) {
            mSelected = pos;
        }

        @Override
        public int getCount() {
            return listIngredients.size();
        }

        @Override
        public Ingredient getItem(int whichItem) {
            return listIngredients.get(whichItem);
        }

        @Override
        public long getItemId (int whichItem) {
            return whichItem;
        }

        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup) {

            // If view has not been inflated yet
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.pantry_entry, viewGroup, false);
            }

            Ingredient temp = listIngredients.get(whichItem);

            TextView txtName = (TextView) view.findViewById(R.id.txtName);
            TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            ImageView imageViewIcon = (ImageView) view.findViewById(R.id.imageViewIcon);

            txtName.setTypeface(typeFace);
            txtDescription.setTypeface(typeFace);

            if (mSelected == whichItem) {
                txtName.setTextColor(Color.parseColor("#ee5959"));
                txtDescription.setTextColor(Color.parseColor("#ee5959"));
            } else {
                txtName.setTextColor(Color.parseColor("#fecd7e"));
                txtDescription.setTextColor(Color.parseColor("#ffffff"));
            }

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
                    + " @ $" + tempCost;

            txtName.setText(temp.getName());
            txtDescription.setText(tempString);

            return view;
        }
    }
}
