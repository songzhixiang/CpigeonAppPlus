package com.cpigeon.app.modular.home.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.NetUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenshuai on 2017/4/11.
 */

public class WebActivity extends BaseActivity {
    public final static String INTENT_DATA_KEY_URL = "url";
    public final static String INTENT_DATA_KEY_BACKNAME = "requestName";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb_progressbar)
    ProgressBar pbProgressbar;
    @BindView(R.id.wvWebview)
    WebView wvWebview;
    @BindView(R.id.vs_tip)
    ViewStub vsTip;
    View vTips;

    private WebSettings webSettings;
    private Map<String, String> mHeaderMap;
    private boolean mIsFristLoadUrl = true;
    private CountDownTimer timeoutCountDownTimer;
    private long timeout = 12000;
    private boolean mIsTimeout = false;
    private boolean mHasError = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        setSupportActionBar(this.toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initWebView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                wvWebview.reload();
                break;
        }
        return true;
    }

    private void initWebView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(INTENT_DATA_KEY_URL);
        String backName = intent.getStringExtra(INTENT_DATA_KEY_BACKNAME);
        if ("".equals(backName)) backName = "返回";

        mHeaderMap = new HashMap<>();
        mHeaderMap.put("u", CommonTool.getUserToken(this));

        webSettings = wvWebview.getSettings();
        String ua = webSettings.getUserAgentString();
        Logger.i(ua);
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //隐藏Zoom缩放按钮
        webSettings.setDisplayZoomControls(false);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        wvWebview.loadUrl(url, mHeaderMap);
        wvWebview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pbProgressbar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == pbProgressbar.getVisibility()) {
                        pbProgressbar.setVisibility(View.VISIBLE);
                    }
                    pbProgressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        wvWebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (mHeaderMap != null)
                    view.loadUrl(url, mHeaderMap);
                else view.loadUrl(url);
                // TODO 设置不加载外面的浏览器
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                Logger.i("get start url地址----------：" + url);
                //这里可以用来判断页面加载前的url地址，等 ，也可以在这里来启动一个progressbar,用来显示正在加载。
//                ((CommonTitleBar) mTitleBar).setTitleText("加载中");
                toolbar.setTitle("加载中");
                updateTips(false);
                mIsTimeout = false;
                if (timeoutCountDownTimer != null) {
                    timeoutCountDownTimer.cancel();
                }
                timeoutCountDownTimer = new CountDownTimer(timeout, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        mIsTimeout = true;
                        /*
                         * 超时后,首先判断页面加载进度,超时并且进度小于100,就执行超时后的动作
                         */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (wvWebview.getProgress() < 100) {
                                    mHasError = true;
                                    wvWebview.stopLoading();
                                }
                            }
                        });
                    }
                };
                timeoutCountDownTimer.start();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pbProgressbar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
                //这里是webview加载结束时调用的，可以在这里结束前面设置的一个progressbar.
                //也可以做一些别的功能的处理，如得到源码中的一些数据，下面会详细说下。

                if (mIsTimeout) {
//                    ((CommonTitleBar) mTitleBar).setTitleText("超时");
                    toolbar.setTitle("超时");
                    updateTips(true);
                } else if (mHasError) {
//                    ((CommonTitleBar) mTitleBar).setTitleText("出错了");
                    toolbar.setTitle("出错了");
                    updateTips(true);
                } else {
//                    ((CommonTitleBar) mTitleBar).setTitleText(view.getTitle());
                    toolbar.setTitle(view.getTitle());
                    updateTips(false);
                }
                Logger.i(url);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                if (mIsFristLoadUrl) {
                    mIsFristLoadUrl = false;
                    return;
                }
                int errorCode = errorResponse.getStatusCode();
                Logger.i("errorCode:" + errorCode);
                if (errorCode > 400 && errorCode < 600) {
                    mHasError = true;
                } else {
                    mHasError = false;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Logger.i(" errorCode = [" + errorCode + "], description = [" + description + "], failingUrl = [" + failingUrl + "]");
//                if (errorCode == 404) {
//                    view.stopLoading();
//                    view.clearView();
//                    view.loadUrl("file:///android_asset/errorCodeHtml/404.html");
//                } else if (errorCode > 500 && errorCode < 600) {
//
//                } else {
//
//                }
                //((CommonTitleBar) mTitleBar).setTitleText("服务器出错了");
                mHasError = true;
            }

        });
    }

    private void updateTips(boolean isError) {
        mHasError = isError;
        if (vTips == null) {
            vTips = vsTip.inflate();
        }
        vsTip.setVisibility(isError ? View.VISIBLE : View.GONE);
        wvWebview.setVisibility(isError ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvWebview.canGoBack()) {
            wvWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }
}
