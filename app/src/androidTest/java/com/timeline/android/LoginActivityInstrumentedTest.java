package com.timeline.android;


import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.VideoView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.*;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityInstrumentedTest
{
//    @Rule
//    public ActivityTestRule mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule = new IntentsTestRule<>(LoginActivity.class);


    @Test
    public void onCreate()
    {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("登录账户")).check(matches(withParent(withId(R.id.toolbar))));
        onView(withId(R.id.bt_username_clear)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bt_pwd_clear)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bt_pwd_eye)).check(matches(allOf((isDisplayed()),hasBackground(R.drawable.pwd_closed))));
        onView(withId(R.id.username)).check(matches(withHint("用户名/邮箱")));
        onView(withId(R.id.password)).check(matches(withHint("密码")));
        onView(withId(R.id.bt_login)).check(matches(withText("登录")));
        onView(withId(R.id.bt_go_register)).check(matches(withText("没有账号？去注册")));
    }

    @Test
    public void typeTextandClear()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.bt_username_clear)).check(matches(allOf(isDisplayed(),isClickable())));
        onView(withId(R.id.bt_pwd_clear)).check(matches(allOf(isDisplayed(),isClickable())));
        onView(withId(R.id.bt_pwd_eye)).perform(click()).check(matches(hasBackground(R.drawable.pwd_open)));
        onView(withId(R.id.bt_username_clear)).perform(click());
        onView(withId(R.id.bt_pwd_clear)).perform(click());
        onView(withId(R.id.username)).check(matches(withText("")));
        onView(withId(R.id.password)).check(matches(withText("")));
    }

    @Test
    public void loginSuccess()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_login)).perform(click());
        intended(hasComponent("com.timeline.android.MainActivity"));
    }

    @Test
    public void loginWrongEmail()
    {
        onView(withId(R.id.username)).perform(typeText("lyx")).check(matches(withText("lyx")));
        onView(withId(R.id.password)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_login)).perform(click());
        onView(withText("邮箱格式不正确")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void loginSpaceEmail()
    {
        onView(withId(R.id.username)).perform(typeText(" lyx@qq.com")).check(matches(withText(" lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_login)).perform(click());
        onView(withText("邮箱格式不正确")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void loginNonEmail()
    {
        onView(withId(R.id.password)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_login)).perform(click());
        onView(withText("邮箱格式不正确")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void loginNonExistentEmail()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@noExist.com")).check(matches(withText("lyx@noExist.com")));
        onView(withId(R.id.password)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_login)).perform(click());
        onView(withText("用户名不存在")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void loginWrongPassword()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText("1234567"),closeSoftKeyboard()).check(matches(withText("1234567")));
        onView(withId(R.id.bt_login)).perform(click());
        onView(withText("用户名或密码错误")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        onView(withId(R.id.username)).check(matches(not(withText(""))));
        onView(withId(R.id.password)).check(matches(not(withText(""))));
    }

    @Test
    public void loginShortPassword()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText("12345"),closeSoftKeyboard()).check(matches(withText("12345")));
        onView(withId(R.id.bt_login)).perform(click());
        onView(withText("密码位数不正确")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void loginNonPassword()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText(""),closeSoftKeyboard()).check(matches(withText("")));
        onView(withId(R.id.bt_login)).perform(click());
        onView(withText("密码位数不正确")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void goRegister()
    {
        onView(withId(R.id.bt_go_register)).perform(click());
        intended(hasComponent("com.timeline.android.RegisterActivity"));
    }
}