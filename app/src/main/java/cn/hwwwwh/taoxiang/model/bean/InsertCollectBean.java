package cn.hwwwwh.taoxiang.model.bean;

/**
 * Created by 97481 on 2017/11/12/ 0012.
 */

public class InsertCollectBean {


    /**
     * error : true
     * error_msg : 以收藏该商品
     */

    private boolean error;
    private String error_msg;
    /**
     * success_msg : 添加商品收藏成功
     */

    private String success_msg;

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

    public String getSuccess_msg() {
        return success_msg;
    }

    public void setSuccess_msg(String success_msg) {
        this.success_msg = success_msg;
    }
}
