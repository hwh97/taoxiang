package cn.hwwwwh.taoxiang.view.iface;

import java.util.List;

import cn.hwwwwh.taoxiang.model.bean.TqgTodayData;

/**
 * Created by 97481 on 2017/10/6/ 0006.
 */

public interface IMainTqgDataView  {

    void setTqgTodayView(List<TqgTodayData.TqgTodayDataBean> list);

    void loadTqgTodayFail(String msg);
}
