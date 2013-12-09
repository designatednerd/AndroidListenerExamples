package com.designatednerd.androidlistenerexamples.presentation.fragment;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.designatednerd.androidlistenerexamples.Constants;
import com.designatednerd.androidlistenerexamples.R;
import com.designatednerd.androidlistenerexamples.model.ClickableTextSpan;
import com.designatednerd.androidlistenerexamples.presentation.activity.MainActivity;

/**
 * Created by Ellen Shapiro on 12/4/13.
 */
public class SpannedTextFragment extends Fragment implements ClickableTextSpan.OnClickListener {

    private static final int NOTIFICATION_ID = 1234;

    /************************
     * Superclass overrides *
     ************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_spanned_text, null, false);

        //Start with the easy case: Add a single span to a text view.
        TextView singleSpanTextView = (TextView)mainView.findViewById(R.id.spanned_text_single);

        ClickableTextSpan.addClickableSpanToTextView(singleSpanTextView, //text view
                getString(R.string.stf_notification_link_text), //clickable text
                this, //listener
                R.color.blue); //link color.

        //Harder case: Add multiple spans to the same text view.
        TextView multipleSpanTextView = (TextView)mainView.findViewById(R.id.spanned_text_multiple);

        String part1 = getString(R.string.stf_multiple_spans_part_1);
        String part2 = getString(R.string.stf_multiple_spans_part_2);

        SpannableString spannedPart1 = ClickableTextSpan.addClickableSpanToString(part1,
                getString(R.string.stf_alert_dialog_link_text),
                this,
                R.color.purple,
                getResources());

        SpannableString spannedPart2 = ClickableTextSpan.addClickableSpanToString(part2,
                getString(R.string.stf_toast_link_text),
                this,
                R.color.red,
                getResources());

        multipleSpanTextView.setText(TextUtils.concat(spannedPart1, spannedPart2));
        ClickableTextSpan.resetMovementMethod(multipleSpanTextView);

        return mainView;
    }


    /*********************************************
     * ClickableTextSpan.OnClickListener Methods *
     *********************************************/

    @Override
    public void onClick(String clickedText) {
        //Use the clicked text to figure out what you want to do.
        if (clickedText.equalsIgnoreCase(getString(R.string.stf_notification_link_text))) {
            postNotification();
        } else if (clickedText.equalsIgnoreCase(getString(R.string.stf_alert_dialog_link_text))) {
            showAlertDialog();
        } else if (clickedText.equalsIgnoreCase(getString(R.string.stf_toast_link_text))) {
            showToast();
        } else {
            Log.d(Constants.LOG_TAG, "Unhandled text click: " + clickedText);
        }
    }

    public void postNotification() {
        //Create the notification and set the required elements
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity());
        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.stf_notification_title))
                .setContentText(getString(R.string.stf_notification_text));

        //Create an intent to launch when the notification is clicked
        Intent launchIntent = new Intent(getActivity(), MainActivity.class);
        launchIntent.putExtra(MainActivity.EXTRA_FRAGMENT_TO_LAUNCH, MainActivity.TAG_KITTENS_FRAGMENT);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent clickedIntent = PendingIntent.getActivity(getActivity(), //context
                                                                        0 , //request code
                                                                launchIntent, //intent to fire
                                           PendingIntent.FLAG_UPDATE_CURRENT); //flags
        notificationBuilder.setContentIntent(clickedIntent);
        notificationBuilder.setAutoCancel(true);

        //Add the notification to the drawer.
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void showAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder
                .setMessage(R.string.stf_alert_text)
                .setNegativeButton(R.string.stf_alert_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialogBuilder.create().show();
    }

    public void showToast() {
        Toast.makeText(getActivity(), "Toast!", Toast.LENGTH_LONG).show();
    }
}
