package com.designatednerd.androidlistenerexamples.presentation.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
 */
public class ImmersiveFragment extends Fragment{

    private TextView mExampleTextView;
    private View mBottomBar;
    private View mDecorView;


    /**
     * Handler to automatically hide after a delay
     */
    Handler mHideSystemUIHandler = new Handler () {
        @Override
        public void handleMessage(Message msg) {
            hideWithImmersiveNonSticky();
        }
    };

    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_immersive, null, false);

        mExampleTextView = (TextView)view.findViewById(R.id.fragment_immersive_textview);
        mBottomBar = view.findViewById(R.id.fragment_immersive_bottom_view);
        mDecorView = getActivity().getWindow().getDecorView();

        if (Build.VERSION.SDK_INT < 14) {
            mExampleTextView.setText(R.string.if_no_immersive_for_you);
        } else {
            //Make a bunch of scrollable text.
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 20; i++) {
                builder.append(getString(R.string.if_homer));
                if (i != 19) {
                    builder.append("\n\n");
                }
            }

            mExampleTextView.setText(builder.toString());

            delayedHide(5000);

            //Set up gesture detector
            mExampleTextView.setClickable(true);

            final GestureDetector clickDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    boolean isVisible = (mDecorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

                    if (isVisible) {
                        hideWithImmersiveNonSticky();
                    } else {
                        showWithImmersiveNonSticky();
                    }

                    return true;
                }
            });

            mExampleTextView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return clickDetector.onTouchEvent(event);
                }
            });
         }

        return view;
    }

    private void delayedHide(int delayMilliseconds) {
        mHideSystemUIHandler.removeMessages(0);
        mHideSystemUIHandler.sendEmptyMessageDelayed(0, delayMilliseconds);
    }


    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 14) {;
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
                        Log.d(Constants.LOG_TAG, "Nav bar height = " + activity.getNavigationBarHeight());
                        params.setMargins(0,0,0, (int)activity.getNavigationBarHeight());
                        mBottomBar.setLayoutParams(params);
                        mBottomBar.requestLayout();

                    } else {
                        mBottomBar.setVisibility(View.GONE);
                    }

                    Log.d(Constants.LOG_TAG, "Decor view height: " + mDecorView.getHeight());
                }
            });
        }
    }

    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    public void toggleVisibleChrome() {
        if (Build.VERSION.SDK_INT >= 14) {
            //Gets the Bitwise-or of flags currently set.
            int uiOptions = mDecorView.getSystemUiVisibility();
            int newUIOptions = uiOptions;

            //Check to see if immersive mode is enabled by or'ing the bitwise flag.
            boolean isImmersiveModeEnabled = (uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions;

            if (isImmersiveModeEnabled) {
                Log.d(Constants.LOG_TAG, "Turning immersive mode mode off. ");
            } else {
                Log.d(Constants.LOG_TAG, "Turning immersive mode mode on.");
            }

            // Navigation bar hiding:  Backwards compatible to API 14, which we're already checking for.
            newUIOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            // Status bar hiding: Backwards compatible to API 16
            if (Build.VERSION.SDK_INT >= 16) {
                newUIOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            }

            //Note: This only *adds* to the effect of having the nav and fullscreen views set
            //Immersive Mode: Backwards compatible to API 19. Wih
            if (Build.VERSION.SDK_INT >= 19) {
                newUIOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            mDecorView.setSystemUiVisibility(newUIOptions);

        }
    }

    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    private void hideWithImmersiveNonSticky() {
        if (Build.VERSION.SDK_INT >= 14) {

            //Preserve navigation bar height
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


}
