package com.xiezhiai.wechatplugin.utils.image.cache;

import android.graphics.Bitmap;

/**
 * Created by shijiwei on 2018/11/1.
 * https://www.jianshu.com/p/f9cfbea586c2
 *
 * @Desc:
 */
public interface Cache {

    /**
     * Retrieve an image for the specified {@code key} or {@code null}.
     */
    Bitmap get(String key);

    /**
     * Store an image in the cache for the specified {@code key}.
     */
    void put(String key, Bitmap bitmap);

    /**
     * Remove an image in the cache for the specified {@code key}.
     */
    void remove(String key);

    /**
     * Returns the current size of the cache in bytes.
     */
    int size();

    /**
     * Returns the maximum size in bytes that the cache can hold.
     */
    int maxSize();

    /**
     * Clear the cache.
     */
    void clear();

}