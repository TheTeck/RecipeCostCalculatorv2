package com.teckemeyer.recipecostcalculatorv2;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by John on 6/22/2016.
 */


public class DialogTutorial extends DialogFragment {

    private int mMessageCode;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        SharedPreferences prefs;
        final SharedPreferences.Editor editor;

        prefs = getActivity().getSharedPreferences("Recipe Cost Calculator", getActivity().MODE_PRIVATE);
        editor = prefs.edit();

        mMessageCode = prefs.getInt("messageCode", -1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate main part of dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_tutorial_message, null);

        TextView txtMessage = (TextView) dialogView.findViewById(R.id.textViewMessage);
        Button btnOK = (Button) dialogView.findViewById(R.id.btnContinue);

        switch (mMessageCode) {
            case 0:
                txtMessage.setText("Welcome to the Recipe Cost Calculator!\nHere you can store any ingredients you want and reuse them in all of your recipes!\n\nThis screen is where you can view all the of the recipes that you have created...\nBut before you can create any recipes, you must put some ingredients into your pantry!");
                break;
            case 1:
                txtMessage.setText("Now this is the \"Pantry\" where you can create and store all of your ingredients to use in recipes.\n\nLet's create an ingredient now by clicking on \"Create Ingredient\" above.");
                break;
            case 2:
                txtMessage.setText("Time to create a new ingredient...\n\nthis is where you specify how much of the ingredient you have in bulk and how much it costs.\n\nBe sure to select the right units here. It can by measured by weight, volume or just number of units.");
                break;
            case 3:
                txtMessage.setText("Great! You now have one ingredient in your pantry you can use in any of your recipes.\n\nFYI: You can modify any ingredient here by clicking on it. Any modifications will be reflected in the recipes that use it.\n\n\nNow let's go back to our recipes...");
                break;
            case 4:
                txtMessage.setText("This screen is looking a little lonely... let's make our first recipe! Just click on \"Create Recipe\" below.");
                break;
            case 5:
                txtMessage.setText("This is where you will create a new recipe.\n\nHere you can add as many ingredients to the recipe as you want (as long as they are in the Pantry).\n\nSo let's add one! Click on \"Add Ingredient to Recipe\" above.");
                break;
            case 6:
                txtMessage.setText("You will find all of your ingredients from the Pantry here. Select one and how much of it you are using in your recipe.\n\nAgain, be sure to select the correct units.");
                break;
            case 7:
                txtMessage.setText("Now your recipe has one ingredient!\n\nYou can add more or delete any ingredients you want here by holding down on it.\nYou can also see that there is already a running total for the cost of your ingredients.\n\nOnce done, just give your recipe a name and submit it.");
                break;
            case 8:
                txtMessage.setText("That's it! You created your first recipe! Now you can click on it and view, edit, delete and add ingredients to your recipe.\nPretty straight forward stuff here!\n\nAlso, if you don't want a recipe anymore, just hold down on it and it's gone.\n\nThat's pretty much all there is to it. Thanks for downloading!");
                break;
            default:
                txtMessage.setText("Something didn't work here :(");
                break;
        }

        builder.setView(dialogView);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putInt("messageCode", mMessageCode + 1);
                editor.commit();
                dismiss();
            }
        });

        return builder.create();
    }
}
