package com.example.bakingapp.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;
import com.example.bakingapp.viewmodels.MainActivityViewModel;
import com.example.bakingapp.views.adapters.SlidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsViewPagerFragment extends Fragment {

    @BindView(R.id.vp_steps_details) ViewPager mViewPagerStepsDetails;
    private Unbinder mUnbinder;

    public StepDetailsViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        observeSelectedRecipe();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mUnbinder.unbind();
    }

    private void observeSelectedRecipe() {
        FragmentActivity mainActivity = getActivity();
        assert mainActivity != null;
        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(mainActivity).get(MainActivityViewModel.class);

        mainActivityViewModel.getSelectedRecipe().observe(this, recipe -> {
            List<Step> steps = recipe.getSteps();
            List<Fragment> stepDetailsList = new ArrayList<>();

            for (Step step : steps) {
                StepDetailPageFragment stepFragment = StepDetailPageFragment.getInstance(step.getDescription(), step.getVideoUrl(), step.getThumbnailUrl());
                stepDetailsList.add(stepFragment);
            }
            startViewPager(stepDetailsList);
        });

        mainActivityViewModel.getSelectedStep().observe(this, step -> {
            setPagerPosition(step.getId());
        });
    }

    private void startViewPager(List<Fragment> fragments) {
        FragmentManager fragmentManager = getFragmentManager();
        SlidePagerAdapter slidePagerAdapter = new SlidePagerAdapter(fragmentManager, fragments);
        mViewPagerStepsDetails.setAdapter(slidePagerAdapter);
    }

    public void setPagerPosition(int item) {
        if (mViewPagerStepsDetails == null) {
            return;
        }
        mViewPagerStepsDetails.setCurrentItem(item);
    }
}
