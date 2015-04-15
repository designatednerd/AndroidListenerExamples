package com.designatednerd.androidlistenerexamples.presentation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.designatednerd.androidlistenerexamples.R;

/**
 * Created by Ellen Shapiro on 11/19/13.
 */
public class KittensFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_kittens, container, false);

        WebView webView = (WebView) mainView.findViewById(R.id.fragment_kittens_webview);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("http://www.emergencykitten.com");

        return mainView;
    }
}