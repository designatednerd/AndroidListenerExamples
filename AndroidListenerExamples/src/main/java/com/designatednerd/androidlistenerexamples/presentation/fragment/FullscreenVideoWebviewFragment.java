package com.designatednerd.androidlistenerexamples.presentation.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.designatednerd.androidlistenerexamples.R;
import com.designatednerd.androidlistenerexamples.presentation.activity.MainActivity;

/**
 * Fragment to demonstrate playing fullscreen video from a WebView.
 * Created by eshapiro on 9/16/13.
 */
public class FullscreenVideoWebviewFragment extends Fragment {
    private WebView mWebView;

    private static final String VOLLEY_VIDEO = "https://developers.google.com/events/io/sessions/325304728";

    @SuppressLint("SetJavaScriptEnabled") //Required for YouTube to work.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_fullscreen_video_webview, container, false);

        mWebView = (WebView) mainView.findViewById(R.id.fragment_fsv_webview);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                MainActivity activity = (MainActivity)getActivity();
                activity.showCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                MainActivity activity = (MainActivity)getActivity();
                activity.hideCustomView();
            }
        });


        mWebView.loadUrl(VOLLEY_VIDEO);

        return mainView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mWebView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}
