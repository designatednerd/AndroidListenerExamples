package com.designatednerd.androidlistenerexamples.presentation.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.designatednerd.androidlistenerexamples.Constants;
import com.designatednerd.androidlistenerexamples.R;
import com.designatednerd.androidlistenerexamples.presentation.activity.MainActivity;

import java.util.List;

/**
 * Created by ellen on 5/12/14.
 */
public class NotificationFragment extends Fragment {
    private View mMainView;
    private List<String> mInboxList;

    private static final int TAG_SIMPLE_NOTIFICATION = 1;
    private static final int TAG_BIG_TEXT_NOTIFICATION = 2;
    private static final int TAG_BIG_PICTURE_NOTIFICATION = 3;
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

        Button inbox = (Button)mMainView.findViewById(R.id.nf_inbox_notification_button);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInboxNotification();
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

    private PendingIntent pendingIntentForNotification() {
        //Create the intent you want to show when the notification is clicked
        Intent intent = new Intent(getActivity(), MainActivity.class);

        //Add any extras (in this case, that you want to relaunch this fragment)
        intent.putExtra(MainActivity.EXTRA_FRAGMENT_TO_LAUNCH, MainActivity.TAG_NOTIFICATION_FRAGMENT);

        //This will hold the intent you've created until the notification is tapped.
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 1, intent, 0);
        return pendingIntent;
    }

    private void showSimpleNotification() {
        //Use the NotificationCompat compatibility library in order to get gingerbread support.
        Notification notification = new NotificationCompat.Builder(getActivity())
                //Title of the notification
                .setContentTitle(getString(R.string.nf_simple_title))
                //Content of the notification once opened
                .setContentText(getString(R.string.nf_simple_message))
                //This bit will show up in the notification area in devices that support that
                .setTicker(getString(R.string.nf_simple_ticker))
                //Icon that shows up in the notification area
                .setSmallIcon(R.drawable.ic_notify)
                //Icon that shows up in the drawer
                .setLargeIcon(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_launcher))
                //Set the intent
                .setContentIntent(pendingIntentForNotification())
                //Build the notification with all the stuff you've just set.
                .build();

        //Add the auto-cancel flag to make it dismiss when clicked on
        //This is a bitmask value so you have to pipe-equals it.
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        //Grab the NotificationManager and post the notification
        NotificationManager notificationManager = (NotificationManager)
                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        //Set a tag so that the same notification doesn't get reposted over and over again and
        //you can grab it again later if you need to.
        notificationManager.notify(TAG_SIMPLE_NOTIFICATION, notification);
    }

    private void showBigTextNotification() {
        //Use the NotificationCompat compatibility library in order to not have this barf on < 4.1 devices.
        Notification notification = new NotificationCompat.Builder(getActivity())
                .setContentTitle(getString(R.string.nf_big_text_title))
                //This will show on devices that don't support the big text and if further notifications
                //come in after the big text notification.
                .setContentText(getString(R.string.nf_big_text_truncated))
                .setTicker(getString(R.string.nf_big_text_truncated))
                .setSmallIcon(R.drawable.ic_notify)
                .setLargeIcon(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_launcher))
                .setContentIntent(pendingIntentForNotification())
                //This will show on devices that do support big text.
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.nf_big_text_long)))
                .build();

        //Same deal as the simple notification.
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager)
                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(TAG_BIG_TEXT_NOTIFICATION, notification);
    }

    private void showBigPictureNotification() {
        //Use the NotificationCompat compatibility library in order to not have this barf on < 4.1 devices.
        Notification notification = new NotificationCompat.Builder(getActivity())
                .setContentTitle(getString(R.string.nf_big_pic_title))
                //This will show on devices that don't support the big picture and if further notifications
                //come in after the big picture notification.
                .setContentText(getString(R.string.nf_big_pic_message))
                .setTicker(getString(R.string.nf_big_pic_message))
                .setSmallIcon(R.drawable.ic_notify)
                .setLargeIcon(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_launcher))
                .setContentIntent(pendingIntentForNotification())
                        //This will show on devices that do support big pictures.
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.chaplin))
                        .setBigContentTitle(getString(R.string.nf_big_pic_content_title)))
                .build();

        //Same deal as the simple notification.
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager)
                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(TAG_BIG_PICTURE_NOTIFICATION, notification);
    }

    private void showInboxNotification() {
        Log.d(Constants.LOG_TAG, "INBOX NOTIFICATION");
    }

    private void showRemoteViewsNotification() {
        Log.d(Constants.LOG_TAG, "REMOTE VIEWS NOTIFICATION");
    }
}
