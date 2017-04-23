package com.cpigeon.app.utils.cache;

import android.util.LruCache;

import java.io.Serializable;

/**
 * Created by chenshuai on 2017/4/22.
 */

public class LruCacheManager {

    private LruCache<String, Entry> lruCache;
    private String TAG = "LruCacheManager";
    private long defaultCacheTime = 30;//默认缓存时间

    public long getDefaultCacheTime() {
        return defaultCacheTime;
    }

    public void setDefaultCacheTime(long millis) {
        this.defaultCacheTime = millis;
    }

    public LruCacheManager() {
        this((int) Runtime.getRuntime().maxMemory() / 1024 / 8);
    }

    //设置自定义大小的LruCache
    public LruCacheManager(int maxSize) {
        lruCache = new LruCache<String, Entry>(maxSize) {

            @Override
            protected int sizeOf(String key, Entry value) {
                return super.sizeOf(key, value);
            }
        };
    }

    /**
     * 写入索引key对应的缓存
     *
     * @param key 索引
     * @return 写入结果
     */
    public <T extends Object> T putCache(String key, T data) {
        return putCache(key, data, defaultCacheTime);
    }

    public <T extends Object> T putCache(String key, T data, long cacheTime) {
        T val = getCache(key);
        if (val == null) {
            if (lruCache != null && data != null) {
                Entry entry = new Entry(key, data);
                entry.cacheTime = cacheTime;
                entry.createTime = System.currentTimeMillis();
                val = (T) lruCache.put(key, entry);
            }
        }
        return val;
    }

    /**
     * 获取缓存
     *
     * @param key 索引key对应的缓存
     * @return 缓存
     */
    public <T extends Object> T getCache(String key) {
        if (lruCache != null) {
            Entry entry = lruCache.get(key);
            if (entry != null && entry.isExpired()) {
                lruCache.remove(key);
                return null;
            }
            return entry == null ? null : (T) entry.data;
        }
        return null;
    }

    public void deleteCache() {
        if (lruCache != null)
            lruCache.evictAll();
    }

    public void removeCache(String key) {
        if (lruCache != null)
            lruCache.remove(key);
    }

    public int size() {
        int size = 0;
        if (lruCache != null)
            size += lruCache.size();
        return size;
    }

    private final class Entry {
        private Object data;
        private String key;
        private long cacheTime;
        private long createTime;

        public Entry(String key) {
            this.key = key;
        }

        public Entry(String key, Object data) {
            this.data = data;
            this.key = key;
        }

        /**
         * 是否过期
         *
         * @return
         */
        public boolean isExpired() {
            if (cacheTime < 0) return false;
            System.out.println(TAG + " " + cacheTime + " " + createTime + " " + (System.currentTimeMillis()));
            return System.currentTimeMillis() - createTime > cacheTime;
        }
    }
}