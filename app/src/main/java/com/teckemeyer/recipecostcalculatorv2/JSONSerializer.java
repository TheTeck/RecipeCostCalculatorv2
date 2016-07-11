package com.teckemeyer.recipecostcalculatorv2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 6/10/2016.
 */
public class JSONSerializer {

    private String mFilename;
    private Context mContext;

    // Simple constructor
    public JSONSerializer (String fn, Context con) {
        mFilename = fn;
        mContext = con;
    }

    public void saveBook (List<Recipe> recipes) throws IOException, JSONException {

        // Make an array in JSON format
        JSONArray jArray = new JSONArray();

        // And load it with the recipes
        for (Recipe r : recipes) {
            jArray.put(r.convertToJSON());
        }

        // Now write it to the private disk space of our app
        Writer  writer = null;

        try {
            OutputStream out = mContext.openFileOutput(mFilename, mContext.MODE_PRIVATE);

            writer = new OutputStreamWriter(out);
            writer.write(jArray.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Recipe> loadBook() throws IOException, JSONException {

        ArrayList<Recipe> recipeList = new ArrayList<>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < jArray.length(); i++) {
                recipeList.add(new Recipe(jArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            Log.i("Error", "file not found");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return recipeList;
    }

    public void savePantry (List<Ingredient> ingredients) throws IOException, JSONException {

        // Make an array in JSON format
        JSONArray jArray = new JSONArray();

        // And load it with the ingredients
        for (Ingredient i : ingredients) {
            jArray.put(i.convertToJSON());
        }

        // Now write it to the private disk space of our app
        Writer  writer = null;

        try {
            OutputStream out = mContext.openFileOutput(mFilename, mContext.MODE_PRIVATE);

            writer = new OutputStreamWriter(out);
            writer.write(jArray.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Ingredient> loadPantry() throws IOException, JSONException {

        ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < jArray.length(); i++) {
                ingredientList.add(new Ingredient(jArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            Log.i("Error", "file not found");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return ingredientList;
    }

}
