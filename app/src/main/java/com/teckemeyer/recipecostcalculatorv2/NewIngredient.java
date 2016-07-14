package com.teckemeyer.recipecostcalculatorv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class NewIngredient extends AppCompatActivity {

    // Holds int reference for the icon
    // 0 means that an icon has not yet been picked
    private int mIcon = 1;
    private int mAmount = 0;
    private float mCost = 0f;
    private TextView mTxtName;
    private TextView mTxtAmount;
    private TextView mTxtUnit;
    private TextView mTxtCost;
    private String mName = "";
    private String mUnit = "units";
    private boolean mError = false;
    float mAlpha = 0.4f;
    private int mMessageCode;
    private Intent intent;

    Typeface typeFace;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ingredient);

        final ImageView ivIcon1 = (ImageView) findViewById(R.id.ivIcon1);
        final ImageView ivIcon2 = (ImageView) findViewById(R.id.ivIcon2);
        final ImageView ivIcon3 = (ImageView) findViewById(R.id.ivIcon3);
        final ImageView ivIcon4 = (ImageView) findViewById(R.id.ivIcon4);
        final ImageView ivIcon5 = (ImageView) findViewById(R.id.ivIcon5);
        final ImageView ivIcon6 = (ImageView) findViewById(R.id.ivIcon6);
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etAmount = (EditText) findViewById(R.id.etAmount);
        final EditText etCost = (EditText) findViewById(R.id.etCost);
        final EditText etYield = (EditText) findViewById(R.id.editYield);
        final Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        final ImageView ivYieldHelp = (ImageView) findViewById(R.id.yieldHelp);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final TextView txtNameWarning = (TextView) findViewById(R.id.txtNameWarning);
        final TextView txtAmountWarning = (TextView) findViewById(R.id.txtAmountWarning);
        final TextView txtCostWarning = (TextView) findViewById(R.id.txtCostWarning);
        TextView txtIcon = (TextView) findViewById(R.id.txtIcon);
        mTxtName = (TextView) findViewById(R.id.txtName);
        mTxtAmount = (TextView) findViewById(R.id.txtAmount);
        mTxtUnit = (TextView) findViewById(R.id.txtUnit);
        mTxtCost = (TextView) findViewById(R.id.txtCost);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/AlegreyaSans-Medium.ttf");

        mTxtName.setTypeface(typeFace);
        mTxtAmount.setTypeface(typeFace);
        mTxtUnit.setTypeface(typeFace);
        mTxtCost.setTypeface(typeFace);
        btnCancel.setTypeface(typeFace);
        btnSubmit.setTypeface(typeFace);

        Pantry thePantry = Pantry.getInstance();
        final ArrayList<Ingredient> theIngredients = thePantry.getIngredients();

        prefs = getSharedPreferences("Recipe Cost Calculator", MODE_PRIVATE);

        mMessageCode = prefs.getInt("messageCode", -1);

        if (mMessageCode == 2) {
            DialogTutorial showTutorial = new DialogTutorial();
            showTutorial.show(getFragmentManager(), "Tutorial");
        }

        // Hide the warnings here
        txtNameWarning.setVisibility(View.INVISIBLE);
        txtAmountWarning.setVisibility(View.INVISIBLE);
        txtCostWarning.setVisibility(View.INVISIBLE);

        // Handle the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#fecd7e"));
                mUnit = String.valueOf(spinner.getSelectedItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing Yet
            }
        });

        // Initial icon selection
        ivIcon1.setAlpha(1f);
        ivIcon2.setAlpha(mAlpha);
        ivIcon3.setAlpha(mAlpha);
        ivIcon4.setAlpha(mAlpha);
        ivIcon5.setAlpha(mAlpha);
        ivIcon6.setAlpha(mAlpha);

        // Handle Icon selection
        ivIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIcon = 1;
                ivIcon1.setAlpha(1f);
                ivIcon2.setAlpha(mAlpha);
                ivIcon3.setAlpha(mAlpha);
                ivIcon4.setAlpha(mAlpha);
                ivIcon5.setAlpha(mAlpha);
                ivIcon6.setAlpha(mAlpha);
            }
        });
        ivIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIcon = 2;
                ivIcon1.setAlpha(mAlpha);
                ivIcon2.setAlpha(1f);
                ivIcon3.setAlpha(mAlpha);
                ivIcon4.setAlpha(mAlpha);
                ivIcon5.setAlpha(mAlpha);
                ivIcon6.setAlpha(mAlpha);
            }
        });
        ivIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIcon = 3;
                ivIcon1.setAlpha(mAlpha);
                ivIcon2.setAlpha(mAlpha);
                ivIcon3.setAlpha(1f);
                ivIcon4.setAlpha(mAlpha);
                ivIcon5.setAlpha(mAlpha);
                ivIcon6.setAlpha(mAlpha);
            }
        });
        ivIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIcon = 4;
                ivIcon1.setAlpha(mAlpha);
                ivIcon2.setAlpha(mAlpha);
                ivIcon3.setAlpha(mAlpha);
                ivIcon4.setAlpha(1f);
                ivIcon5.setAlpha(mAlpha);
                ivIcon6.setAlpha(mAlpha);
            }
        });
        ivIcon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIcon = 5;
                ivIcon1.setAlpha(mAlpha);
                ivIcon2.setAlpha(mAlpha);
                ivIcon3.setAlpha(mAlpha);
                ivIcon4.setAlpha(mAlpha);
                ivIcon5.setAlpha(1f);
                ivIcon6.setAlpha(mAlpha);
            }
        });
        ivIcon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIcon = 6;
                ivIcon1.setAlpha(mAlpha);
                ivIcon2.setAlpha(mAlpha);
                ivIcon3.setAlpha(mAlpha);
                ivIcon4.setAlpha(mAlpha);
                ivIcon5.setAlpha(mAlpha);
                ivIcon6.setAlpha(1f);
            }
        });

        // Handle clicks on help icon for "yield"
        ivYieldHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(NewIngredient.this, YieldInfo.class);
                startActivity(intent);
            }
        });

        // Handle the Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Handle the Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAmount = 0;
                mCost = 0f;

                try {
                    if (etAmount.getText().toString().trim() != null) {
                        mAmount += Integer.parseInt(etAmount.getText().toString().trim());
                    }
                } catch (NumberFormatException e) {
                    mAmount = 0;
                }

                try {
                    if (etCost.getText().toString().trim() != null) {
                        mCost += Float.parseFloat(etCost.getText().toString().trim());
                    }
                } catch (NumberFormatException e) {
                    mCost = 0f;
                }

                mName = etName.getText().toString();
                mError = false;

                String sAmount = etAmount.getText().toString().trim();
                String sCost = etCost.getText().toString().trim();

                if (etName.getText().toString().trim().equals("")) {
                    txtNameWarning.setVisibility(View.VISIBLE);
                    mError = true;
                } else {
                    txtNameWarning.setVisibility(View.INVISIBLE);
                }

                if (sAmount.equals(null)) {
                    txtAmountWarning.setVisibility(View.VISIBLE);
                    mError = true;
                } else if (mAmount <= 0) {
                    txtAmountWarning.setVisibility(View.VISIBLE);
                    mError = true;
                } else {
                    txtAmountWarning.setVisibility(View.INVISIBLE);
                }

                if (sCost.equals(null)) {
                    txtCostWarning.setVisibility(View.VISIBLE);
                    mError = true;
                } else if (mCost <= 0) {
                    txtCostWarning.setVisibility(View.VISIBLE);
                    mError = true;
                } else {
                    txtCostWarning.setVisibility(View.INVISIBLE);
                }

                int mYield = 100;
                try {
                    if (etYield.getText().toString().trim() != null) {
                        mYield = Integer.parseInt(etYield.getText().toString().trim());
                    }
                } catch (NumberFormatException e) {
                    mYield = 100;
                }

                if (mYield < 0) {
                    mYield = 0;
                } else if (mYield > 100) {
                    mYield = 100;
                }

                if (!mError){
                    // Create a new ingredient
                    Ingredient ingredient = new Ingredient();

                    // Give each ingredient created an unique identifier
                    Calendar cal = Calendar.getInstance();
                    ingredient.setID(cal.getTimeInMillis());

                    mName = mName.toUpperCase();

                    ingredient.setUnit(mUnit);
                    ingredient.setAmount(mAmount);
                    ingredient.setAmountDivider(1);
                    ingredient.setIcon(mIcon);
                    ingredient.setName(mName);
                    ingredient.setCost(mCost);
                    ingredient.setYield(mYield);

                    theIngredients.add(ingredient);

                    Toast.makeText(NewIngredient.this, "" + ingredient.getName() + " added to Pantry", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });
    }
}
