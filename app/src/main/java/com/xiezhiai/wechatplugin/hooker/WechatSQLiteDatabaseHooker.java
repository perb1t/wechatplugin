package com.xiezhiai.wechatplugin.hooker;

import android.content.ContentValues;
import android.database.Cursor;

import com.xiezhiai.wechatplugin.core.Config;
import com.xiezhiai.wechatplugin.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.xiezhiai.wechatplugin.utils.LogUtil.log;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by shijiwei on 2018/10/12.
 *
 * @Desc:
 */
public class WechatSQLiteDatabaseHooker implements IHooker {

    /* 微信朋友圈数据库 */
    public static Object xSnsSQLiteDatabase;
    /* 微信通用数据库 */
    public static Object xEnSQLiteDatabase;

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {

        findAndHookMethod(
                Config.xWechat.SQLiteDatabase,
                "openDatabase",
                String.class, Config.xWechat.SQLiteCursorFactory, int.class, Config.xWechat.DatabaseErrorHandler,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        Object caller = param.thisObject;
                        String path = (String) param.args[0];
                        Object factory = param.args[1];
                        int flags = (int) param.args[2];
                        Object handler = param.args[3];
                        Object db = param.getResult();

                        LogUtil.log("微信数据库打开：" + path);

                        if (path.endsWith("SnsMicroMsg.db")) {
                            xSnsSQLiteDatabase = db;
                        }
                    }
                }
        );

        findAndHookMethod(
                Config.xWechat.SQLiteDatabase,
                "updateWithOnConflict",
                String.class, ContentValues.class, String.class, String[].class, int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

//                        val thisObject        = param.thisObject
//                        val table             = param.args[0] as String
//                        val values            = param.args[1] as ContentValues
//                        val whereClause       = param.args[2] as String?
//                        val whereArgs         = param.args[3] as Array<String>
//                        val conflictAlgorithm = param.args[4] as Int
//                        val result            = param.result as Int

                        Object thisObject = param.thisObject;
                        String table = (String) param.args[0];

                        if (thisObject.toString().endsWith("EnMicroMsg.db")) {
                            xEnSQLiteDatabase = thisObject;
//                            getDatabaseTableNames(thisObject);
                        }

//                        LogUtil.log("微信数据库更新：" + thisObject.toString() + " . " + table);

                    }
                });

        findAndHookMethod(
                Config.xWechat.SQLiteDatabase,
                "queryWithFactory",
                Config.xWechat.SQLiteCursorFactory, boolean.class, String.class, String[].class, String.class, String[].class,
                String.class, String.class, String.class, String.class, Config.xWechat.CancellationSignal,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Object thisObject = param.thisObject;
                        String table = (String) param.args[2];
                        String[] columns = (String[]) param.args[3];
                        String selection = (String) param.args[4];
                        String[] selectionArgs = (String[]) param.args[5];

                        if (thisObject.toString().endsWith("EnMicroMsg.db")) {
                            xEnSQLiteDatabase = thisObject;
                        }

//                        LogUtil.log("微信数据库查询：" + thisObject.toString() + " . "
//                                + "\n| table: " + table + "  columns: " + LogUtil.splitJoint(columns) + "  selection: " + selection + " selectionArgs: " + LogUtil.splitJoint(selectionArgs)
//                        );


                    }
                }
        );

    }

    @Override
    public void release() {

    }


    /**
     * 获取数据库文件的table列表
     *
     * @param db
     * @return
     */
    private List<String> printDatabaseTableNames(Object db) {
        List<String> tableNames = new ArrayList<>();
        Object cursor = XposedHelpers.callMethod(db, "query",
                "sqlite_master", new String[]{"name"}, "type = ?", new String[]{"table"},
                null, null, null, null);

        while ((boolean) XposedHelpers.callMethod(cursor, "moveToNext")) {
            String table = (String) XposedHelpers.callMethod(cursor, "getString", 0);
            tableNames.add(table);
        }
        String[] a = new String[tableNames.size()];
        tableNames.toArray(a);
        LogUtil.log("微信数据库表名查询 " + db.toString() + " : \n" + LogUtil.splitJoint(a));
        return tableNames;
    }

    /**
     * 微信数据库表打印
     *
     * @param table
     */
    public static void printTable(String table) {
        Object db = WechatSQLiteDatabaseHooker.xEnSQLiteDatabase;
        if (db != null) {
            try {
                Object cursor = callMethod(db, "query",
                        table,
                        null, null, null,
                        null, null, null, null);
                String[] columnNames = (String[]) callMethod(cursor, "getColumnNames");
                int count = (int) callMethod(cursor, "getCount");
                log("微信数据库表打印 | " + table + " | 表数据条数 ：" + count + "  表字段 ： " + LogUtil.splitJoint(columnNames));
                while ((boolean) callMethod(cursor, "moveToNext")) {
                    for (int i = 0; i < columnNames.length; i++) {
                        int type = (int) callMethod(cursor, "getType", i);
                        log("微信数据库表打印 " + columnNames[i] + " | type =  " + type);
                        if (Cursor.FIELD_TYPE_STRING == type) {
                            log("微信数据库表打印 ： " + columnNames[i] + " = " + callMethod(cursor, "getString", i));
                        }
                    }
                }
            } catch (Exception e) {
                log("微信数据库表打印 ：" + e.getMessage());
            } finally {
            }
        }
    }

}
