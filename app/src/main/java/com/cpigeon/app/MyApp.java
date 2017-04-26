package com.cpigeon.app;

import android.app.Application;
import android.content.Context;

import com.cpigeon.app.broadcastreceiver.NetStateReceiver;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.cache.CacheManager;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Cache;
import com.tencent.bugly.crashreport.CrashReport;

import org.xutils.x;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/4/5.
 */

public class MyApp extends Application {
    private static MyApp instance;
    private static String TAG = "AndySong";

    public static MyApp getInstance() {
        return instance;
    }

    public static CpigeonData mCpigeonData;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        instance = this;
        //开启网络广播监听
        NetStateReceiver.registerNetworkStateReceiver(this);
        if (!BuildConfig.DEBUG) {
            //bugly
            CrashReport.initCrashReport(getApplicationContext(), "f7c8c8f49a", BuildConfig.DEBUG);
        } else {
            Logger.init(TAG);
        }
        mCpigeonData = CpigeonData.getInstance();
        //极光推送
//        JPushInterface.setDebugMode(BuildConfig.DEBUG);
//        JPushInterface.init(this);

        CacheManager.setCacheModel(CacheManager.ALL_ALLOW);
        CacheManager.setMemaryCacheTime(30 * 1000);
        CacheManager.setDiskCacheTime(3 * 60 * 1000);
        CacheManager.init(this);
    }

    /**
     * 低内存自动杀死
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        NetStateReceiver.unRegisterNetworkStateReceiver(this);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
