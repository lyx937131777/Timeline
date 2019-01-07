package com.timeline.android;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.timeline.android.db.Article;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PushActivityInstrumentedTest
{

    @Rule
    public IntentsTestRule<PushActivity> intentsTestRule = new IntentsTestRule<>(PushActivity.class,true,false);

    private Article article = new Article(10,"lyx@qq.com","吕轶霄","Test content","empty",new Date());
    @Test
    public void onCreateWithPush()
    {
        Intent intent = new Intent();
        intent.putExtra("type","push");
        intentsTestRule.launchActivity(intent);
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("Timeline")).check(matches(withParent(withId(R.id.toolbar))));
        onView(withId(R.id.commit)).check(matches(isDisplayed()));
        onView(withId(R.id.content)).check(matches(allOf(isDisplayed(),withHint("说点什么..."))));
        onView(withId(R.id.img)).check(matches(isDisplayed()));
    }

    @Test
    public void onCreateWithEdit()
    {
        Intent intent = new Intent();
        intent.putExtra("type","edit");
        intent.putExtra("article",article);
        intentsTestRule.launchActivity(intent);
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("Timeline")).check(matches(withParent(withId(R.id.toolbar))));
        onView(withId(R.id.commit)).check(matches(isDisplayed()));
        onView(withId(R.id.content)).check(matches(allOf(isDisplayed(),withHint("说点什么..."),withText("Test content"))));
        onView(withId(R.id.img)).check(matches(isDisplayed()));
    }

    @Test
    public void typeWithPush()
    {
        Intent intent = new Intent();
        intent.putExtra("type","push");
        intentsTestRule.launchActivity(intent);
        onView(withId(R.id.content)).perform(typeText("Test to type something"),closeSoftKeyboard()).check(matches(withText("Test to type something")));
    }

    @Test
    public void typeWithEdit()
    {
        Intent intent = new Intent();
        intent.putExtra("type","edit");
        intent.putExtra("article",article);
        intentsTestRule.launchActivity(intent);
        onView(withId(R.id.content)).perform(typeText(" + Edit"),closeSoftKeyboard()).check(matches(withText("Test content + Edit")));
    }

    @Test
    public void commitWithPush()
    {
        Intent intent = new Intent();
        intent.putExtra("type","push");
        intentsTestRule.launchActivity(intent);
        onView(withId(R.id.content)).perform(typeText("Test to push by Espresso."),closeSoftKeyboard()).check(matches(withText("Test to push by Espresso.")));
        onView(withId(R.id.commit)).perform(click());
        onView(withText("发布成功")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText("确定")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        assertTrue(intentsTestRule.getActivity().isFinishing());
    }

    @Test
    public void commitWithEdit()
    {
        Intent intent = new Intent();
        intent.putExtra("type","edit");
        intent.putExtra("article",article);
        intentsTestRule.launchActivity(intent);
        onView(withId(R.id.content)).perform(typeText(" + Test to edit by Espresso."),closeSoftKeyboard()).check(matches(withText("Test content + Test to edit by Espresso.")));
        onView(withId(R.id.commit)).perform(click());
        onView(withText("发布成功")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText("确定")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        assertTrue(intentsTestRule.getActivity().isFinishing());
    }

    @Test
    public void commitNothing()
    {
        Intent intent = new Intent();
        intent.putExtra("type","push");
        intentsTestRule.launchActivity(intent);
        onView(withId(R.id.commit)).perform(click());
        onView(withText("发布内容不得为空")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText("确定")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
    }

    @Test
    public void commitTooLong()
    {
        Intent intent = new Intent();
        intent.putExtra("type","push");
        intentsTestRule.launchActivity(intent);
        StringBuilder content = new StringBuilder("Long content test ");
        for(int i = 0; i < 15; i++)
        {
            content.append("0123456789");
        }
        onView(withId(R.id.content)).perform(typeText(content.toString()));
        onView(withId(R.id.commit)).perform(click());
        onView(withText("字数不得大于140")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText("确定")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
    }

//    @Test
//    public void onImageClick()
//    {
//        Intent intent = new Intent();
//        intent.putExtra("type","push");
//        intentsTestRule.launchActivity(intent);
//        onView(withId(R.id.bt_dialog_camera)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.bt_dialog_album)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.bt_dialog_cancel)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.img)).perform(click());
//        onView(withId(R.id.bt_dialog_camera)).check(matches(isDisplayed()));
//        onView(withId(R.id.bt_dialog_album)).check(matches(isDisplayed()));
//        onView(withId(R.id.bt_dialog_cancel)).check(matches(isDisplayed()));
//    }

//    @Test
//    public void onImageCameraClick()
//    {
//        Intent intent = new Intent();
//        intent.putExtra("type","push");
//        intentsTestRule.launchActivity(intent);
//        onView(withId(R.id.img)).perform(click());
//        onView(withId(R.id.bt_dialog_camera)).perform(click());
//    }

}