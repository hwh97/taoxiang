package cn.hwwwwh.taoxiang.dagger;

import javax.inject.Singleton;

import cn.hwwwwh.taoxiang.base.BaseActivity;
import cn.hwwwwh.taoxiang.dagger.module.PresenterModule;
import cn.hwwwwh.taoxiang.view.activity.AliSdkWebViewProxyActivity;
import cn.hwwwwh.taoxiang.view.activity.CollectActivity;
import cn.hwwwwh.taoxiang.view.activity.MainActivity;
import cn.hwwwwh.taoxiang.view.activity.TmrActivity;
import dagger.Component;

/**
 * Created by 97481 on 2017/10/5/ 0005.
 */

@Singleton
@Component(modules = {PresenterModule.class})
public interface PresenterComponent {

    /**
     * 注入点
     * @param activity  表示需要使用DaggerPresenterComponent.create().inject(this);注入的地方，
     *                  注意，此处一定不要使用Activity，需要使用MainActivity，否则的话会报空指针异常。
     *                  因为这里的注入点是什么，就会到该类里面去找。如果写Activity，就会到Activity里面去找，
     *                  而Activity中并没有@inject，即没有需要注入的地方，所以在生成的DaggerPresenterComponent
     *                  中，方法就不会被调用。
     */

    void inject( MainActivity activity);

    void inject(TmrActivity activity);

    void inject(AliSdkWebViewProxyActivity activity);

    void inject(CollectActivity collectActivity);

    void inject(BaseActivity baseActivity);
}
