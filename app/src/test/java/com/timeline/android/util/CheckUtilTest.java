package com.timeline.android.util;

import android.content.Context;

import com.timeline.android.BuildConfig;
import com.timeline.android.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(sdk = 27)
public class CheckUtilTest
{
//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();
//
//    @Mock
//    Context mockContext;

    CheckUtil checkUtil;

    @ParameterizedRobolectricTestRunner.Parameter(value = 0)
    public String userID;
    @ParameterizedRobolectricTestRunner.Parameter(value = 1)
    public String password;
    @ParameterizedRobolectricTestRunner.Parameter(value = 2)
    public String confirm;
    @ParameterizedRobolectricTestRunner.Parameter(value = 3)
    public String nickname;
    @ParameterizedRobolectricTestRunner.Parameter(value = 4)
    public boolean expectedLogin;
    @ParameterizedRobolectricTestRunner.Parameter(value = 5)
    public boolean expectedRegister;
    @ParameterizedRobolectricTestRunner.Parameter(value = 6)
    public String expectedLoginToast;
    @ParameterizedRobolectricTestRunner.Parameter(value = 7)
    public String expectedRegisterToast;

    @ParameterizedRobolectricTestRunner.Parameters
    public static Collection prepareData()
    {
        Object [] [] objs = {{"123@qq.com","123456","123456","123",true,true,null,null},
                {"123456","123456","123456","123",false,false,"邮箱格式不正确","请输入正确的邮箱"},
                {"123456@qq.com","12345","12345","123",false,false,"密码位数不正确","请输入至少6位的密码"},
                {"123456@qq.com","123456","1234567","123",true,false,null,"两次密码输入不一致"},
                {"123456@qq.com","123456","123456","",true,false,null,"昵称不得为空"}};
        return Arrays.asList(objs);
    }


    @Before
    public void setUp() throws Exception
    {
//        MockitoAnnotations.initMocks(this);
        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);
        checkUtil = new CheckUtil(loginActivity);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void checkLogin()
    {
       boolean result = checkUtil.checkLogin(userID, password);
       assertEquals(expectedLogin,result);
        assertEquals(expectedLoginToast,ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void checkRegister()
    {
        boolean result = checkUtil.checkRegister(userID,password,confirm,nickname);
        assertEquals(expectedRegister,result);
        assertEquals(expectedRegisterToast,ShadowToast.getTextOfLatestToast());
    }
}