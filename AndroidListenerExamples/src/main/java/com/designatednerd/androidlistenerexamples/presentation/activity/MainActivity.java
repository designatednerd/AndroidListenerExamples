package com.designatednerd.androidlistenerexamples.presentation.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.designatednerd.androidlistenerexamples.R;
import com.designatednerd.androidlistenerexamples.Constants;
import com.designatednerd.androidlistenerexamples.presentation.fragment.FullscreenVideoWebviewFragment;

public class MainActivity extends ActionBarActivity {

    //Drawer Variables
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private enum DrawerIndex  {
        INDEX_VIDEO;

        public static DrawerIndex fromInteger(int index) {
            switch(index) {
                case 0:
                    return INDEX_VIDEO;
            }
            return null;
        }
    };



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

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedMenuItemAtPosition(position);
            }
        });

        //Setup the toggle to show/hide the drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.drawer_close);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            //Automatically select the first item
            selectedMenuItemAtPosition(0);
        }

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
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
                videoFragment.setRetainInstance(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, videoFragment, TAG_VIDEO_FRAGMENT)
                        .commit();

                break;
            default:
                Log.e(Constants.LOG_TAG, "Unhandled position selection from drawer " + position);
                break;
        }
    }

    /**
     * FULLSCREEN VIDEO HELPER CODE
     *
     * special thanks to: http://stackoverflow.com/a/15127046/681493
     */

    /**
     * Fullscreen video variables
     */
    //Fragment tag
    private static final String TAG_VIDEO_FRAGMENT = "fragment_video";

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
}
