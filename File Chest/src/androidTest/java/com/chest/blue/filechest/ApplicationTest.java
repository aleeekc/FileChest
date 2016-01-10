package com.chest.blue.filechest;

import android.app.Application;
import android.support.test.espresso.assertion.ViewAssertions;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

@LargeTest
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getApplication();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testListGoesOverTheFold() {
       onView(withText("Hello world!")).check(ViewAssertions.matches(isDisplayed()));
    }

}
