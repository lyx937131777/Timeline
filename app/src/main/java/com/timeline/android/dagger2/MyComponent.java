package com.timeline.android.dagger2;

import com.timeline.android.presenter.ArticlePresenter;
import com.timeline.android.presenter.LoginPresenter;
import com.timeline.android.presenter.MainPresenter;
import com.timeline.android.presenter.PushPresenter;
import com.timeline.android.presenter.RegisterPresenter;

import dagger.Component;

@Component(modules = {MyModule.class})
public interface MyComponent
{
    LoginPresenter loginPresenter();

    RegisterPresenter registerPresenter();

    ArticlePresenter articlePresenter();

    PushPresenter pushPresenter();
}
