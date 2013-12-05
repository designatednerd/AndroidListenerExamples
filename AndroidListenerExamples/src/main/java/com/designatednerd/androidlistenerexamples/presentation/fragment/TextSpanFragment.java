package com.designatednerd.androidlistenerexamples.presentation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.designatednerd.androidlistenerexamples.Constants;
import com.designatednerd.androidlistenerexamples.R;
import com.designatednerd.androidlistenerexamples.model.ClickableTextSpan;

/**
 * Created by Ellen Shapiro on 12/4/13.
 */
public class TextSpanFragment extends Fragment implements ClickableTextSpan.OnClickListener {


    /************************
     * Superclass overrides *
     ************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_spanned_text, null, false);

        //Start with the easy case: Add a single span to a text view.
        TextView singleSpanTextView = (TextView)mainView.findViewById(R.id.spanned_text_single);

        ClickableTextSpan.addClickableSpanToTextView(singleSpanTextView, //text view
                getString(R.string.notification_link_text), //clickable text
                this, //listener
                R.color.blue); //link color.

        //Harder case: Add multiple spans to the same text view.
        TextView multipleSpanTextView = (TextView)mainView.findViewById(R.id.spanned_text_multiple);

        String part1 = getString(R.string.multiple_spans_part_1);
        String part2 = getString(R.string.multiple_spans_part_2);

        SpannableString spannedPart1 = ClickableTextSpan.addClickableSpanToString(part1,
                getString(R.string.alert_dialog_link_text),
                this,
                R.color.purple,
                getResources());

        SpannableString spannedPart2 = ClickableTextSpan.addClickableSpanToString(part2,
                getString(R.string.toast_link_text),
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
        if (clickedText.equalsIgnoreCase(getString(R.string.notification_link_text))) {
            postNotification();
        } else if (clickedText.equalsIgnoreCase(getString(R.string.alert_dialog_link_text))) {
            showAlertDialog();
        } else if (clickedText.equalsIgnoreCase(getString(R.string.toast_link_text))) {
            showToast();
        } else {
            Log.d(Constants.LOG_TAG, "Unhandled text click: " + clickedText);
        }
    }

    public void postNotification() {
        //TODO: actually post notification
        Log.d(Constants.LOG_TAG, "Notification!");
    }

    public void showAlertDialog() {
        //TODO: Show alert dialog
        Log.d(Constants.LOG_TAG, "Alert Dialog!");
    }

    public void showToast() {
        //TODO: Toast!
        Log.d(Constants.LOG_TAG, "Toast!");
    }
}
