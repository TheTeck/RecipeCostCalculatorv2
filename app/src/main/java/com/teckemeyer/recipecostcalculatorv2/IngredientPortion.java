package com.teckemeyer.recipecostcalculatorv2;

/**
 * Created by John on 6/12/2016.
 */
public class IngredientPortion {

    private long mIngredient;
    private int mUsedAmount;
    private int mUsedAmountDivider;
    private String mUsedUnit;
    private String mName;
    private float mCost;
    private String mFullDescription;

    public String getFullDescription() {
        return mFullDescription;
    }

    public void setFullDescription(String mFullDescription) {
        this.mFullDescription = mFullDescription;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public float getCost() {
        return mCost;
    }

    public void setCost(float mCost) {
        this.mCost = mCost;
    }

    public String getUsedUnit() {
        return mUsedUnit;
    }

    public void setUsedUnit(String mUsedUnit) {
        this.mUsedUnit = mUsedUnit;
    }

    public long getIngredientID() {
        return mIngredient;
    }

    public void setIngredientID(long mIngredient) {
        this.mIngredient = mIngredient;
    }

    public int getUsedAmount() {
        return mUsedAmount;
    }

    public void setUsedAmount(int mUsedAmount) {
        this.mUsedAmount = mUsedAmount;
    }

    public int getUsedAmountDivider() {
        return mUsedAmountDivider;
    }

    public void setUsedAmountDivider(int mUsedAmountDivider) {
        this.mUsedAmountDivider = mUsedAmountDivider;
    }
}
