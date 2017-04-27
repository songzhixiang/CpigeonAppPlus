package com.cpigeon.app.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cpigeon.app.BuildConfig;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.orhanobut.logger.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenshuai on 2017/4/18.
 * 核心服务
 */

public class MainActivityService extends Service {
    ThisBinder binder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null)
            binder = new ThisBinder();
        startSingleLoginCheckTimer();
        return binder;
    }

    OnDeviceLoginCheckListener onDeviceLoginCheckListener;

    public void setOnDeviceLoginCheckListener(OnDeviceLoginCheckListener onDeviceLoginCheckListener) {
        this.onDeviceLoginCheckListener = onDeviceLoginCheckListener;
    }

    Timer mSingleLoginCheckTimer;

    CallAPI.Callback singleLoginCheckCallback = new CallAPI.Callback() {
        @Override
        public void onSuccess(Object data) {
            if (data != null)
                SharedPreferencesTool.Save(getApplicationContext(), "sltoken", data, SharedPreferencesTool.SP_FILE_LOGIN);
        }

        @Override
        public void onError(int errorType, Object data) {
            if (errorType == ERROR_TYPE_API_RETURN) {
                //// TODO: 2017/4/18
                ApiResponse apiResponse = (ApiResponse) data;
                if (20002 == apiResponse.getErrorCode()) {
                    Logger.d("当前账号在新设备上登录并使用");
                    Logger.d(apiResponse.getData());

                    UseDevInfo useDevInfo = ((ApiResponse<UseDevInfo>) apiResponse).getData();
                    if (onDeviceLoginCheckListener != null && onDeviceLoginCheckListener.onOtherDeviceLogin(useDevInfo))
                        stopSingleLoginCheckTimer();
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public void stopSingleLoginCheckTimer() {
        if (mSingleLoginCheckTimer != null) {
            mSingleLoginCheckTimer.cancel();
            mSingleLoginCheckTimer.purge();
            mSingleLoginCheckTimer = null;
        }
    }

    public void startSingleLoginCheckTimer() {

        stopSingleLoginCheckTimer();
        mSingleLoginCheckTimer = new Timer("SingleLoginCheckTimer");
        mSingleLoginCheckTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                boolean res = SharedPreferencesTool.Get(getApplicationContext(), "logined", false, SharedPreferencesTool.SP_FILE_LOGIN);
                res &= SharedPreferencesTool.Get(getApplicationContext(), "userid", 0, SharedPreferencesTool.SP_FILE_LOGIN).equals(
                        Integer.valueOf(EncryptionTool.decryptAES(SharedPreferencesTool.Get(getApplicationContext(), "token", "", SharedPreferencesTool.SP_FILE_LOGIN).toString()).split("\\|")[0]));
                if (!res) {
                    return;
                }
                String devid = CommonTool.getCombinedDeviceID(getApplicationContext());
                String dev = android.os.Build.MODEL;
                String ver = String.valueOf(CommonTool.getVersionCode(getApplicationContext()));
                String appid = BuildConfig.APPLICATION_ID;
                String sltoken = SharedPreferencesTool.Get(getApplicationContext(), "sltoken", "", SharedPreferencesTool.SP_FILE_LOGIN);
                CallAPI.singleLoginCheck(getApplicationContext(), devid, dev, ver, appid, sltoken, singleLoginCheckCallback);
            }
        }, BuildConfig.DEBUG ? 3000 : 0, BuildConfig.DEBUG ? 30000 : 90000);
    }

    /**
     * 使用设备信息
     */
    public class UseDevInfo {

        /**
         * type : Android
         * time : 2017-04-18 14:22:19
         * devinfo : BLT-10
         */

        private String type;
        private String time;
        private String devinfo;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDevinfo() {
            return devinfo;
        }

        public void setDevinfo(String devinfo) {
            this.devinfo = devinfo;
        }

        public String getString() {
            return "UseDevInfo{" +
                    "type='" + type + '\'' +
                    ", time='" + time + '\'' +
                    ", devinfo='" + devinfo + '\'' +
                    '}';
        }
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn,
                               int flags) {//调用bindServer，发现第二个传参就是ServiceConnection对象
        Logger.d("");
        return super.bindService(service, conn, flags);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopSingleLoginCheckTimer();
        return super.onUnbind(intent);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Logger.d("");

        super.unbindService(conn);
    }

    public interface OnDeviceLoginCheckListener {
        boolean onOtherDeviceLogin(UseDevInfo useDevInfo);
    }

    public class ThisBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public MainActivityService getService() {
            return MainActivityService.this;
        }
    }

}
