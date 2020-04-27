package com.xiezhiai.wechatplugin.utils;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by shijiwei on 2018/11/1.
 *
 * @Desc:
 */
public class VerifyUtil {

    /**
     * @param edit
     * @return
     */
    public static Result isEmptyInput(EditText edit) {
        if (edit == null)
            return new Result(true, null);
        if (edit.getText() == null || TextUtils.isEmpty(edit.getText().toString()))
            return new Result(true, null);
        return new Result(false, edit.getText().toString());
    }

    public static class Result {
        public boolean isEmpty;
        public String value;

        public Result(boolean isEmpty, String value) {
            this.isEmpty = isEmpty;
            this.value = value;
        }
    }

    public static String safetyValue(String value) {
        return value == null ? "" : value;
    }
}
