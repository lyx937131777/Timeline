package com.timeline.android;

import android.content.Intent;
import android.widget.EditText;

import com.timeline.android.presenter.LoginPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LoginPresenter mockLoginPresenter;

    private LoginActivity loginActivity;
    @Before
    public void setUp() throws Exception
    {
       loginActivity =  Robolectric.setupActivity(LoginActivity.class);
       loginActivity.setLoginPresenter(mockLoginPresenter);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void onLoginClick()
    {
        ((EditText) loginActivity.findViewById(R.id.username)).setText("lyx@qq.com");
        ((EditText) loginActivity.findViewById(R.id.password)).setText("123456");
        loginActivity.findViewById(R.id.bt_login).performClick();
        verify(mockLoginPresenter).login("lyx@qq.com","123456");
    }

    @Test
    public void onGoRegisterClick()
    {
        loginActivity.findViewById(R.id.bt_go_register).performClick();
        Intent expected = new Intent(loginActivity,RegisterActivity.class);
        ShadowActivity shadowActivity = Shadow.extract(loginActivity);
        assertEquals(expected.toString(), shadowActivity.getNextStartedActivity().toString());
    }
}