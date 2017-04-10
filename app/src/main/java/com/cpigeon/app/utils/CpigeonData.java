package com.cpigeon.app.utils;

import android.content.Context;
import android.util.Log;


import com.cpigeon.app.modular.usercenter.model.bean.CpigeonUserServiceInfo;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 * 中鸽数据 统一
 */
public class CpigeonData {
    public static final int USER_SIGN_STATUS_NONE = -1;
    public static final int USER_SIGN_STATUS_SIGNED = 1;
    public static final int USER_SIGN_STATUS_NOT_SIGN = 0;
    private static CpigeonData mCpigeonData;
    private int userScore = 0;
    private int userId = 0;
    private String userBindPhone = "";
    private double userBalance = 0d;
    private CpigeonUserServiceInfo userFootSearchServiceInfo;
    private List<OnWxPayListener> onWxPayListenerList;
    private UserInfo.DataBean mCurrUserInfo;
    private List<OnDataChangedListener> onDataChangedListenerList;
    // private WeakReference<List<OnWxPayListener>> onWxPayListenerListRef;

    private int mSignStatus = USER_SIGN_STATUS_NONE;

    private CpigeonData() {
    }

    public static CpigeonData getInstance() {
        if (mCpigeonData == null) {
            synchronized (CpigeonData.class) {
                if (mCpigeonData == null) {
                    mCpigeonData = new CpigeonData();
                }
            }
        }
        return mCpigeonData;
    }

    /**
     * 初始化数据
     */
    public void initialization() {
        userScore = 0;
        userBalance = 0;
        userId = 0;
        userBindPhone = "";
        userFootSearchServiceInfo = null;
        onWxPayListenerList = null;
        mCurrUserInfo = null;
        mSignStatus = USER_SIGN_STATUS_NONE;
    }

    public List<Integer> getUserServicesIds() {
        List<Integer> data = new ArrayList<>();
        synchronized (this) {
            if (userFootSearchServiceInfo != null)
                data.add(userFootSearchServiceInfo.getServiceId());

        }
        return data;
    }

    /**
     * 获取用户足环查询服务信息
     *
     * @return
     */
    public CpigeonUserServiceInfo getUserFootSearchServiceInfo() {
        synchronized (this) {
            return userFootSearchServiceInfo;
        }
    }

