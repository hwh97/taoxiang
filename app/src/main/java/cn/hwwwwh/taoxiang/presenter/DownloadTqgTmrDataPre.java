package cn.hwwwwh.taoxiang.presenter;

import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.api.RetrofitUtils;
import cn.hwwwwh.taoxiang.base.BasePresenter;
import cn.hwwwwh.taoxiang.model.bean.TqgTmrData;
import cn.hwwwwh.taoxiang.model.bean.TqgTodayData;
import cn.hwwwwh.taoxiang.view.activity.TmrActivity;
import cn.hwwwwh.taoxiang.view.iface.ITmrTqgDataView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 97481 on 2017/10/8/ 0008.
 */

public class DownloadTqgTmrDataPre extends BasePresenter<ITmrTqgDataView,TmrActivity> {

    public DownloadTqgTmrDataPre(ITmrTqgDataView view, TmrActivity activity) {
        super(view, activity);
    }

    public void loadTmrData(String url,int type,int page){
        ApiUtils.getTqgApi(url)
                .getTqgTmrData(type,page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TqgTmrData>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.d=d;
                    }

                    @Override
                    public void onNext(@NonNull TqgTmrData tqgTmrData) {
                        if(getView()!=null)
                            if(!tqgTmrData.isError())
                                getView().setTmrData(tqgTmrData.getTqg_tmr_data());
                            else
                                getView().setFailView(tqgTmrData.getError_msg());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(getView()!=null)
                            getView().setFailView("网络出错");
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }
}
