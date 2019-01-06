package com.timeline.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;

import com.timeline.android.db.Article;
import com.timeline.android.presenter.ArticlePresenter;
import com.timeline.android.presenter.MainPresenter;
import com.timeline.android.util.MyApplication;

import org.assertj.core.api.Assert;
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

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class ArticleActivityTest
{

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ArticlePresenter mockArticlePresenter;

    private ArticleActivity articleActivity;

    @Before
    public void setUp() throws Exception
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        sharedPreferences.edit().putString("userID", "lyx@qq.com").commit();
        Article article = new Article(10000,"lyx@qq.com","吕轶霄","测试内容","empty",new Date());
        Intent intent = new Intent();
        intent.putExtra("article",article);
        articleActivity =  Robolectric.buildActivity(ArticleActivity.class,intent).setup().get();
        articleActivity.setArticlePresenter(mockArticlePresenter);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void onCreate()
    {

        assertEquals(View.GONE , articleActivity.findViewById(R.id.img_card).getVisibility());
    }

    @Test
    public void onEditClick()
    {
        articleActivity.findViewById(R.id.edit).performClick();
        verify(mockArticlePresenter).edit();
    }

    @Test
    public void onDeleteClick()
    {
        articleActivity.findViewById(R.id.delete).performClick();
        verify(mockArticlePresenter).delete();
    }

    @Test
    public void onActivityResult()
    {
        assertFalse(articleActivity.isFinishing());
        articleActivity.onActivityResult(1,Activity.RESULT_OK,null);
        assertTrue(articleActivity.isFinishing());
    }
}