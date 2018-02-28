package cn.hwwwwh.taoxiang.presenter;

import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.base.BaseActivity;
import cn.hwwwwh.taoxiang.base.BasePresenter;
import cn.hwwwwh.taoxiang.model.bean.QuanCryBean;
import cn.hwwwwh.taoxiang.view.activity.MainActivity;
import cn.hwwwwh.taoxiang.view.iface.IMainQuanCryVIew;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 97481 on 2017/10/4/ 0004.
 */

public class DownloadQuanCryPre extends BasePresenter<IMainQuanCryVIew,BaseActivity> {

    private final String TAG = DownloadQuanCryPre.class.getSimpleName();

    public DownloadQuanCryPre(IMainQuanCryVIew view, BaseActivity activity) {
        super(view, activity);
    }

    public void loadCry(int couponType){
        Observable<QuanCryBean> quanCryBeanObservable;
        if(couponType==0){
            quanCryBeanObservable= ApiUtils.getTqgApi(ApiUrls.cryApiUrl)
                    .getQuanCryData("0");
        }else{
            if(couponType==1){
                couponType=0;
            }else{
                couponType=1;
            }
            quanCryBeanObservable= ApiUtils.getTqgApi(ApiUrls.cryApiUrl)
                    .getTkjdQuanCryData(couponType);
        }
        quanCryBeanObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                //.compose(getActivity().<QuanCryBean>bindToLifecycle())//RxLifeCycle实现
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<QuanCryBean>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.d=d;
                    }

                    @Override
                    public void onNext(@NonNull QuanCryBean quanCryBean) {

                        if (getView() != null&&getActivity()!=null)
                            if(!quanCryBean.isError())
                                getView().loadCry(quanCryBean.getQuan_category());
                            else{
                                getView().loadCryFail(quanCryBean.getError_msg());
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
