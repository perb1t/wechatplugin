package com.xiezhiai.wechatplugin.ui.aty;


import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;


import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.xiezhiai.wechatplugin.R;

import com.xiezhiai.wechatplugin.func.nohttp.URLManager;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleRequest;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleResult;

import com.xiezhiai.wechatplugin.func.simulate.ControlService;
import com.xiezhiai.wechatplugin.func.simulate.SimulateManager;
import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.func.transfer.PluginMessasge;
import com.xiezhiai.wechatplugin.func.transfer.PluginServer;
import com.xiezhiai.wechatplugin.model.wechat.Message;
import com.xiezhiai.wechatplugin.model.xiezhi.entity.TabEntity;
import com.xiezhiai.wechatplugin.func.transfer.PluginClient;
import com.xiezhiai.wechatplugin.service.PluginService;
import com.xiezhiai.wechatplugin.ui.app.WechatPluginApplication;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.ui.base.BaseFragment;
import com.xiezhiai.wechatplugin.ui.frg.GroupHelperFragment;
import com.xiezhiai.wechatplugin.ui.frg.MyFragment;
import com.xiezhiai.wechatplugin.ui.frg.QALibFragment;
import com.xiezhiai.wechatplugin.utils.AppUtil;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.utils.XposedManager;
import com.xiezhiai.wechatplugin.utils.file.FileTransfer;

import com.xiezhiai.wechatplugin.widget.CommonDilalog;
import com.xiezhiai.wechatplugin.widget.NoScrollViewPager;

import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private NoScrollViewPager viewPager;
    private CommonTabLayout tabLayout;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private int lastTaIndex = 3;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {

        checkPermission(Permission.Group.STORAGE);
//        mFragments.add(new HomeFragment());
        mFragments.add(new GroupHelperFragment());
        mFragments.add(new QALibFragment());
        mFragments.add(new MyFragment());

//        mTabEntities.add(new TabEntity(getString(R.string.tab_home), R.mipmap.icon_home_selected, R.mipmap.icon_home_unselect));
        mTabEntities.add(new TabEntity(getString(R.string.tab_group_helper), R.mipmap.icon_gh_selectd, R.mipmap.icon_gh_unselected));
        mTabEntities.add(new TabEntity(getString(R.string.tab_qalib), R.mipmap.icon_qalib_selected, R.mipmap.icon_qalib_unselected));
        mTabEntities.add(new TabEntity(getString(R.string.tab_my), R.mipmap.icon_my_selected, R.mipmap.icon_my_unselectd));

    }

    @Override
    public void initialView() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        tabLayout.setTabData(mTabEntities);
    }

    @Override
    public void initialEvn() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
                lastTaIndex = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        viewPager.setCurrentItem(lastTaIndex);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, PluginClient.class));
        PluginService.startServer(this);
        if (PluginHandler.cookie.isPluginLogin() && !PluginHandler.cookie.isInitialize()) {
            viewPager.setCurrentItem(3);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkPermission(Permission.Group.STORAGE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.e("++++++++++++++  onNewIntent  +++++++++++");
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PluginClient.class));
        PluginService.stopServer(this);
        super.onDestroy();
    }

    @Override
    public void onBackKeyPressed() {

        Intent backHome = new Intent(Intent.ACTION_MAIN);
        backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        backHome.addCategory(Intent.CATEGORY_HOME);
        startActivity(backHome);
    }

    @Override
    public void onNetworkStateChanged(int type, boolean isAvailable) {

    }


    private class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabEntities.get(position).getTabTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    /**
     * 检测App运行需要权限
     *
     * @param permission
     */
    private void checkPermission(final String... permission) {
        AndPermission.with(this)
                .runtime()
                .permission(permission)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> strings) {

                        if (strings.size() == permission.length) {
                            /* 检测Xposed的运行环境*/
                            boolean hasInstall = XposedManager.checkHasInstallXposed(MainActivity.this, XposedManager.VirtualXposed[0]);
                            if (false) {
                                commonDilalog.setCommonDilalogListener(new CommonDilalog.CommonDilalogListener() {
                                    @Override
                                    public void onTopCancel(CommonDilalog dilalog) {
                                        dilalog.dismiss();
                                        WechatPluginApplication.getApplication().exit();
                                    }

                                    @Override
                                    public void onCancel(CommonDilalog dilalog) {
                                        dilalog.dismiss();
                                        WechatPluginApplication.getApplication().exit();
                                    }

                                    @Override
                                    public void onConfim(CommonDilalog dilalog) {
                                        dilalog.dismiss();
                                        XposedManager.installXposed(MainActivity.this, XposedManager.VirtualXposed[1], new FileTransfer.FileTransferListener() {
                                            @Override
                                            public void copy(int progress) {

                                            }

                                            @Override
                                            public void complete(boolean success, Object bundle) {
                                                AppUtil.installApk(MainActivity.this, XposedManager.dest + "/" + XposedManager.VirtualXposed[1]);
                                            }
                                        });

                                    }
                                });
                                commonDilalog.show("检测到系统未安装Xposed,是否安装?", Color.parseColor("#333333"),
                                        Gravity.LEFT | Gravity.CENTER_VERTICAL,
                                        "安装");
                            }
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> strings) {

                        commonDilalog.setCommonDilalogListener(new CommonDilalog.CommonDilalogListener() {
                            @Override
                            public void onTopCancel(CommonDilalog dilalog) {
                                dilalog.dismiss();
                                WechatPluginApplication.getApplication().exit();
                            }

                            @Override
                            public void onCancel(CommonDilalog dilalog) {
                                dilalog.dismiss();
                                WechatPluginApplication.getApplication().exit();
                            }

                            @Override
                            public void onConfim(CommonDilalog dilalog) {
                                checkPermission(Permission.Group.STORAGE);
                            }
                        });

                        commonDilalog.show("我们需要获取您的部分权限，否则将无法正常使用小晓微信助手。", Color.parseColor("#333333"),
                                Gravity.LEFT | Gravity.CENTER_VERTICAL,
                                "确认");
                    }
                }).start();
    }


    @Override
    public void onHttpSucceed(int what, SimpleResult ret, Response response) {
        super.onHttpSucceed(what, ret, response);
        if (ret == null) return;
    }

    @Override
    public void onHttpFailed(int what, SimpleResult ret, Response response) {
        super.onHttpFailed(what, ret, response);
        if (ret != null) {
            showMsg(ret.getMessage());
        }
    }

    @Override
    public void onHttpFinish(int what) {
        super.onHttpFinish(what);
    }


    /**
     * 更新配置
     */
    public void updateSettingConfig(String wxID) {
        PluginClient.tansferMessage(new PluginMessasge(
                PluginMessasge.NOTIFY_UPDATE_COOKIE,
                PluginHandler.cookie
        ));
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Update_Setting_Config.getURL(), URLManager.Update_Setting_Config.method, SimpleResult.class);
        JSONObject config = JSON.parseObject(JSON.toJSONString(PluginHandler.cookie.getPermission()));
        config.put("wx_id", wxID);
        request.setRequestBodyAsJson(config);
        addTask2Queue(
                URLManager.Update_Setting_Config.action,
                request
        );
    }


}
