package cn.hwwwwh.taoxiang.view.activity;

import android.content.Intent;
import android.os.Handler;

import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.base.BaseActivity;

/**
 * Created by 97481 on 2017/11/20/ 0020.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        //延迟2S跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void initBundleData() {

    }
}
