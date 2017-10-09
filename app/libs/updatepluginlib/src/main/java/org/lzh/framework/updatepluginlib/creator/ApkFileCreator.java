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

import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.UpdateBuilder;
import cn.hwwwwh.taoxiang.CoustomView.update.updatepluginlib.UpdateConfig;

import java.io.File;

/**
 * 此接口用于定制下载文件的本地路径。
 *
 * <p>设置方式：通过{@link UpdateConfig#fileCreator(ApkFileCreator)}或者{@link UpdateBuilder#fileCreator(ApkFileCreator)}进行配置
 *
 * <p>默认实现方式：{@link DefaultFileChecker}
 *
 * <p>注意事项：在Android 7.0上，对于非私有目录的访问是需要进行动态权限申请的。所以当你将下载缓存路径配置到非私有目录下时，请注意权限问题。
 *
 * @author haoge
 */
public interface ApkFileCreator {

    File create(String versionName);
}
