package com.designatednerd.androidlistenerexamples.presentation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.designatednerd.androidlistenerexamples.Constants;
import com.designatednerd.androidlistenerexamples.R;

import java.util.List;

/**
 * Created by ellen on 5/12/14.
 */
public class NotificationFragment extends Fragment {
    private View mMainView;
    private List<String> mRollupList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_notification, container, false);

        //Setup the buttons
        Button simple = (Button)mMainView.findViewById(R.id.nf_simple_notification_button);
        simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSimpleNotification();
            }
        });

        Button bigText = (Button)mMainView.findViewById(R.id.nf_big_text_notification_button);
        bigText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBigTextNotification();
            }
        });

        Button bigPicture = (Button)mMainView.findViewById(R.id.nf_big_picture_notification_button);
        bigPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBigPictureNotification();
            }
        });

        Button rollup = (Button)mMainView.findViewById(R.id.nf_rollup_notification_button);
        rollup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRollupNotification();
            }
        });

        Button remoteViews = (Button)mMainView.findViewById(R.id.nf_remote_views_notification_button);
        remoteViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemoteViewsNotification();
            }
        });



        return mMainView;
    }


    private void showSimpleNotification() {
        Log.d(Constants.LOG_TAG, "SIMPLE NOTIFICATION");
    }

    private void showBigTextNotification() {
        Log.d(Constants.LOG_TAG, "BIG TEXT NOTIFICATION");
    }

    private void showBigPictureNotification() {
        Log.d(Constants.LOG_TAG, "BIG PICTURE NOTIFICATION");
    }

    private void showRollupNotification() {
        Log.d(Constants.LOG_TAG, "ROLL UP NOTIFICATION");
    }

    private void showRemoteViewsNotification() {
        Log.d(Constants.LOG_TAG, "REMOTE VIEWS NOTIFICATION");
    }
}
