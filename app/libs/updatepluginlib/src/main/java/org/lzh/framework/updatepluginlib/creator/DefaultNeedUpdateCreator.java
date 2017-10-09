/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.creator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;

import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.R;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.model.Update;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.util.SafeDialogOper;

/**
 * 默认使用的在检查到有更新时的通知创建器：创建一个弹窗提示用户当前有新版本需要更新。
 *
 * @author haoge
 * @see DialogCreator
 */
public class DefaultNeedUpdateCreator extends DialogCreator {
    @Override
    public Dialog create(final Update update, Activity activity) {

        if (activity == null || activity.isFinishing()) {
            Log.e("DialogCreator--->","Activity was recycled or finished,dialog shown failed!");
            return null;
        }

        String updateContent = activity.getText(R.string.update_version_name)
                + ": " + update.getVersionName() + "\n\n\n"
                + update.getUpdateContent();
        AlertDialog.Builder builder =  new AlertDialog.Builder(activity)
                .setMessage(updateContent)
                .setTitle(R.string.update_title)
                .setPositiveButton(R.string.update_immediate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendDownloadRequest(update);
                        SafeDialogOper.safeDismissDialog((Dialog) dialog);
                    }
                });
        if (update.isIgnore() && !update.isForced()) {
            builder.setNeutralButton(R.string.update_ignore, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendUserIgnore(update);
                    SafeDialogOper.safeDismissDialog((Dialog) dialog);
                }
            });
        }

        if (!update.isForced()) {
            builder.setNegativeButton(R.string.update_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendUserCancel();
                    SafeDialogOper.safeDismissDialog((Dialog) dialog);
                }
            });
        }
        builder.setCancelable(false);
        return builder.create();
    }
}
