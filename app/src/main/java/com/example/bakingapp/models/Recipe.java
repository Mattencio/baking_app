package com.example.bakingapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @Expose
    @SerializedName("id")
    private Integer mId;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("ingredients")
    private List<Ingredient> mIngredients;

    @Expose
    @SerializedName("steps")
    private List<Step> mSteps;

    @Expose
    @SerializedName("servings")
    private Integer mServings;

    public Recipe(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public Integer getId() {
        return mId;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public List<Step> getSteps() {
        return mSteps;
    }
}
