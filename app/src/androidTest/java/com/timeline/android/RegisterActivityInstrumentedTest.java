package com.timeline.android;

import android.provider.ContactsContract;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.timeline.android.presenter.RegisterPresenter;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityInstrumentedTest
{
//    @Rule
//    public ActivityTestRule mActivityRule = new ActivityTestRule(RegisterActivity.class);

    @Rule
    public IntentsTestRule<RegisterActivity> intentsTestRule = new IntentsTestRule<>(RegisterActivity.class);

    @Test
    public void onCreate()
    {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText("注册账户")).check(matches(withParent(withId(R.id.toolbar))));
        onView(withId(R.id.bt_username_clear)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bt_pwd_clear)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bt_confirm_pwd_clear)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bt_nickname_clear)).check(matches(not(isDisplayed())));
        onView(withId(R.id.username)).check(matches(withHint("用户名/邮箱")));
        onView(withId(R.id.password)).check(matches(withHint("密码")));
        onView(withId(R.id.confirm_password)).check(matches(withHint("确认密码")));
        onView(withId(R.id.nickname)).check(matches(withHint("昵称")));
        onView(withId(R.id.bt_register)).check(matches(withText("注册")));
    }

    @Test
    public void TypeAndClear()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.confirm_password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.nickname)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.bt_username_clear)).check(matches(allOf(isDisplayed(),isClickable())));
        onView(withId(R.id.bt_pwd_clear)).check(matches(allOf(isDisplayed(),isClickable())));
        onView(withId(R.id.bt_confirm_pwd_clear)).check(matches(allOf(isDisplayed(),isClickable())));
        onView(withId(R.id.bt_nickname_clear)).check(matches(allOf(isDisplayed(),isClickable())));
        onView(withId(R.id.bt_username_clear)).perform(click());
        onView(withId(R.id.bt_pwd_clear)).perform(click());
        onView(withId(R.id.bt_confirm_pwd_clear)).perform(click());
        onView(withId(R.id.bt_nickname_clear)).perform(click());
        onView(withId(R.id.username)).check(matches(withText("")));
        onView(withId(R.id.password)).check(matches(withText("")));
        onView(withId(R.id.confirm_password)).check(matches(withText("")));
        onView(withId(R.id.nickname)).check(matches(withText("")));
    }

    @Test
    public void registerSuccess()
    {
        SimpleDateFormat ft = new SimpleDateFormat("MMddHHmmss");
        String userID = ft.format(new Date())+ "@test.com";
        onView(withId(R.id.username)).perform(typeText(userID)).check(matches(withText(userID)));
        onView(withId(R.id.password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.confirm_password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.nickname)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_register)).perform(click());
        onView(withText("注册成功！")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText("确定")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        assertTrue(intentsTestRule.getActivity().isFinishing());
    }

   @Test
    public void registerRepeat()
   {
       onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
       onView(withId(R.id.password)).perform(typeText("123456")).check(matches(withText("123456")));
       onView(withId(R.id.confirm_password)).perform(typeText("123456")).check(matches(withText("123456")));
       onView(withId(R.id.nickname)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
       onView(withId(R.id.bt_register)).perform(click());
       onView(withText("该账户已被注册！")).inRoot(isDialog()).check(matches(isDisplayed()));
       onView(withText("确定")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
       assertFalse(intentsTestRule.getActivity().isFinishing());
   }

    @Test
    public void registerWrongEmail()
    {
        onView(withId(R.id.username)).perform(typeText("lyx")).check(matches(withText("lyx")));
        onView(withId(R.id.password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.confirm_password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.nickname)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_register)).perform(click());
        onView(withText("请输入正确的邮箱")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void registerNonEmail()
    {
        onView(withId(R.id.password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.confirm_password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.nickname)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_register)).perform(click());
        onView(withText("请输入正确的邮箱")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void registerShortPassword()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText("12345")).check(matches(withText("12345")));
        onView(withId(R.id.confirm_password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.nickname)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_register)).perform(click());
        onView(withText("请输入至少6位的密码")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void registerNonPassword()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.confirm_password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.nickname)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_register)).perform(click());
        onView(withText("请输入至少6位的密码")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void registerDifferentPassword()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText("123456a")).check(matches(withText("123456a")));
        onView(withId(R.id.confirm_password)).perform(typeText("123456A")).check(matches(withText("123456A")));
        onView(withId(R.id.nickname)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_register)).perform(click());
        onView(withText("两次密码输入不一致")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void registerDifferentLengthPassword()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText("123456a")).check(matches(withText("123456a")));
        onView(withId(R.id.confirm_password)).perform(typeText("123456aa")).check(matches(withText("123456aa")));
        onView(withId(R.id.nickname)).perform(typeText("123456"),closeSoftKeyboard()).check(matches(withText("123456")));
        onView(withId(R.id.bt_register)).perform(click());
        onView(withText("两次密码输入不一致")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void registerNoNickname()
    {
        onView(withId(R.id.username)).perform(typeText("lyx@qq.com")).check(matches(withText("lyx@qq.com")));
        onView(withId(R.id.password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.confirm_password)).perform(typeText("123456")).check(matches(withText("123456")));
        onView(withId(R.id.nickname)).perform(typeText(""),closeSoftKeyboard()).check(matches(withText("")));
        onView(withId(R.id.bt_register)).perform(click());
        onView(withText("昵称不得为空")).inRoot(withDecorView(not(is(intentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }
}