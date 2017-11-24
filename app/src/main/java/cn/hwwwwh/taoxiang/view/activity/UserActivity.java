package cn.hwwwwh.taoxiang.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hwwwwh.taoxiang.CoustomView.CircleImageView;
import cn.hwwwwh.taoxiang.CoustomView.MyBottomSheetDialog;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.base.BaseActivity;
import cn.hwwwwh.taoxiang.utils.ToastUtils;
import cn.hwwwwh.taoxiang.utils.Util;
import cn.hwwwwh.taoxiang.widget.RLoadingDialog;

import static com.alibaba.baichuan.trade.biz.applink.adapter.AppLinkConstants.REQUESTCODE;

public class UserActivity extends BaseActivity {

    @BindView(R.id.toolbar_userinfo)
    Toolbar toolbarUserinfo;
    @BindView(R.id.userHeaderPic)
    CircleImageView userHeaderPic;
    @BindView(R.id.userAccount)
    TextView userAccount;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.bindInfo)
    TextView bindInfo;
    @BindView(R.id.changePwdView)
    LinearLayout changePwdView;
    @BindView(R.id.activity_userinfo)
    BottomSheetLayout activityUserinfo;
    @BindView(R.id.headPicView)
    LinearLayout headPicView;
    @BindView(R.id.nickNameView)
    LinearLayout nickNameView;
    @BindView(R.id.tb_account)
    TextView tbAccount;
    @BindView(R.id.tb_lv)
    LinearLayout tbLv;
    @BindView(R.id.logout_btn)
    Button logout_btn;

    RLoadingDialog rLoadingDialog;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10002;
    private static final int PICK_ACTIVITY_REQUEST_CODE = 10003;
    private static final int CROP_ACTIVITY_REQUEST_CODE = 10008;
    private static final int REQUESTCODE = 10010;

    private String imageFilePath; //拍照和选择照片后图片路径
    private Uri pickPhotoImageUri; //API22以下相册选择图片uri

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user;
    }

    @Override
    protected void init() {
        rLoadingDialog = new RLoadingDialog(this, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#11B57C"));
        }
        toolbarUserinfo.setTitle("个人信息");
        toolbarUserinfo.setTitleTextAppearance(this, R.style.ToolbarTitle);
        setSupportActionBar(toolbarUserinfo);
        toolbarUserinfo.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbarUserinfo.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbarUserinfo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initUser();

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

    protected void initUser() {
        rLoadingDialog.show();
        if(AlibcLogin.getInstance().isLogin()){
            tbAccount.setText(AlibcLogin.getInstance().getSession().nick);
        }else{
            tbAccount.setText("未登陆");
        }
        UMSSDK.getLoginUser(new OperationCallback<User>() {
            @Override
            public void onCancel() {
                super.onCancel();
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (rLoadingDialog != null && rLoadingDialog.isShowing()) {
                    rLoadingDialog.dismiss();
                }
                Log.d("testtaoxiang", throwable.getMessage());
                super.onFailed(throwable);
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                if (rLoadingDialog != null && rLoadingDialog.isShowing()) {
                    rLoadingDialog.dismiss();
                }
                Glide.with(getApplicationContext()).load(user.avatar.get()[0]).asBitmap().into(userHeaderPic);
                bindInfo.setText(user.phone.get());
                nickName.setText(user.nickname.get());
                userAccount.setText(user.phone.get());
            }
        });
    }

    private void openPicBottomSheet() {
        final MyBottomSheetDialog dialog = new MyBottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_picchoose, null);
        dialog.setContentView(view);
        dialog.show();
        final TextView takePic = (TextView) view.findViewById(R.id.takePic);
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                dialog.dismiss();
            }
        });
        final TextView Album = (TextView) view.findViewById(R.id.Album);
        Album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
                dialog.dismiss();
            }
        });
    }

    private void openNickBottomSheet() {
        final MyBottomSheetDialog dialog = new MyBottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_nickname, null);
        dialog.setContentView(view);
        dialog.show();
        final LinearLayout done_view = (LinearLayout) view.findViewById(R.id.done_view);
        final EditText nickEdit = (EditText) view.findViewById(R.id.nickEdit);
        nickEdit.setText(nickName.getText().toString());
        done_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkString(nickEdit.getText().toString())) {
                    if (!nickEdit.getText().toString().isEmpty())
                        ToastUtils.showToast(UserActivity.this, "检测到昵称不符要求");
                    else
                        ToastUtils.showToast(UserActivity.this, "昵称不能为空");
                } else {
                    if(rLoadingDialog!=null &&!rLoadingDialog.isShowing()){
                        rLoadingDialog.show();
                    }
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("nickname", nickEdit.getText().toString().trim());
                    updateUser(map);
                    dialog.dismiss();
                }
            }
        });

    }

    private void openPwdBottomSheet() {
        final MyBottomSheetDialog dialog = new MyBottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_pwd, null);
        view.setFocusable(true);
        dialog.setContentView(view);
        dialog.show();
        final EditText oldPwd = (EditText) view.findViewById(R.id.oldEdit);
        final EditText newPwd = (EditText) view.findViewById(R.id.newEdit);
        LinearLayout doneView = (LinearLayout) view.findViewById(R.id.done_view);
        doneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!oldPwd.getText().toString().isEmpty() && !newPwd.getText().toString().isEmpty()) {
                    if (oldPwd.getText().length() >= 6 && newPwd.getText().length() >= 6) {
                        if(rLoadingDialog!=null &&!rLoadingDialog.isShowing()){
                            rLoadingDialog.show();
                        }
                        changePassword(oldPwd.getText().toString().trim(), newPwd.getText().toString().trim());
                        dialog.dismiss();
                    } else
                        ToastUtils.showToast(getApplicationContext(), "密码长度错误");
                } else {
                    ToastUtils.showToast(getApplicationContext(), "请补全信息");
                }
            }
        });

    }

    private boolean checkString(String s) {
        return s.matches("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
    }

    //从相册中取图片
    private void pickPhoto() {
        if (permission()) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_ACTIVITY_REQUEST_CODE);
        } else {
            ToastUtils.showToast(UserActivity.this, "请同意相机权限及存储权限");
        }
    }

    //拍照获取图片
    private void takePhoto() {
        if (permission()) {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                File imageFile = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                if (!imageFile.getParentFile().exists()) imageFile.getParentFile().mkdirs();
                imageFilePath = imageFile.getPath();
                //兼容性判断
                Uri imageUri;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    imageUri = Util.file2Uri(this, imageFile);
                } else {
                    imageUri = Uri.fromFile(imageFile);
                }
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI

                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        } else {
            ToastUtils.showToast(UserActivity.this, "请同意相机权限及存储权限");
        }
    }

    /**
     * 申请权限
     */
    private boolean permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUESTCODE);
            return false;
        }else
         return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)||
                        !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    AskForPermission();
                }
            }
    }

    private void AskForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请同意相机及存储权限!");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("转去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //CropHelper.handleResult(this, requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                //拍照
                if (resultCode == Activity.RESULT_OK) {
                    if(rLoadingDialog!=null &&!rLoadingDialog.isShowing()){
                        rLoadingDialog.show();
                    }
                    updateHeadPic(imageFilePath);
                }
                break;

            case PICK_ACTIVITY_REQUEST_CODE:
                //从相册选择
                if (data != null && resultCode == Activity.RESULT_OK) {
                    if(rLoadingDialog!=null &&!rLoadingDialog.isShowing()){
                        rLoadingDialog.show();
                    }
                    imageFilePath = Util.getPathByUri4kitkat(this, data.getData());
                    updateHeadPic(imageFilePath);
                }
                break;
        }

    }

    public boolean updateHeadPic(String url) {
        UMSSDK.uploadAvatar(url, new OperationCallback<HashMap<String, Object>>() {
            @Override
            public void onSuccess(HashMap<String, Object> stringObjectHashMap) {
                super.onSuccess(stringObjectHashMap);
                HashMap<String, Object> map = new HashMap<String, Object>();
                //[http://f1.sdk.mob.com/ums/avatar/202/f89/72b7f4e5dcb82c4517a88fd82d_500.jpg,
                // http://f1.sdk.mob.com/ums/avatar/202/f89/72b7f4e5dcb82c4517a88fd82d_100.jpg, http://f1.sdk.mob.com/ums/avatar/202/f89/72b7f4e5dcb82c4517a88fd82d_50.jpg]
                String url = stringObjectHashMap.get("avatar").toString();
                if (url.startsWith("[")) {
                    url = url.substring(1);
                }
                if (url.endsWith("]")) {
                    url = url.substring(0, url.length() - 1);
                }
                String[] array = url.split(",");

                map.put("avatarId", stringObjectHashMap.get("id").toString());
                map.put("avatar", array);

                updateUser(map);
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                Log.d("testtaoxiang", "upload pic fail info:" + throwable.getMessage());
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }
        });
        return true;
    }

    public void updateUser(final HashMap<String, Object> update) {
        UMSSDK.updateUserInfo(update, new OperationCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                if(rLoadingDialog!=null &&rLoadingDialog.isShowing()){
                    rLoadingDialog.dismiss();
                }
                ToastUtils.showToast(UserActivity.this, "更新用户信息成功");
                initUser();
                Log.d("testtaoxiang", "update user success:");
            }

            @Override
            public void onFailed(Throwable throwable) {
                if(rLoadingDialog!=null &&rLoadingDialog.isShowing()){
                    rLoadingDialog.dismiss();
                }
                super.onFailed(throwable);
                Log.d("testtaoxiang", "update user fail info:" + throwable.getMessage());
            }

        });
    }

    public void changePassword(String oldPwd, String newPwd) {
        UMSSDK.changePassword(newPwd, oldPwd, new OperationCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                if(rLoadingDialog!=null &&rLoadingDialog.isShowing()){
                    rLoadingDialog.dismiss();
                }
                ToastUtils.showToast(UserActivity.this, "修改用户密码成功");
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                if(rLoadingDialog!=null &&rLoadingDialog.isShowing()){
                    rLoadingDialog.dismiss();
                }
                Log.d("testtaoxiang","修改用户密码失败" + throwable.getMessage());
                ToastUtils.showToast(UserActivity.this, "修改用户密码失败" + throwable.getMessage());
            }

        });
    }

    @OnClick({R.id.headPicView, R.id.nickNameView, R.id.changePwdView,R.id.logout_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.headPicView:
                openPicBottomSheet();
                break;
            case R.id.nickNameView:
                openNickBottomSheet();
                break;
            case R.id.changePwdView:
                openPwdBottomSheet();
                break;
            case R.id.logout_btn:
                if(rLoadingDialog!=null &&!rLoadingDialog.isShowing()){
                    rLoadingDialog.show();
                }
                UMSSDK.logout(new OperationCallback<Void>(){
                    @Override
                    public void onSuccess(Void aVoid) {
                        super.onSuccess(aVoid);
                        if(rLoadingDialog!=null &&rLoadingDialog.isShowing()){
                            rLoadingDialog.dismiss();
                        }
                        ToastUtils.showToast(getApplicationContext(),"退出登陆成功");
                        Intent intent=new Intent(UserActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        super.onFailed(throwable);
                        if(rLoadingDialog!=null &&rLoadingDialog.isShowing()){
                            rLoadingDialog.dismiss();
                        }
                        ToastUtils.showToast(getApplicationContext(),"退出登陆成功");
                    }
                });
                break;
        }
    }

    @OnClick(R.id.tb_lv)
    public void onViewClicked() {
        if(AlibcLogin.getInstance().isLogin()){
            new AlertDialog.Builder(this).setTitle("确认").setMessage("确定退出当前淘宝账户吗？")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlibcLogin.getInstance().logout(new AlibcLoginCallback() {
                                @Override
                                public void onSuccess(int i) {
                                    ToastUtils.showToast(UserActivity.this, "退出淘宝账号成功");
                                    tbAccount.setText("未登录");
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    ToastUtils.showToast(UserActivity.this, "退出淘宝账号失败");
                                }
                            });
                        }
                    })
                    .setNegativeButton("取消",null).show();
        }else{
            AlibcLogin.getInstance().showLogin(new AlibcLoginCallback() {
                @Override
                public void onSuccess(int i) {
                    ToastUtils.showToast(UserActivity.this, "登陆成功");
                    tbAccount.setText(AlibcLogin.getInstance().getSession().nick);
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtils.showToast(UserActivity.this, "登陆失败");
                }
            });
        }
    }
}
