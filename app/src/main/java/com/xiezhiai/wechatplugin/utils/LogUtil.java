package com.xiezhiai.wechatplugin.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by shijiwei on 2018/10/12.
 *
 * @Desc:
 */
public class LogUtil {

    /* Debug状态输出log日志 */
    public static boolean isDebug = true;

    public static String TAG = "邂智科技";

    public static void log(String content) {
        log(TAG, content);
    }

    public static void log(String tag, String content) {
        log(tag, content, false);
    }

    public static void log(String tag, String content, boolean logXP) {
        if (isDebug) {
            Log.e(tag, content);
            if (logXP) XposedBridge.log(tag + " : " + content);
        }
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    public static void w(String msg) {
        if (isDebug)
            Log.w(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }

    /**
     * 字符数组拼接成字符串
     *
     * @param args
     * @return
     */
    public static String splitJoint(String[] args) {
        String result = "";
        if (args != null && args.length != 0) {
            for (String arg : args) {
                result += "，" + arg;
            }
            result = result.substring(1, result.length());
        }
        return result;
    }


    /**
     * @param keys
     * @param values
     * @return
     */
    public static String splitJointKeyValue(String[] keys, String[] values) {
        String result = "";
        if (keys == null || keys.length == 0 || values == null || values.length == 0) {
            result = "keys or values is null";
        } else {
            for (int i = 0; i < keys.length; i++) {
                result += keys[i] + " = " + values[i] + "，";
            }
        }
        return result;
    }

    public static void logStackTraces(String tag) {
        logStackTraces(tag, 20, 0);
    }

    public static void logStackTraces(String tag, int methodCount, int methodOffset) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String level = "";
        log(tag + "---------logStackTraces start----------");
        for (int i = methodCount; i >= 1; i--) {
            int stackIndex = i + methodOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("|")
                    .append(' ')
                    .append(level)
                    .append(trace[stackIndex].getClassName())
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            level += "   ";
            log(tag + builder.toString());
        }
        log(tag + "---------logStackTraces end----------");
    }

    /**
     * 打印函数的传参
     *
     * @param param
     */
    public static void logMethodParams(XC_MethodHook.MethodHookParam param) {

        StringBuilder buf = new StringBuilder();
        try {
            buf.append(param.method.getDeclaringClass().toString() + "." + param.method.getName() + "()");
            Object[] args = param.args;
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    buf.append((i == 0 ? "\n" : "") + "arg[" + i + "] = " + args[i] + "\n");
                }
            }
        } catch (Exception e) {
            buf.append(" is NULL or it has not params ");
        } finally {
            log(buf.toString());
        }
    }

    /**
     * 打印Activity的Intent传参
     *
     * @param activity
     */
    public static void logActivityIntentExtras(Activity activity) {

        StringBuilder buf = new StringBuilder();
        try {
            Intent intent = activity.getIntent();
            buf.append("当前页面传参 [" + activity.getClass().toString() + "]\n");
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                buf.append(
                        "当前页面传参" +
                                activity.getClass().getName()
                                + " | key = " + key
                                + " | value = " + value
                                + " | class = " + (value == null ? null : value.getClass().getName())
                                + "\n"
                );
            }
        } catch (Exception e) {
            buf.append("当前页面传参 Activity is NULL or it has not extras data ");
        } finally {
            log(buf.toString());
        }
    }

    public static void logIntentExtras(Intent intent) {
        StringBuilder buf = new StringBuilder();
        try {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                buf.append(
                        "intent extras"
                                + " | key = " + key
                                + " | value = " + value
                                + " | class = " + (value == null ? null : value.getClass().getName())
                                + "\n"
                );
            }
        } catch (Exception e) {
            buf.append("Intent is NULL or it has not extras data ");
        } finally {
            log(buf.toString());
        }
    }
}
