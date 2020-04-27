package com.xiezhiai.wechatplugin.func.transfer.dao;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shijiwei on 2018/11/20.
 *
 * @Desc:
 */
public class WXCommonDao<K, V> implements Dao<K, V> {

    private HashMap<K, ArrayList<V>> entry;

    public WXCommonDao() {
        synchronized (Dao.class) {
            if (entry == null) {
                entry = new HashMap<>();
            }
        }
    }

    @Override
    public void put(K key, ArrayList<V> value) {
        synchronized (Dao.class) {
            entry.put(key, value);
        }
    }

    @Override
    public ArrayList<V> get(K key) {
        synchronized (Dao.class) {
            return entry.get(key);
        }
    }

    @Override
    public void remove(K key) {
        synchronized (Dao.class) {
            entry.remove(key);
        }
    }

    @Override
    public int size() {
        synchronized (Dao.class) {
            return entry.size();
        }
    }

    @Override
    public void clear() {
        synchronized (Dao.class) {
            entry.clear();
        }
    }
}
