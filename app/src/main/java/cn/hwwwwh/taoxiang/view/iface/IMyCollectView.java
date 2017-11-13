package cn.hwwwwh.taoxiang.view.iface;

import java.util.List;

import cn.hwwwwh.taoxiang.model.bean.CollectBean;

/**
 * Created by 97481 on 2017/11/12/ 0012.
 */

public interface IMyCollectView {

    void downloadSuccess(List<CollectBean.CollectDataBean> list);

    void downloadFail(String msg);
}
