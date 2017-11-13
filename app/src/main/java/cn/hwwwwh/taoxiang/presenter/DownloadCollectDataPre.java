package cn.hwwwwh.taoxiang.presenter;

import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.base.BasePresenter;
import cn.hwwwwh.taoxiang.model.bean.CollectBean;
import cn.hwwwwh.taoxiang.view.activity.CollectActivity;
import cn.hwwwwh.taoxiang.view.iface.IMyCollectView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 97481 on 2017/11/12/ 0012.
 */

public class DownloadCollectDataPre extends BasePresenter<IMyCollectView,CollectActivity> {

    public DownloadCollectDataPre(IMyCollectView view, CollectActivity activity) {
        super(view, activity);
    }

    public void downloadCollect(String userId,int page){
        ApiUtils.getTqgApi(ApiUrls.tqgApiUrl)
                .getMyCollectData(userId,page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CollectBean>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.d=d;
                    }

                    @Override
                    public void onNext(@NonNull CollectBean collectBean) {
                        if (getView() != null)
                            if(!collectBean.isError())
                                getView().downloadSuccess(collectBean.getCollectData());
                            else{
                                getView().downloadFail(collectBean.getError_msg());
                            }
                        onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(getView()!=null)
                            getView().downloadFail("网络出错"+e.getMessage());
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }
}
