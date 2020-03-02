package com.example.bakingapp.retrofit;

import android.net.Uri;

import com.example.bakingapp.models.Recipe;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private static final String JSON_PATH = "topher/2017/May/59121517_baking/";
    private static final String ERROR_MESSAGE = "THERE WAS AN ERROR GETTING THE RECIPES";
    private final RecipesService mRecipesService;

    public interface RecipesListener {
        void onRecipesReceived(List<Recipe> recipes);
        void onError(Throwable error);
    }

    public RetrofitManager() {
        Retrofit retrofit = getRetrofit();
        mRecipesService = retrofit.create(RecipesService.class);
    }

    public void getRecipes(final RecipesListener listener) {
        Call<List<Recipe>> request = mRecipesService.getRecipes();
        request.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipeList;
                    if (response.body() != null) {
                        recipeList = response.body();
                        listener.onRecipesReceived(recipeList);
                    } else {
                        Throwable error = new RuntimeException(ERROR_MESSAGE);
                        listener.onError(error);
                    }

                } else {
                    int code = response.code();
                    String message = response.message();
                    String errorMessage = "Code: " + code + " - " + message;
                    Throwable error = new RuntimeException(errorMessage);
                    listener.onError(error);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable error) {
                listener.onError(error);
            }
        });
    }

    private Retrofit getRetrofit() {
        Gson gson = getGson();
        return new Retrofit.Builder()
                .baseUrl(getUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private String getUrl() {
        return Uri.parse(BASE_URL).buildUpon().appendEncodedPath(JSON_PATH).build().toString();
    }

    private Gson getGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}
