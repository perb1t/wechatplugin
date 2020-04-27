package com.xiezhiai.wechatplugin.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleResult;
import com.xiezhiai.wechatplugin.func.nohttp.interfaces.HttpResponseListener;
import com.xiezhiai.wechatplugin.func.nohttp.interfaces.RequestListener;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.ServerError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.permission.Setting;

import java.net.ProtocolException;

/**
 * Created by shijiwei on 2017/9/8.
 *
 * @VERSION 1.0
 */

public abstract class BaseFragment extends Fragment implements RequestListener {

    private static final String TAG = "BaseFragment";

    public RequestQueue mQueue;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResId(), container, false);
            mQueue = NoHttp.newRequestQueue();
            initialData(savedInstanceState);
            initialView(rootView);
            initialEvn();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) parent.removeView(rootView);

        return rootView;
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
    public abstract void initialView(View view);

    /**
     * Initializes the view widget response callback event,invoked in the onCreate () of the life cycle
     */
    public abstract void initialEvn();


    /**
     * Add network requests task to queues
     *
     * @param what
     * @param request
     */
    public void addTask2Queue(int what, Request request) {

        request.setCancelSign(this);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        mQueue.add(what, request, new HttpResponseListener(this));
    }


    @Override
    public void onHttpStart(int what) {

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
            LogUtil.e(TAG, "| action: " + what + " | code: " + ret.getCode() + " | message： " + ret.getMessage() + " | data: " + ret.getData());
            if (ret.getCode() == -3) {
                ((BaseActivity) getActivity()).logout(true);
            }
        }
    }

    @Override
    public void onHttpFailed(int what, Response response) {

        SimpleResult ret = (SimpleResult) response.get();
        onHttpFailed(what, ret, response);

//        Exception exception = response.getException();
//        if (exception instanceof NetworkError) {// 网络不好
//            Toast.makeText(getActivity(), getResources().getString(R.string.net_erro), Toast.LENGTH_SHORT);
//        } else if (exception instanceof TimeoutError) {// 请求超时
//            Toast.makeText(getActivity(), getResources().getString(R.string.request_out_time), Toast.LENGTH_SHORT);
//        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
//            Toast.makeText(getActivity(), getResources().getString(R.string.not_found_server), Toast.LENGTH_SHORT);
//        } else if (exception instanceof URLError) {// URL是错的
//            Toast.makeText(getActivity(), getResources().getString(R.string.url_erro), Toast.LENGTH_SHORT);
//        } else if (exception instanceof NotFoundCacheError) {
//            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
//        } else if (exception instanceof ProtocolException) {
//
//        } else if (exception instanceof ServerError) {
//        } else {
//
//        }

    }

    public void onHttpFailed(int what, SimpleResult ret, Response response) {

        if (ret == null) {
            showMsg("系统异常!");
        } else {
            LogUtil.e(TAG, "| action: " + what + " | code: " + ret.getCode() + " | message： " + ret.getMessage() + " | data: " + ret.getData());
        }
    }

    @Override
    public void onHttpFinish(int what) {

    }

    public void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public Dialog getLooadingDialog() {
        return ((BaseActivity) getActivity()).loadingDialog;
    }

    /***
     * 显示软键盘
     * @param view
     */
    public void showSoftInput(View view) {
        ((BaseActivity) getActivity()).showSoftInput(view);
    }
}
