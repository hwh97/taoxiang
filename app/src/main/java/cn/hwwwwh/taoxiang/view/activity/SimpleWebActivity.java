package cn.hwwwwh.taoxiang.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.base.BaseActivity;

public class SimpleWebActivity extends BaseActivity {

    @BindView(R.id.toolbar_simple_web)
    Toolbar toolbarSimpleWeb;
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_simple_web;
    }

    @Override
    protected void init() {
        toolbarSimpleWeb.setTitle("应用协议");
        toolbarSimpleWeb.setTitleTextAppearance(this, R.style.ToolbarTitle);
        toolbarSimpleWeb.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbarSimpleWeb);
        toolbarSimpleWeb.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbarSimpleWeb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.tomato));
        }
        webview.loadUrl("file:///android_asset/protocol.html");
    }

    @Override
    protected void initBundleData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
