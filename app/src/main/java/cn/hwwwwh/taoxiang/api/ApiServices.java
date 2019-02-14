package cn.hwwwwh.taoxiang.api;

import cn.hwwwwh.taoxiang.model.bean.CollectBean;
import cn.hwwwwh.taoxiang.model.bean.InsertCollectBean;
import cn.hwwwwh.taoxiang.model.bean.QuanBean;
import cn.hwwwwh.taoxiang.model.bean.QuanCryBean;
import cn.hwwwwh.taoxiang.model.bean.TqgTodayData;
import cn.hwwwwh.taoxiang.model.bean.UpdateBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 97481 on 2017/10/4/ 0004.
 */

public interface ApiServices {

    @GET("update.json")
    Observable<UpdateBean> getUpdateInfo();

    @GET("QuanCryApi.php")
    Observable<QuanCryBean> getQuanCryData(@Query("method") String method);

    @GET("TkjdQuanCryApi.php")
    Observable<QuanCryBean> getTkjdQuanCryData(@Query("type") int type);

    @GET("QuanApi.php")
    Observable<QuanBean> getQuanData(@Query("type") String type, @Query("page") int page
            , @Query("category") String category, @Query("lp") double lp, @Query("mp") Double mp, @Query("keyword") String keyword);

    @GET("TkjdQuanApi.php")
    Observable<QuanBean> getTkjdQuanData(@Query("isDp") int isDp,@Query("type") String type, @Query("page") int page
            , @Query("category") String category, @Query("lp") double lp, @Query("mp") Double mp, @Query("keyword") String keyword);

    @GET("IsCollectApi.php")
    Observable<InsertCollectBean> isCollect(@Query("UserId") String userId,@Query("GoodsID") String goodId);

    @GET("CollectInsert.php")
    Observable<InsertCollectBean> requestCollect(@Query("UserId") String userId,@Query("GoodsID") String goodId);

    @GET("CollectApi.php")
    Observable<CollectBean> getMyCollectData(@Query("UserId") String userId,@Query("page")int page);

    //删除单个无效商品
    @GET("CollectDeleteApi.php")
    Observable<InsertCollectBean> deleteCollect(@Query("UserId") String userId,@Query("GoodsID") String goodId);

    @GET("CollectUpdateApi.php")
    Observable<InsertCollectBean> deleteAllInvalidCollect(@Query("UserId") String userId);

}
