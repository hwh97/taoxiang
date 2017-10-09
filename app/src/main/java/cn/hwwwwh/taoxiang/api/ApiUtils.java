package cn.hwwwwh.taoxiang.api;

/**
 * 接口工具类
 *
 * @author ZhongDaFeng
 */

public class ApiUtils {

    private static ApiServices TqgApi;

    public static ApiServices getTqgApi(String url) {
        if (TqgApi == null) {
            TqgApi = RetrofitUtils.get(url).retrofit().create(ApiServices.class);
        }
        return TqgApi;
    }


}
