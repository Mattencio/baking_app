package com.example.bakingapp.views.fragments;


import android.content.Context;
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
import com.example.bakingapp.viewmodels.MainActivityViewModel;
import com.example.bakingapp.views.adapters.RecipesListAdapter;
import com.example.bakingapp.views.adapters.RecipesListAdapter.OnRecipeClickListener;

public class RecipesListFragment extends Fragment {

    @BindView(R.id.rv_recipes_list)
    RecyclerView mRecyclerRecipesList;

    private Unbinder mUnbinder;
    private RecipesListAdapter mRecipesListAdapter;
    private MainActivityViewModel mMainActivityViewModel;

    public RecipesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        Context context = inflater.getContext();
        FragmentActivity mainActivity = getActivity();
        assert mainActivity != null;
        mMainActivityViewModel = ViewModelProviders.of(mainActivity).get(MainActivityViewModel.class);
        OnRecipeClickListener onRecipeClickCallback = (OnRecipeClickListener)context;
        observeRecipes(onRecipeClickCallback);
        return view;
    }

    private void observeRecipes(OnRecipeClickListener onRecipeClickCallback) {
        mMainActivityViewModel.getRecipes().observe(this, recipesList -> {
            if (recipesList == null || recipesList.isEmpty()) {
                return;
            }
            mRecipesListAdapter = new RecipesListAdapter(recipesList, onRecipeClickCallback);
            mRecyclerRecipesList.setAdapter(mRecipesListAdapter);
            mMainActivityViewModel.setIdleResource(false);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
