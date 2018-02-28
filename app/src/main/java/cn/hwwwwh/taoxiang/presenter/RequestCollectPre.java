package cn.hwwwwh.taoxiang.presenter;

import android.util.Log;
import android.view.View;

import com.trello.rxlifecycle2.RxLifecycle;

import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.base.BaseActivity;
import cn.hwwwwh.taoxiang.base.BasePresenter;
import cn.hwwwwh.taoxiang.model.bean.InsertCollectBean;
import cn.hwwwwh.taoxiang.view.activity.AliSdkWebViewProxyActivity;
import cn.hwwwwh.taoxiang.view.iface.IRequestCollectView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 97481 on 2017/11/12/ 0012.
 */

public class RequestCollectPre extends BasePresenter<IRequestCollectView,BaseActivity> {

    public RequestCollectPre(IRequestCollectView view, BaseActivity activity) {
        super(view, activity);
    }

    public void requestCollect(String userId, String goodId, final View view, final int pos){
        ApiUtils.getTqgApi(ApiUrls.tqgApiUrl)
                .requestCollect(userId,goodId)
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
                        if(getView()!=null) {
                           if (!insertCollectBean.isError()){
                               //Log.d("testtaoxiang",insertCollectBean.getSuccess_msg());
                               getView().requestSuccess(insertCollectBean.getSuccess_msg(),view,pos);
                           }else
                               getView().requestFail("网络出错" );
                        }
                        onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(getView()!=null) {
                            getView().requestFail("网络出错");
                           // Log.d("testtaoxiang",e.getMessage());
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
