package com.timeline.android.presenter;

import com.timeline.android.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MainPresenterTest
{

    private MainActivity mainActivity;

    @Before
    public void setUp() throws Exception
    {
        mainActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void refreshArticle()
    {
        mainActivity.getMainPresenter().refreshArticle();

    }

    @Test
    public void moreArticle()
    {
        mainActivity.getMainPresenter().moreArticle();

    }
}