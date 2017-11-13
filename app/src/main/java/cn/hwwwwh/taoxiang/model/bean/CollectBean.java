package cn.hwwwwh.taoxiang.model.bean;

import java.util.List;

/**
 * Created by 97481 on 2017/11/12/ 0012.
 */

public class CollectBean {


    /**
     * error : true
     * error_msg : 参数丢失
     */

    private boolean error;
    private String error_msg;
    private List<CollectDataBean> CollectData;

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

    public List<CollectDataBean> getCollectData() {
        return CollectData;
    }

    public void setCollectData(List<CollectDataBean> CollectData) {
        this.CollectData = CollectData;
    }


    public static class CollectDataBean {
        /**
         * collect_userId : 0146543b80080000
         * collect_goods_id : 546986125954
         * collect_goods_pic : https://img.alicdn.com/imgextra/i4/287136390/TB2YwmXoYBkpuFjy1zkXXbSpFXa_!!287136390.jpg
         * collect_goods_title : 车载充气床汽车成人床垫后排轿车后座睡垫车中气垫旅行SUV车震床
         * collect_goods_short_title : 沿途车载充气床汽车成人床
         * collect_goods_cat : 7
         * collect_goods_cat_name : 文体车品
         * collect_goods_price : 139
         * collect_goods_finalprice : 124
         * collect_goods_sales : 1211
         * collect_is_tmall : 1
         * collect_coupon_id : 6d99e42e56bd470c96c23786dd332759
         * collect_coupon_link : http://shop.m.taobao.com/shop/coupon.htm?seller_id=287136390&activity_id=6d99e42e56bd470c96c23786dd332759
         * collect_coupon_price : 15
         * collect_coupon_end_time : 1510675199
         * "isInvalid": false
         */

        private String collect_userId;
        private long collect_goods_id;
        private String collect_goods_pic;
        private String collect_goods_title;
        private String collect_goods_short_title;
        private int collect_goods_cat;
        private String collect_goods_cat_name;
        private Double collect_goods_price;
        private Double collect_goods_finalprice;
        private int collect_goods_sales;
        private int collect_is_tmall;
        private String collect_coupon_id;
        private String collect_coupon_link;
        private Double collect_coupon_price;
        private int collect_coupon_end_time;
        private boolean isInvalid;
        private boolean isCollect=true;

        public void setCollect(boolean collect) {
            isCollect = collect;
        }

        public boolean isCollect() {
            return isCollect;
        }

        public void setInvalid(boolean invalid) {
            isInvalid = invalid;
        }

        public boolean isInvalid() {
            return isInvalid;
        }

        public String getCollect_userId() {
            return collect_userId;
        }

        public void setCollect_userId(String collect_userId) {
            this.collect_userId = collect_userId;
        }

        public long getCollect_goods_id() {
            return collect_goods_id;
        }

        public void setCollect_goods_id(long collect_goods_id) {
            this.collect_goods_id = collect_goods_id;
        }

        public String getCollect_goods_pic() {
            return collect_goods_pic;
        }

        public void setCollect_goods_pic(String collect_goods_pic) {
            this.collect_goods_pic = collect_goods_pic;
        }

        public String getCollect_goods_title() {
            return collect_goods_title;
        }

        public void setCollect_goods_title(String collect_goods_title) {
            this.collect_goods_title = collect_goods_title;
        }

        public String getCollect_goods_short_title() {
            return collect_goods_short_title;
        }

        public void setCollect_goods_short_title(String collect_goods_short_title) {
            this.collect_goods_short_title = collect_goods_short_title;
        }

        public int getCollect_goods_cat() {
            return collect_goods_cat;
        }

        public void setCollect_goods_cat(int collect_goods_cat) {
            this.collect_goods_cat = collect_goods_cat;
        }

        public String getCollect_goods_cat_name() {
            return collect_goods_cat_name;
        }

        public void setCollect_goods_cat_name(String collect_goods_cat_name) {
            this.collect_goods_cat_name = collect_goods_cat_name;
        }

        public Double getCollect_goods_price() {
            return collect_goods_price;
        }

        public void setCollect_goods_price(Double collect_goods_price) {
            this.collect_goods_price = collect_goods_price;
        }

        public Double getCollect_goods_finalprice() {
            return collect_goods_finalprice;
        }

        public void setCollect_goods_finalprice(Double collect_goods_finalprice) {
            this.collect_goods_finalprice = collect_goods_finalprice;
        }

        public int getCollect_goods_sales() {
            return collect_goods_sales;
        }

        public void setCollect_goods_sales(int collect_goods_sales) {
            this.collect_goods_sales = collect_goods_sales;
        }

        public int getCollect_is_tmall() {
            return collect_is_tmall;
        }

        public void setCollect_is_tmall(int collect_is_tmall) {
            this.collect_is_tmall = collect_is_tmall;
        }

        public String getCollect_coupon_id() {
            return collect_coupon_id;
        }

        public void setCollect_coupon_id(String collect_coupon_id) {
            this.collect_coupon_id = collect_coupon_id;
        }

        public String getCollect_coupon_link() {
            return collect_coupon_link;
        }

        public void setCollect_coupon_link(String collect_coupon_link) {
            this.collect_coupon_link = collect_coupon_link;
        }

        public Double getCollect_coupon_price() {
            return collect_coupon_price;
        }

        public void setCollect_coupon_price(Double collect_coupon_price) {
            this.collect_coupon_price = collect_coupon_price;
        }

        public int getCollect_coupon_end_time() {
            return collect_coupon_end_time;
        }

        public void setCollect_coupon_end_time(int collect_coupon_end_time) {
            this.collect_coupon_end_time = collect_coupon_end_time;
        }
    }
}
