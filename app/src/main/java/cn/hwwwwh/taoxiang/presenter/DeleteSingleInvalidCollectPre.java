package cn.hwwwwh.taoxiang.presenter;

import android.util.Log;
import android.view.View;

import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.base.BasePresenter;
import cn.hwwwwh.taoxiang.model.bean.InsertCollectBean;
import cn.hwwwwh.taoxiang.view.activity.CollectActivity;
import cn.hwwwwh.taoxiang.view.iface.IDeleteSingleInValidCollectView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 97481 on 2017/11/13/ 0013.
 */

public class DeleteSingleInvalidCollectPre extends BasePresenter<IDeleteSingleInValidCollectView,CollectActivity> {

    public DeleteSingleInvalidCollectPre(IDeleteSingleInValidCollectView view, CollectActivity activity) {
        super(view, activity);
    }

    public void delete(String userId, String goodId , final View view, final int pos){
        ApiUtils.getTqgApi(ApiUrls.tqgApiUrl)
                .deleteCollect(userId,goodId)
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
                                Log.d("testtaoxiang",insertCollectBean.getSuccess_msg());
                                getView().deleteSuccess(insertCollectBean.getSuccess_msg(),view,pos);
                            }else
                                getView().deleteFail("网络出错" + insertCollectBean.getError_msg());
                        }
                        onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(getView()!=null) {
                            getView().deleteFail("网络出错" + e.getMessage());
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
}