    /**
     * 设置用户足环查询服务信息
     *
     * @param userServiceInfo
     */
    public void setUserFootSearchServiceInfo(CpigeonUserServiceInfo userServiceInfo) {
        synchronized (this) {
            this.userFootSearchServiceInfo = userServiceInfo;
        }
        triggerOnDataChanged();
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public int getUserId(Context context) {
        if (userId != 0)
            return userId;
        synchronized (this) {
            if (userId != 0)
                return userId;
            userId = (int) SharedPreferencesTool.Get(context, "userid", 0, SharedPreferencesTool.SP_FILE_LOGIN);
            if (userId <= 0) {
                //解析token
                try {
                    userId = Integer.valueOf(EncryptionTool.decryptAES((String) SharedPreferencesTool.Get(context, "token", "", SharedPreferencesTool.SP_FILE_LOGIN)).split("\\|")[0]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return userId;
        }
    }

    /**
     * 设置用户ID
     *
     * @param userId
     */
    public void setUserId(int userId) {
        synchronized (this) {
            this.userId = userId;
        }
        triggerOnDataChanged();
    }

    public void setUserInfo(UserInfo.DataBean mCurrUserInfo) {
        synchronized (this) {
            this.mCurrUserInfo = mCurrUserInfo;
        }
        triggerOnDataChanged();
    }

    public UserInfo.DataBean getUserInfo() {
        synchronized (this) {
            return mCurrUserInfo;
        }
    }

    public String getUserBindPhone() {
        synchronized (this) {
            return userBindPhone;
        }
    }

    public void setUserBindPhone(String userBindPhone) {
        synchronized (this) {
            this.userBindPhone = userBindPhone;
        }
        triggerOnDataChanged();
    }

    /**
     * 获取用户积分
     *
     * @return
     */
    public int getUserScore() {
        synchronized (this) {
            return userScore;
        }
    }

    /**
     * 设置用户积分
     *
     * @param userScore
     */
    public void setUserScore(int userScore) {
        synchronized (this) {
            this.userScore = userScore;
        }
        triggerOnDataChanged();
    }

    /**
     * 获取用户余额
     *
     * @return
     */
    public double getUserBalance() {
        synchronized (this) {
            return userBalance;
        }
    }

    /**
     * 设置用户余额
     *
     * @param userBalance
     */
    public void setUserBalance(double userBalance) {
        synchronized (this) {
            this.userBalance = userBalance;
        }
        triggerOnDataChanged();
    }

    /**
     * 初始化微信支付回调引用
     */
    private void initWxPayListenerListRef() {
        if (this.onWxPayListenerList == null) {
            synchronized (CpigeonData.class) {
                if (this.onWxPayListenerList == null) {
                    this.onWxPayListenerList = new ArrayList<>();
                }
            }
        }
    }

    /**
     * 添加微信支付回调
     *
     * @param onWxPayListener
     */
    public void addOnWxPayListener(OnWxPayListener onWxPayListener) {
        initWxPayListenerListRef();
        synchronized (this) {
            this.onWxPayListenerList.add(onWxPayListener);
        }
    }

    /**
     * 移除微信支付回调
     *
     * @param onWxPayListener
     */
    public void removeOnWxPayListener(OnWxPayListener onWxPayListener) {
        initWxPayListenerListRef();
        synchronized (this) {
            this.onWxPayListenerList.remove(onWxPayListener);
        }
    }

    /**
     * 触发微信回调
     *
     * @param wxPayReturnCode
     */
    public void onWxPay(Context context, int wxPayReturnCode) {
        if (context instanceof IWXAPIEventHandler) {
            if (this.onWxPayListenerList == null || onWxPayListenerList == null) return;
            for (OnWxPayListener listener : this.onWxPayListenerList) {
                listener.onPayFinished(wxPayReturnCode);
            }
        } else {
            Log.e("ERROR", "onWxPay called error");
        }
    }

    /**
     * 触发信息修改回调
     */
    private void triggerOnDataChanged() {
        if (onDataChangedListenerList == null) return;
        for (OnDataChangedListener listener : onDataChangedListenerList) {
            listener.OnDataChanged(this);
        }
    }

    /**
     * 初始化数据改变回调引用
     */
    private void initOnDataChangedListRef() {
        if (this.onDataChangedListenerList == null) {
            synchronized (this) {
                if (this.onDataChangedListenerList == null) {
                    this.onDataChangedListenerList = new ArrayList<>();
                }
            }
        }
    }

    /**
     * 添加信息修改回调
     *
     * @param
     */
    public void addOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        initOnDataChangedListRef();
        synchronized (this) {
            this.onDataChangedListenerList.add(onDataChangedListener);
        }
    }

    /**
     * 移除信息修改回调
     *
     * @param
     */
    public void removeOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        initOnDataChangedListRef();
        synchronized (this) {
            this.onDataChangedListenerList.remove(onDataChangedListener);
        }
    }

    /**
     * 获取用户签到状态
     * @return
     */
    public int getUserSignStatus() {
        return mSignStatus;
    }

    /**
     * 设置用户签到状态
     * @param signStatus <code>USER_SIGN_STATUS_NONE</code> <code>USER_SIGN_STATUS_NOT_SIGN</code> <code>USER_SIGN_STATUS_SIGNED</code>
     */
    public void setUserSignStatus(int signStatus) {
        if (signStatus != USER_SIGN_STATUS_NONE || signStatus != USER_SIGN_STATUS_NOT_SIGN || signStatus != USER_SIGN_STATUS_SIGNED) {
            return;
        }
        synchronized (this) {
            this.mSignStatus = signStatus;
        }
    }

    public interface OnDataChangedListener {
        void OnDataChanged(CpigeonData cpigeonData);
    }

    public interface OnWxPayListener {
        int ERR_OK = 0;
        int ERR_COMM = -1;
        int ERR_USER_CANCEL = -2;
        int ERR_SENT_FAILED = -3;
        int ERR_AUTH_DENIED = -4;
        int ERR_UNSUPPORT = -5;
        int ERR_BAN = -6;

        /**
         * @param wxPayReturnCode <see>com.tencent.mm.sdk.modelbase.BaseResp.ErrCode</see>
         */
        void onPayFinished(int wxPayReturnCode);
    }
}