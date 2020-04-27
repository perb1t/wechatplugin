package com.xiezhiai.wechatplugin.ui.aty;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;

/**
 * Created by shijiwei on 2018/11/12.
 *
 * @Desc:
 */
public class AboutUsActivity extends BaseActivity implements CommonTopBar.CommonTopBarListener {

    private static final String TAG = "AboutUsActivity";
    private final String SPECIFICATION_URL = "";

    private CommonTopBar commonTopBar;
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {

    }

    @Override
    public void initialView() {
        commonTopBar = findViewById(R.id.common_topbar);
        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.web_progress_bar);
    }

    @Override
    public void initialEvn() {

        commonTopBar.setCommonTopBarListener(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //以下两条设置可以使页面适应手机屏幕的分辨率，完整的显示在屏幕上
        //设置是否使用WebView推荐使用的窗口
        webSettings.setUseWideViewPort(true);
        //设置WebView加载页面的模式
        webSettings.setLoadWithOverviewMode(true);
        //支持屏幕缩放、隐藏缩放指示控件
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        loadUrl("https://www.xiaoxiaobot.com/mobile/about");
    }

    @Override
    public void onBackKeyPressed() {
        goBack();
    }

    @Override
    public void onNetworkStateChanged(int type, boolean isAvailable) {

    }

    @Override
    public void onTopLeftButtonClick(View v) {
        goBack();
    }

    @Override
    public void onTopRightButtonClick(View v) {

    }


    private void goBack() {
        if (webView.canGoBack())
            webView.goBack();
        else
            finish();

    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            LogUtil.i(TAG, " url : " + url);
            if (url.equalsIgnoreCase(SPECIFICATION_URL)) {
                //TODO
            } else {
                loadUrl(url);
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                webView.loadData(error.getErrorCode() + "", "text/html", "utf-8");
            }
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if (newProgress == 100)
                progressBar.setVisibility(View.GONE);
            super.onProgressChanged(view, newProgress);
        }
    }

    private void loadUrl(String url) {
        progressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(url);
    }
}
