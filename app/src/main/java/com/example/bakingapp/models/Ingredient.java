package com.example.bakingapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @Expose
    @SerializedName("quantity")
    private Float mQuantity;

    @Expose
    @SerializedName("measure")
    private String mMeasure;

    @Expose
    @SerializedName("ingredient")
    private String mIngredientName;

    public String getIngredientName() {
        return mIngredientName;
    }
}
