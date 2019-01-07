package com.timeline.android;

import com.timeline.android.db.Article;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({LoginActivityInstrumentedTest.class,RegisterActivityInstrumentedTest.class,
        MainActivityInstrumentedTest.class,ArticleActivityInstrumentedTest.class,PushActivityInstrumentedTest.class})
public class SuiteInstrumentedTest
{
}
