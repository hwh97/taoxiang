package cn.hwwwwh.taoxiang.view.iface;

import java.util.List;

import cn.hwwwwh.taoxiang.base.IBaseView;
import cn.hwwwwh.taoxiang.model.bean.QuanCryBean;
import cn.hwwwwh.taoxiang.model.bean.TodayCry;

/**
 * Created by 97481 on 2017/10/4/ 0004.
 */

public interface IMainQuanCryVIew {

    void loadCry(List<QuanCryBean.QuanCategoryBean> tqg_today_category);

    void loadCryFail(String msg);
}
