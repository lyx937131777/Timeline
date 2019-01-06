package com.timeline.android.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.util.Log;

import com.timeline.android.LoginActivity;
import com.timeline.android.MainActivity;
import com.timeline.android.util.CheckUtil;
import com.timeline.android.util.LogUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowToast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 27)
public class LoginPresenterTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Context mockContext;

    @Mock
    CheckUtil mockCheckUtil;

    @Mock
    SharedPreferences sharedPreferences;

    LoginPresenter loginPresenter;

//    public LoginPresenterTest(String userID, String password)
//    {
//        this.userID = userID;
//        this.password = password;
//    }

    @Before
    public void setUp() throws Exception
    {
        loginPresenter = new LoginPresenter(mockContext,sharedPreferences, mockCheckUtil);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void mockLogin()
    {
//        when(mockCheckUtil.checkLogin(anyString(),anyString())).thenReturn(true);
        loginPresenter.login("123@qq.com","123456");
        verify(mockCheckUtil).checkLogin("123@qq.com","123456");
//        verify(mockCheckUtil).checkRegister("12345@qq.com","123456","123456","12");
    }

    @Test
    public void login() throws InterruptedException
    {
        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);
        loginActivity.getLoginPresenter().login("123@qq.com","123456");
        Thread.sleep(3000);
        Intent expected = new Intent(loginActivity,MainActivity.class);
        ShadowActivity shadowActivity = Shadow.extract(loginActivity);
        assertEquals(expected.toString(), shadowActivity.getNextStartedActivity().toString());
        assertTrue(loginActivity.isFinishing());
    }

//    @Test
//    public void loginWithErrorUserID() throws InterruptedException
//    {
//        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);
//        loginActivity.getLoginPresenter().login("errorID@qq.com","errorpassword");
//        Thread.sleep(3000);
//        String expected = "用户名不存在";
//        assertEquals(expected,ShadowToast.getTextOfLatestToast());
//    }
//
//    @Test
//    public void loginWithErrorPassword() throws InterruptedException
//    {
//        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);
//        loginActivity.getLoginPresenter().login("lyx@qq.com","errorpassword");
//        Thread.sleep(3000);
//        String expected = "用户名或密码错误";
//        assertEquals(expected,ShadowToast.getTextOfLatestToast());
////        Intent expected2 = new Intent(loginActivity,MainActivity.class);
////        ShadowActivity shadowActivity = Shadow.extract(loginActivity);
////        assertEquals(expected2.toString(), shadowActivity.getNextStartedActivity().toString());
//    }
}