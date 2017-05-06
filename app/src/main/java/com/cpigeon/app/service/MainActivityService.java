package com.cpigeon.app.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cpigeon.app.BuildConfig;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.modular.usercenter.view.activity.LoginActivity;
import com.cpigeon.app.service.databean.UseDevInfo;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

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

    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

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
                    if (onDeviceLoginCheckListener != null)
                        onDeviceLoginCheckListener.onOtherDeviceLogin(useDevInfo);
                    Date time = DateTool.strToDateTime(useDevInfo.getTime());
                    StringBuilder builder = new StringBuilder();
                    builder.append("您的账号于")
                            .append(dateTimeFormat.format(time))
                            .append("在另一台")
                            .append(useDevInfo.getType())
                            .append(TextUtils.isEmpty(useDevInfo.getDevinfo()) ? "" : "(" + useDevInfo.getDevinfo() + ")")
                            .append("设备登录。如非本人操作，则密码可能已泄漏，建议尽快修改密码。");
                    SweetAlertDialog dialog = new SweetAlertDialog(AppManager.getAppManager().getTopActivity(), SweetAlertDialog.WARNING_TYPE);
//                    dialog.getWindow().setType(WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW);
                    dialog.setTitleText("下线通知")
                            .setContentText(builder.toString())
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent intent = new Intent(AppManager.getAppManager().getTopActivity(), LoginActivity.class);
                                    AppManager.getAppManager().getTopActivity().startActivity(intent);
                                }
                            });
                    dialog.setCancelable(false);
                    dialog.show();
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
