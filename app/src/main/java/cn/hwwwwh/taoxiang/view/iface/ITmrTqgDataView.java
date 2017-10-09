package cn.hwwwwh.taoxiang.view.iface;

import java.util.List;

import cn.hwwwwh.taoxiang.model.bean.TqgTmrData;

/**
 * Created by 97481 on 2017/10/8/ 0008.
 */

public interface ITmrTqgDataView {

    void setTmrData(List<TqgTmrData.TqgTmrDataBean> list);

    void setFailView(String msg);
}
