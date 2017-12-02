package cn.hwwwwh.taoxiang.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.qiangxi.checkupdatelibrary.bean.CheckUpdateInfo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hwwwwh.taoxiang.CoustomView.ForceUpdateDialog;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.base.BaseActivity;
import cn.hwwwwh.taoxiang.model.bean.UpdateBean;
import cn.hwwwwh.taoxiang.utils.CleanMessageUtil;
import cn.hwwwwh.taoxiang.utils.ToastUtils;
import cn.hwwwwh.taoxiang.widget.RLoadingDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.qiangxi.checkupdatelibrary.utils.ApplicationUtil.getVersionCode;

public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.toolbar_setting)
    Toolbar toolbarSetting;
    @BindView(R.id.user_view)
    LinearLayout userView;
    @BindView(R.id.wifi_switch)
    SwitchCompat wifiSwitch;
    @BindView(R.id.cache_switch)
    SwitchCompat cacheSwitch;
    @BindView(R.id.cache_clear)
    LinearLayout cacheClear;
    @BindView(R.id.protocol_lv)
    LinearLayout protocolLv;
    @BindView(R.id.update_lv)
    LinearLayout updateLv;
    @BindView(R.id.logout_btn)
    Button logoutBtn;
    @BindView(R.id.cache_text)
    TextView cacheText;
    @BindView(R.id.update_text)
    TextView updateText;
    @BindView(R.id.update_switch)
    SwitchCompat updateSwitch;

    RLoadingDialog rLoadingDialog;
    CheckUpdateInfo mCheckUpdateInfo;
    ForceUpdateDialog mForceUpdateDialog;
    Boolean isNeedUpdate = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        rLoadingDialog = new RLoadingDialog(this, true);
        toolbarSetting.setTitle("应用设置");
        toolbarSetting.setTitleTextAppearance(this, R.style.ToolbarTitle);
        toolbarSetting.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbarSetting);
        toolbarSetting.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbarSetting.setNavigationOnClickListener(new View.OnClickListener() {
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
        loadBtn();
        loadSwitch();
        try {
            cacheText.setText(CleanMessageUtil.getTotalCacheSize(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        wifiSwitch.setOnCheckedChangeListener(this);
        cacheSwitch.setOnCheckedChangeListener(this);
        updateSwitch.setOnCheckedChangeListener(this);

        RxView.clicks(updateLv)
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ApiUtils.getTqgApi(ApiUrls.tqgApiUrl)
                                .getUpdateInfo()
                                .unsubscribeOn(Schedulers.io())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<UpdateBean>() {
                                    Disposable d;

                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {
                                        this.d = d;
                                    }

                                    @Override
                                    public void onNext(@NonNull UpdateBean updateBean) {
                                        if (getVersionCode(getApplicationContext()) < updateBean.getUpdate_ver_code()) {
                                            //LinearLayout gallery = (LinearLayout) navView.getMenu().findItem(R.id.nav_update).getActionView();
                                            //gallery.setVisibility(View.VISIBLE);
                                            mCheckUpdateInfo = new CheckUpdateInfo();
                                            mCheckUpdateInfo.setAppName(updateBean.getUpdate_app_name())
                                                    // .setIsForceUpdate(1)//设置是否强制更新,该方法的参数只要和服务端商定好什么数字代表强制更新即可
                                                    .setNewAppReleaseTime(updateBean.getUpdate_Time())//软件发布时间
                                                    // .setNewAppSize(12.3f)//单位为M
                                                    .setNewAppSize(Float.parseFloat(updateBean.getUpdate_app_size()))
                                                    .setNewAppUrl(updateBean.getUpdate_url())
                                                    .setNewAppVersionCode(updateBean.getUpdate_ver_code())//新app的VersionCode
                                                    .setNewAppVersionName(updateBean.getUpdate_ver_name())
                                                    .setNewAppUpdateDesc(updateBean.getUpdate_content());
                                            forceUpdateDialogClick(updateBean.isIgnore_able());
                                        } else {
                                            ToastUtils.showToast(getApplicationContext(), "当前已是最新版本");
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        ToastUtils.showToast(getApplicationContext(), "网络出错");
                                        onComplete();
                                    }

                                    @Override
                                    public void onComplete() {
                                        d.dispose();
                                    }

                                });
                    }
                });
    }

    private void loadSwitch() {
        boolean wifi_switch=getSharedPreferences().getBoolean("wifi_switch",true);
        boolean cache_switch=getSharedPreferences().getBoolean("cache_switch",true);
        boolean update_switch=getSharedPreferences().getBoolean("update_switch",true);
        wifiSwitch.setChecked(wifi_switch);
        cacheSwitch.setChecked(cache_switch);
        updateSwitch.setChecked(update_switch);
    }

    @Override
    protected void initBundleData() {
        isNeedUpdate = getIntent().getBooleanExtra("isNeedUpdate", false);
        if (isNeedUpdate) {
            updateText.setText("发现新版本");
        } else {
            updateText.setText("当前已是最新版本");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBtn();
    }

    @OnClick({R.id.user_view, R.id.cache_clear, R.id.protocol_lv, R.id.logout_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_view:
                Intent intent = null;
                if (!UMSSDK.getLoginUserToken().isEmpty() && !UMSSDK.getLoginUserId().isEmpty()) {
                    intent = new Intent(SettingActivity.this, UserActivity.class);
                } else {
                    intent = new Intent(SettingActivity.this, LoginActivity.class);
                    intent.putExtra("isNeedFinish", false);
                }
                startActivity(intent);
                break;
            case R.id.cache_clear:
                CleanMessageUtil.clearAllCache(getApplicationContext(), new CleanMessageUtil.clearSuccess() {
                    @Override
                    public void deleteSuccess() {
                        try {
                            ToastUtils.showToast(getApplicationContext(), "清除缓存成功");
                            cacheText.setText(CleanMessageUtil.getTotalCacheSize(getApplicationContext()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void deleteFail() {
                        ToastUtils.showToast(getApplicationContext(), "清除缓存失败");
                    }
                });
                break;
            case R.id.protocol_lv:
                intent=new Intent(SettingActivity.this,SimpleWebActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_btn:
                if (rLoadingDialog != null && !rLoadingDialog.isShowing()) {
                    rLoadingDialog.show();
                }
                UMSSDK.logout(new OperationCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        super.onSuccess(aVoid);
                        if (rLoadingDialog != null && rLoadingDialog.isShowing()) {
                            rLoadingDialog.dismiss();
                        }
                        loadBtn();
                        ToastUtils.showToast(getApplicationContext(), "退出登陆成功");
                        //finish();
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        super.onFailed(throwable);
                        if (rLoadingDialog != null && rLoadingDialog.isShowing()) {
                            rLoadingDialog.dismiss();
                        }
                        loadBtn();
                        ToastUtils.showToast(getApplicationContext(), "退出登陆失败");
                    }
                });
                break;
        }
    }

    private void loadBtn() {
        if (!UMSSDK.getLoginUserToken().isEmpty() && !UMSSDK.getLoginUserId().isEmpty()) {
            logoutBtn.setVisibility(View.VISIBLE);
        } else {
            logoutBtn.setVisibility(View.GONE);
        }
    }

    /**
     * setForce(true);
     * 强制更新,checkupdatelibrary中提供的默认强制更新Dialog,您完全可以自定义自己的Dialog,
     */
    public void forceUpdateDialogClick(boolean isForce) {
        mForceUpdateDialog = new ForceUpdateDialog(SettingActivity.this);
        mForceUpdateDialog.setForce(isForce);
        mForceUpdateDialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                .setFileName("taoxiang.apk")
                .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib").show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.wifi_switch:
                getSharedPreferences().edit().putBoolean("wifi_switch",isChecked).apply();
                break;
            case R.id.cache_switch:
                getSharedPreferences().edit().putBoolean("cache_switch",isChecked).apply();
                break;
            case R.id.update_switch:
                getSharedPreferences().edit().putBoolean("update_switch",isChecked).apply();
                break;
        }
    }
}
