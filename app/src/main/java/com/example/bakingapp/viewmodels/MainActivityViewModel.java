package com.example.bakingapp.viewmodels;

import android.app.Application;

import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;
import com.example.bakingapp.retrofit.RetrofitManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.idling.CountingIdlingResource;

public class MainActivityViewModel extends AndroidViewModel {

    //Testing
    @Nullable
    private final CountingIdlingResource mDownloadingIdleResource = new CountingIdlingResource("DOWNLOADING_IDLE_RESOURCE");
    private int mIdleResourceCounter = 0;

    private MutableLiveData<List<Recipe>> mRecipesLiveData = new MutableLiveData<>();
    private MutableLiveData<Recipe> mSelectedRecipeLiveData = new MutableLiveData<>();
    private MutableLiveData<Step> mSelectedStepLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mToolbarTitle = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Recipe>> getRecipes() {
        RetrofitManager retrofitManager = new RetrofitManager();
        retrofitManager.getRecipes(new RetrofitManager.RecipesListener() {
            @Override
            public void onRecipesReceived(List<Recipe> recipes) {
                mRecipesLiveData.postValue(recipes);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                mRecipesLiveData.postValue(null);
            }
        });
        return mRecipesLiveData;
    }

    public LiveData<Recipe> getSelectedRecipe() {
        return mSelectedRecipeLiveData;
    }

    public LiveData<Step> getSelectedStep() {
        return mSelectedStepLiveData;
    }

    public void setSelectedStep(Step selectedStep) {
        mSelectedStepLiveData.postValue(selectedStep);
    }

    public LiveData<String> getToolbarTitle() {
        return mToolbarTitle;
    }

    public void setSelectedRecipe(Recipe selectedRecipe) {
        mSelectedRecipeLiveData.postValue(selectedRecipe);
    }

    public void setToolbarTitle(String newTitle) {
        mToolbarTitle.postValue(newTitle);
    }

    @Nullable
    public CountingIdlingResource getDownloadingIdleResource() {
        return mDownloadingIdleResource;
    }

    public void incrementIdleResource() {
        assert mDownloadingIdleResource != null;
        mIdleResourceCounter++;
        mDownloadingIdleResource.increment();
    }

    public void decrementIdleResource() {
        assert mDownloadingIdleResource != null;
        if (--mIdleResourceCounter <= 0) {
            return;
        }
        mDownloadingIdleResource.decrement();
    }
}
