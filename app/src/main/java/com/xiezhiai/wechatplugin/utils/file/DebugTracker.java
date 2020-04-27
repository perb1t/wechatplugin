package com.xiezhiai.wechatplugin.utils.file;

import android.util.Log;

import com.xiezhiai.wechatplugin.utils.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Handler;

/**
 * Created by shijiwei on 2018/11/21.
 *
 * @Desc:
 */
public class DebugTracker {

    private static final String TAG = "DebugTracker";

    static final String LN = "\n";
    static final String SPACE = "   ";
    static final String HEADER = "|__ ";
    static final int LEAF_NODE = 0;
    private static StringBuilder track = new StringBuilder();


    /**
     * 打印类继承关系
     *
     * @param cls
     * @return
     */
    public static String printlnClassTrack(Class<?> cls) {
        track.delete(0, track.length());
        track.append(SPACE + LN);
        printlnClassTrack(cls, 0);
        LogUtil.e(TAG + "  打印类继承关系  " + track.toString());
        return track.toString();
    }


    /**
     * 打印内部类
     *
     * @param cls
     */
    public static void printlnInnerClasses(Class<?> cls) {
        if (cls != null) {
            Class<?>[] classes = cls.getDeclaredClasses();
            StringBuilder builder = new StringBuilder();
            builder.append(SPACE + LN);
            builder.append(cls.toString() + LN);
            if (classes != null || classes.length != 0) {
                for (Class c : classes) {
                    builder.append(SPACE + HEADER + c + LN);
                }
            }
            LogUtil.e(TAG + "  打印内部类  " + builder.toString());
        }

    }


    /**
     * 打印成员函数
     *
     * @param cls
     */
    public static void printlnMethods(Class<?> cls) {
        if (cls != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(SPACE + LN);
            builder.append(cls.toString() + LN);
            Method[] declaredMethods = cls.getDeclaredMethods();
            for (Method m : declaredMethods) {
                if (m != null) {
                    m.setAccessible(true);
                    String name = m.getName();
                    Class<?> returnType = m.getReturnType();
                    Class<?>[] parameterTypes = m.getParameterTypes();
                    int modifiers = m.getModifiers();
                    String pt = new String();
                    for (int i = 0; i < parameterTypes.length; i++) {
                        pt += parameterTypes[i].getName() + " , ";
                    }
                    builder.append(SPACE + HEADER + returnType.toString() + "  " + name + "(" + pt + ")" + LN);
                }
            }

            LogUtil.e(TAG + "  打印成员函数  " + builder.toString());

        }
    }

    /**
     * 打印成员变量
     *
     * @param cls
     */
    public static void printlnFields(Class<?> cls) {
        if (cls != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(SPACE + LN);
            builder.append(cls.toString() + LN);
            Field[] declaredFields = cls.getDeclaredFields();
            if (declaredFields != null) {
                for (Field f : declaredFields) {
                    f.setAccessible(true);
                    String name = f.getName();
                    Class<?> type = f.getType();
                    builder.append(SPACE + HEADER + name + " (" + type.getName() + ") " + LN);
                }

                LogUtil.e(TAG + "  打印成员变量  " + builder);
            }
        }

    }

    /**
     * 打印对象的属性
     *
     * @param o
     */
    public static void printObect(Object o) {

        if (o != null) {
            Class<?> cls = o.getClass();
            Field[] declaredFields = cls.getDeclaredFields();
            StringBuilder builder = new StringBuilder();
            builder.append(SPACE + LN);
            builder.append(cls.toString() + LN);
            for (Field f : declaredFields) {
                if (f != null) {
                    try {
                        f.setAccessible(true);
                        Class<?> type = f.getType();
                        String name = f.getName();
                        Object value = f.get(o);
                        builder.append(SPACE + HEADER + type + "  " + name + "  " + value + LN);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            LogUtil.e(TAG + " 打印对象的属性 " + builder.toString());
        }
    }

    private static void printlnClassTrack(Class<?> cls, int depth) {
        if (cls != null) {
            Class<?> superclass = cls.getSuperclass();
            Class<?>[] interfaces = cls.getInterfaces();

            track.append(generateSpaceHeader(depth) + cls + LN);
            if (superclass != null) {
                printlnClassTrack(superclass, depth + 1);
            }

            for (Class interf : interfaces) {
                if (interf != null) {
                    printlnClassTrack(interf, depth + 1);
                }
            }
        }

    }

    private static String generateSpaceHeader(int depth) {
        if (depth <= 0) {
            return "";
        } else {
            String space = "";
            for (int i = 0; i < depth; i++) {
                space += SPACE;
            }
            return depth + space + HEADER;
        }
    }


}
