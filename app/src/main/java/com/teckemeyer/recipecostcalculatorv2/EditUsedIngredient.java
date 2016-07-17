package com.teckemeyer.recipecostcalculatorv2;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class EditUsedIngredient extends AppCompatActivity {

    private static IngredientPortion mTemp;
    private TextView mIngredientName;
    private TextView mIngredientDescription;
    private Button mCancel;
    private Button mOK;
    private EditText mAmount;
    private Spinner spinner1;
    private Spinner spinner2;
    private String tempString;
    private ArrayAdapter<String> spinnerAdapter;
    private String mAmountDivider;
    private String mUnitUsed;

    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_used_ingredient);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/AlegreyaSans-Medium.ttf");

        mIngredientName = (TextView) findViewById(R.id.txtIngredientName);
        mIngredientDescription = (TextView) findViewById(R.id.txtIngredientDescription);
        mAmount = (EditText) findViewById(R.id.editIngredientAmount);
        mOK = (Button) findViewById(R.id.btnOK);
        mCancel = (Button) findViewById(R.id.btnCancel);
        spinner1 = (Spinner) findViewById(R.id.spinnerDivisor);
        spinner2 = (Spinner) findViewById(R.id.spinnerUnit);

        mIngredientName.setTypeface(typeFace);
        mIngredientDescription.setTypeface(typeFace);

        mIngredientName.setText(mTemp.getName());
        mIngredientDescription.setText(mTemp.getFullDescription());
        mAmount.setText(String.valueOf(mTemp.getUsedAmount()));

        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Convert the values
                int mIntAmountUsed = Integer.parseInt(mAmount.getText().toString().trim());
                int mADU; //Amount Divider Used
                String dividerView = "";

                switch (mAmountDivider) {
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

                /*
                IngredientPortion useIngredient = new IngredientPortion();
                useIngredient.setUsedAmount(mIntAmountUsed);
                useIngredient.setUsedAmountDivider(mADU);
                useIngredient.setUsedUnit(mUnitUsed);
                useIngredient.setIngredientID(mTemp.getIngredientID());
                useIngredient.setName(mTemp.getName());

                if (mADU != 1) {
                    dividerView = "/" + mADU;
                }
                useIngredient.setFullDescription("" + mIntAmountUsed + dividerView + " " + mUnitUsed);

                CurrentRecipe thisRecipe = CurrentRecipe.getInstance();
                ArrayList<IngredientPortion> theRecipe = thisRecipe.getIngredientList();

                theRecipe.add(useIngredient);*/

                mTemp.setUsedAmount(mIntAmountUsed);
                mTemp.setUsedAmountDivider(mADU);
                mTemp.setUsedUnit(mUnitUsed);

                if (mADU != 1) {
                    dividerView = "/" + mADU;
                }
                mTemp.setFullDescription("" + mIntAmountUsed + dividerView + " " + mUnitUsed);

                finish();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Sets the appropriate spinner for the ingredient selected
        tempString = mTemp.getUsedUnit();

        if (tempString.equals("units")) {
            spinnerAdapter = new ArrayAdapter<>(EditUsedIngredient.this, R.layout.spinner_units, getResources().getStringArray(R.array.units));
        } else if (tempString.equals("ounces") || tempString.equals("pounds")){
            spinnerAdapter = new ArrayAdapter<>(EditUsedIngredient.this, R.layout.spinner_units, getResources().getStringArray(R.array.weight));
        } else if (tempString.equals("milligrams") || tempString.equals("grams") || tempString.equals("kilograms")) {
            spinnerAdapter = new ArrayAdapter<>(EditUsedIngredient.this, R.layout.spinner_units, getResources().getStringArray(R.array.weight_metric));
        } else {
            spinnerAdapter = new ArrayAdapter<>(EditUsedIngredient.this, R.layout.spinner_units, getResources().getStringArray(R.array.volume));
        }
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();

        // Handle the amount divider used spinner
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#ee5959"));
                mAmountDivider = String.valueOf(spinner1.getSelectedItem());

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

    public static void setSelected(IngredientPortion ip) {
        mTemp = ip;
    }
}
