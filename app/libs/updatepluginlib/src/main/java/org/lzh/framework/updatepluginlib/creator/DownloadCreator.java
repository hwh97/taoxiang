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

import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.UpdateBuilder;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.UpdateConfig;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.callback.UpdateDownloadCB;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.model.Update;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.strategy.UpdateStrategy;

/**
 * apk下载任务的通知创建器
 *
 * <p>配置方式：通过{@link UpdateConfig#downloadDialogCreator(DownloadCreator)}或者{@link UpdateBuilder#downloadDialogCreator(DownloadCreator)}
 *
 * <p>默认实现：{@link DefaultNeedDownloadCreator}
 *
 * <p>触发逻辑：当配置的更新策略{@link UpdateStrategy#isShowDownloadDialog()}设定为true时。此通知创建器将被触发
 *
 * @author haoge
 */
public interface DownloadCreator {

    /**
     * 创建一个下载任务的下载进度回调。此回调将用于接收下载任务的状态并更新UI。
     *
     * @param update 更新数据实体类
     * @param activity 顶部的Activity实例。通过{@link ActivityManager#topActivity()}进行获取
     * @return 被创建的回调器。允许为null。
     */
    UpdateDownloadCB create(Update update, Activity activity);
}
