package com.timeline.android.presenter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.timeline.android.ArticleActivity;
import com.timeline.android.MainActivity;
import com.timeline.android.PushActivity;
import com.timeline.android.db.Article;
import com.timeline.android.util.MyApplication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ArticlePresenterTest
{
    private ArticleActivity articleActivity;

    private SharedPreferences sharedPreferences;
    private Article article;

    @Before
    public void setUp() throws Exception
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        sharedPreferences.edit().putString("userID", "lyx@qq.com").commit();
        article = new Article(10000,"lyx@qq.com","吕轶霄","测试内容","empty",new Date());
        Intent intent = new Intent();
        intent.putExtra("article",article);
        articleActivity =  Robolectric.buildActivity(ArticleActivity.class,intent).setup().get();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void createMenu()
    {
        assertTrue(articleActivity.getArticlePresenter().createMenu());
    }

    @Test
    public void createNomenu()
    {
        sharedPreferences.edit().putString("userID", "differentID@qq.com").commit();
        assertFalse(articleActivity.getArticlePresenter().createMenu());
    }

    @Test
    public void edit()
    {
        articleActivity.getArticlePresenter().edit();
        Intent expected = new Intent(articleActivity,PushActivity.class);
        expected.putExtra("type","edit");
        expected.putExtra("article",article);
        ShadowActivity shadowActivity = Shadow.extract(articleActivity);
        assertEquals(expected.toString(), shadowActivity.getNextStartedActivity().toString());
    }

    @Test
    public void delete() throws InterruptedException
    {
        assertFalse(articleActivity.isFinishing());
        articleActivity.getArticlePresenter().delete();
    }
}