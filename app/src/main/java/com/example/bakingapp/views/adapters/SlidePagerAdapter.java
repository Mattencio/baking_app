package com.example.bakingapp.views.adapters;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SlidePagerAdapter extends FragmentStatePagerAdapter {

    private static final int mBehavior = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
    private List<Fragment> mFragments;

    public SlidePagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm, mBehavior);
        mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
