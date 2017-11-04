package cn.hwwwwh.taoxiang.model.bean;

/**
 * Created by 97481 on 2017/10/9/ 0009.
 */

public class UpdateBean {

    /**
     * update_url : http://cs.hwwwwh.cn/taoxiang.apk
     * update_content : 1,更新测试1/n 2,更新测试2/n3,更新测试3.
     * update_ver_code : 2
     * update_ver_name : 1.1
     * update_Time : 2017-10-1000: 00
     * update_app_name : 淘享
     * ignore_able : true
     */

    private String update_url;
    private String update_content;
    private int update_ver_code;
    private String update_ver_name;
    private String update_Time;
    private String update_app_name;
    private boolean ignore_able;
    /**
     * ignore_able : true
     * update_app_size : 3.5M
     */

    private String update_app_size;
    /**
     * shape_text : 淘享下载地址：http://cs.hwwwwh.cn/taoxiang.apk，复制浏览器打开
     */

    private String share_text;

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public String getUpdate_content() {
        return update_content;
    }

    public void setUpdate_content(String update_content) {
        this.update_content = update_content;
    }

    public int getUpdate_ver_code() {
        return update_ver_code;
    }

    public void setUpdate_ver_code(int update_ver_code) {
        this.update_ver_code = update_ver_code;
    }

    public String getUpdate_ver_name() {
        return update_ver_name;
    }

    public void setUpdate_ver_name(String update_ver_name) {
        this.update_ver_name = update_ver_name;
    }

    public String getUpdate_Time() {
        return update_Time;
    }

    public void setUpdate_Time(String update_Time) {
        this.update_Time = update_Time;
    }

    public String getUpdate_app_name() {
        return update_app_name;
    }

    public void setUpdate_app_name(String update_app_name) {
        this.update_app_name = update_app_name;
    }

    public boolean isIgnore_able() {
        return ignore_able;
    }

    public void setIgnore_able(boolean ignore_able) {
        this.ignore_able = ignore_able;
    }


    public String getUpdate_app_size() {
        return update_app_size;
    }

    public void setUpdate_app_size(String update_app_size) {
        this.update_app_size = update_app_size;
    }

    public String getShare_text() {
        return share_text;
    }

    public void setShare_text(String share_text) {
        this.share_text = share_text;
    }
}
