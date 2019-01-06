package com.timeline.android;


import com.timeline.android.presenter.SuitePresenterTest;
import com.timeline.android.util.CheckUtil;
import com.timeline.android.util.CheckUtilTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SuiteActivityTest.class,SuitePresenterTest.class,CheckUtilTest.class})
public class SuiteTest
{
}
