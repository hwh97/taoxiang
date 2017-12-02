package cn.hwwwwh.taoxiang.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
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
import cn.hwwwwh.taoxiang.base.RxBus;
import cn.hwwwwh.taoxiang.utils.ToastUtils;
import cn.hwwwwh.taoxiang.widget.RLoadingDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.toolbar_login)
    Toolbar toolbarLogin;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.forget_tv)
    TextView forgetTv;

    private boolean isNeedFinish=true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        toolbarLogin.setTitle("登陆");
        toolbarLogin.setTitleTextAppearance(this,R.style.ToolbarTitle);
        setSupportActionBar(toolbarLogin);
        toolbarLogin.setTitleTextColor(Color.WHITE);
        toolbarLogin.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbarLogin.setNavigationOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    protected void initBundleData() {
        isNeedFinish=getIntent().getBooleanExtra("isNeedFinish",true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        RxView.clicks(btnLogin)
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        final RLoadingDialog rLoadingDialog=new RLoadingDialog(LoginActivity.this,true);
                        rLoadingDialog.show();
                        UMSSDK.loginWithPhoneNumber("86",phoneEt.getText().toString().trim(),password.getText().toString().trim(),new OperationCallback<User>(){
                            @Override
                            public void onSuccess(User user) {
                                super.onSuccess(user);
                                rLoadingDialog.dismiss();
                                ToastUtils.showToast(getApplicationContext(),"登陆成功");
                                if(isNeedFinish){
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }else {
                                    RxBus.getIntanceBus().post(true);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailed(Throwable t) {
                                super.onFailed(t);
                                rLoadingDialog.dismiss();
                                Log.d("testtaoxiang","login fail"+t.getMessage());
                                try {
                                    HashMap<String, Object> errMap = new Hashon().fromJson(t.getMessage());
                                    if (errMap != null && errMap.containsKey("error")) {
                                        ToastUtils.showToast(getApplicationContext(),(String) errMap.get("error"));
                                    } else {
                                        ToastUtils.showToast(getApplicationContext(),t.getMessage());
                                    }
                                } catch (Throwable tt) {
                                    ToastUtils.showToast(getApplicationContext(),t.getMessage());
                                }
                            }
                            @Override
                            public void onCancel() {
                                super.onCancel();
                            }
                        });
                    }
                });


    }

    @OnClick({R.id.btnLogin, R.id.forget_tv})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.btnLogin:

                break;
            case R.id.forget_tv:
                intent=new Intent(LoginActivity.this,ResetPwdActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_register) {
            Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
