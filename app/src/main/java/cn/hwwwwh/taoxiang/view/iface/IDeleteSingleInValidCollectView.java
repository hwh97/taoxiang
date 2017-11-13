package cn.hwwwwh.taoxiang.view.iface;

import android.view.View;

/**
 * Created by 97481 on 2017/11/13/ 0013.
 */

public interface IDeleteSingleInValidCollectView {

    void deleteSuccess(String msg, View view, int pos);

    void deleteFail(String msg);
}
