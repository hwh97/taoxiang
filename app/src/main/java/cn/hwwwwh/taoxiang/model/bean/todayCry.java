package cn.hwwwwh.taoxiang.model.bean;

import java.util.List;

/**
 * Created by 97481 on 2017/10/4/ 0004.
 */

public class TodayCry {


    /**
     * error : false
     * tqg_today_category : [{"tqg_category_name":"数码电器","num":232},{"tqg_category_name":"母婴童装","num":133},{"tqg_category_name":"女鞋","num":112},{"tqg_category_name":"家纺家居","num":88},{"tqg_category_name":"男装","num":80},{"tqg_category_name":"运动户外","num":74},{"tqg_category_name":"美妆","num":70},{"tqg_category_name":"女装","num":47},{"tqg_category_name":"个护家清","num":36},{"tqg_category_name":"食品","num":26},{"tqg_category_name":"居家百货","num":11},{"tqg_category_name":"医药保健","num":6},{"tqg_category_name":"箱包服配","num":5},{"tqg_category_name":"车品旅行","num":4},{"tqg_category_name":"手表配饰","num":3},{"tqg_category_name":"内衣","num":1}]
     */

    private boolean error;
    private List<TqgTodayCategoryBean> tqg_today_category;
    /**
     * error_msg : 参数丢失
     */

    private String error_msg;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<TqgTodayCategoryBean> getTqg_today_category() {
        return tqg_today_category;
    }

    public void setTqg_today_category(List<TqgTodayCategoryBean> tqg_today_category) {
        this.tqg_today_category = tqg_today_category;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public static class TqgTodayCategoryBean {
        /**
         * tqg_category_name : 数码电器
         * num : 232
         */

        private String tqg_category_name;
        private int num;

        public String getTqg_category_name() {
            return tqg_category_name;
        }

        public void setTqg_category_name(String tqg_category_name) {
            this.tqg_category_name = tqg_category_name;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
