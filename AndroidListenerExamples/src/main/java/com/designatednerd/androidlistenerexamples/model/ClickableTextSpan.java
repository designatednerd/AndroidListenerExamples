package com.designatednerd.androidlistenerexamples.model;

import android.content.res.Resources;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.designatednerd.androidlistenerexamples.Constants;

/**
 * Created by Ellen Shapiro on 12/4/13.
 *
 * Suggested by http://stackoverflow.com/a/7409726/681493.
 */
public class ClickableTextSpan extends ClickableSpan {

    public interface OnClickListener {
        /**
         * Called any time the span is clicked.
         */
        public void onClick(String clickedText);
    }

    private OnClickListener mListener;
    private int mLinkColor;
    private String mLinkedText;

    /***************
     * CONSTRUCTOR *
     ***************/
    public ClickableTextSpan(OnClickListener listener, int linkColor) {
        mListener = listener;
        mLinkColor = linkColor;
    }


    /************************
     * SUPERCLASS OVERRIDES *
     ************************/
    @Override
    public void onClick(View widget) {
        if (mListener != null) {
            //Forward the click notification to this text span's onClickListener.
            mListener.onClick(mLinkedText);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        //Set the custom color for the link
        ds.setColor(mLinkColor);
    }

    /************************************
     * ADDING CLICKABLE SPANS TO THINGS *
     ************************************/
    public static SpannableString addClickableSpanToString(String stringToAddSpanTo, String clickableText,
                                                           ClickableTextSpan.OnClickListener listener, int linkColor,
                                                           Resources resources) {

        //Create your text span
        ClickableTextSpan textSpan = new ClickableTextSpan(listener, linkColor);

        //Grab a SpannableString to add links to.
        SpannableString spannableTextViewString = SpannableString.valueOf(stringToAddSpanTo);

        int linkStart = stringToAddSpanTo.indexOf(clickableText);
        if (linkStart == -1) {
            //The index was not found, this isn't going to work.
            Log.d(Constants.LOG_TAG, "Text to make clickable not found in TextView");
            return null;
        }

        int linkEnd = linkStart + clickableText.length();


        spannableTextViewString.setSpan(textSpan,
                linkStart,
                linkEnd,
                Spanned.SPAN_MARK_MARK);

        spannableTextViewString.setSpan(new ForegroundColorSpan(resources.getColor(linkColor)),
                linkStart,
                linkEnd,
                0);

        //Set a reference to the linked text
        textSpan.mLinkedText = clickableText;

        return spannableTextViewString;
    }


    /**
     * Adds a link which can be clicked to any TextView.
     * @param textView - The text view, with its raw text already set, to use to set clickable text.
     * @param clickableText - The text to turn into a link.
     * @param listener - The onClick listener to be called when the link is clicked.
     * @param linkColor - The integer representing a color to turn this link.
     */
    public static void addClickableSpanToTextView(TextView textView, String clickableText,
                                                  ClickableTextSpan.OnClickListener listener, int linkColor) {
        String textViewText = textView.getText().toString();

        SpannableString spannableTextViewString = addClickableSpanToString(textViewText, clickableText, listener, linkColor, textView.getResources());

        //Take the spanned string and set it back to the text view.
        textView.setText(spannableTextViewString);

        resetMovementMethod(textView);
    }

    /**
     * Resets the link movement method, fixing occasional bugs caused by setting spanned text.
     * @param textViewToReset - The text view to perform the reset on
     */
    public static void resetMovementMethod(TextView textViewToReset) {
        MovementMethod currentMovementMethod = textViewToReset.getMovementMethod();
        if (currentMovementMethod == null || !(currentMovementMethod instanceof LinkMovementMethod)) {
            textViewToReset.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
