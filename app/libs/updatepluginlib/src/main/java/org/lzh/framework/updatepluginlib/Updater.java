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
package cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib;

import android.util.Log;

import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.business.DownloadWorker;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.business.UpdateExecutor;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.business.UpdateWorker;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.callback.DefaultCheckCB;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.callback.DefaultDownloadCB;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.creator.FileChecker;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.model.Update;

import java.io.File;

/**
 * The Dispatcher class to request to launch check-task or download-task
 */
public final class Updater {
    private static Updater updater;
    private UpdateExecutor executor;

    private Updater() {
        executor = UpdateExecutor.getInstance();
    }
    public static Updater getInstance() {
        if (updater == null) {
            updater = new Updater();
        }
        return updater;
    }

    /**
     * Request to launch a {@link UpdateWorker} task from this builder
     *
     * @param builder The {@link UpdateWorker} instance provider.
     */
    void checkUpdate(UpdateBuilder builder) {
        // define a default callback to receive update event send by check task
        DefaultCheckCB checkCB = new DefaultCheckCB();
        checkCB.setBuilder(builder);
        checkCB.onCheckStart();

        UpdateWorker checkWorker = builder.getCheckWorker();
        if (checkWorker.isRunning()) {
            Log.e("Updater","Already have a update task running");
            checkCB.onCheckError(new RuntimeException("Already have a update task running"));
            return;
        }
        checkWorker.setBuilder(builder);
        checkWorker.setCheckCB(checkCB);
        executor.check(checkWorker);
    }

    /**
     * Request to launch a {@link DownloadWorker} task from this builder.
     *
     * @param update update instance, should not be null;
     * @param builder The {@link DownloadWorker} instance provider
     */
    public void downUpdate(Update update,UpdateBuilder builder) {
        // define a default download callback to receive callback from download task
        DefaultDownloadCB downloadCB = new DefaultDownloadCB();
        downloadCB.setBuilder(builder);
        downloadCB.setUpdate(update);

        FileChecker fileChecker = builder.getFileChecker();
        File cacheFile = builder.getFileCreator().create(update.getVersionName());
        if (cacheFile != null && cacheFile.exists()
                && fileChecker.checkPreFile(update,cacheFile.getAbsolutePath())) {
            // check success: skip download and show install dialog if needed.
            downloadCB.showInstallDialogIfNeed(cacheFile);
            return;
        }

        DownloadWorker downloadWorker = builder.getDownloadWorker();
        if (downloadWorker.isRunning()) {
            Log.e("Updater","Already have a download task running");
            downloadCB.onDownloadError(new RuntimeException("Already have a download task running"));
            return;
        }

        downloadWorker.setUpdate(update);
        downloadWorker.setDownloadCB(downloadCB);
        downloadWorker.setCacheFileName(builder.getFileCreator().create(update.getVersionName()));

        executor.download(downloadWorker);
    }

}
