package com.timeline.android;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.timeline.android.db.Article;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ArticleActivityInstrumentedTest
{
    @Rule
    public IntentsTestRule<ArticleActivity> intentsTestRule = new IntentsTestRule<>(ArticleActivity.class,true,false);

    private Article article = new Article(10,"lyx@qq.com","吕轶霄","Test content","empty",new Date());

    @Test
    public void onCreate()
    {
        Intent intent = new Intent();
        article.setTimeStamp("刚刚");
        intent.putExtra("article",article);
        intentsTestRule.launchActivity(intent);
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("Timeline")).check(matches(withParent(withId(R.id.toolbar))));
        onView(withId(R.id.edit)).check(matches(isDisplayed()));
        onView(withId(R.id.delete)).check(matches(isDisplayed()));
        onView(withId(R.id.author)).check(matches(allOf(isDisplayed(),withText("吕轶霄"))));
        onView(withId(R.id.date)).check(matches(allOf(isDisplayed(),withText("刚刚"))));
        onView(withId(R.id.content)).check(matches(allOf(isDisplayed(),withText("Test content"))));
    }

    @Test
    public void onDeleteClick()
    {
        Intent intent = new Intent();
        article.setTimeStamp("刚刚");
        intent.putExtra("article",article);
        intentsTestRule.launchActivity(intent);
        onView(withId(R.id.delete)).perform(click());
    }

    @Test
    public void onEditClick()
    {
        Intent intent = new Intent();
        article.setTimeStamp("刚刚");
        intent.putExtra("article",article);
        intentsTestRule.launchActivity(intent);
        onView(withId(R.id.edit)).perform(click());
        intended(hasComponent("com.timeline.android.PushActivity"));
    }

}