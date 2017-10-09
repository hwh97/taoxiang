package cn.hwwwwh.taoxiang.model;


/**
 * Created by cretin on 2017/4/21.
 */

public class UpdateModel{

    /**
     * id : test
     * page : 1
     * rows : 10
     * isForceUpdate : 0
     * preBaselineCode : 0
     * versionName : V1.0.1
     * versionCode : 3
     * downurl : http://120.24.5.102/Webconfig/frj01_211_jiagu_sign.apk
     * updateLog : 1、修复bug
     * size : 10291218
     * hasAffectCodes : 1|2
     * createTime : 1489651956000
     * iosVersion : 1
     */

    private int isForceUpdate;
    private int preBaselineCode;
    private String versionName;
    private int versionCode;
    private String downurl;
    private String updateLog;
    private String size;
    private String hasAffectCodes;
    private long createTime;
    private int iosVersion;


    public int getIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(int isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public int getPreBaselineCode() {
        return preBaselineCode;
    }

    public void setPreBaselineCode(int preBaselineCode) {
        this.preBaselineCode = preBaselineCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDownurl() {
        return downurl;
    }

    public void setDownurl(String downurl) {
        this.downurl = downurl;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHasAffectCodes() {
        return hasAffectCodes;
    }

    public void setHasAffectCodes(String hasAffectCodes) {
        this.hasAffectCodes = hasAffectCodes;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(int iosVersion) {
        this.iosVersion = iosVersion;
    }


    public int getVersionCodes() {
        return getVersionCode();
    }


    public int getIsForceUpdates() {
        return getIsForceUpdate();
    }


    public int getPreBaselineCodes() {
        return getPreBaselineCode();
    }


    public String getVersionNames() {
        return getVersionName();
    }


    public String getDownurls() {
        return getDownurl();
    }


    public String getUpdateLogs() {
        return getUpdateLog();
    }


    public String getApkSizes() {
        return getSize();
    }


    public String getHasAffectCodess() {
        return getHasAffectCodes();
    }
}
