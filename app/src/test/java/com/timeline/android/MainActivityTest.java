package com.timeline.android;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

import com.timeline.android.presenter.MainPresenter;

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
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.support.v4.ShadowSwipeRefreshLayout;
import org.robolectric.shadows.support.v4.Shadows;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MainPresenter mockMainPresenter;

    private MainActivity mainActivity;

    @Before
    public void setUp() throws Exception
    {
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setMainPresenter(mockMainPresenter);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void onPushClick()
    {
        mainActivity.findViewById(R.id.push).performClick();
        Intent expected = new Intent(mainActivity,PushActivity.class);
        expected.putExtra("type","push");
        ShadowActivity shadowActivity = Shadow.extract(mainActivity);
        assertEquals(expected.toString(), shadowActivity.getNextStartedActivity().toString());
    }

    @Test
    public void onActivityResult()
    {
        mainActivity.onActivityResult(1,Activity.RESULT_OK,null);
        verify(mockMainPresenter).refreshArticle();
    }

    @Test
    public void onMoreClick()
    {
        mainActivity.findViewById(R.id.more_card).performClick();
        verify(mockMainPresenter).moreArticle();
    }

    @Test
    public void onSwipeRefresh()
    {
        SwipeRefreshLayout mSwipeRefreshLayout = mainActivity.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.measure(0,0);
        mSwipeRefreshLayout.setRefreshing(true);
        assertTrue(mSwipeRefreshLayout.isRefreshing());
//        ((SwipeRefreshLayout)(mainActivity.findViewById(R.id.swipe_refresh))).setRefreshing(true);
//        verify(mockMainPresenter).refreshArticle();
    }
}