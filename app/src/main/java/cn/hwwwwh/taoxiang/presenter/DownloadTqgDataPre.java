package cn.hwwwwh.taoxiang.presenter;

import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.base.BasePresenter;
import cn.hwwwwh.taoxiang.model.bean.TqgTodayData;
import cn.hwwwwh.taoxiang.view.activity.MainActivity;
import cn.hwwwwh.taoxiang.view.iface.IMainTqgDataView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 97481 on 2017/10/6/ 0006.
 */

public class DownloadTqgDataPre extends BasePresenter<IMainTqgDataView,MainActivity> {

    public DownloadTqgDataPre(IMainTqgDataView view, MainActivity activity) {
        super(view, activity);
    }

    public void downloadTqgTodayData(String type,int page,String category,Double lp,Double mp,String keyword){
        ApiUtils.getTqgApi(ApiUrls.tqgApiUrl)
                .getTqgTodayData(type,page,category,lp,mp,keyword)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TqgTodayData>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.d=d;
                    }

                    @Override
                    public void onNext(@NonNull TqgTodayData tqgTodayData) {
                        if(getView()!=null)
                            if(!tqgTodayData.isError())
                                getView().setTqgTodayView(tqgTodayData.getTqg_today_data());
                            else{
                                getView().loadTqgTodayFail(tqgTodayData.getError_msg());
                            }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(getView()!=null)
                            getView().loadTqgTodayFail("网络出错");
                       onComplete();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }
}
