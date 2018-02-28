package cn.hwwwwh.taoxiang.presenter;

import android.util.Log;

import org.reactivestreams.Subscription;

import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.base.BasePresenter;
import cn.hwwwwh.taoxiang.model.bean.QuanBean;
import cn.hwwwwh.taoxiang.view.activity.MainActivity;
import cn.hwwwwh.taoxiang.view.iface.IMainQuanDataView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 97481 on 2017/10/6/ 0006.
 */

public class DownloadQuanDataPre extends BasePresenter<IMainQuanDataView,MainActivity> {

    public DownloadQuanDataPre(IMainQuanDataView view, MainActivity activity) {
        super(view, activity);
    }

    public void downloadQuanData(int couponType,String type, int page, String category, Double lp, Double mp, String keyword){
        Observable<QuanBean> observable;
        if(couponType==0){
            observable=ApiUtils.getTqgApi(ApiUrls.tqgApiUrl)
                    .getQuanData(type,page,category,lp,mp,keyword);
        }else{
            if(couponType==1){
                couponType=0;
            }else{
                couponType=1;
            }
            observable=ApiUtils.getTqgApi(ApiUrls.tqgApiUrl)
                    .getTkjdQuanData(couponType,type,page,category,lp,mp,keyword);
        }

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QuanBean>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.d=d;
                    }

                    @Override
                    public void onNext(@NonNull QuanBean quanBean) {
                        if(getView()!=null)
                            if(!quanBean.isError())
                                getView().setTqgTodayView(quanBean.getQuan_data());
                            else{
                                getView().loadTqgTodayFail(quanBean.getError_msg());
                            }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(getView()!=null) {
                            getView().loadTqgTodayFail("请检查网络");
                           //Log.d("taoxiang",e.getMessage());
                        }
                       onComplete();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }
}
