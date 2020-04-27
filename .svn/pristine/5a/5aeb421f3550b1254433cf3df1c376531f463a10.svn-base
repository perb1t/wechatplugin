package com.xiezhiai.wechatplugin.func.nohttp.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.model.xiezhi.WXUser;
import com.xiezhiai.wechatplugin.utils.encrypt.MD5;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.util.Iterator;

/**
 * Created by shijiwei on 2017/8/1.
 */
public class SimpleRequest<Result> extends RestRequest<Result> {

    private static final String TAG = "SimpleRequest";

    private Class<Result> clz;

    public SimpleRequest(String url, Class<Result> clz) {
        this(url, RequestMethod.POST, clz);
    }

    public SimpleRequest(String url, RequestMethod requestMethod, Class<Result> clz) {

        super(url, requestMethod);
        this.clz = clz;
        WXUser inServiceWXUser = PluginHandler.getInServiceWXUser();
        setHeader(inServiceWXUser == null ? null : inServiceWXUser.getId(),
                PluginHandler.cookie.getUserID(),
                PluginHandler.cookie.getTenantID(),
                PluginHandler.cookie.getSign());

    }

    @Override
    public Result parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String responseString = StringRequest.parseResponseString(responseHeaders, responseBody);
        return JSON.parseObject(responseString, clz);
    }

    /**
     * 添加请求参数 [javabean，map，json，collection]
     *
     * @param parameter
     */
    public void setRequestParameters(Object parameter) {
        if (parameter == null) return;
        JSONObject params = (JSONObject) JSON.toJSON(parameter);
        Iterator<?> it = params.keySet().iterator();
        String value;
        String key;
        while (it.hasNext()) {
            key = it.next().toString();
            value = params.getString(key);
            add(key, value);
        }
    }

    /**
     * 多类型参数[file,image]
     *
     * @return
     */
    public SimpleRequest setMultipartFormEnable() {
        setMultipartFormEnable(true);

        //表单提交，添加请求参数的方式：

        //this.add("name", "yoldada") // String类型
        //this.add("age", 18) // int类型
        //this.add("sex", '0') // char类型
        //this.add("time", 16346468473154) // long类型
        // 添加Bitmap
        //this.add("image1", new BitmapBinary(bitmap))
        // 添加File
//        this.add("file1", new FileBinary(file))
        // 添加ByteArray
        //this.add("file2", new ByteArrayBinary(byte[]))
        // 添加InputStream
        //this.add("file3", new InputStreamBinary(inputStream));
        return this;
    }

    public void setRequestBodyAsJson(Object requestParam) {
        this.setConnectTimeout(30 * 1000);
        this.setContentType("application/json");
        if (requestParam != null)
            this.setDefineRequestBodyForJson(JSON.toJSONString(requestParam));
    }

    /**
     * 设置请求头
     *
     * @param userID
     * @param tenanID
     * @param sign
     */
    public void setHeader(String wxID, String userID, String tenanID, String sign) {
        getHeaders().clear();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String salt = MD5.getSalt();
        String timeSign = MD5.getMD5Str(userID + "&" + timestamp + "&" + salt);
        addHeader("timestamp", timestamp);
        addHeader("user_id", userID);
        addHeader("tenant_id", tenanID);
        addHeader("wx_id", wxID);
        addHeader("salt", salt);
        addHeader("sign", sign);
        addHeader("time_sign", timeSign);
    }

}
