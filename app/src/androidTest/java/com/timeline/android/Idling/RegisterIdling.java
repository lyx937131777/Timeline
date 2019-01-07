package com.timeline.android.Idling;

import android.support.test.espresso.IdlingResource;

import com.timeline.android.RegisterActivity;

public class RegisterIdling implements IdlingResource
{

    private IdlingResource.ResourceCallback mCallback;
    private RegisterActivity registerActivity;
    public RegisterIdling(RegisterActivity registerActivity){
        this.registerActivity = registerActivity;
    }
    @Override
    public String getName() {
        return "RegisterIdling";// 注册回调的key，确保唯一
    }

    @Override
    public boolean isIdleNow() {
        boolean isIdle = registerActivity != null && registerActivity.isSyncFinished();
        if (isIdle && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.mCallback=callback;
    }
}
