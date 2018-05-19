package com.steptowin.app.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * desc:webview fragment
 * author：zg
 * date:17/7/19
 * time:上午10:49
 */
public class WebFragment extends BaseFragment {
    @Bind(R.id.webview)
    WebView webView;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_webview;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 打开本包内asset目录下的index.html文件
//        webView.loadUrl("file:///android_asset/scheme.html");
// 打开本地sd卡内的index.html文件
//        webView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
// 打开指定URL的html文件
//        webView.loadUrl("http://wap.baidu.com");

        String content = "<a href=\"jihe://app.steptowin.com?from=market&type=activity\">open Android app</a>";
        webView.loadData(content, "text/html", "UTF-8");

    }
}
