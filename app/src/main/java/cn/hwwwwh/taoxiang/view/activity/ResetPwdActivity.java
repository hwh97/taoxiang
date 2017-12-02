package cn.hwwwwh.taoxiang.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mob.tools.utils.Hashon;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.base.BaseActivity;
import cn.hwwwwh.taoxiang.utils.ToastUtils;
import cn.hwwwwh.taoxiang.widget.RLoadingDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ResetPwdActivity extends BaseActivity {

    @BindView(R.id.resetpwd_toolbar)
    Toolbar resetpwd_toolbar;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.sendcode_btn)
    Button sendcodeBtn;
    @BindView(R.id.code_et)
    EditText codeEt;
    @BindView(R.id.pwd_et)
    EditText pwdEt;
    @BindView(R.id.btnResetPwd)
    Button btnResetPwd;

    private int MAX_COUNT_TIME = 60;
    Disposable mDisposable;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void init() {
        resetpwd_toolbar.setTitle("密码找回");
        resetpwd_toolbar.setTitleTextAppearance(this,R.style.ToolbarTitle);
        resetpwd_toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(resetpwd_toolbar);
        resetpwd_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        resetpwd_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        initResetPwd();
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

    @OnClick({R.id.sendcode_btn, R.id.btnResetPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sendcode_btn:
                sendResetCode();
                break;
            case R.id.btnResetPwd:
                break;
        }
    }

    public void sendResetCode(){
        final RLoadingDialog rLoadingDialog = new RLoadingDialog(this, true);
        rLoadingDialog.show();
        UMSSDK.sendVerifyCodeForResetPassword("86", phoneEt.getText().toString().trim(), new OperationCallback<Boolean>() {
            public void onSuccess(Boolean is) {
                rLoadingDialog.dismiss();
                Log.d("testtaoxiang", "send code success");
                countCode();
                if (!is) {
                    Toast.makeText(ResetPwdActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailed(Throwable t) {
                rLoadingDialog.dismiss();
                try {
                    HashMap<String, Object> errMap = new Hashon().fromJson(t.getMessage());
                    if (errMap != null && errMap.containsKey("error")) {
                        ToastUtils.showToast(getApplicationContext(), (String) errMap.get("error"));
                    } else {
                        ToastUtils.showToast(getApplicationContext(), t.getMessage());
                    }
                } catch (Throwable tt) {
                    ToastUtils.showToast(getApplicationContext(), t.getMessage());
                }
                Log.d("testtaoxiang", "send code fail" + t.getMessage());
                // Toast.makeText(RegisterActivity.this, "send code fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void countCode() {
        try {
            RxView.enabled(sendcodeBtn).accept(false);
            sendcodeBtn.setBackgroundColor(Color.GRAY);//背景色设为灰色
            RxTextView.text(sendcodeBtn).accept("剩余" + MAX_COUNT_TIME + " 秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.io()).take(MAX_COUNT_TIME)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return MAX_COUNT_TIME - (aLong + 1);
                    }
                }).observeOn(AndroidSchedulers.mainThread());

        Consumer<Long> consumer = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (aLong == 0) {
                    RxView.enabled(sendcodeBtn).accept(true);
                    sendcodeBtn.setBackgroundColor(Color.parseColor("#4169E1"));//背景色设为灰色
                    RxTextView.text(sendcodeBtn).accept("发送");
                } else {

                    RxTextView.text(sendcodeBtn).accept("剩余" + aLong + " 秒");
                }
            }
        };

        mDisposable = observable.subscribe(consumer);
    }

    protected void initResetPwd() {
        RxView.clicks(btnResetPwd)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (pwdEt.getText().length() > 6 && phoneEt.getText().length() == 11 && codeEt.length() == 4) {
                            User user = new User();
                            user.nickname.set("_" + phoneEt.getText().toString().trim());
                            UMSSDK.resetPasswordWithPhoneNumber("86", phoneEt.getText().toString().trim(), codeEt.getText().toString().trim(),
                                    pwdEt.getText().toString(), new OperationCallback<Void>() {
                                        @Override
                                        public void onCancel() {
                                            super.onCancel();
                                        }

                                        @Override
                                        public void onFailed(Throwable throwable) {
                                            Log.d("testtaoxiang", throwable.getMessage());
                                            ToastUtils.showToast(getApplicationContext(), throwable.getMessage());
                                            super.onFailed(throwable);
                                        }

                                        @Override
                                        public void onSuccess(Void v) {
                                            ToastUtils.showToast(ResetPwdActivity.this, "重置密码成功,返回登陆");
                                            finish();
                                            super.onSuccess(v);
                                        }
                                    });
                        } else {
                            ToastUtils.showToast(getApplicationContext(), "请检查输入信息");
                        }
                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
