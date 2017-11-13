package cn.hwwwwh.taoxiang;


import android.app.Application;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.mob.MobApplication;

import cn.hwwwwh.taoxiang.dagger.PresenterComponent;


/**
 * 阿里百川电商
 * 项目名称：阿里巴巴电商SDK
 * 开发团队：阿里巴巴百川商业化团队
 * 阿里巴巴电商SDK答疑群号：1200072507
 * <p/>
 * 功能说明：全局application
 */
public class AliSdkApplication  extends MobApplication{

    public static AliSdkApplication application = null;
    public static PresenterComponent presenterComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        //电商SDK初始化
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                //Toast.makeText(AliSdkApplication.this, "初始化成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(AliSdkApplication.this, "初始化应用失败,错误码="+code+" / 错误消息="+msg, Toast.LENGTH_SHORT).show();
            }
        });


    }
    public static synchronized AliSdkApplication getInstance() {
        return application;
    }

    public static PresenterComponent getPresenterComponent() {

        /*
            1. 在Application层对注入器进行初始化
              DaggerAppComponent是dagger2编译阶段生成的类，用于生成注入器Component的实例，如果没有的话，ReBuild一下
                -builder()  创建构造器
                -appModule()    传入本次生成依赖需要的模型
                -build()    创建Component的实例
         */
//        if (presenterComponent == null) {
//            presenterComponent = DaggerPresenterComponent.builder()
//                    .presenterModule(new PresenterModule())
//                    .build();
//            presenterComponent = DaggerPresenterComponent.create();   // 作用同上
//        }
        return presenterComponent;
    }
}