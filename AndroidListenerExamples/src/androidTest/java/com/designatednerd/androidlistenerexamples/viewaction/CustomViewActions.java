package com.designatednerd.androidlistenerexamples.viewaction;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.util.TreeIterables;
import android.text.Layout;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.designatednerd.androidlistenerexamples.model.ClickableTextSpan;

import org.hamcrest.Matcher;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by ellen on 4/15/15.
 *
 * This class provides application-specific ViewActions which can be used for testing.
 */
public class CustomViewActions {

    /**
     * A custom ViewAction which allows the system to wait for a view matching a passed in matcher
     * @param aViewMatcher The matcher to wait for
     * @param timeout How long, in milliseconds, to wait for this match.
     * @return The constructed @{link ViewAction}.
     */
    public static ViewAction waitForMatch(final Matcher<View> aViewMatcher, final long timeout) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Waiting for view matching " + aViewMatcher;
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();

                //What time is it now, and what time will it be when this has timed out?
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + timeout;

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (aViewMatcher.matches(child)) {
                            //we found it! Yay!
                            return;
                        }
                    }

                    //Didn't find it, loop around a bit.
                    uiController.loopMainThreadForAtLeast(50);
                } while (System.currentTimeMillis() < endTime);

                //The action has timed out.
                throw new PerformException.Builder()
                        .withActionDescription(getDescription())
                        .withViewDescription("")
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    public static ViewAction tapClickableSpanWithText(final String aFullText, final String aSubstring) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Trying to tap \"" + aSubstring + "\" in view with full text \"" + aFullText + "\"";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                if (view instanceof TextView) {

                    //Figure out where the hell the clickable text IS.
                    //Care of: http://stackoverflow.com/a/11934366/681493

                    TextView textView = (TextView) view;
                    Layout textViewLayout = textView.getLayout();


                    SpannableString fullSpannable = (SpannableString) textView.getText();

                    Object[] spans = fullSpannable.getSpans(0, fullSpannable.length(), Object.class);

                    ClickableTextSpan span = null;
                    for (Object object : spans) {
                        if (object instanceof ClickableTextSpan) {
                            ClickableTextSpan foundSpan = (ClickableTextSpan)object;
                            if (foundSpan.getText().equals(aSubstring)) {
                                //Found the correct span!
                                span = foundSpan;
                            }
                        }
                    }

                    if (span == null) {
                        throw new PerformException.Builder()
                                .withActionDescription(getDescription())
                                .withViewDescription("")
                                .withCause(new InvalidParameterException("The TextView passed in did not contain a ClickableTextSpan!"))
                                .build();
                    }

                    //Else, keep going.
                    int startOffsetOfClickable = fullSpannable.getSpanStart(span);
                    float startXOfClickable = textViewLayout.getPrimaryHorizontal(startOffsetOfClickable);
                    int currentLineStartOffset = textViewLayout.getLineForOffset(startOffsetOfClickable);
                    float lineTop = textViewLayout.getLineTop(currentLineStartOffset);
                    float lineBottom = textViewLayout.getLineBottom(currentLineStartOffset);
                    float lineMiddle = (lineTop + lineBottom) / 2;

                    int[] xyOnscreen = new int[2];
                    textView.getLocationOnScreen(xyOnscreen);

                    float startOfXOnScreen = startXOfClickable + xyOnscreen[0];
                    float middleYOfFirstLineOnScreen = lineMiddle + xyOnscreen[1];

                    final float[] coords = { startOfXOnScreen, middleYOfFirstLineOnScreen };

                    CoordinatesProvider provider = new CoordinatesProvider() {
                        @Override
                        public float[] calculateCoordinates(View view) {
                            return coords;
                        }
                    };

                    GeneralClickAction action = new GeneralClickAction(Tap.SINGLE, provider, Press.FINGER);
                    action.perform(uiController, view);
                    return;
                } else {
                    //Did not find it.
                    throw new PerformException.Builder()
                            .withActionDescription(getDescription())
                            .withViewDescription("")
                            .withCause(new InvalidParameterException("The view passed in was not a TextView!"))
                            .build();
                }
            }
        };
    }
}
