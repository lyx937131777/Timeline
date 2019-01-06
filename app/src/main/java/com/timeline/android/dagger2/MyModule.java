package com.timeline.android.dagger2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.timeline.android.presenter.ArticlePresenter;
import com.timeline.android.presenter.LoginPresenter;
import com.timeline.android.presenter.MainPresenter;
import com.timeline.android.presenter.PushPresenter;
import com.timeline.android.presenter.RegisterPresenter;
import com.timeline.android.util.CheckUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class MyModule
{
    private Context context;

    public MyModule(Context context)
    {
        this.context = context;
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    public CheckUtil provideCheckUtil(Context context)
    {
        return new CheckUtil(context);
    }

    @Provides
    public Context provideContext()
    {
        return context;
    }

    @Provides
    public LoginPresenter provideLoginPresenter(Context context, SharedPreferences pref, CheckUtil checkUtil)
    {
        return new LoginPresenter(context,pref,checkUtil);
    }

    @Provides
    public RegisterPresenter provideRegisterPresenter(Context context, CheckUtil checkUtil)
    {
        return new RegisterPresenter(context,checkUtil);
    }

    @Provides
    public ArticlePresenter provideArticlePresenter(Context context, SharedPreferences pref)
    {
        return new ArticlePresenter(context,pref);
    }

    @Provides
    public PushPresenter providePushPresenter(Context context, SharedPreferences pref)
    {
        return new PushPresenter(context, pref);
    }
}
