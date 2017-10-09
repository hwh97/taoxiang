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

import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.business.DownloadWorker;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.business.UpdateWorker;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.callback.UpdateCheckCB;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.callback.UpdateDownloadCB;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.creator.ApkFileCreator;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.creator.DialogCreator;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.creator.DownloadCreator;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.creator.FileChecker;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.creator.InstallCreator;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.model.CheckEntity;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.model.UpdateChecker;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.model.UpdateParser;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.strategy.InstallStrategy;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.strategy.UpdateStrategy;

/**
 * 此类用于建立真正的更新任务。每个更新任务对应于一个{@link UpdateBuilder}实例。
 *
 * <p>创建更新任务有两种方式：<br>
 *      1. 通过{@link #create()}进行创建，代表将使用默认提供的全局更新配置。此默认更新配置通过{@link UpdateConfig#getConfig()}进行获取。<br>
 *      2. 通过{@link #create(UpdateConfig)}指定使用某个特殊的更新配置。<br>
 *
 * <p>此Builder中的所有配置项，均在{@link UpdateConfig}中有对应的相同方法名的配置函数。此两者的关系为：
 * 在更新流程中，当Builder中未设置对应的配置，将会使用在{@link UpdateConfig}更新配置中所提供的默认配置进行使用
 *
 * <p>正常启动：调用{@link #check()}进行启动。
 *
 * @author haoge
 */
public class UpdateBuilder {

    private UpdateWorker checkWorker;
    private DownloadWorker downloadWorker;
    private UpdateCheckCB checkCB;
    private UpdateDownloadCB downloadCB;
    private CheckEntity entity;
    private UpdateStrategy strategy;
    private DialogCreator updateDialogCreator;
    private InstallCreator installDialogCreator;
    private DownloadCreator downloadDialogCreator;
    private UpdateParser jsonParser;
    private ApkFileCreator fileCreator;
    private UpdateChecker updateChecker;
    private FileChecker fileChecker;
    private InstallStrategy installStrategy;
    private UpdateConfig config;
    
    private UpdateBuilder(UpdateConfig config) {
        this.config = config;
    }

    /**
     * 使用默认全局配置进行更新任务创建，默认全局配置可通过{@link UpdateConfig#getConfig()}进行获取
     * @return Builder
     */
    public static UpdateBuilder create() {
        return create(UpdateConfig.getConfig());
    }

    /**
     * 指定该更新任务所使用的更新配置。可通过{@link UpdateConfig#createConfig()}进行新的更新配置创建。
     * @param config 指定使用的更新配置
     * @return Builder
     */
    public static UpdateBuilder create(UpdateConfig config) {
        return new UpdateBuilder(config);
    }

    public UpdateBuilder url(String url) {
        this.entity = new CheckEntity().setUrl(url);
        return this;
    }

    public UpdateBuilder checkEntity (CheckEntity entity) {
        this.entity = entity;
        return this;
    }

    public UpdateBuilder updateChecker (UpdateChecker checker) {
        this.updateChecker = checker;
        return this;
    }

    public UpdateBuilder fileChecker(FileChecker checker) {
        this.fileChecker = checker;
        return this;
    }

    public UpdateBuilder checkWorker(UpdateWorker checkWorker) {
        this.checkWorker = checkWorker;
        return this;
    }

    public UpdateBuilder downloadWorker(DownloadWorker downloadWorker) {
        this.downloadWorker = downloadWorker;
        return this;
    }

    public UpdateBuilder downloadCB(UpdateDownloadCB downloadCB) {
        this.downloadCB = downloadCB;
        return this;
    }

    public UpdateBuilder checkCB (UpdateCheckCB checkCB) {
        this.checkCB = checkCB;
        return this;
    }

    public UpdateBuilder jsonParser (UpdateParser jsonParser) {
        this.jsonParser = jsonParser;
        return this;
    }

    public UpdateBuilder fileCreator (ApkFileCreator fileCreator) {
        this.fileCreator = fileCreator;
        return this;
    }

    public UpdateBuilder downloadDialogCreator (DownloadCreator downloadDialogCreator) {
        this.downloadDialogCreator = downloadDialogCreator;
        return this;
    }

    public UpdateBuilder installDialogCreator (InstallCreator installDialogCreator) {
        this.installDialogCreator = installDialogCreator;
        return this;
    }

    public UpdateBuilder updateDialogCreator(DialogCreator updateDialogCreator) {
        this.updateDialogCreator = updateDialogCreator;
        return this;
    }

    public UpdateBuilder strategy(UpdateStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public UpdateBuilder installStrategy(InstallStrategy installStrategy) {
        this.installStrategy = installStrategy;
        return this;
    }

    /**
     * 启动更新任务。可在任意线程进行启动。
     */
    public void check() {
        Updater.getInstance().checkUpdate(this);
    }

    public UpdateStrategy getStrategy() {
        if (strategy == null) {
            strategy = config.getStrategy();
        }
        return strategy;
    }

    public CheckEntity getCheckEntity () {
        if (this.entity == null) {
            this.entity = config.getCheckEntity();
        }
        return this.entity;
    }

    public UpdateChecker getUpdateChecker() {
        if (updateChecker == null) {
            updateChecker = config.getUpdateChecker();
        }
        return updateChecker;
    }

    public FileChecker getFileChecker() {
        return fileChecker != null ? fileChecker : config.getFileChecker();
    }

    public DialogCreator getUpdateDialogCreator() {
        if (updateDialogCreator == null) {
            updateDialogCreator = config.getUpdateDialogCreator();
        }
        return updateDialogCreator;
    }

    public InstallCreator getInstallDialogCreator() {
        if (installDialogCreator == null) {
            installDialogCreator = config.getInstallDialogCreator();
        }
        return installDialogCreator;
    }

    public DownloadCreator getDownloadDialogCreator() {
        if (downloadDialogCreator == null) {
            downloadDialogCreator = config.getDownloadDialogCreator();
        }
        return downloadDialogCreator;
    }

    public UpdateParser getJsonParser() {
        if (jsonParser == null) {
            jsonParser = config.getJsonParser();
        }
        return jsonParser;
    }

    public UpdateWorker getCheckWorker() {
        if (checkWorker == null) {
            checkWorker = config.getCheckWorker();
        }
        return checkWorker;
    }

    public DownloadWorker getDownloadWorker() {
        if (downloadWorker == null) {
            downloadWorker = config.getDownloadWorker();
        }
        return downloadWorker;
    }

    public ApkFileCreator getFileCreator() {
        if (fileCreator == null) {
            fileCreator = config.getFileCreator();
        }
        return fileCreator;
    }

    public UpdateCheckCB getCheckCB() {
        if (checkCB == null) {
            checkCB = config.getCheckCB();
        }
        return checkCB;
    }

    public UpdateDownloadCB getDownloadCB() {
        if (downloadCB == null) {
            downloadCB = config.getDownloadCB();
        }
        return downloadCB;
    }

    public InstallStrategy getInstallStrategy() {
        if (installStrategy == null) {
            installStrategy = config.getInstallStrategy();
        }
        return installStrategy;
    }

}
