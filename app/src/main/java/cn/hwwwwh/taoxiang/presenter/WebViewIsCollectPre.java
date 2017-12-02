package cn.hwwwwh.taoxiang.presenter;

import android.util.Log;

import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.base.BasePresenter;
import cn.hwwwwh.taoxiang.model.bean.InsertCollectBean;
import cn.hwwwwh.taoxiang.utils.ToastUtils;
import cn.hwwwwh.taoxiang.view.activity.AliSdkWebViewProxyActivity;
import cn.hwwwwh.taoxiang.view.iface.IWebViewCollectView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 97481 on 2017/11/12/ 0012.
 */

public class WebViewIsCollectPre extends BasePresenter<IWebViewCollectView,AliSdkWebViewProxyActivity> {

    public WebViewIsCollectPre(IWebViewCollectView view, AliSdkWebViewProxyActivity activity) {
        super(view, activity);
    }


    public void isCollect(String userId,String goodId,final CallBack callBack){
        ApiUtils.getTqgApi(ApiUrls.tqgApiUrl)
                .isCollect(userId,goodId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InsertCollectBean>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.d=d;
                    }

                    @Override
                    public void onNext(@NonNull InsertCollectBean insertCollectBean) {
                        Log.d("testtaoxiang",insertCollectBean.isError()+"");
                        if(getView()!=null) {
                            if(callBack!=null){
                                callBack.initSuccess();
                            }
                            if(!insertCollectBean.isError())
                                getView().setCollect();
                            else
                                getView().setUnCollect();
                        }
                        onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(getView()!=null) {
                            ToastUtils.showToast(getActivity().getApplicationContext(),"网络出错" + e.getMessage());
                            Log.d("testtaoxiang",e.getMessage());
                        }
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }

    public interface CallBack{

        void initSuccess();

    }
}
