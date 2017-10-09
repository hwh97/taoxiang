package cn.hwwwwh.taoxiang.base;

import android.app.Application;

public class AppController extends Application {
    //须在AndroidManifest添加android：nane=".AppController"重要！！！！
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //侧滑返回
       //this.registerActivityLifecycleCallbacks(ActivityStack.getInstance());

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }



}