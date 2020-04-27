package com.xiezhiai.wechatplugin.ui.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleResult;
import com.xiezhiai.wechatplugin.func.nohttp.interfaces.HttpResponseListener;
import com.xiezhiai.wechatplugin.func.nohttp.interfaces.RequestListener;
import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.ui.app.WechatPluginApplication;
import com.xiezhiai.wechatplugin.utils.DensityUtils;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.utils.PluginLoginManager;
import com.xiezhiai.wechatplugin.utils.network.NetworkUtility;
import com.xiezhiai.wechatplugin.utils.others.StatusBarCompat;
import com.xiezhiai.wechatplugin.widget.CommonDilalog;
import com.xiezhiai.wechatplugin.widget.LoadingDialog;
import com.xiezhiai.wechatplugin.widget.UserAccountDialog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by shijiwei on 2017/8/31.
 *
 * @VERSION 1.0
 */

public abstract class BaseActivity extends AppCompatActivity implements
        NetworkUtility.OnNetworkChangedListener,
        RequestListener {

    private static final String TAG = "BaseActivity";

    private WechatPluginApplication mApplication;
    private NetworkUtility mNetworkUtility;

    public RequestQueue mQueue;

    private boolean isNetworkAvailable;
    public UserAccountDialog userAccountDialog;
    public LoadingDialog loadingDialog;
    public CommonDilalog commonDilalog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBar(this, true, true);
        StatusBarCompat.setStatusTextColor(true, this);
        DensityUtils.setOrientation(this, DensityUtils.WIDTH);
        setContentView(getLayoutResId());
        mApplication = WechatPluginApplication.getApplication();
        mApplication.addAct2Stack(this);
        mNetworkUtility = new NetworkUtility(this, this);
        userAccountDialog = new UserAccountDialog(this);
        loadingDialog = new LoadingDialog(this);
        commonDilalog = new CommonDilalog(this);
        mQueue = NoHttp.newRequestQueue();
        initialData(savedInstanceState);
        initialView();
        initialEvn();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        loadingDialog.dismiss();
        userAccountDialog.dismiss();
        mNetworkUtility.unregisterReceiver(this);
        mApplication.removeActFromStack(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
            /* 切换为竖屏 */
            LogUtil.e(TAG + " 切换为竖屏");
        } else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            /* 切换为横屏 */
            LogUtil.e(TAG + " 切换为横屏");

        }
    }

    @Override
    public void onBackPressed() {
        onBackKeyPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View focus = getWindow().getDecorView().findFocus();
            if (isShouldHideKeyboard(focus, event)) {
                hideSoftInput(getWindow().getDecorView());
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Gets the resource id of the layout file for the current page
     * invoked in the onCreate () of the life cycle
     *
     * @return
     */
    public abstract int getLayoutResId();

    /**
     * Initialize data and declare data collections,invoked in the onCreate () of the life cycle
     */
    public abstract void initialData(Bundle savedInstanceState);

    /**
     * Initialize the view widget,invoked in the onCreate () of the life cycle
     */
    public abstract void initialView();

    /**
     * Initializes the view widget response callback event,invoked in the onCreate () of the life cycle
     */
    public abstract void initialEvn();

    /**
     * Interceptor system returns a key event
     */
    public abstract void onBackKeyPressed();

    /**
     * Monitor the network status of the device
     */
    public abstract void onNetworkStateChanged(int type, boolean isAvailable);


    @Override
    public void onNetworkChanged(int type, boolean isAvailable) {
        isNetworkAvailable = isAvailable;
        onNetworkStateChanged(type, isAvailable);
    }


    @Override
    public void onHttpStart(int what) {
        LogUtil.i(TAG + " onHttpStart ", " " + what);
    }

    @Override
    public void onHttpSucceed(int what, Response response) {
        SimpleResult ret = (SimpleResult) response.get();
        onHttpSucceed(what, ret, response);
    }

    public void onHttpSucceed(int what, SimpleResult ret, Response response) {
        if (ret == null) {
            showMsg("系统异常!");
        } else {
            LogUtil.i(TAG + " onHttpSucceed ", "| action: " + what + "  | code: " + ret.getCode() + "  | message: " + ret.getMessage() + "  | data: " + ret.getData());
            if (ret.getCode() == -3) {
                showMsg(ret.getMessage());
                logout(true);
            }
        }
    }

    @Override
    public void onHttpFailed(int what, Response response) {

        SimpleResult ret = (SimpleResult) response.get();
        onHttpFailed(what, ret, response);
    }

    public void onHttpFailed(int what, SimpleResult ret, Response response) {
        if (ret == null) {
            showMsg("系统异常!");
        } else {
            LogUtil.e(TAG + " onHttpFailed ", "| action: " + what + "  | code: " + ret.getCode() + "  | message: " + ret.getMessage() + "  | data: " + ret.getData());
        }
    }

    @Override
    public void onHttpFinish(int what) {
        LogUtil.e(TAG + " onHttpFinish ", " " + what);
        loadingDialog.dismiss();
    }


    /**
     * Add network requests task to queues
     *
     * @param what
     * @param request
     */
    public void addTask2Queue(int what, Request request) {
        addTask2Queue(what, request, false);
    }

    public void addTask2Queue(int what, Request request, boolean showLoadingDialog) {
        if (showLoadingDialog) loadingDialog.show();
        request.setCancelSign(this);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        mQueue.add(what, request, new HttpResponseListener(this));
    }

    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /***
     * 显示软键盘
     * @param view
     */
    public void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getRawX() > left && event.getRawY() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }


    /**
     * 退出登录
     *
     * @param isNeedCancelRequest
     */
    public void logout(boolean isNeedCancelRequest) {
        PluginLoginManager.updateLoginStatus(this, false);
        PluginHandler.logout();
        if (isNeedCancelRequest)
            mQueue.cancelAll();
    }

}
