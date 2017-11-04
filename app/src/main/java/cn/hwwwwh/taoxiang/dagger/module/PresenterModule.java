package cn.hwwwwh.taoxiang.dagger.module;

import javax.inject.Singleton;

import cn.hwwwwh.taoxiang.presenter.DownloadQuanCryPre;
import cn.hwwwwh.taoxiang.presenter.DownloadQuanDataPre;
import cn.hwwwwh.taoxiang.presenter.DownloadTqgTmrDataPre;
import cn.hwwwwh.taoxiang.view.activity.MainActivity;
import cn.hwwwwh.taoxiang.view.activity.TmrActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by 97481 on 2017/10/5/ 0005.
 */

@Module
public class PresenterModule  {

    DownloadQuanCryPre downloadQuanCryPre;
    DownloadQuanDataPre downloadQuanDataPre;
    DownloadTqgTmrDataPre downloadTqgTmrDataPre;

    public PresenterModule(MainActivity activity) {
        downloadQuanCryPre =new DownloadQuanCryPre(activity,activity);
        downloadQuanDataPre =new DownloadQuanDataPre(activity,activity);
    }

    public PresenterModule(TmrActivity tmrActivity){
        downloadTqgTmrDataPre=new DownloadTqgTmrDataPre(tmrActivity,tmrActivity);
    }

    /* Provides 对外提供依赖对象*/
    @Singleton
    @Provides
    public DownloadQuanCryPre downloadCryPre(){
        return downloadQuanCryPre;
    }

    @Singleton
    @Provides
    public DownloadQuanDataPre downloadTqgDataPre(){
        return downloadQuanDataPre;
    }

    @Singleton
    @Provides
    public DownloadTqgTmrDataPre downloadTqgTmrDataPre(){
        return downloadTqgTmrDataPre;
    }

}
