package cn.hwwwwh.taoxiang.presenter;

import android.util.Log;

import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.base.BasePresenter;
import cn.hwwwwh.taoxiang.model.bean.TodayCry;
import cn.hwwwwh.taoxiang.view.activity.MainActivity;
import cn.hwwwwh.taoxiang.view.iface.IMainCryVIew;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 97481 on 2017/10/4/ 0004.
 */

public class DownloadCryPre extends BasePresenter<IMainCryVIew,MainActivity> {

    private final String TAG = DownloadCryPre.class.getSimpleName();

    public DownloadCryPre(IMainCryVIew view, MainActivity activity) {
        super(view, activity);
    }

    public void loadCry(String method){
        ApiUtils.getTqgApi(ApiUrls.cryApiUrl)
                .getCryData("0")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TodayCry>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.d=d;
                    }

                    @Override
                    public void onNext(@NonNull TodayCry todayCry) {
                        Log.d(TAG, "onNext: " +todayCry.getTqg_today_category().get(0).getTqg_category_name());
                        if (getView() != null)
                            if(!todayCry.isError())
                                getView().loadCry(todayCry.getTqg_today_category());
                            else{
                                getView().loadCryFail(todayCry.getError_msg());
                            }

                        onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(getView()!=null)
                            getView().loadCryFail("网络出错");
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }

}
