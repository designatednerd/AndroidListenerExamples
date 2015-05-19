package com.designatednerd.androidlistenerexamples.test;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.designatednerd.androidlistenerexamples.R;
import com.designatednerd.androidlistenerexamples.presentation.activity.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.designatednerd.androidlistenerexamples.viewaction.CustomViewActions.*;


/**
 * Created by ellen on 4/15/15.
 *
 * NOTE: Since the test runner is set in the build.gradle, you don't have to explicitly call it out here.
 */
public class EspressoUITestingExample {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    private static final long UI_TEST_TIMEOUT = 5 * 1000; //5 seconds
    private static final String TEST_LOG_TAG = "UI Tests!";

    /******************
     * SETUP/TEARDOWN *
     ******************/

    @Before
    public void setUp() {
        Log.d(TEST_LOG_TAG, "Test was set up!");
    }

    @After
    public void tearDown() {
        Log.d(TEST_LOG_TAG, "Test was torn down!");
    }

    /**************************
     * PRIVATE HELPER METHODS *
     **************************/

    /**
     * Convenience method to help get the title for the given drawer index
     * @param aIndex The DrawerIndex object for the fragment you wish to access
     * @return The title of that item, as a string.
     */
    private String titleForDrawerIndex(MainActivity.DrawerIndex aIndex) {
        int index = MainActivity.DrawerIndex.valueOf(aIndex);
        String[] titles = getContext().getResources().getStringArray(R.array.examples_array);
        return titles[index];
    }

    /**
     * @return The current context.
     */
    private Context getContext() {
        return mainActivity.getActivity();
    }

    /**
     * Helper to go to the spanned text section since we are navigating here more than once.
     */
    private void goToSpannedTextSection() {
        //Open up the drawer to be able to check something
        openDrawer(R.id.drawer_layout);

        //Tap the "Spanned Text" title
        onView(withText(titleForDrawerIndex(MainActivity.DrawerIndex.INDEX_TEXT_SPAN)))
                .perform(click());

        //Wait for the spanned text scrollview
        onView(isRoot())
                .perform(waitForMatch(withId(R.id.spanned_text_scrollview), UI_TEST_TIMEOUT));

        //Make sure the drawer closed.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed()));
    }

    /*********
     * TESTS *
     *********/

    @Test
    public void navigateToKittens() {
        //Open up the drawer to be able to check something
        openDrawer(R.id.drawer_layout);

        //Tap the "Kittens" title
        onView(withText(titleForDrawerIndex(MainActivity.DrawerIndex.INDEX_KITTENS)))
                .perform(click());

        //Wait for the webview to show up.
        onView(isRoot())
                .perform(waitForMatch(withId(R.id.fragment_kittens_webview), UI_TEST_TIMEOUT));

        //Make sure the drawer closed.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed()));
    }

    @Test
    public void navigateToSpannedText() {
        //Use the helper method to perform this navigation.
        goToSpannedTextSection();
    }

    @Test
    public void alertShowsInSpannedText() {
        //Use the helper method to go to the spanned text section.
        goToSpannedTextSection();

        //Check tap on the alert dialog link text using the custom action
        String fullText = getContext().getString(R.string.stf_multiple_spans_plain_text);
        String alertText = getContext().getString(R.string.stf_alert_dialog_link_text);
        onView(withId(R.id.spanned_text_multiple))
                .perform(tapClickableSpanWithText(fullText, alertText));

        //Wait for the alert to show up.
        /* NOTE: Simply adding this method may not be sufficient for all devices - some show
                AlertDialogs in a completely separate window, and so waiting for a match on the
                root view will fail unless you wait for the new window to show up before you start
                looping through it, looking for this match.

                But it works on genymotion, so for my purposes: ¯\_(ツ)_/¯
        */
        onView(isRoot())
                .perform(waitForMatch(withText(R.string.stf_alert_text), UI_TEST_TIMEOUT));
    }

    @Test
    public void navigateToVideo() {
        //Open the drawer and select the "Fullscreen Video" item
        openDrawer(R.id.drawer_layout);
        onView(withText(titleForDrawerIndex(MainActivity.DrawerIndex.INDEX_VIDEO)))
                .perform(click());

        //Wait for the video webview, then make sure the drawer closed.
        onView(isRoot())
                .perform(waitForMatch(withId(R.id.fragment_fsv_webview), UI_TEST_TIMEOUT));
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed()));
    }

    @Test
    public void navigateBackToNotifications() {
        //Use the helper method to go to the spanned text section.
        goToSpannedTextSection();

        //Open the drawer and select the "Notification" item
        openDrawer(R.id.drawer_layout);
        onView(withText(titleForDrawerIndex(MainActivity.DrawerIndex.INDEX_NOTIFICATION)))
                .perform(click());

        //Wait for the root view of the notification fragment, and make sure the drawer closed
        onView(isRoot())
                .perform(waitForMatch(withId(R.id.nf_root_view), UI_TEST_TIMEOUT));
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed()));

    }
}
