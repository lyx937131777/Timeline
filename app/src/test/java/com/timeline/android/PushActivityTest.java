package com.timeline.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;

import com.timeline.android.db.Article;
import com.timeline.android.presenter.ArticlePresenter;
import com.timeline.android.presenter.PushPresenter;
import com.timeline.android.util.MyApplication;

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
import org.robolectric.shadows.ShadowToast;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class PushActivityTest
{

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PushPresenter mockPushPresenter;

    private PushActivity pushActivity;

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
        pushActivity.setPushPresenter(mockPushPresenter);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testEditContent()
    {
        String result = ((EditText)(pushActivity.findViewById(R.id.content))).getText().toString();
        String expected = article.getContent();
        assertEquals(expected,result);
    }

    @Test
    public void onImageViewClick()
    {
        assertFalse(pushActivity.getDialog().isShowing());
        pushActivity.findViewById(R.id.img).performClick();
        assertTrue(pushActivity.getDialog().isShowing());
    }

    @Test
    public void onCameraClick() throws URISyntaxException
    {
        pushActivity.getDialog().findViewById(R.id.bt_dialog_camera).performClick();
        Intent expected = new Intent("android.media.action.IMAGE_CAPTURE");
        expected.putExtra(MediaStore.EXTRA_OUTPUT, new URI(""));
        ShadowActivity shadowActivity = Shadow.extract(pushActivity);
        assertEquals(expected.getAction(), shadowActivity.getNextStartedActivity().getAction());
        assertFalse(pushActivity.getDialog().isShowing());
    }

    @Test
    public void onCancelClick()
    {
        pushActivity.findViewById(R.id.img).performClick();
        pushActivity.getDialog().findViewById(R.id.bt_dialog_cancel).performClick();
        assertFalse(pushActivity.getDialog().isShowing());
    }

    @Test
    public void onCommitClick()
    {
        pushActivity.findViewById(R.id.commit).performClick();
        verify(mockPushPresenter).commit();
    }

    @Test
    public void onRequestPermissionsResultAccept()
    {
        pushActivity.onRequestPermissionsResult(1,new String[]{""},new int[]{PackageManager.PERMISSION_GRANTED});
        Intent expected = new Intent("android.intent.action.GET_CONTENT");
        expected.setType("image/*");
        ShadowActivity shadowActivity = Shadow.extract(pushActivity);
        assertEquals(expected.toString(), shadowActivity.getNextStartedActivity().toString());
    }

    @Test
    public void onRequestPermissionsResultRefuse()
    {
        pushActivity.onRequestPermissionsResult(1,new String[]{""},new int[]{});
        String expected = "You denied the permission";
        assertEquals(expected.toString(), ShadowToast.getTextOfLatestToast());
    }
}