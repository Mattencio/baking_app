package com.example.bakingapp.views;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.View;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;
import com.example.bakingapp.viewmodels.MainActivityViewModel;
import com.example.bakingapp.views.adapters.RecipesListAdapter.OnRecipeClickListener;
import com.example.bakingapp.views.adapters.StepsListAdapter.OnStepClickListener;
import com.example.bakingapp.views.fragments.RecipeStepsFragment;
import com.example.bakingapp.views.fragments.StepDetailsViewPagerFragment;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener, OnStepClickListener {

    private MainActivityViewModel mMainActivityViewModel;
    private boolean mIsTwoPanels;

    @Nullable
    private final CountingIdlingResource mDownloadingIdleResource = new CountingIdlingResource("DOWNLOADING_IDLE_RESOURCE");

    @Nullable
    @BindView(R.id.f_recipe_details) View mDetailsFragment;
    @BindString(R.string.app_name) String mAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        isIdleResourceWaiting(mDownloadingIdleResource, true);
        setContentView(R.layout.activity_main);
        listenToToolbarTitleChanges();
        listenToIdleResource();
        ButterKnife.bind(this);
        mIsTwoPanels = mDetailsFragment != null;
    }

    private void listenToIdleResource() {
        mMainActivityViewModel.getIdleResource().observe(this, isWaiting -> {
            isIdleResourceWaiting(mDownloadingIdleResource, isWaiting);
        });
    }

    private void listenToToolbarTitleChanges() {
        mMainActivityViewModel.getToolbarTitle().observe(this, this::setTitle);
    }

    @Override
    public void OnRecipeClicked(Recipe recipe) {
        mMainActivityViewModel.setSelectedRecipe(recipe);
        RecipeStepsFragment newFragment = new RecipeStepsFragment();
        placeNewFragment(newFragment, false);
    }

    private void placeNewFragment(Fragment newFragment, boolean toSecondPanel) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        int targetContainer = toSecondPanel && mIsTwoPanels ? R.id.f_recipe_details : R.id.f_recipes_list;
        fragmentTransaction.replace(targetContainer, newFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void OnStepClicked(Step step) {
        mMainActivityViewModel.setSelectedStep(step);
        StepDetailsViewPagerFragment newFragment = new StepDetailsViewPagerFragment();
        placeNewFragment(newFragment, true);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int backStackEntryCount = fm.getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            fm.popBackStack();
            setTitle(mAppName);
        } else if (backStackEntryCount > 1) {
            super.onBackPressed();
        }
    }

    @VisibleForTesting
    public boolean isTwoPanels() {
        return mIsTwoPanels;
    }

    @VisibleForTesting
    public IdlingResource getDownloadingIdlingResource() {
        return mDownloadingIdleResource;
    }

    private void isIdleResourceWaiting(CountingIdlingResource idleResource, Boolean isWaiting) {
        if (idleResource == null) {
            return;
        }
        if (isWaiting) {
            idleResource.increment();
        } else {
            idleResource.decrement();
        }
    }
}
