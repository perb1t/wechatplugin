package com.xiezhiai.wechatplugin.model.wechat;

import android.os.Environment;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/12.
 *
 * @Desc: 设备当前安装微信Apk信息
 */
public class Wechat {

    /* 本地最后一次登录的微信用户 */
    public static UserInfo loginUser = new UserInfo();
    public static List<String> wxClasses = new ArrayList<String>();

    public static final String PACKAGE_NAME = "com.tencent.mm";
    public static final String MM_PREFERENCES = "com.tencent.mm_preferences";
    public static final String AUTH_INFO_KEY_PREFS = "auth_info_key_prefs";

    public static String versionName;
    public static String versionNumber;
    public static int versionCode;

    /**
     * 解析Sns
     */
    public static String PROTOCAL_SNS_DETAIL_CLASS = "com.tencent.mm.protocal.b.atp";
    public static String PROTOCAL_SNS_DETAIL_METHOD = "a";
    public static String SNS_XML_GENERATOR_CLASS = "com.tencent.mm.plugin.sns.f.i";
    public static String SNS_XML_GENERATOR_METHOD = "a";
    public static String PROTOCAL_SNS_OBJECT_CLASS = "com.tencent.mm.protocal.b.aqi";
    public static String PROTOCAL_SNS_OBJECT_METHOD = "a";
    public static String PROTOCAL_SNS_OBJECT_USERID_FIELD = "iYA";
    public static String PROTOCAL_SNS_OBJECT_NICKNAME_FIELD = "jyd";
    public static String PROTOCAL_SNS_OBJECT_TIMESTAMP_FIELD = "fpL";
    public static String PROTOCAL_SNS_OBJECT_COMMENTS_FIELD = "jJX";
    public static String PROTOCAL_SNS_OBJECT_LIKES_FIELD = "jJU";
    public static String SNS_OBJECT_EXT_AUTHOR_NAME_FIELD = "jyd";
    public static String SNS_OBJECT_EXT_REPLY_TO_FIELD = "jJM";
    public static String SNS_OBJECT_EXT_COMMENT_FIELD = "fsI";
    public static String SNS_OBJECT_EXT_AUTHOR_ID_FIELD = "iYA";
    public static String SNS_DETAIL_FROM_BIN_METHOD = "am";
    public static String SNS_OBJECT_FROM_BIN_METHOD = "am";


    /**
     * 数据库相关
     */
    public static String WECHAT_PACKAGE_SQLITE;
    public Class<?> SQLiteDatabase;
    public Class<?> SQLiteCursorFactory;
    public Class<?> DatabaseErrorHandler;
    public Class<?> CancellationSignal;

    /**
     * 红包相关
     */
    public static boolean hasTimingIdentifier = true;
    public Class<?> LuckyMoneyReceiveUI;
    public Class<?> ReceiveUIParamName;
    public Class<?> RequestCaller;
    public Class<?> ReceiveLuckyMoneyRequest;
    public Class<?> LuckyMoneyRequest;
    public Class<?> GetTransferRequest;
    public Class<?> NetworkRequest;

    public String ReceiveUIMethod;
    public String RequestCallerMethod;
    public String ReceiveLuckyMoneyRequestMethod;
    public String GetNetworkByModelMethod;


    /**
     * 通讯录
     */
    public  Class<?> AddressAdapter;
    public  Class<?> ConversationWithCacheAdapter;

}

