package com.timeline.android;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;

import com.timeline.android.db.Article;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest
{

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void onCreate()
    {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("Timeline")).check(matches(withParent(withId(R.id.toolbar))));
        onView(withId(R.id.push)).check(matches(isDisplayed()));
        onView(withId(R.id.more_text)).check(matches(withText("更多")));
        onView(isRoot()).perform(swipeUp());
        onView(isRoot()).perform(swipeUp());
        onView(withId(R.id.more_card)).check(matches(isDisplayed()));
        onView(withId(R.id.more_text)).check(matches(isDisplayed()));
        onView(isRoot()).perform(swipeDown());
        onView(withId(R.id.more_card)).check(matches(not(isDisplayed())));
        onView(withId(R.id.more_text)).check(matches(not(isDisplayed())));
    }

    @Test
    public void onPushClick()
    {
        onView(withId(R.id.push)).perform(click());
        intended(hasComponent("com.timeline.android.PushActivity"));
    }

    @Test
    public void itemClick()
    {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(hasComponent("com.timeline.android.ArticleActivity"));
    }
}