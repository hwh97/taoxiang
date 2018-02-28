package cn.hwwwwh.taoxiang.model.bean;

import java.util.List;

/**
 * Created by 97481 on 2017/11/1/ 0001.
 */

public class QuanCryBean {


    /**
     * error : false
     * quan_category : [{"goods_cat_name":"居家","num":1477},{"goods_cat_name":"母婴","num":758},{"goods_cat_name":"鞋包配饰","num":680},{"goods_cat_name":"美妆","num":598},{"goods_cat_name":"美食","num":552},{"goods_cat_name":"数码家电","num":536},{"goods_cat_name":"内衣","num":472},{"goods_cat_name":"文体车品","num":463},{"goods_cat_name":"女装","num":383},{"goods_cat_name":"男装","num":259},{"goods_cat_name":"其他","num":1}]
     */

    private boolean error;
    private List<QuanCategoryBean> quan_category;
    private String error_msg;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public List<QuanCategoryBean> getQuan_category() {
        return quan_category;
    }

    public void setQuan_category(List<QuanCategoryBean> quan_category) {
        this.quan_category = quan_category;
    }

    public static class QuanCategoryBean {
        /**
         * goods_cat_name : 居家
         * num : 1477
         */

        private String goods_cat_name;
        private int num;

        public String getGoods_cat_name() {
            return goods_cat_name;
        }

        public void setGoods_cat_name(String goods_cat_name) {
            this.goods_cat_name = goods_cat_name;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
