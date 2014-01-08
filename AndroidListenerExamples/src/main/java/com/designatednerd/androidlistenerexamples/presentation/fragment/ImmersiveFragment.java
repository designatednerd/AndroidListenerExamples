package com.designatednerd.androidlistenerexamples.presentation.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.designatednerd.androidlistenerexamples.Constants;
import com.designatednerd.androidlistenerexamples.R;
import com.designatednerd.androidlistenerexamples.presentation.activity.MainActivity;

/**
 * Created by Ellen Shapiro on 1/6/14.
 *
 * Note: While this currently works on API 14+, it looks pretty terrible on anything below 17.
 * Revisions welcome.
 */
public class ImmersiveFragment extends Fragment{

    /**
     * The text view where our text is displayed
     */
    private TextView mExampleTextView;

    /**
     * An example bottom bar representing your app's control UI
     */
    private View mBottomBar;

    /**
     * A reference to the Window's DecorView
     */
    private View mDecorView;

    private boolean mStickyMode;

    /**
     * A default length of time after which to automatically hide the controls.
     */
    private static final int DEFAULT_HIDE_MILLISECONDS = 5000;

    /**
     * Handler to automatically hide after a specified delay
     */
    Handler mHideSystemUIHandler = new Handler () {
        @Override
        public void handleMessage(Message msg) {
            if (!mStickyMode) {
                hideWithImmersiveNonSticky();
            }
        }
    };

    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_immersive, null, false);

        //Grab instance variables
        mExampleTextView = (TextView)view.findViewById(R.id.fragment_immersive_textview);
        mBottomBar = view.findViewById(R.id.fragment_immersive_bottom_view);
        mDecorView = getActivity().getWindow().getDecorView();

        //CHANGE ME TO SEE NON-STICKY VS. STICKY MODES
        mStickyMode = false;

        //Check to make sure the API can at least *sort of* support this.
        if (Build.VERSION.SDK_INT < 14) {
            //Message of sadness for old phones.
            mExampleTextView.setText(R.string.if_no_immersive_for_you);
        } else {
            //Make a bunch of scrollable text.
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 50; i++) {
                builder.append(getString(R.string.if_homer));
                if (i != 49) {
                    builder.append("\n\n");
                }
            }

            mExampleTextView.setText(builder.toString());

            if (!mStickyMode) {
                //Automatically hide after the default time interval on load.
                delayedHide(DEFAULT_HIDE_MILLISECONDS);

                //Set up gesture detector to hide/show on tap.
                mExampleTextView.setClickable(true);

                final GestureDetector clickDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        //Get the current visibility so that it may be toggled.
                        boolean isVisible = (mDecorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

                        if (isVisible) {
                            //Tell the system UI handler to cancel any automatic hide
                            cancelExistingAutoHides();
                            hideWithImmersiveNonSticky();
                        } else {
                            showWithImmersiveNonSticky();
                        }

                        return true;
                    }
                });

                //Add onTouchListener to grab the event and pass it to the click handler.
                mExampleTextView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return clickDetector.onTouchEvent(event);
                    }
                });

            }
         }

        return view;
    }

    /**
     * Wrapper method to call the hide handler after a specified delay
     * @param delayMilliseconds - The number of milliseconds to hide
     */
    private void delayedHide(int delayMilliseconds) {
        cancelExistingAutoHides();

        //Trigger the handler after the specified delay.
        mHideSystemUIHandler.sendEmptyMessageDelayed(0, delayMilliseconds);
    }


    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (Build.VERSION.SDK_INT >= 14) {
            //Note! This does NOT get called in IMMERSIVE_STICKY mode, so if you have UI that you
            // want to have tied to the nav bars, you should use plain IMMERSIVE mode.
            mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    boolean visible = (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

                    //Make sure the visibility state of your app's chrome is tied to the visibility of the system chrome.
                    if (visible) {
                        mBottomBar.setVisibility(View.VISIBLE);

                        //Reset the bottom of the bottom bar since you're in fullscreen mode
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mBottomBar.getLayoutParams();
                        MainActivity activity = (MainActivity)getActivity();

                        params.setMargins(0,0,0, activity.getNavigationBarHeight());
                        mBottomBar.setLayoutParams(params);
                        mBottomBar.requestLayout();

                        //SDK 18+: Shows Automatically.
                        if (Build.VERSION.SDK_INT < 18) {
                            activity.getSupportActionBar().show();
                            //TODO: Fix layout issue after re-showing.
                        }

                    } else {
                        mBottomBar.setVisibility(View.GONE);

                        //SDK 18+: Hides Automatically.
                        if (Build.VERSION.SDK_INT < 18) {
                            ActionBarActivity activity = (ActionBarActivity)getActivity();
                            activity.getSupportActionBar().hide();
                        }
                    }
                }
            });
        }
    }

    /**
     * Hides the
     */
    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    private void hideWithImmersiveNonSticky() {
        if (Build.VERSION.SDK_INT >= 14) {

            //Preserve navigation bar height by triggering this calculation.
            MainActivity activity = (MainActivity)getActivity();
            activity.getNavigationBarHeight();

            mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    private void showWithImmersiveNonSticky() {
        if (Build.VERSION.SDK_INT >= 14) {
            mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    public void handleWindowFocusChange(boolean hasFocus) {
        if (hasFocus) {
            Log.d(Constants.LOG_TAG, "Immersive fragment window has focus");
            if (!mStickyMode) {
                //Once the window regains focus, hide the UI automatically.
                delayedHide(DEFAULT_HIDE_MILLISECONDS);
            } else {
                //Set up the sticky flag here!
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        } else {
            Log.d(Constants.LOG_TAG, "Immersive fragment window has ADD");
            //When the window loses focus, don't auto-hide (which would hide displaying menu items
            // which the user just opened, for instance)
            cancelExistingAutoHides();
        }
    }

    private void cancelExistingAutoHides() {
        mHideSystemUIHandler.removeMessages(0);
    }
}
