package com.xiezhiai.wechatplugin.hooker;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by shijiwei on 2018/10/12.
 *
 * @Desc:
 */
public interface IHooker {

    void hook(XC_LoadPackage.LoadPackageParam lpparam);

    void release();
}
