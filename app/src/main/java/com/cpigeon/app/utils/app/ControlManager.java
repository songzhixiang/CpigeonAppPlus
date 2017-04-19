package com.cpigeon.app.utils.app;

import android.content.Context;

import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.WeakHandler;

/**
 * Created by chenshuai on 2017/4/18.
 */

public class ControlManager {

    Context context;
    WeakHandler mHandler;

    public ControlManager(Context context) {
        this.context = context;
        mHandler = new WeakHandler();
    }

    /**
     * 检查当前APP是否为可用版本
     */
    public void checkIsAvailableVersion(final OnCheckedListener onCheckedListener) {
        CallAPI.checkIsAvailableVersion(context, new CallAPI.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                if (onCheckedListener != null) {
                    onCheckedListener.onChecked(data);
                }
            }

            @Override
            public void onError(int errorType, Object data) {
                //延时30秒再检查
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkIsAvailableVersion(onCheckedListener);
                    }
                }, 30000);
            }
        });
    }

    public interface OnCheckedListener {
        /**
         * @param isAvailableVersion 是否为可用版本
         */
        void onChecked(boolean isAvailableVersion);
    }
}
