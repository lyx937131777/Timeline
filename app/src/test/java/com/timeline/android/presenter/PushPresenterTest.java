package com.timeline.android.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.timeline.android.PushActivity;
import com.timeline.android.db.Article;
import com.timeline.android.util.MyApplication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class PushPresenterTest
{
    private PushActivity pushActivity;
    private PushPresenter pushPresenter;
    private Article article;

    @Before
    public void setUp() throws Exception
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        sharedPreferences.edit().putString("userID", "lyx@qq.com").commit();
        article = new Article(10000,"lyx@qq.com","吕轶霄","测试内容","empty",new Date());
        Intent intent = new Intent();
        intent.putExtra("type","edit");
        intent.putExtra("article",article);
        pushActivity =  Robolectric.buildActivity(PushActivity.class,intent).setup().get();
        pushPresenter = pushActivity.getPushPresenter();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void commit()
    {
        pushPresenter.commit();
    }

    @Test
    public void setContentAndImagepath()
    {
        pushPresenter.setContentAndImagepath("新测试内容",null);
        assertEquals(null,pushPresenter.getImagePath());
        assertEquals("新测试内容",pushPresenter.getArticle().getContent());
    }
}