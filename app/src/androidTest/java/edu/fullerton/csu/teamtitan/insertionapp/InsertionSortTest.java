package edu.fullerton.csu.teamtitan.insertionapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by yamin on 9/8/16.
 */
@RunWith(AndroidJUnit4.class)
public class InsertionSortTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void test_ElementsAreShown() throws Exception {
        onView(withId(R.id.txtInput))
            .check(matches(isDisplayed()));

        onView(withId(R.id.btnSort))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnQuit))
                .check(matches(isDisplayed()));
    }
}
