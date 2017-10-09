package cn.hwwwwh.taoxiang.dagger.module;

import javax.inject.Singleton;

import cn.hwwwwh.taoxiang.presenter.DownloadCryPre;
import cn.hwwwwh.taoxiang.presenter.DownloadTqgDataPre;
import cn.hwwwwh.taoxiang.presenter.DownloadTqgTmrDataPre;
import cn.hwwwwh.taoxiang.view.activity.MainActivity;
import cn.hwwwwh.taoxiang.view.activity.TmrActivity;
import cn.hwwwwh.taoxiang.view.iface.IMainCryVIew;
import dagger.Module;
import dagger.Provides;

/**
 * Created by 97481 on 2017/10/5/ 0005.
 */

@Module
public class PresenterModule  {

    DownloadCryPre downloadCryPre;
    DownloadTqgDataPre downloadTqgDataPre;
    DownloadTqgTmrDataPre downloadTqgTmrDataPre;

    public PresenterModule(MainActivity activity) {
        downloadCryPre=new DownloadCryPre(activity,activity);
        downloadTqgDataPre=new DownloadTqgDataPre(activity,activity);
    }

    public PresenterModule(TmrActivity tmrActivity){
        downloadTqgTmrDataPre=new DownloadTqgTmrDataPre(tmrActivity,tmrActivity);
    }

    /* Provides 对外提供依赖对象*/
    @Singleton
    @Provides
    public DownloadCryPre downloadCryPre(){
        return  downloadCryPre;
    }

    @Singleton
    @Provides
    public DownloadTqgDataPre downloadTqgDataPre(){
        return  downloadTqgDataPre;
    }

    @Singleton
    @Provides
    public DownloadTqgTmrDataPre downloadTqgTmrDataPre(){
        return downloadTqgTmrDataPre;
    }

}
