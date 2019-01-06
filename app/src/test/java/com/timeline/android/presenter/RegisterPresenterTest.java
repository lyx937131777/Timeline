package com.timeline.android.presenter;

import android.content.Context;

import com.timeline.android.RegisterActivity;
import com.timeline.android.util.CheckUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class RegisterPresenterTest
{

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Context mockContext;

    @Mock
    CheckUtil mockCheckUtil;

    RegisterPresenter registerPresenter;

    @ParameterizedRobolectricTestRunner.Parameter(value = 0)
    public String userID;
    @ParameterizedRobolectricTestRunner.Parameter(value = 1)
    public String password;
    @ParameterizedRobolectricTestRunner.Parameter(value = 2)
    public String confirm;
    @ParameterizedRobolectricTestRunner.Parameter(value = 3)
    public String nickname;
    @ParameterizedRobolectricTestRunner.Parameter(value = 4)
    public String expected;

    @ParameterizedRobolectricTestRunner.Parameters
    public static Collection prepareData()
    {
        Object [] [] objs = {{"123@qq.com","123456","123456","123",null},
                {"123456","123456","123456","123","请输入正确的邮箱"},
                {"123456@qq.com","12345","12345","123","请输入至少6位的密码"},
                {"123456@qq.com","123456","1234567","123","两次密码输入不一致"},
                {"123456@qq.com","123456","123456","","昵称不得为空"}};
        return Arrays.asList(objs);
    }

    @Before
    public void setUp() throws Exception
    {
        registerPresenter = new RegisterPresenter(mockContext,mockCheckUtil);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void mockRegister()
    {
//        when(mockCheckUtil.checkRegister(anyString(),anyString(),anyString(),anyString())).thenReturn(true);
        registerPresenter.register(userID,password,confirm,nickname);
        verify(mockCheckUtil).checkRegister(anyString(),anyString(),anyString(),anyString());
    }

    @Test
    public void register()
    {
        RegisterActivity registerActivity = Robolectric.setupActivity(RegisterActivity.class);
        registerActivity.getRegisterPresenter().register(userID,password,confirm,nickname);
        assertEquals(expected,ShadowToast.getTextOfLatestToast());
    }
}