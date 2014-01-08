package com.designatednerd.androidlistenerexamples.presentation.activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.designatednerd.androidlistenerexamples.Constants;
import com.designatednerd.androidlistenerexamples.R;
import com.designatednerd.androidlistenerexamples.presentation.fragment.FullscreenVideoWebviewFragment;
import com.designatednerd.androidlistenerexamples.presentation.fragment.ImmersiveFragment;
import com.designatednerd.androidlistenerexamples.presentation.fragment.KittensFragment;
import com.designatednerd.androidlistenerexamples.presentation.fragment.SpannedTextFragment;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    //Drawer Variables
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu mMenu;

    //Variables for Immersive Mode.
    private int mStatusBarHeight;
    private int mNavigationBarHeight;

    //Enum with handler to automatically return the proper enum value based on an integer.
    private enum DrawerIndex  {
        INDEX_VIDEO,
        INDEX_KITTENS,
        INDEX_TEXT_SPAN,
        INDEX_IMMERSIVE;

        public static DrawerIndex fromInteger(int index) {
            switch(index) {
                case 3:
                    return INDEX_VIDEO;
                case 2:
                    return INDEX_KITTENS;
                case 1:
                    return INDEX_TEXT_SPAN;
                case 0:
                    return INDEX_IMMERSIVE;
                default:
                    return null;
            }
        }

        public static DrawerIndex fromTag(String tag) {
            if (tag.equalsIgnoreCase(TAG_VIDEO_FRAGMENT)) {
                return INDEX_VIDEO;
            } else if (tag.equalsIgnoreCase(TAG_KITTENS_FRAGMENT)) {
                return INDEX_KITTENS;
            } else if (tag.equalsIgnoreCase(TAG_TEXT_SPAN_FRAGMENT)) {
                return INDEX_TEXT_SPAN;
            } else if (tag.equalsIgnoreCase(TAG_IMMERSIVE_FRAGMENT)) {
                return INDEX_IMMERSIVE;
            } else {
                return null;
            }
        }

        public static int valueOf(DrawerIndex index) {
            switch (index) {
                case INDEX_VIDEO:
                    return 3;
                case INDEX_KITTENS:
                    return 2;
                case INDEX_TEXT_SPAN:
                    return 1;
                case INDEX_IMMERSIVE:
                    return 0;
                default:
                    return -1;
            }
        }
    };

    //Fragment tags
    public static final String TAG_KITTENS_FRAGMENT = "fragment_kittens";
    public static final String TAG_VIDEO_FRAGMENT = "fragment_video";
    public static final String TAG_TEXT_SPAN_FRAGMENT = "fragment_text_span";
    public static final String TAG_IMMERSIVE_FRAGMENT = "fragment_immersive";

    //Extras
    public static final String EXTRA_FRAGMENT_TO_LAUNCH = "fragment_to_launch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup the drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        //Set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.examples_array)));

        //Add an OnItemClickListener to the listView.
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Take action based on the selection
                selectedMenuItemAtPosition(position);

                //Close the drawer.
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        //Setup the toggle to show/hide the drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                if (Build.VERSION.SDK_INT >= 11) {
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                } else {
                    //Call it directly on gingerbread.
                    onPrepareOptionsMenu(mMenu);
                }

                startFullscreenIfNeeded();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                if (Build.VERSION.SDK_INT >= 11) {
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                } else {
                    //Call it directly on gingerbread.
                    onPrepareOptionsMenu(mMenu);
                }
            }

            /** Called as the drawer slides by percentage **/
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //SlideOffset comes in on a scale of 0-1.
                //40% looks roughly about right to swap out
                if (slideOffset > 0.4) {
                    getSupportActionBar().setTitle(R.string.drawer_close);
                    stopFullscreenIfNeeded();
                } else {
                    setTitleForActiveTag();
                }
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null && !getIntent().hasExtra(EXTRA_FRAGMENT_TO_LAUNCH)) {
            //Automatically select the first item
            selectedMenuItemAtPosition(0);
        } else if (getIntent().hasExtra(EXTRA_FRAGMENT_TO_LAUNCH)) {
            //Handle any passed-in intent. 
            String tagToLaunch = getIntent().getStringExtra(EXTRA_FRAGMENT_TO_LAUNCH);
            DrawerIndex itemToLaunch = DrawerIndex.fromTag(tagToLaunch);
            int indexToLaunch = DrawerIndex.valueOf(itemToLaunch);
            selectedMenuItemAtPosition(indexToLaunch);
        } //else saved instance state is not null and you should probably handle that. I'm not.
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    //Note: For this to fire, you have to declare in your AndroidManifest.xml what changes
    // you're handling. For this activity, I've declared android:configChanges="orientation|screenSize",
    // to show that this activity is going to handle rotation.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);

        //If there's a custom view, then set the customViewContainer as the content view.
        if (mCustomView != null) {
            setContentView(mCustomViewContainer);

            //TODO: Better handling of rotation including maintenance of play/pause state. 
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //If the drawer toggle was not selected,
        switch (item.getItemId()) {
            case R.id.action_share:
                Toast.makeText(this, "Share!", Toast.LENGTH_LONG).show();
                break;
            default:
                Log.d(Constants.LOG_TAG, "Unhandled action item!");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        // If the nav drawer is open, hide action items that are related to the content view.
        menu.findItem(R.id.action_share).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Method to handle selection from the drawer menu.
     * @param position - The index at which the user has selected a menu item.
     */
    private void selectedMenuItemAtPosition(int position) {
        switch (DrawerIndex.fromInteger(position)) {
            case INDEX_VIDEO:
                Log.d(Constants.LOG_TAG, "Selected Video");
                FullscreenVideoWebviewFragment videoFragment = new FullscreenVideoWebviewFragment();
                showFragment(videoFragment, TAG_VIDEO_FRAGMENT);
                break;
            case INDEX_KITTENS:
                Log.d(Constants.LOG_TAG, "Selected Kittens!");
                KittensFragment kittensFragment = new KittensFragment();
                showFragment(kittensFragment, TAG_KITTENS_FRAGMENT);
                break;
            case INDEX_TEXT_SPAN:
                Log.d(Constants.LOG_TAG, "Selected Text Span!");
                SpannedTextFragment spannedTextFragment = new SpannedTextFragment();
                showFragment(spannedTextFragment, TAG_TEXT_SPAN_FRAGMENT);
                break;
            case INDEX_IMMERSIVE:
                Log.d(Constants.LOG_TAG, "Selected Immersive!");
                ImmersiveFragment immersiveFragment = new ImmersiveFragment();
                showFragment(immersiveFragment, TAG_IMMERSIVE_FRAGMENT);
            default:
                Log.e(Constants.LOG_TAG, "Unhandled position selection from drawer " + position);
                break;
        }
    }


    /**
     * Shows the given fragment and updates the action bar title
     * @param fragment - The fragment to show
     * @param tag - The tag associated with the fragment.
     */
    public void showFragment(Fragment fragment, String tag) {
        fragment.setRetainInstance(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment, tag)
                .addToBackStack(null)
                .commit();
        setTitleForTag(tag);
    }

    /**
     * Gets the fragment tag of the currently visible fragment
     * @return The currently visible fragment's tag
     */
    private String getActiveFragmentTag() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments == null) {
            return null;
        }

        for (Fragment fragment : fragments) {
            if (fragment.isVisible()) {
                return fragment.getTag();
            }
        }

        return null;
    }

    /**
     * Gets the title based on the passed in fragment tag.
     * @param fragmentTag - The fragment tag of the fragment you want the title for.
     * @return The title for the given fragment.
     */
    private String titleForTag(String fragmentTag) {
        if (fragmentTag == null) {
            return getString(R.string.app_name);
        }
        DrawerIndex tagIndex = DrawerIndex.fromTag(fragmentTag);
        int tagValue = DrawerIndex.valueOf(tagIndex);

        String title = getResources().getStringArray(R.array.examples_array)[tagValue];
        return title;
    }

    /**
     * Sets the title based on a given fragment's tag to the action bar.
     * @param tag - The tag to use to find the title.
     */
    private void setTitleForTag(String tag) {
        getSupportActionBar().setTitle(titleForTag(tag));
    }

    /**
     * Sets the title to the action bar based on the tag of the currently active fragment.
     */
    private void setTitleForActiveTag() {
        String activeTag = getActiveFragmentTag();
        getSupportActionBar().setTitle(titleForTag(activeTag));
    }

    /**
     * Calculates the height of the Status Sar (the bar at the top of the screen).
     * @return The height of the Status Bar.
     */
    private int getStatusBarHeight() {
        if (mStatusBarHeight == 0) {
            Rect screenFrame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(screenFrame);
            mStatusBarHeight = screenFrame.top;
        }

        return mStatusBarHeight;
    }

    /**
     * Calculates the height of the Navigation Bar (ie, the bar at the bottom of the screen with
     * the Home, Back, and Recent buttons).
     * @return The height of the Navigation Bar. Will return 0 if this is in hardware.
     */
    @SuppressLint("Deprecation") //Device getHeight() is deprecated but the replacement is API 13+
    public int getNavigationBarHeight() {
        if (mNavigationBarHeight == 0) {
            Rect visibleFrame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(visibleFrame);
            DisplayMetrics outMetrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= 17) {
                //The sane way to calculate this.
                getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
                mNavigationBarHeight = outMetrics.heightPixels - visibleFrame.bottom;
            } else {
                getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

                //visibleFrame will return the same as the outMetrics the first time through,
                // then will have the visible frame the full size of the screen when it comes back
                // around to close. OutMetrics will always be the view - the nav bar.
                mNavigationBarHeight = visibleFrame.bottom - outMetrics.heightPixels;
            }
        }

        return mNavigationBarHeight;
    }

    /**
     * FULLSCREEN VIDEO HELPER CODE
     *
     * special thanks to: http://stackoverflow.com/a/15127046/681493
     */

    /**
     * Fullscreen video variables
     */
    //Stores the custom view passed back by the WebChromeClient
    private View mCustomView;

    //Stores the main content view of the activity
    private View mContentView;

    //Container in which to place the custom view.
    private FrameLayout mCustomViewContainer;

    //Stores the CustomViewCallback interface.
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    /**
     * Method to mirror onShowCustomView from the WebChrome client, allowing WebViews in a Fragment
     * to show custom views.
     * @param view - The custom view.
     * @param callback - The callback interface for the custom view.
     */
    public void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        //If there's already a custom view, this is a duplicate call, and we should
        // terminate the new view, then bail out.
        if (mCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }

        //Create a reusable set of FrameLayout.LayoutParams
        FrameLayout.LayoutParams fullscreenParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        //Save the drawer view into an instance variable, then hide it.
        mContentView = findViewById(R.id.drawer_layout);
        mContentView.setVisibility(View.GONE);

        //Create a new custom view container
        mCustomViewContainer = new FrameLayout(MainActivity.this);
        mCustomViewContainer.setLayoutParams(fullscreenParams);
        mCustomViewContainer.setBackgroundResource(android.R.color.black);

        //Set view to instance variable, then add to container.
        mCustomView = view;
        view.setLayoutParams(fullscreenParams);
        mCustomViewContainer.addView(mCustomView);
        mCustomViewContainer.setVisibility(View.VISIBLE);

        //Save the callback an instance variable.
        mCustomViewCallback = callback;

        //Hide the action bar
        getSupportActionBar().hide();

        //Set the custom view container as the activity's content view.
        setContentView(mCustomViewContainer);
    }

    /**
     * Method to mirror onShowCustomView from the WebChrome client, allowing WebViews in a Fragment
     * to hide custom views.
     */
    public void hideCustomView() {
        if (mCustomView == null) {
            //Nothing to hide - return.
            return;
        } else {
            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            mCustomViewContainer.removeView(mCustomView);
            mCustomViewContainer.setVisibility(View.GONE);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;

            // Show the ActionBar
            getSupportActionBar().show();

            // Show the content view.
            mContentView.setVisibility(View.VISIBLE);
            setContentView(mContentView);
        }
    }

    @Override
    public void onBackPressed() {
        //If there's a custom view, hide it
        if (mCustomView != null) {
            hideCustomView();
        } else {
            //Otherwise, treat back press normally.
            super.onBackPressed();
        }
    }

    /**
     * Adds margins to compensate for the {@link ImmersiveFragment}'s setting of FullScreen mode if
     * it is the topmost fragment.
     */
    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    private void stopFullscreenIfNeeded() {
        if (Build.VERSION.SDK_INT >= 14 && //At least ICS
            getActiveFragmentTag().equalsIgnoreCase(TAG_IMMERSIVE_FRAGMENT)) { //In the immersive fragment
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mDrawerLayout.getLayoutParams();
            //TODO: Fix this calculation for SDK < 18
            params.setMargins(0, getSupportActionBar().getHeight() + getStatusBarHeight(), 0, 0);

            mDrawerLayout.setLayoutParams(params);
            mDrawerLayout.requestLayout();
        }
    }

    /**
     * Removes or adds margins to compensate for the {@link ImmersiveFragment}'s setting of FullScreen mode.
     *
     * Will remove the margins if going back to the {@link ImmersiveFragment}, and will add them if going to
     * any other fragment
     */
    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    private void startFullscreenIfNeeded() {
        if (Build.VERSION.SDK_INT >= 14) { //At least ICS
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mDrawerLayout.getLayoutParams();
            //TODO: Fix this calculation for SDK < 18
            if (getActiveFragmentTag().equalsIgnoreCase(TAG_IMMERSIVE_FRAGMENT)) { //In the immersive fragment
                params.setMargins(0, 0, 0, 0);
            } else {
                params.setMargins(0, getSupportActionBar().getHeight() + getStatusBarHeight(), 0, getNavigationBarHeight());
            }

            mDrawerLayout.setLayoutParams(params);
            mDrawerLayout.requestLayout();
        }
    }

    @SuppressLint("NewApi") //Suppressed since we're wrapping the call for an API 11+ method in a check for API 14+.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= 14 &&
            getActiveFragmentTag().equalsIgnoreCase(TAG_IMMERSIVE_FRAGMENT)) {
            //Forward the window focus change to the immersive fragment.
            ImmersiveFragment immersiveFragment = (ImmersiveFragment)getSupportFragmentManager().findFragmentByTag(TAG_IMMERSIVE_FRAGMENT);
            immersiveFragment.handleWindowFocusChange(hasFocus);
        }
    }
}
