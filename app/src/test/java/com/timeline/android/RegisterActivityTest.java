package com.timeline.android;

import android.widget.EditText;

import com.timeline.android.presenter.LoginPresenter;
import com.timeline.android.presenter.RegisterPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class RegisterActivityTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RegisterPresenter mockRegisterPresenter;

    private RegisterActivity registerActivity;
    @Before
    public void setUp() throws Exception
    {
        registerActivity =  Robolectric.setupActivity(RegisterActivity.class);
        registerActivity.setRegisterPresenter(mockRegisterPresenter);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void onRegisterClick()
    {
        ((EditText) registerActivity.findViewById(R.id.username)).setText("lyx@qq.com");
        ((EditText) registerActivity.findViewById(R.id.password)).setText("123456");
        ((EditText) registerActivity.findViewById(R.id.confirm_password)).setText("123456");
        ((EditText) registerActivity.findViewById(R.id.nickname)).setText("吕轶霄");
        registerActivity.findViewById(R.id.bt_register).performClick();
        verify(mockRegisterPresenter).register("lyx@qq.com","123456","123456","吕轶霄");
    }
}