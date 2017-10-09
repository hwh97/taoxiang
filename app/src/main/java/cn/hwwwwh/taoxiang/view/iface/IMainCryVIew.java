package cn.hwwwwh.taoxiang.view.iface;

import java.util.List;

import cn.hwwwwh.taoxiang.base.IBaseView;
import cn.hwwwwh.taoxiang.model.bean.TodayCry;

/**
 * Created by 97481 on 2017/10/4/ 0004.
 */

public interface IMainCryVIew {

    void loadCry(List<TodayCry.TqgTodayCategoryBean> tqg_today_category);

    void loadCryFail(String msg);
}
