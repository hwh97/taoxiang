package cn.hwwwwh.taoxiang.view.activity;

import android.content.Intent;
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
import com.mob.MobSDK;
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

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.phone_et)
    EditText phone_et;
    @BindView(R.id.sendcode_btn)
    Button sendcode_btn;
    @BindView(R.id.code_et)
    EditText code_et;
    @BindView(R.id.pwd_et)
    EditText pwd_et;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnLinkToLoginScreen)
    TextView btnLinkToLoginScreen;
    @BindView(R.id.register_toolbar)
    Toolbar register_toolbar;

    private boolean beforeSend;

    private int MAX_COUNT_TIME = 60;
    Disposable mDisposable;

    private boolean isNeedFinish=true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        // 初始化SDK
        MobSDK.init(getApplicationContext(), "217506d8f2a7f", "5feee680a235952be92c5d8b637a7c97");
        initRegister();
        register_toolbar.setTitle("用户注册");
        register_toolbar.setTitleTextAppearance(this,R.style.ToolbarTitle);
        register_toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(register_toolbar);
        register_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        register_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.royalblue));
        }
    }


    @OnClick({R.id.sendcode_btn, R.id.btnRegister, R.id.btnLinkToLoginScreen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendcode_btn:
                final RLoadingDialog rLoadingDialog = new RLoadingDialog(this, true);
                rLoadingDialog.show();
                UMSSDK.sendVerifyCodeForRegitser("86", phone_et.getText().toString().trim(), new OperationCallback<Boolean>() {
                    public void onSuccess(Boolean is) {
                        rLoadingDialog.dismiss();
                        Log.d("testtaoxiang", "send code success");
                        countCode();
                        if (!is) {
                            Toast.makeText(RegisterActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.btnRegister:

                break;
            case R.id.btnLinkToLoginScreen:

                break;
        }
    }

    protected void countCode() {
        try {
            RxView.enabled(sendcode_btn).accept(false);
            sendcode_btn.setBackgroundColor(Color.GRAY);//背景色设为灰色
            RxTextView.text(sendcode_btn).accept("剩余" + MAX_COUNT_TIME + " 秒");
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
                    RxView.enabled(sendcode_btn).accept(true);
                    sendcode_btn.setBackgroundColor(Color.parseColor("#4169E1"));//背景色设为灰色
                    RxTextView.text(sendcode_btn).accept("发送");
                } else {

                    RxTextView.text(sendcode_btn).accept("剩余" + aLong + " 秒");
                }
            }
        };

        mDisposable = observable.subscribe(consumer);
    }

    protected void initRegister() {
        RxView.clicks(btnRegister)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (pwd_et.getText().length() > 6 && phone_et.getText().length() == 11 && code_et.length() == 4) {
                            User user = new User();
                            user.nickname.set("_" + phone_et.getText().toString().trim());
                            UMSSDK.registerWithPhoneNumber("86", phone_et.getText().toString().trim(), code_et.getText().toString().trim(),
                                    pwd_et.getText().toString(), null, new OperationCallback<User>() {
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
                                        public void onSuccess(User user) {
                                            ToastUtils.showToast(RegisterActivity.this, "注册成功");
//                                            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
//                                            startActivity(intent);
                                            //默认登陆,为了方便，返回登陆一次
                                            finish();
                                            super.onSuccess(user);
                                        }
                                    });
                        } else {
                            ToastUtils.showToast(getApplicationContext(), "请检查输入信息字段");
                        }
                    }
                });

    }

    @Override
    protected void initBundleData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    
}
