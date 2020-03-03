package com.example.bakingapp.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;
import com.example.bakingapp.viewmodels.MainActivityViewModel;
import com.example.bakingapp.views.adapters.StepsListAdapter;
import com.example.bakingapp.views.adapters.StepsListAdapter.OnStepClickListener;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepsFragment extends Fragment {

    @BindView(R.id.rv_recipe_steps) RecyclerView mStepsRecyclerView;

    private MainActivityViewModel mMainActivityViewModel;
    private StepsListAdapter mStepsListAdapter;
    private Unbinder mUnbinder;

    public RecipeStepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        FragmentActivity mainActivity = getActivity();
        assert mainActivity != null;
        OnStepClickListener stepClickCallback = (OnStepClickListener)mainActivity;
        mMainActivityViewModel = ViewModelProviders.of(mainActivity).get(MainActivityViewModel.class);
        observeSteps(stepClickCallback);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mUnbinder.unbind();
    }

    private void observeSteps(OnStepClickListener onStepClickCallback) {
        mMainActivityViewModel.getSelectedRecipe().observe(this, selectedRecipe -> {
            mMainActivityViewModel.setToolbarTitle(selectedRecipe.getName());
            List<Step> stepsList = selectedRecipe.getSteps();
            mStepsListAdapter = new StepsListAdapter(stepsList, onStepClickCallback);
            mStepsRecyclerView.setAdapter(mStepsListAdapter);
        });
    }
}
