package com.example.bakingapp;

import com.example.bakingapp.views.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.*;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {
    @Rule public ActivityTestRule<MainActivity> mMainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mDownloadingIdleResource;
    private boolean mIsTwoPanels;

    @Before
    public void registerIdlingResource() {
        mDownloadingIdleResource = mMainActivityActivityTestRule.getActivity().getDownloadingIdlingResource();
        mIsTwoPanels = mMainActivityActivityTestRule.getActivity().isTwoPanels();
        IdlingRegistry.getInstance().register(mDownloadingIdleResource);
    }

    @Test
    public void recipesListIsShown_recipesAreDownloaded() {
        ViewInteraction recipesRecyclerView = onView(withId(R.id.rv_recipes_list));
        recipesRecyclerView.check(matches(isDisplayed()));
        recipesRecyclerView.check(matches(hasDescendant(withText("Nutella Pie"))));
        recipesRecyclerView.check(matches(hasDescendant(withText("Brownies"))));
        recipesRecyclerView.check(matches(hasDescendant(withText("Yellow Cake"))));
        recipesRecyclerView.check(matches(hasDescendant(withText("Cheesecake"))));

        recipesRecyclerView.perform(actionOnItemAtPosition(0, click()));
        ViewInteraction stepsList = onView(withId(R.id.rv_recipe_steps));
        stepsList.check(matches(hasDescendant(withText("Prep the cookie crust."))));
        int lastItemPosition = getRecyclerView().getAdapter().getItemCount() - 1;
        stepsList
                .perform(scrollToPosition(lastItemPosition))
                .perform(actionOnItemAtPosition(lastItemPosition, click()));

        if (mIsTwoPanels) {
            onView(withId(R.id.f_recipe_details)).check(matches(hasDescendant(withText("Finishing Steps"))));
            //change shown step
            stepsList
                    .perform(scrollToPosition(0))
                    .perform(actionOnItemAtPosition(0, click()))
                    .check(matches(hasDescendant(withText("Recipe Introduction"))));
        } else {
            onView(withText("Finishing Steps")).check(matches(isDisplayed()));
            Espresso.pressBack();
            stepsList
                    .perform(scrollToPosition(0))
                    .perform(actionOnItemAtPosition(0, click()));
            onView(withText("Recipe Introduction")).check(matches(isDisplayed()));
        }
    }

    private RecyclerView getRecyclerView() {
        return mMainActivityActivityTestRule.getActivity().findViewById(R.id.rv_recipe_steps);
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mDownloadingIdleResource);
    }
}
