package cn.hwwwwh.taoxiang.api;

import cn.hwwwwh.taoxiang.model.bean.QuanBean;
import cn.hwwwwh.taoxiang.model.bean.QuanCryBean;
import cn.hwwwwh.taoxiang.model.bean.TodayCry;
import cn.hwwwwh.taoxiang.model.bean.TqgTmrData;
import cn.hwwwwh.taoxiang.model.bean.TqgTodayData;
import cn.hwwwwh.taoxiang.model.bean.UpdateBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 97481 on 2017/10/4/ 0004.
 */

public interface ApiServices {

    @GET("tqgCgyApi.php")
    Observable<TodayCry> getCryData(@Query("method") String method);

    @GET("tqgTodayApi.php")
    Observable<TqgTodayData> getTqgTodayData(@Query("type") String type, @Query("page") int page
            ,@Query("category") String category,@Query("lp") double lp,@Query("mp") Double mp, @Query("keyword") String keyword);

    @GET("tqgTmrApi.php")
    Observable<TqgTmrData> getTqgTmrData(@Query("type") int type, @Query("page") int page);

    @GET("update.json")
    Observable<UpdateBean> getUpdateInfo();

    @GET("QuanCryApi.php")
    Observable<QuanCryBean> getQuanCryData(@Query("method") String method);

    @GET("QuanApi.php")
    Observable<QuanBean> getQuanData(@Query("type") String type, @Query("page") int page
            , @Query("category") String category, @Query("lp") double lp, @Query("mp") Double mp, @Query("keyword") String keyword);

}
