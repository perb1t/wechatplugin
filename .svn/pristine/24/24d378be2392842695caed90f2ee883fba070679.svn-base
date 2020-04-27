package com.xiezhiai.wechatplugin.utils;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

/**
 * Created by shijiwei on 2018/10/30.
 *
 * @Desc:
 */
public class Gallery {

    public static final int ALBUM_RESULT_CODE = 100;

    /**
     * 打开系统相册
     */
    public static void openSysAlbum(Activity context) {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(albumIntent, ALBUM_RESULT_CODE);
    }

}
