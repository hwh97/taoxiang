package cn.hwwwwh.taoxiang.view.iface;

import android.view.View;

/**
 * Created by 97481 on 2017/11/12/ 0012.
 */

public interface IRequestCollectView {

     void requestSuccess(String msg, View view,int pos);

     void requestFail(String msg);
}
