package cn.hwwwwh.taoxiang.dagger.module;

import javax.inject.Singleton;

import cn.hwwwwh.taoxiang.presenter.DeleteAllInvalidCollectPre;
import cn.hwwwwh.taoxiang.presenter.DeleteSingleInvalidCollectPre;
import cn.hwwwwh.taoxiang.presenter.DownloadCollectDataPre;
import cn.hwwwwh.taoxiang.presenter.DownloadQuanCryPre;
import cn.hwwwwh.taoxiang.presenter.DownloadQuanDataPre;
import cn.hwwwwh.taoxiang.presenter.DownloadTqgTmrDataPre;
import cn.hwwwwh.taoxiang.presenter.RequestCollectPre;
import cn.hwwwwh.taoxiang.presenter.WebViewIsCollectPre;
import cn.hwwwwh.taoxiang.view.activity.AliSdkWebViewProxyActivity;
import cn.hwwwwh.taoxiang.view.activity.CollectActivity;
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
    WebViewIsCollectPre webViewIsCollectPre;
    RequestCollectPre requestCollectPre;
    DownloadCollectDataPre downloadCollectDataPre;
    DeleteSingleInvalidCollectPre deleteSingleInvalidCollectPre;
    DeleteAllInvalidCollectPre deleteAllInvalidCollectPre;


    public PresenterModule(MainActivity activity) {
        downloadQuanCryPre =new DownloadQuanCryPre(activity,activity);
        downloadQuanDataPre =new DownloadQuanDataPre(activity,activity);
    }

    public PresenterModule(AliSdkWebViewProxyActivity webViewProxyActivity){
        webViewIsCollectPre=new WebViewIsCollectPre(webViewProxyActivity,webViewProxyActivity);
        requestCollectPre=new RequestCollectPre(webViewProxyActivity,webViewProxyActivity);
    }

    public PresenterModule(TmrActivity tmrActivity){
        downloadTqgTmrDataPre=new DownloadTqgTmrDataPre(tmrActivity,tmrActivity);
    }

    public PresenterModule(CollectActivity collectActivity){
        downloadCollectDataPre=new DownloadCollectDataPre(collectActivity,collectActivity);
        deleteSingleInvalidCollectPre =new DeleteSingleInvalidCollectPre(collectActivity,collectActivity);
        requestCollectPre=new RequestCollectPre(collectActivity,collectActivity);
        deleteAllInvalidCollectPre= new DeleteAllInvalidCollectPre(collectActivity,collectActivity);
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
    public WebViewIsCollectPre webViewIsCollectPre(){
        return webViewIsCollectPre;
    }

    @Singleton
    @Provides
    public RequestCollectPre requestCollectPre(){return  requestCollectPre;}

    @Singleton
    @Provides
    public DownloadTqgTmrDataPre downloadTqgTmrDataPre(){
        return downloadTqgTmrDataPre;
    }

    @Singleton
    @Provides
    public DownloadCollectDataPre downloadCollectDataPre(){
        return downloadCollectDataPre;
    }
    @Singleton
    @Provides
    public DeleteSingleInvalidCollectPre deleteSingleVaildCollectPre(){
        return deleteSingleInvalidCollectPre;
    }

    @Singleton
    @Provides
    public DeleteAllInvalidCollectPre deleteAllInvalidCollectPre(){
        return deleteAllInvalidCollectPre;
    }

}
