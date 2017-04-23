package com.cpigeon.app.utils.cache;

/**
 * Created by chenshuai on 2017/4/23.
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


public class DiskLruCacheManager {

    private static int maxSize = 20 * 1024 * 1024;
    private DiskLruCache mDiskLruCache;
    private final static String defaultName = "default";

    public long getDefaultCacheTime() {
        return defaultCacheTime;
    }

    public void setDefaultCacheTime(long millis) {
        this.defaultCacheTime = millis;
        if (mDiskLruCache != null)
            mDiskLruCache.setDefaultCacheTime(defaultCacheTime);
    }

    private long defaultCacheTime = -1;//默认缓存时间，小于0时不检查过期，

    public DiskLruCacheManager(Context context) {
        this(context, defaultName, maxSize);
    }

    public DiskLruCacheManager(Context context, int maxDiskLruCacheSize) {
        this(context, defaultName, maxDiskLruCacheSize);
    }

    public DiskLruCacheManager(Context context, String dirName) {
        this(context, dirName, maxSize);
    }

    public DiskLruCacheManager(Context context, String dirName, int maxDiskLruCacheSize) {
        try {
            mDiskLruCache = DiskLruCache.open(getDiskCacheFile(context, dirName), getAppVersion(context), 1, maxDiskLruCacheSize);
            mDiskLruCache.setDefaultCacheTime(defaultCacheTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件夹地址，如果不存在，则创建
     *
     * @param context 上下文
     * @param dirName 文件名
     * @return File 文件
     */
    private File getDiskCacheFile(Context context, String dirName) {
        File cacheDir = packDiskCacheFile(context, dirName);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    /**
     * 获取文件夹地址
     *
     * @param context 上下文
     * @param dirName 文件名
     * @return File 文件
     */
    private File packDiskCacheFile(Context context, String dirName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + dirName);
    }

    /**
     * 获取当前应用程序的版本号。
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    private String Md5(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    /**
     * 将缓存记录同步到journal文件中。
     */
    public void fluchCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取硬盘缓存
     *
     * @param key 所有
     * @return 缓存
     */
    public <T> T getDiskCache(String key) {
        String md5Key = Md5(key);
        T data = null;
        try {
            if (mDiskLruCache != null) {
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(md5Key);
                if (snapshot != null) {
                    InputStream input = snapshot.getInputStream(0);
                    ObjectInputStream objectInputStream = new ObjectInputStream(input);
                    data = (T) objectInputStream.readObject();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 设置key对应的缓存
     *
     * @param key  索引
     * @param data String格式数据
     * @return 是否写入
     */
    public boolean putDiskCache(String key, Object data, long cacheTime) {
        String md5Key = Md5(key);
        try {
            if (mDiskLruCache != null) {
                if (mDiskLruCache.get(md5Key) != null) {
                    return true;
                }
                DiskLruCache.Editor editor = mDiskLruCache.edit(md5Key, cacheTime);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(data);
                    if (outputStream != null) {
                        editor.commit();
                        return true;
                    } else {
                        editor.abort();
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public boolean putDiskCache(String key, Object data) {
        String md5Key = Md5(key);
        try {
            if (mDiskLruCache != null) {
                if (mDiskLruCache.get(md5Key) != null) {
                    return true;
                }
                DiskLruCache.Editor editor = mDiskLruCache.edit(md5Key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(data);
                    if (outputStream != null) {
                        editor.commit();
                        return true;
                    } else {
                        editor.abort();
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void deleteDiskCache() {
        try {
            if (mDiskLruCache != null) {
                mDiskLruCache.delete();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void removeDiskCache(String key) {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.remove(key);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void deleteFile(Context context, String dirName) {
        try {
            DiskLruCache.deleteContents(packDiskCacheFile(context, dirName));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int size() {
        int size = 0;
        if (mDiskLruCache != null) {
            size = (int) mDiskLruCache.size();
        }
        return size;
    }

    public void close() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}