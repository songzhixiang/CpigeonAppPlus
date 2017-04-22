package com.cpigeon.app.utils;

import android.content.Context;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;


import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IGetUserBandPhone;
import com.cpigeon.app.commonstandard.model.daoimpl.GetUserBandPhoneImpl;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonUserServiceInfo;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import static com.cpigeon.app.MyApp.mCpigeonData;

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
    private List<WeakReference<OnWxPayListener>> onWxPayListenerList;
    private UserInfo.DataBean mCurrUserInfo;
    private List<WeakReference<OnDataChangedListener>> onDataChangedListenerList;
    // private WeakReference<List<OnWxPayListener>> onWxPayListenerListRef;

    private int mSignStatus = USER_SIGN_STATUS_NONE;
    //标记是否可以调用更新数据回调
    boolean dataIsChanged = false;

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
        userBalance = 0d;
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
            dataIsChanged = this.userFootSearchServiceInfo == null || !this.userFootSearchServiceInfo.equals(userServiceInfo);
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
            dataIsChanged = this.userId != userId;
            this.userId = userId;
        }
        triggerOnDataChanged();
    }

    public void setUserInfo(UserInfo.DataBean mCurrUserInfo) {
        synchronized (this) {
            dataIsChanged = this.mCurrUserInfo == null || !this.mCurrUserInfo.equals(mCurrUserInfo);
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
            dataIsChanged = TextUtils.isEmpty(this.userBindPhone) || !this.userBindPhone.equals(userBindPhone);
            this.userBindPhone = userBindPhone;
        }
        triggerOnDataChanged();
    }

    /**
     * 获取用户鸽币
     *
     * @return
     */
    public int getUserScore() {
        synchronized (this) {
            return userScore;
        }
    }

    /**
     * 设置用户鸽币
     *
     * @param userScore
     */
    public void setUserScore(int userScore) {
        synchronized (this) {
            dataIsChanged = this.userScore != userScore;
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
            dataIsChanged = this.userBalance != userBalance;
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
        //清理为空的引用
        Iterator<WeakReference<OnWxPayListener>> iterator = onWxPayListenerList.iterator();
        while (iterator.hasNext()) {
            WeakReference<OnWxPayListener> ref = iterator.next();
            if (ref.get() == null)
                iterator.remove();
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
            this.onWxPayListenerList.add(new WeakReference<OnWxPayListener>(onWxPayListener));
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
            for (WeakReference<OnWxPayListener> ref : this.onWxPayListenerList) {
                if (ref.get() != null)
                    ref.get().onPayFinished(wxPayReturnCode);
            }
        } else {
            Log.e("ERROR", "onWxPay called error");
        }
    }

    /**
     * 触发信息修改回调
     */
    private void triggerOnDataChanged() {
        if (onDataChangedListenerList == null || !dataIsChanged) return;
        for (WeakReference<OnDataChangedListener> listener : onDataChangedListenerList) {
            if (listener.get() != null)
                listener.get().OnDataChanged(this);
        }
        dataIsChanged = false;
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
        //清理为空的引用
        Iterator<WeakReference<OnDataChangedListener>> iterator = onDataChangedListenerList.iterator();
        while (iterator.hasNext()) {
            WeakReference<OnDataChangedListener> ref = iterator.next();
            if (ref.get() == null)
                iterator.remove();
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
            this.onDataChangedListenerList.add(new WeakReference<OnDataChangedListener>(onDataChangedListener));
        }
    }

    /**
     * 移除信息修改回调
     *
     * @param
     */
    private void removeOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        initOnDataChangedListRef();
        synchronized (this) {
            this.onDataChangedListenerList.remove(onDataChangedListener);
        }
    }

    /**
     * 获取用户签到状态
     *
     * @return
     */
    public int getUserSignStatus() {
        return mSignStatus;
    }

    /**
     * 设置用户签到状态
     *
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

    /**
     * 用户数据
     */
    public static class DataHelper {
        WeakHashMap<String, Long> lastUpdateMap = new WeakHashMap<>();
        static WeakReference<DataHelper> mDataHelperRef;
        long cacheTime = 1000 * 90;

        private DataHelper() {
        }

        public static DataHelper getInstance() {
            if (mDataHelperRef == null || mDataHelperRef.get() == null) {
                synchronized (DataHelper.class) {
                    if (mDataHelperRef == null || mDataHelperRef.get() == null) {
                        mDataHelperRef = new WeakReference(new DataHelper());
                    }
                }
            }
            return mDataHelperRef.get();
        }

        /**
         * 判断是否可以更新数据
         *
         * @param key
         * @return
         */
        private boolean canUpdate(String key) {
            return canUpdate(key, this.cacheTime);
        }

        /**
         * 判断是否可以更新数据
         *
         * @param key
         * @param cacheTime 缓存时间
         * @return
         */
        private boolean canUpdate(String key, long cacheTime) {
            if (lastUpdateMap.containsKey(key)) {
                long lastupdateTime = lastUpdateMap.get(key);
                if (System.currentTimeMillis() - lastupdateTime > cacheTime) {
                    lastUpdateMap.remove(key);
                    return true;
                }
                return false;
            }
            return true;
        }

        /**
         * 保存更新时间
         *
         * @param key
         */
        private void saveUpdateTime(String key) {
            if (lastUpdateMap.containsKey(key)) {
                lastUpdateMap.remove(key);
            }
            lastUpdateMap.put(key, System.currentTimeMillis());
        }

        /**
         * 从服务器获取用户余额与鸽币
         */
        public void updateUserBalanceAndScoreFromServer() {
            if (canUpdate("getUserBalanceAndScoreFromServer"))
                CallAPI.getUserYuEAndJiFen(MyApp.getInstance(), new CallAPI.Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {
                        CpigeonData.getInstance().setUserBalance((double) data.get("yue"));
                        CpigeonData.getInstance().setUserScore((int) data.get("jifen"));
                        saveUpdateTime("getUserBalanceAndScoreFromServer");
                    }

                    @Override
                    public void onError(int errorType, Object data) {

                    }
                });
        }

        /**
         * 更新用户签到状态
         * <p>当签到状态为USER_SIGN_STATUS_SIGNED时，不会更新</p>
         */
        public void updateUserSignStatus() {
            if (canUpdate("updateUserSignStatus") && USER_SIGN_STATUS_SIGNED != mCpigeonData.getUserSignStatus())
                CallAPI.getUserSignStatus(MyApp.getInstance(), System.currentTimeMillis() / 1000, new CallAPI.Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        CpigeonData.getInstance().setUserSignStatus(data ? USER_SIGN_STATUS_SIGNED : USER_SIGN_STATUS_NOT_SIGN);
                        saveUpdateTime("updateUserSignStatus");
                    }

                    @Override
                    public void onError(int errorType, Object data) {
                        CpigeonData.getInstance().setUserSignStatus(USER_SIGN_STATUS_NONE);
                    }
                });
        }

        /**
         * @param onDataHelperUpdateLisenter
         */
        public void updateUserInfo(final OnDataHelperUpdateLisenter<UserInfo.DataBean> onDataHelperUpdateLisenter) {
            if (canUpdate("updateUserInfo")) {
                CallAPI.getBasicUserInfo(MyApp.getInstance(), new CallAPI.Callback<UserInfo.DataBean>() {
                    @Override
                    public void onSuccess(UserInfo.DataBean data) {
                        CpigeonData.getInstance().setUserInfo(data);
                        saveUpdateTime("updateUserInfo");
                        if (onDataHelperUpdateLisenter != null)
                            onDataHelperUpdateLisenter.onUpdated(data);
                    }

                    @Override
                    public void onError(int errorType, Object data) {
                        if (onDataHelperUpdateLisenter != null)
                            onDataHelperUpdateLisenter.onError(OnDataHelperUpdateLisenter.ERR_REQUST_EXCEPTION, "获取用户信息失败");
                    }
                });
            } else {
                if (onDataHelperUpdateLisenter != null)
                    onDataHelperUpdateLisenter.onError(OnDataHelperUpdateLisenter.ERR_NOT_NEED_UPDATE, null);
            }
        }

        /**
         * 更新用户绑定手机号码
         *
         * @param onCompleteListener
         */
        public void updateUserBandPhone(final OnDataHelperUpdateLisenter<String> onCompleteListener) {
            if (canUpdate("updateUserBandPhone")) {

                CallAPI.getUserBandPhone(MyApp.getInstance(), new CallAPI.Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {
                        String userPhone = (String) data.get("phone");
                        if ((int) data.get("band") == 1) {
                            CpigeonData.getInstance().setUserBindPhone(userPhone);
                            saveUpdateTime("updateUserBandPhone");
                            if (onCompleteListener != null)
                                onCompleteListener.onUpdated(userPhone);
                            return;
                        }
                        if (onCompleteListener != null)
                            onCompleteListener.onError(OnDataHelperUpdateLisenter.ERR_REQUST_EXCEPTION, "获取绑定号码失败");
                    }

                    @Override
                    public void onError(int errorType, Object data) {
                        if (onCompleteListener != null)
                            onCompleteListener.onError(OnDataHelperUpdateLisenter.ERR_REQUST_EXCEPTION, "获取绑定号码失败");
                    }
                });
            } else {
                if (onCompleteListener != null) {
                    onCompleteListener.onError(OnDataHelperUpdateLisenter.ERR_NOT_NEED_UPDATE, null);
                }
            }
        }

        public interface OnDataHelperUpdateLisenter<T> {

            int ERR_NOT_NEED_UPDATE = 12;
            int ERR_REQUST_EXCEPTION = 1221;

            void onUpdated(T data);

            void onError(int errortype, String msg);
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