package com.cpigeon.app.utils;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cpigeon.app.modular.cpigeongroup.model.bean.BadGuy;
import com.cpigeon.app.modular.cpigeongroup.model.bean.CpigeonGroupUserInfo;
import com.cpigeon.app.modular.cpigeongroup.model.bean.Message;
import com.cpigeon.app.modular.cpigeongroup.model.bean.MyFoucs;
import com.cpigeon.app.modular.cpigeongroup.model.bean.ShieldMessage;
import com.cpigeon.app.modular.footsearch.model.bean.FootQueryResult;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeons;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsXH;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.bean.CpigeonServicesInfo;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonUserServiceInfo;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;
import com.cpigeon.app.service.MainActivityService;
import com.cpigeon.app.service.databean.UseDevInfo;
import com.cpigeon.app.utils.cache.CacheManager;
import com.cpigeon.app.utils.databean.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.ex.DbException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.cpigeon.app.utils.CpigeonConfig.CACHE_MATCH_INFO_TIME;
import static com.cpigeon.app.utils.CpigeonConfig.getDataDb;


/**
 * Created by Administrator on 2016/11/25.
 */

public class CallAPI {
    /**
     * 单点登录检查
     *
     * @param devid
     * @param dev
     * @param ver
     * @param appid
     * @param sltoken
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable singleLoginCheck(Context context,
                                                                         String devid,
                                                                         String dev,
                                                                         String ver,
                                                                         String appid,
                                                                         String sltoken, @NonNull final Callback callback) {
        RequestParams params = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.SINGLE_LOGIN_CHECK_URL);
        pretreatmentParams(params);
        params.addQueryStringParameter("devid", devid);
        params.addQueryStringParameter("dev", dev);
        params.addQueryStringParameter("ver", ver);
        params.addQueryStringParameter("appid", appid);
        params.addQueryStringParameter("sltoken", sltoken);
        params.addQueryStringParameter("t", "1");
        params.addQueryStringParameter("u", CpigeonData.getInstance().getUserId(context) + "");
        params.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(params);
        return x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Logger.i(result);
                try {
                    ApiResponse<UseDevInfo> apiResponse = JSON.parseObject(result, new TypeReference<ApiResponse<UseDevInfo>>() {
                    });
                    if (apiResponse.isStatus()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, apiResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取更新信息
     *
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUpdateInfo(@NonNull final Callback<List<UpdateManager.UpdateInfo>> callback) {
        RequestParams params = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.UPDATE_CHECK_URL);
        pretreatmentParams(params);
        params.setCacheMaxAge(CpigeonConfig.CACHE_UPDATE_INFO_TIME);
        return x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Logger.i(result);
                try {
                    List<UpdateManager.UpdateInfo> apiResponse = JSON.parseObject(result, new TypeReference<List<UpdateManager.UpdateInfo>>() {
                    });
                    callback.onSuccess(apiResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 注册用户
     *
     * @param phoneNumber
     * @param password
     * @param yzm
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable registUser(String phoneNumber, String password, String yzm,
                                                                   @NonNull final Callback callback) {
        RequestParams params = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.REGIST_URL);
        pretreatmentParams(params);
        params.addParameter("t", phoneNumber);
        params.addParameter("y", yzm);
        params.addParameter("p", EncryptionTool.encryptAES(password));
        addApiSign(params);
        params.setCacheMaxAge(0);
        return x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);

                    if (obj.getBoolean("status")) {
                        callback.onSuccess(null);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 找回用户密码
     *
     * @param phoneNumber
     * @param password
     * @param yzm
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable findUserPassword(String phoneNumber, String password, String yzm,
                                                                         @NonNull final Callback callback) {
        RequestParams params = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.FIND_PASSWORD_URL);
        pretreatmentParams(params);
        params.addParameter("t", phoneNumber);
        params.addParameter("y", yzm);
        params.addParameter("p", EncryptionTool.encryptAES(password));
        addApiSign(params);
        params.setCacheMaxAge(0);
        return x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);

                    if (obj.getBoolean("status")) {
                        callback.onSuccess(null);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public static org.xutils.common.Callback.Cancelable getWXPrePayOrderForOrder(Context context, long orderid,
                                                                                 @NonNull final Callback<PayReq> callback) {
        RequestParams params = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GET_WX_PREPAY_ORDER_URL);
        pretreatmentParams(params);
        params.addParameter("oid", orderid);
        params.addParameter("u", CpigeonData.getInstance().getUserId(context));
        params.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(params);
        params.setCacheMaxAge(0);
        return x.http().get(params, new org.xutils.common.Callback.CommonCallback<JSONObject>() {

            @Override
            public void onSuccess(JSONObject result) {
                Logger.i(result.toString());
                try {
                    if (result.has("status") && result.getBoolean("status")) {
                        try {
                            PayReq req = new PayReq();

                            JSONObject obj = result.getJSONObject("data");
                            req.appId = obj.getString("appid");// 微信开放平台审核通过的应用APPID
                            req.partnerId = obj.getString("partnerid");// 微信支付分配的商户号
                            req.prepayId = obj.getString("prepayid");// 预支付订单号，app服务器调用“统一下单”接口获取
                            req.nonceStr = obj.getString("noncestr");// 随机字符串，不长于32位，服务器小哥会给咱生成
                            req.timeStamp = obj.getString("timestamp");// 时间戳，app服务器小哥给出
                            req.packageValue = obj.getString("package");// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
                            req.sign = obj.getString("sign");// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
                            callback.onSuccess(req);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, result.getInt("errorCode"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 发送验证码
     *
     * @param yzmType  TYPE_YZM_REGIST,TYPE_YZM_FIND_PASSWORD
     * @param phone
     * @param callback
     */
    public static org.xutils.common.Callback.Cancelable sendYZM(DATATYPE.YZM yzmType,
                                                                String phone,
                                                                @NonNull final Callback callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.PHONE_YZM_URL);
        pretreatmentParams(requestParams);
        //params.addParameter("devices_id", CommonTool.getCombinedDeviceID(this));
        requestParams.addParameter("t", phone);
        requestParams.addParameter("v", EncryptionTool.encryptAES("appcpigeon|" + System.currentTimeMillis() / 1000));
        requestParams.addParameter("p", yzmType.getValue());
//        params.setConnectTimeout(CpigeonConfig.CONNECT_TIMEOUT);
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Logger.d(result + "");
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        callback.onSuccess(obj.getString("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static org.xutils.common.Callback.Cancelable autoLogin(Context context,
                                                                  @NonNull final Callback callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.AUTO_LOGIN_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", CpigeonData.getInstance().getUserId(context));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Logger.d(result + "");
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        callback.onSuccess(obj.getInt("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取用户余额、鸽币信息
     *
     * @param context
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserYuEAndJiFen(Context context,
                                                                           @NonNull final Callback<Map<String, Object>> callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.USER_YUE_JIFEN_INFO_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", CpigeonData.getInstance().getUserId(context));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("用户余额鸽币信息获取完成");
                        Map<String, Object> map = null;
                        if (!obj.isNull("data")) {
                            obj = obj.getJSONObject("data");
                            map = new HashMap<String, Object>();
                            map.put("yue", obj.has("ye") ? obj.getDouble("ye") : 0);
                            map.put("jifen", obj.has("jf") ? obj.getInt("jf") : 0);
                        }
                        callback.onSuccess(map);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 获取用户余额、鸽币信息
     *
     * @param context
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserBandPhone(Context context,
                                                                         @NonNull final Callback<Map<String, Object>> callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GET_USER_BANDPHONE_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", CpigeonData.getInstance().getUserId(context));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("用户绑定手机号信息获取完成");
                        Map<String, Object> map = null;
                        if (!obj.isNull("data")) {
                            obj = obj.getJSONObject("data");
                            map = new HashMap<String, Object>();
                            map.put("phone", obj.has("phone") ? obj.getString("phone") : "");
                            map.put("band", obj.has("band") ? obj.getInt("band") : 0);
                        }
                        callback.onSuccess(map);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 获取用户鸽币记录
     *
     * @param context
     * @param pageSize
     * @param pageIndex
     * @param key
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserScoreRecord(Context context,
                                                                           int pageSize,
                                                                           int pageIndex,
                                                                           String key,
                                                                           @NonNull final Callback<List<UserScore>> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GET_USER_SCORE_RECORD_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", userid);
        requestParams.addParameter("pi", pageIndex);
        requestParams.addParameter("ps", pageSize);
        requestParams.addParameter("key", key);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setCacheMaxAge(CpigeonConfig.CACHE_COMMON_1MIN_TIME);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            boolean hasError = true;

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                hasError = false;
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("用户鸽币记录信息获取完成");
                        List<UserScore> data = new ArrayList<UserScore>();
                        UserScore userScore;
                        if (!obj.isNull("data")) {
                            JSONArray array = obj.getJSONArray("data");
                            if (array != null) {
                                for (int i = 0; i < array.length(); i++) {
                                    obj = array.getJSONObject(i);
                                    userScore = new UserScore();
                                    userScore.setItem(obj.has("item") ? obj.getString("item") : "");
                                    userScore.setTime(obj.has("time") ? obj.getString("time") : "");
                                    userScore.setId(obj.has("id") ? obj.getInt("id") : 0);
                                    userScore.setScore(obj.has("score") ? obj.getInt("score") : 0);
                                    userScore.setUserid(userid);
                                    data.add(userScore);
                                }
                            }
                        }
                        callback.onSuccess(data);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.i("");
            }

            @Override
            public void onFinished() {
                Logger.i("");
                if (hasError)
                    callback.onError(Callback.ERROR_TYPE_OTHER, null);
            }

        });
    }

    /**
     * 获取用户订单列表
     *
     * @param context
     * @param pageSize
     * @param pageIndex
     * @param key
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserOrderList(Context context,
                                                                         int pageSize,
                                                                         int pageIndex,
                                                                         String key,
                                                                         @NonNull final Callback<List<CpigeonOrderInfo>> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GET_ORDER_LIST_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", userid);
        requestParams.addParameter("pi", pageIndex);
        requestParams.addParameter("ps", pageSize);
        requestParams.addParameter("key", key);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setCacheMaxAge(CpigeonConfig.CACHE_COMMON_1MIN_TIME);
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            boolean hasError = true;

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                hasError = false;
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("用户订单信息获取完成");
                        List<CpigeonOrderInfo> data = new ArrayList<CpigeonOrderInfo>();
                        CpigeonOrderInfo cpigeonOrderInfo;
                        if (!obj.isNull("data")) {
                            JSONArray array = obj.getJSONArray("data");
                            if (array != null) {
                                for (int i = 0; i < array.length(); i++) {
                                    obj = array.getJSONObject(i);
                                    cpigeonOrderInfo = new CpigeonOrderInfo();
                                    cpigeonOrderInfo.setOrderNumber(obj.has("number") ? obj.getString("number") : "");
                                    cpigeonOrderInfo.setOrderName(obj.has("item") ? obj.getString("item") : "");
                                    cpigeonOrderInfo.setOrderTime(obj.has("time") ? obj.getString("time") : "");
                                    cpigeonOrderInfo.setPayWay(obj.has("payway") ? obj.getString("payway") : "");
                                    cpigeonOrderInfo.setStatusName(obj.has("statusname") ? obj.getString("statusname") : "");
                                    cpigeonOrderInfo.setId(obj.has("id") ? obj.getInt("id") : 0);
                                    cpigeonOrderInfo.setScores(obj.has("scores") ? obj.getInt("scores") : 0);
                                    cpigeonOrderInfo.setServiceId(obj.has("serviceid") ? obj.getInt("serviceid") : 0);
                                    cpigeonOrderInfo.setPrice(obj.has("price") ? obj.getDouble("price") : 0);
                                    cpigeonOrderInfo.setIspaid(obj.has("ispay") ? obj.getInt("ispay") == 1 : false);
                                    cpigeonOrderInfo.setUserid(userid);
                                    data.add(cpigeonOrderInfo);
                                }
                            }
                        }
                        callback.onSuccess(data);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (hasError)
                    callback.onError(Callback.ERROR_TYPE_OTHER, null);
            }

        });
    }

    /**
     * 获取用户头像
     *
     * @param context
     * @param username
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserHeadImg(Context context,
                                                                       String username,
                                                                       @NonNull final Callback<String> callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GET_USER_HEAD_IMG_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", username);
        requestParams.setCacheMaxAge(CpigeonConfig.CACHE_COMMON_1MIN_TIME);
        return x.http().get(requestParams, new org.xutils.common.Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                if (result != null) {
                    dealData(result);
                    return true;
                }
                return false;
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("获取用户头像完成");
                        callback.onSuccess(obj.getString("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 设置用户登录密码
     *
     * @param context
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable setUserPwd(Context context,
                                                                   String oldPwd,
                                                                   String newPwd,
                                                                   @NonNull final Callback<Boolean> callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.SET_USER_PWD_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", CpigeonData.getInstance().getUserId(context));
        requestParams.addParameter("op", EncryptionTool.encryptAES(oldPwd));
        requestParams.addParameter("np", EncryptionTool.encryptAES(newPwd));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("设置用户登录密码");
                        callback.onSuccess(true);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 设置用户支付密码
     *
     * @param context
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable setUserPayPwd(Context context,
                                                                      String yzm,
                                                                      String payPwd,
                                                                      String phone,
                                                                      @NonNull final Callback<Boolean> callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.SET_USER_PAYPWD_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", CpigeonData.getInstance().getUserId(context));
        requestParams.addParameter("y", yzm);
        requestParams.addParameter("t", phone);
        requestParams.addParameter("p", EncryptionTool.encryptAES(payPwd));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("设置支付密码完成");
                        callback.onSuccess(true);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }


    /**
     * 获取比赛信息
     *
     * @param context
     * @param matchType
     * @param
     * @param days
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getMatchInfo(final Context context,
                                                                     final DATATYPE.MATCH matchType,
                                                                     int days,
                                                                     @NonNull final Callback<List<MatchInfo>> callback) {

//        Date currDate = new Date();
//        currDate = new Date(currDate.getTime() - 1000 * 60 * 60 * 24 * days);
//        long beginTime = DateTool.strToDateTime(String.format("%04d-%02d-%02d 00:00:00", currDate.getYear() + 1900, currDate.getMonth() + 1, currDate.getDate())).getTime() / 1000;
        long beginTime = System.currentTimeMillis() / 1000 - 60 * 60 * 24 * days;
        return getMatchInfo(context, matchType, beginTime, 0, callback);
    }

    /**
     * 获取比赛信息
     *
     * @param context
     * @param matchType
     * @param
     * @param beginTime
     * @param endTime
     * @param callback
     * @return
     */
    private static org.xutils.common.Callback.Cancelable getMatchInfo(final Context context,
                                                                      final DATATYPE.MATCH matchType,
                                                                      long beginTime,
                                                                      long endTime,
                                                                      @NonNull final Callback<List<MatchInfo>> callback) {
        return getMatchInfo(context, matchType, beginTime, endTime, -1, callback);
    }

    /**
     * 获取比赛信息
     *
     * @param context
     * @param matchType
     * @param
     * @param beginTime
     * @param endTime
     * @param
     * @param count
     * @param callback
     * @return
     */
    private static org.xutils.common.Callback.Cancelable getMatchInfo(final Context context,
                                                                      final DATATYPE.MATCH matchType,
                                                                      long beginTime,
                                                                      long endTime,
                                                                      int count,
                                                                      @NonNull final Callback<List<MatchInfo>> callback) {

        //计算TOKEN
        String userToken = CommonTool.getUserToken(context);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.RACE_ITEM_INFO_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("t", matchType.getValue());
        requestParams.addParameter("c", count);
        requestParams.addParameter("ht", 1);
        requestParams.addHeader("u", userToken);
        requestParams.setCacheMaxAge(CACHE_MATCH_INFO_TIME);

        requestParams.addParameter("bt", beginTime);
        if (endTime != 0)
            requestParams.addParameter("et", endTime);
        final String cacheKey = getCacheKey(CPigeonApiUrl.RACE_ITEM_INFO_URL, requestParams);
        List<MatchInfo> data = CacheManager.getCache(cacheKey);

        if (data != null) {
            callback.onSuccess(data);
            return null;
        }
        addApiSign(requestParams);
        return x.http().get
                (requestParams, new org.xutils.common.Callback.CacheCallback<String>() {
                    @Override
                    public boolean onCache(String result) {
                        if (result != null) dealData(result);
                        return true;// true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (result != null) dealData(result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ex.printStackTrace();
                        callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                    }

                    private void dealData(String result) {

//                        Logger.json(result);
                        Logger.d(result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.getBoolean("status")) {
                                JSONArray array = obj.getJSONArray("data");
                                List<MatchInfo> list = new ArrayList<MatchInfo>();
                                MatchInfo matchInfo;
                                JSONObject jo;
//                                Logger.i("赛事共计：" + array.length());`
                                StringBuilder builder = new StringBuilder();
                                builder.append("DELETE FROM matchInfo WHERE dt='jg' OR( st>'");
                                builder.append(DateTool.dateToStr(new Date(System.currentTimeMillis() - 1000 * 24 * 60 * 60 * (matchType == DATATYPE.MATCH.GP ? CpigeonConfig.LIVE_DAYS_GP : matchType == DATATYPE.MATCH.XH ? CpigeonConfig.LIVE_DAYS_XH : 0))) + "' AND ssid NOT IN (");

                                for (int i = 0; i < array.length(); i++) {
                                    jo = array.getJSONObject(i);
                                    matchInfo = new MatchInfo();
                                    matchInfo.setSsid(EncryptionTool.encryptAES(jo.getString("ssid")));
                                    builder.append("'" + matchInfo.getSsid() + "',");
                                    matchInfo.setGcys(jo.has("gcys") ? jo.getInt("gcys") : 0);
                                    matchInfo.setBsmc(jo.has("bsmc") ? jo.getString("bsmc") : "");
                                    matchInfo.setArea(jo.has("area") ? jo.getString("area") : "");
                                    matchInfo.setSfjd(jo.has("sfjd") ? jo.getString("sfjd") : "");
                                    matchInfo.setWd(jo.has("wd") ? jo.getString("wd") : "");
                                    matchInfo.setMc(jo.has("mc") ? jo.getString("mc") : "");
                                    matchInfo.setJd(jo.has("jd") ? jo.getString("jd") : "");
                                    matchInfo.setSfz(jo.has("sfz") ? jo.getString("sfz") : "");
                                    matchInfo.setCpz(jo.has("cpz") ? jo.getString("cpz") : "");
                                    matchInfo.setCpy(jo.has("cpy") ? jo.getString("cpy") : "");
                                    matchInfo.setFx(jo.has("fx") ? jo.getString("fx") : "");
                                    matchInfo.setBskj(jo.has("bskj") ? jo.getDouble("bskj") : 0);
                                    matchInfo.setFl(jo.has("fl") ? jo.getString("fl") : "");
                                    matchInfo.setSt(jo.has("st") ? jo.getString("st") : "");
                                    matchInfo.setCsys(jo.has("csys") ? jo.getInt("csys") : 0);
                                    matchInfo.setXsys(jo.has("xsys") ? jo.getInt("xsys") : 0);
                                    matchInfo.setSlys(jo.has("slys") ? jo.getInt("slys") : 0);
                                    matchInfo.setZzid(jo.has("zzid") ? jo.getInt("zzid") : 0);
                                    matchInfo.setSfwd(jo.has("sfwd") ? jo.getString("sfwd") : "");
                                    matchInfo.setTq(jo.has("tq") ? jo.getString("tq") : "");
                                    matchInfo.setXmbt(jo.has("xmbt") ? jo.getString("xmbt") : "");
                                    matchInfo.setLx(jo.has("lx") ? jo.getString("lx") : "");
                                    matchInfo.setDt(jo.has("dt") ? jo.getString("dt") : "bs");
                                    matchInfo.setRuid(jo.has("ruid") ? jo.getInt("ruid") : 0);
                                    list.add(matchInfo);
                                }
                                builder.deleteCharAt(builder.length() - 1);
                                builder.append(")");
                                //当前类型
                                builder.append((matchType == DATATYPE.MATCH.XH ? "AND lx='xh'" : matchType == DATATYPE.MATCH.GP ? " AND lx='gp'" : ""));
                                builder.append(")");
                                DbManager db = x.getDb(getDataDb());
                                if (list.size() != 0) {
                                    try {
                                        db.execNonQuery(builder.toString());//除此之外的数据全部删除
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }
                                }
                                CacheManager.put(cacheKey, list);
                                try {
                                    db.saveOrUpdate(list);
                                    Logger.i(String.format("list.size=%s\n%s",
                                            list.size(),
                                            builder.toString()));
//                                            db.selector(MatchInfo.class).where("lx", "=", matchInfo.getLx()).and("st", ">", DateTool.getDayBeginTimeStr(new Date(System.currentTimeMillis() - 1000 * 24 * 60 * 60 * 3))).count()));
                                    callback.onSuccess(list);

                                } catch (DbException e) {
                                    e.printStackTrace();
                                    callback.onError(Callback.ERROR_TYPE_SAVE_TO_DB_EXCEPTION, e);
                                }
                            } else {
                                callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, e);
                        }

                    }
                });
    }

    /**
     * 统计归巢羽数
     *
     * @param context
     * @param matchType
     * @param ssid
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getRaceHomingCount(final Context context,
                                                                           final DATATYPE.MATCH matchType,
                                                                           String ssid,
                                                                           @NonNull final Callback<List<Map<String, Object>>> callback) {


        String userToken = CommonTool.getUserToken(context);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.RACE_ITEM_HOMING_COUNT_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("t", matchType.getValue());
        requestParams.addHeader("u", userToken);
        if (!TextUtils.isEmpty(ssid)) requestParams.addParameter("bi", ssid);
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Logger.i(result);
                Logger.i(String.valueOf(result.length()));
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        JSONArray array = obj.getJSONArray("data");
                        Map<String, Object> map;
                        Logger.i("共计：" + array.length());
                        for (int i = 0; i < array.length(); i++) {
                            obj = array.getJSONObject(i);
                            map = new HashMap<String, Object>();
                            map.put("c", obj.getString("c"));
                            map.put("s", obj.getString("s"));
                            dataList.add(map);
                        }
                    }
                    callback.onSuccess(dataList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.d("JSON解析错误");
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 获取报到数据
     * (数据已保存到SQLite)
     *
     * @param context
     * @param matchType
     * @param ssid
     * @param foot
     * @param name
     * @param hascz
     * @param pager
     * @param pagesize
     * @param czIndex
     * @param callback
     */
    public static org.xutils.common.Callback.Cancelable getReportData(final Context context,
                                                                      final String matchType,
                                                                      final String ssid,
                                                                      final String foot,
                                                                      final String name,
                                                                      final boolean hascz,
                                                                      int pager,
                                                                      int pagesize,
                                                                      int czIndex,
                                                                      String sKey,
                                                                      @NonNull final Callback<List> callback) {
        //计算TOKEN
        String userToken = CommonTool.getUserToken(context);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.RACE_REPORT_INFO_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("t", "gp".equals(matchType) ? 1 : 2);
        requestParams.addHeader("u", userToken);
        requestParams.addParameter("hcz", hascz ? 1 : 0);
        requestParams.addParameter("c", czIndex);
        requestParams.addParameter("bi", ssid);
        if (!("".equals(foot) || "".equals(name))) {
            requestParams.addParameter("f", foot);
            requestParams.addParameter("n", name);
        }
        requestParams.addParameter("pi", pager < 0 ? -1 : pager);
        requestParams.addParameter("ps", pagesize > 0 ? pagesize : 100);
        if (!"".equals(sKey))
            requestParams.addParameter("s", sKey);
        requestParams.setCacheMaxAge(CpigeonConfig.CACHE_MATCH_REPORT_INFO_TIME);

        final String cacheKey = getCacheKey(CPigeonApiUrl.RACE_REPORT_INFO_URL, requestParams);
        List data = CacheManager.getCache(cacheKey);
        if (data != null) {
            callback.onSuccess(data);
            return null;
        }
//        requestParams.setConnectTimeout(CpigeonConfig.CONNECT_TIMEOUT);
        addApiSign(requestParams);
        return x.http().get
                (requestParams, new org.xutils.common.Callback.CacheCallback<String>() {
                    boolean hasError = true;

                    @Override
                    public boolean onCache(String result) {
                        if (result != null) dealData(result);
                        return true;// true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (result != null) dealData(result);
//                        Logger.json(result);
                    }

                    private void dealData(final String result) {
                        hasError = false;
                        Logger.d(result);
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    ApiResponse apiResponse;
                                    if ("gp".equals(matchType)) {
                                        ApiResponse<List<MatchReportGP>> apiResponse1 = JSON.parseObject(result, new TypeReference<ApiResponse<List<MatchReportGP>>>() {
                                        });
                                        apiResponse = apiResponse1;
                                        Logger.i("gp报到数据共计：" + (apiResponse1.getData() == null ? "null" : apiResponse1.getData().size() + ""));
                                        if (apiResponse1.getData() != null)
                                            for (MatchReportGP item : apiResponse1.getData()) {
                                                item.setFoot(EncryptionTool.encryptAES(item.getFoot()));
                                            }
                                    } else {
                                        ApiResponse<List<MatchReportXH>> apiResponse1 = JSON.parseObject(result, new TypeReference<ApiResponse<List<MatchReportXH>>>() {
                                        });
                                        apiResponse = apiResponse1;
                                        Logger.i("xh报到数据共计：" + (apiResponse1.getData() == null ? "null" : apiResponse1.getData().size() + ""));
                                        if (apiResponse1.getData() != null)
                                            for (MatchReportXH item : apiResponse1.getData()) {
                                                item.setFoot(EncryptionTool.encryptAES(item.getFoot()));
                                            }
                                    }
                                    if (apiResponse.isStatus()) {
                                        CacheManager.put(cacheKey, apiResponse.getData());
                                        callback.onSuccess((List) apiResponse.getData());
                                    } else {
                                        callback.onError(Callback.ERROR_TYPE_API_RETURN, apiResponse.getErrorCode());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                                }
                            }
                        }.start();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ex.printStackTrace();
                        callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        if (hasError)
                            callback.onError(Callback.ERROR_TYPE_OTHER, null);
                    }
                });
    }

    /**
     * 获取集鸽数据（上笼清单）
     * (数据已保存到SQLite)
     *
     * @param context
     * @param matchType
     * @param ssid
     * @param foot
     * @param name
     * @param hascz
     * @param pager
     * @param pagesize
     * @param czIndex
     * @param callback
     */
    public static org.xutils.common.Callback.Cancelable getPigeonsData(Context context,
                                                                       final String matchType,
                                                                       final String ssid,
                                                                       final String foot,
                                                                       final String name,
                                                                       final boolean hascz,
                                                                       int pager,
                                                                       int pagesize,
                                                                       int czIndex,
                                                                       String sKey,
                                                                       @NonNull final Callback<List> callback) {


        //计算TOKEN
        String userToken = CommonTool.getUserToken(context);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.RACE_PIGEONS_INFO_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("t", "gp".equals(matchType) ? 1 : 2);
        requestParams.addHeader("u", userToken);
        requestParams.addParameter("hcz", hascz ? 1 : 0);
        requestParams.addParameter("c", czIndex);
        if (!"".equals(foot) && !"".equals(name)) {
            requestParams.addParameter("f", foot);
            requestParams.addParameter("n", name);
        }
        requestParams.addParameter("bi", ssid);
        requestParams.addParameter("pi", pager < 0 ? -1 : pager);
        requestParams.addParameter("ps", pagesize > 0 ? pagesize : 100);
        if (!"".equals(sKey))
            requestParams.addParameter("s", sKey);
        requestParams.setCacheMaxAge(CpigeonConfig.CACHE_MATCH_REPORT_INFO_TIME);

        final String cacheKey = getCacheKey(CPigeonApiUrl.RACE_PIGEONS_INFO_URL, requestParams);
        List data = CacheManager.getCache(cacheKey);
        if (data != null) {
            callback.onSuccess(data);
            return null;
        }
        addApiSign(requestParams);
        return x.http().get
                (requestParams, new org.xutils.common.Callback.CacheCallback<String>() {
                    @Override
                    public boolean onCache(String result) {
                        if (result != null) dealData(result);
                        return true;// true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
                    }

                    @Override
                    public void onSuccess(final String result) {
                        if (result != null) dealData(result);

                    }

                    private void dealData(final String result) {
                        Logger.d("isMain=" + (Looper.getMainLooper() == Looper.myLooper()));
                        List list;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.getBoolean("status")) {
                                if ("gp".equals(matchType)) {
                                    ApiResponse<List<MatchPigeonsGP>> apiResponse = JSON.parseObject(result, new TypeReference<ApiResponse<List<MatchPigeonsGP>>>() {
                                    });
                                    list = apiResponse.getData();
                                    Logger.i("gp集鸽数据共计：" + (list == null ? "null" : list.size() + ""));
                                    if (apiResponse.getData() != null)
                                        for (MatchPigeons item : apiResponse.getData()) {
                                            item.setFoot(EncryptionTool.encryptAES(item.getFoot()));
                                        }
                                } else {
                                    ApiResponse<List<MatchPigeonsXH>> apiResponse = JSON.parseObject(result, new TypeReference<ApiResponse<List<MatchPigeonsXH>>>() {
                                    });
                                    list = apiResponse.getData();
                                    Logger.i("xh集鸽数据共计：" + (list == null ? "null" : list.size() + ""));
                                    if (apiResponse.getData() != null)
                                        for (MatchPigeons item : apiResponse.getData()) {
                                            item.setFoot(EncryptionTool.encryptAES(item.getFoot()));
                                        }
                                }
                                CacheManager.put(cacheKey, list, 60 * 1000, 1000 * 60 * 60 * 24);
                                callback.onSuccess(list);

                            } else {
                                callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ex.printStackTrace();
                        callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
    }

    /**
     * 获取公告(数据未保存到SQLite)
     *
     * @param context
     * @param matchType
     * @param ssid
     * @param callback
     */
    public static org.xutils.common.Callback.Cancelable getBulletin(Context context,
                                                                    final String matchType,
                                                                    final String ssid,
                                                                    @NonNull final Callback<List<Bulletin>> callback) {


        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.RACE_BULLETIN_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("t", "gp".equals(matchType) ? 1 : "xh".equals(matchType) ? 2 : 3);
        requestParams.addParameter("bi", ssid);
        requestParams.setCacheMaxAge(CpigeonConfig.CACHE_BULLETIN_INFO_TIME);

        final String cacheKey = getCacheKey(CPigeonApiUrl.RACE_BULLETIN_URL, requestParams);
        List<Bulletin> data = CacheManager.getCache(cacheKey);
        if (data != null) {
            callback.onSuccess(data);
            return null;
        }
//        requestParams.setConnectTimeout(CpigeonConfig.CONNECT_TIMEOUT);
        return x.http().get(requestParams, new org.xutils.common.Callback.CacheCallback<String>() {

            @Override
            public boolean onCache(String result) {
                if (result != null) dealData(result);
                return true;// true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);

            }

            private void dealData(String result) {

                Bulletin bull;
                List<Bulletin> listData = new ArrayList<Bulletin>();
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status") && !obj.isNull("data")) {
                        if (!"".equals("ssid")) {
                            obj = obj.getJSONObject("data");
                            if (obj != null) {
                                bull = new Bulletin(ssid, obj.getString("gg"), obj.getString("sj"));
                                listData.add(bull);
                            }
                        } else {
                            JSONArray array = obj.getJSONArray("data");
                            if (array != null) {
                                for (int i = 0; i < array.length(); i++) {
                                    obj = array.getJSONObject(i);
                                    bull = new Bulletin(ssid, obj.getString("gg"), obj.getString("sj"));
                                    listData.add(bull);
                                }
                            }
                        }
                        CacheManager.put(cacheKey, listData, 1000 * 60, 1000 * 60 * 60 * 2);
                        callback.onSuccess(listData);
                    } else if (obj.getBoolean("status")) {
//                        Logger.i("没有公告");
                        callback.onSuccess(listData);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 获取公告(数据未保存到SQLite)
     *
     * @param context
     * @param matchType
     * @param ssid
     * @param callback
     */
    public static org.xutils.common.Callback.Cancelable getRaceGroupsCount(Context context,
                                                                           final String matchType,
                                                                           final String ssid,
                                                                           @NonNull final Callback<List<HashMap<String, Object>>> callback) {


        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.RACE_GROUPS_COUNT_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("t", "gp".equals(matchType) ? 1 : "xh".equals(matchType) ? 2 : 3);
        requestParams.addParameter("bi", ssid);
        requestParams.setCacheMaxAge(CpigeonConfig.CACHE_RACE_GROUPS_COUNT_TIME);
//        requestParams.setConnectTimeout(CpigeonConfig.CONNECT_TIMEOUT);
        final String cacheKey = getCacheKey(CPigeonApiUrl.RACE_GROUPS_COUNT_URL, requestParams);
        List<HashMap<String, Object>> data = CacheManager.getCache(cacheKey);
        if (data != null) {
            callback.onSuccess(data);
            return null;
        }
//
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CacheCallback<String>() {

            @Override
            public boolean onCache(String result) {
                if (result != null) dealData(result);
                return true;// true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            private void dealData(String result) {
                HashMap<String, Object> map;
                List<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status") && !obj.isNull("data")) {
                        if (!"".equals(ssid)) {
                            JSONArray array = obj.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                obj = array.getJSONObject(i);
                                map = new HashMap<String, Object>();
                                map.put("dt", obj.get("dt"));
                                map.put("c1", obj.get("c1"));
                                map.put("c2", obj.get("c2"));
                                map.put("c3", obj.get("c3"));
                                map.put("c4", obj.get("c4"));
                                map.put("c5", obj.get("c5"));
                                map.put("c6", obj.get("c6"));
                                map.put("c7", obj.get("c7"));
                                map.put("c8", obj.get("c8"));
                                map.put("c9", obj.get("c9"));
                                map.put("c10", obj.get("c10"));
                                map.put("c11", obj.get("c11"));
                                map.put("c12", obj.get("c12"));
                                map.put("c13", obj.get("c13"));
                                map.put("c14", obj.get("c14"));
                                map.put("c15", obj.get("c15"));
                                map.put("c16", obj.get("c16"));
                                map.put("c17", obj.get("c17"));
                                map.put("c18", obj.get("c18"));
                                map.put("c19", obj.get("c19"));
                                map.put("c20", obj.get("c20"));
                                map.put("c21", obj.get("c21"));
                                map.put("c22", obj.get("c22"));
                                map.put("c23", obj.get("c23"));
                                map.put("c24", obj.get("c24"));
                                listData.add(map);
                            }
                        }
                        CacheManager.put(cacheKey, listData);
                        callback.onSuccess(listData);
                    } else if (obj.getBoolean("status")) {
                        Logger.i("没有插组统计数据");
                        callback.onSuccess(listData);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }
        });

    }

    /**
     * 获取新闻类型列表
     *
     * @param context
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getNewsTypes(Context context,
                                                                     @NonNull final Callback<List<String>> callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.NEWS_TYPE_URL);
        pretreatmentParams(requestParams);
        requestParams.setCacheMaxAge(CpigeonConfig.CACHE_TIME_NEWS_LIST);
        return x.http().get(requestParams, new org.xutils.common.Callback.CacheCallback<String>() {

            @Override
            public boolean onCache(String result) {
                if (result != null) dealData(result);
                return true;// true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                List<String> listData = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status") && !obj.isNull("data")) {
                        JSONArray array = obj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            listData.add(array.getString(i));

                        }
                        callback.onSuccess(listData);
                    } else if (obj.getBoolean("status")) {
                        Logger.i("获取资讯类型失败");
                        callback.onSuccess(listData);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }


    /**
     * 足环查询
     *
     * @return
     */
    public static org.xutils.common.Callback.Cancelable footQuery(Context context,
                                                                  final String skey,
                                                                  @NonNull final Callback<Map<String, Object>> callback) {


        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.FOOT_QUERY_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("s", skey);
        requestParams.addParameter("u", CpigeonData.getInstance().getUserId(context));
        requestParams.addParameter("t", 1);
        requestParams.addHeader("u", CommonTool.getUserToken(context));//      requestParams.setConnectTimeout(CpigeonConfig.CONNECT_TIMEOUT);

        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("足环查询完成");
                        Map<String, Object> map = new HashMap<String, Object>();

                        try {
                            String msg = obj.getString("msg");
                            String[] msgs = msg.split("\\|");
                            map.put("rest", Integer.valueOf(msgs[0]));
                            map.put("resultCount", Integer.valueOf(msgs[1]));
                            map.put("maxShowCount", Integer.valueOf(msgs[2]));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            if (map.containsKey("rest"))
                                map.remove("rest");
                            if (map.containsKey("resultCount"))
                                map.remove("resultCount");
                            map.put("rest", -1);
                        }
                        List<FootQueryResult> list = new ArrayList<FootQueryResult>();
                        FootQueryResult foot;
                        JSONArray array = obj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            obj = array.getJSONObject(i);
                            foot = new FootQueryResult();
                            foot.setMc(obj.has("mc") ? obj.getInt("mc") : 0);
                            foot.setBskj(obj.has("bskj") ? obj.getDouble("bskj") : 0);
                            foot.setCsys(obj.has("csys") ? obj.getInt("csys") : 0);
                            foot.setFoot(obj.has("foot") ? obj.getString("foot") : "");
                            foot.setOrgname(obj.has("orgname") ? obj.getString("orgname") : "");
                            foot.setSpeed(obj.has("speed") ? obj.getDouble("speed") : 0);
                            foot.setSt(obj.has("st") ? obj.getString("st") : "");
                            foot.setXmmc(obj.has("xmmc") ? obj.getString("xmmc") : "");
                            foot.setName(obj.has("name") ? obj.getString("name") : null);
                            list.add(foot);
                        }
                        map.put("data", list);
                        callback.onSuccess(map);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 用户在用服务信息
     *
     * @param context
     * @param name     服务名称，不可为空
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserService(Context context,
                                                                       final String name,
                                                                       @NonNull final Callback<CpigeonUserServiceInfo> callback) {


        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.USER_SERVICE_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("n", name);
        requestParams.addParameter("u", CpigeonData.getInstance().getUserId(context));
        requestParams.addHeader("u", CommonTool.getUserToken(context));

        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.d(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
//                        Logger.i("获取用户在用服务完成");
                        CpigeonUserServiceInfo cpigeonUserServiceInfo = null;
                        if (!obj.isNull("data")) {
                            cpigeonUserServiceInfo = new CpigeonUserServiceInfo();
                            obj = obj.getJSONObject("data");
                            cpigeonUserServiceInfo.setServiceId(obj.has("serviceid") ? obj.getInt("serviceid") : 0);
                            cpigeonUserServiceInfo.setNumbers(obj.has("numbers") ? obj.getInt("numbers") : 0);
                            cpigeonUserServiceInfo.setServicetimes(obj.has("servicetimes") ? obj.getInt("servicetimes") : 0);
                            cpigeonUserServiceInfo.setUnitname(obj.has("unitname") ? obj.getString("unitname") : "");
                            cpigeonUserServiceInfo.setPackageName(obj.has("package") ? obj.getString("package") : "");
                            cpigeonUserServiceInfo.setBrief(obj.has("brief") ? obj.getString("brief") : "");
                            cpigeonUserServiceInfo.setName(obj.has("name") ? obj.getString("name") : "");
                            cpigeonUserServiceInfo.setShowNumber(obj.has("showNum") ? obj.getInt("showNum") : 0);
                        }
                        callback.onSuccess(cpigeonUserServiceInfo);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 检查当前版本是否是可用版本
     *
     * @param context
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable checkIsAvailableVersion(Context context,
                                                                                @NonNull final Callback<Boolean> callback) {


        final RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.IS_AVAILABLE_VERSION_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("vc", CommonTool.getVersionCode(context));
        requestParams.addParameter("t", "android");
        requestParams.setCacheMaxAge(CpigeonConfig.CACHE_COMMON_30MIN_TIME);
        final String cacheKey = getCacheKey(CPigeonApiUrl.RACE_GROUPS_COUNT_URL, requestParams);
        Boolean data = CacheManager.getCache(cacheKey);
        if (data != null) {
            callback.onSuccess(data);
            return null;
        }
//

        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.d(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("获取可用版本信息完成");

                        boolean res = !obj.isNull("data") ? obj.getBoolean("data") : false;
                        CacheManager.put(cacheKey, res, 1000 * 60, 1000 * 60 * 60 * 12);
                        callback.onSuccess(res);

                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 查询已上架服务（套餐）
     *
     * @param context
     * @param name
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getServicesInfo(Context context,
                                                                        final String name,
                                                                        @NonNull final Callback<List<CpigeonServicesInfo>> callback) {
        return getServicesInfo(context, name, "", callback);
    }

    /**
     * 查询所有服务（套餐）
     *
     * @param context
     * @param name
     * @param shelvesType 上架类型：1：包含已上架和未上架服务；默认为已上架服务
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getServicesInfo(Context context,
                                                                        final String name,
                                                                        final String shelvesType,
                                                                        @NonNull final Callback<List<CpigeonServicesInfo>> callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.SERVICES_INFO_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("n", name);
        requestParams.addParameter("t", shelvesType);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setCacheMaxAge(CpigeonConfig.CACHE_SERVICES_INFO_LIST);//设置缓存时间，一个小时
        Logger.d("缓存时间：" + CpigeonConfig.CACHE_SERVICES_INFO_LIST);
        final String cacheKey = getCacheKey(CPigeonApiUrl.RACE_GROUPS_COUNT_URL, requestParams);
        List<CpigeonServicesInfo> data = CacheManager.getCache(cacheKey);
        if (data != null) {
            callback.onSuccess(data);
            return null;
        }
        return x.http().get(requestParams, new org.xutils.common.Callback.CacheCallback<String>() {

            @Override
            public boolean onCache(String result) {
                if (result != null) {
                    dealData(result);
                    return true;
                }
                return false;
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.d(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("获取服务信息完成");
                        List<CpigeonServicesInfo> data = new ArrayList<CpigeonServicesInfo>();

                        CpigeonServicesInfo info;
                        JSONArray array = obj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            obj = array.getJSONObject(i);
                            info = new CpigeonServicesInfo();
                            info.setId(obj.has("id") ? obj.getInt("id") : 0);
                            info.setName(obj.has("name") ? obj.getString("name") : "");
                            info.setPackageName(obj.has("package") ? obj.getString("package") : "");
                            info.setBrief(obj.has("brief") ? obj.getString("brief") : "");
                            info.setDetial(obj.has("detial") ? obj.getString("detial") : "");
                            info.setUnitname(obj.has("unitname") ? obj.getString("unitname") : "");
                            info.setOpentime(obj.has("opentime") ? obj.getString("opentime") : "");
                            info.setExpiretime(obj.has("expiretime") ? obj.getString("expiretime") : "");
                            info.setServicetimes(obj.has("servicetimes") ? obj.getInt("servicetimes") : 0);
                            info.setFlag(obj.has("flag") ? obj.getInt("flag") : 0);
                            info.setPrice(obj.has("price") ? obj.getDouble("price") : 0);
                            info.setScores(obj.has("scores") ? obj.getInt("scores") : 0);
                            data.add(info);
                        }
                        CacheManager.put(cacheKey, data, 1000 * 60, 1000 * 60 * 60 * 3);
                        callback.onSuccess(data);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }


    /**
     * 创建服务订单
     *
     * @param context
     * @param serviceId
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable createServiceOrder(Context context,
                                                                           final int serviceId,
                                                                           @NonNull final Callback<CpigeonOrderInfo> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.CREATE_SERVICE_ORDER_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("sid", serviceId);
        requestParams.addParameter("u", userid);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("创建订单完成");
                        CpigeonOrderInfo info = null;
                        if (!obj.isNull("data")) {
                            obj = obj.getJSONObject("data");
                            info = new CpigeonOrderInfo();
                            info.setId(obj.has("id") ? obj.getInt("id") : 0);
                            info.setOrderTime(obj.has("time") ? obj.getString("time") : "");
                            info.setOrderNumber(obj.has("number") ? obj.getString("number") : "");
                            info.setOrderName(obj.has("item") ? obj.getString("item") : "");
                            info.setPrice(obj.has("price") ? obj.getDouble("price") : 0);
                            info.setScores(obj.has("scores") ? obj.getInt("scores") : 0);
                            info.setUserid(userid);
                        }
                        callback.onSuccess(info);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 创建充值订单  2017-02-28 ---szx
     *
     * @param context
     * @param money
     * @param method
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable createRechargeOrder(Context context,
                                                                            final double money,
                                                                            final int method,
                                                                            @NonNull final Callback<CpigeonRechargeInfo.DataBean> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GREATERECHARGEORDER);
        pretreatmentParams(requestParams);
        requestParams.addParameter("m", money);
        requestParams.addParameter("w", method);
        requestParams.addParameter("u", userid);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("订单创建成功");
                        CpigeonRechargeInfo.DataBean dataBean = null;
                        if (!obj.isNull("data")) {
                            obj = obj.getJSONObject("data");
                            dataBean = new CpigeonRechargeInfo.DataBean();
                            dataBean.setId(obj.has("id") ? obj.getInt("id") : 0);
                        }
                        callback.onSuccess(dataBean);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 获取充值订单列表 2017-02-28 ---szx
     *
     * @param context
     * @param ps       一页获取的数据量
     * @param pi       获取页码的数据
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getRechargeList(Context context,
                                                                        final int ps,
                                                                        final int pi,
                                                                        @NonNull final Callback<List<CpigeonRechargeInfo.DataBean>> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GETRECHARGELIST);
        pretreatmentParams(requestParams);
        requestParams.addParameter("ps", ps);
        requestParams.addParameter("pi", pi);
        requestParams.addParameter("u", userid);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("获取订单完成");
                        ApiResponse<List<CpigeonRechargeInfo.DataBean>> apiResponse = JSON.parseObject(result, new TypeReference<ApiResponse<List<CpigeonRechargeInfo.DataBean>>>() {
                        });
                        Logger.d(apiResponse.getData().size() + "");
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 创建微信预支付订单 2017-02-28  ---szx
     *
     * @param context
     * @param did
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getWXPrePayOrderForRecharge(Context context,
                                                                                    final long did,
                                                                                    @NonNull final Callback<PayReq> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GETWXPREPAYORDERFORRECHARGE);
        pretreatmentParams(requestParams);
        requestParams.addParameter("did", did);
        requestParams.addParameter("t", "android");
        requestParams.addParameter("u", userid);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Logger.d(result.toString());
                ApiResponse<PayReq> res = JSON.parseObject(result.toString(), new TypeReference<ApiResponse<PayReq>>() {
                });
                if (res.isStatus()) {
//                    JSONObject obj = result.getJSONObject("data");
//                    req.appId = obj.getString("appid");// 微信开放平台审核通过的应用APPID
//                    req.partnerId = obj.getString("partnerid");// 微信支付分配的商户号
//                    req.prepayId = obj.getString("prepayid");// 预支付订单号，app服务器调用“统一下单”接口获取
//                    req.nonceStr = obj.getString("noncestr");// 随机字符串，不长于32位，服务器小哥会给咱生成
//                    req.timeStamp = obj.getString("timestamp");// 时间戳，app服务器小哥给出
//                    req.packageValue = obj.getString("package");// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
//                    req.sign = obj.getString("sign");// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
                    callback.onSuccess(res.getData());
                } else {
                    callback.onError(Callback.ERROR_TYPE_API_RETURN, res.getErrorCode());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 增加赛鸽的观看量 2017-03-02 ---szx
     *
     * @param context
     * @param t
     * @param bi
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable addRaceClickCount(Context context,
                                                                          final DATATYPE.MATCH t,
                                                                          final String bi,
                                                                          @NonNull final Callback<Boolean> callback) {
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.ADDRACECLICKCOUNT);
        pretreatmentParams(requestParams);
        requestParams.addParameter("t", t.getValue());
        requestParams.addParameter("bi", bi);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);

        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<JSONObject>() {

            @Override
            public void onSuccess(JSONObject result) {
                Logger.d(result.toString());
                if (!result.has("status") || result.isNull("data")) {
                    try {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, result.getInt("errorCode"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                try {
//                    if (result.getBoolean("data")) {
//                        Logger.e("增加访问量成功");
//                    }
                    callback.onSuccess(result.getBoolean("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 判断用户是否签到 2017-03-06  ---szx
     *
     * @param context
     * @param t
     * @param
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserSignStatus(Context context,
                                                                          final Long t,
                                                                          @NonNull final Callback<Boolean> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GETUSERSIGNSTATUS);
        pretreatmentParams(requestParams);
        requestParams.addParameter("t", t);
        requestParams.addParameter("u", userid);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<JSONObject>() {

            @Override
            public void onSuccess(JSONObject result) {

//                Logger.d(result.toString());
                if (!result.has("status") || result.isNull("data")) {
                    try {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, result.getInt("errorCode"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                try {
                    if (result.getBoolean("data")) {
                        Logger.e("签到成功");
                    }
                    callback.onSuccess(result.getBoolean("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 获取用户基本信息 --szx
     *
     * @param context
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getBasicUserInfo(Context context,
                                                                         @NonNull final Callback<UserInfo.DataBean> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GETBASICUSERINFO);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", userid);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("获取用户基本信息成功");
                        ApiResponse<UserInfo.DataBean> apiResponse = JSON.parseObject(result, new TypeReference<ApiResponse<UserInfo.DataBean>>() {
                        });
                        Logger.d(apiResponse.getData().getUsername() + "");
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 提交修改用户基本信息 -- szx
     *
     * @param context
     * @param ni       昵称
     * @param s        性别
     * @param b        生日
     * @param si       签名
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable motifyBasicUserInfo(Context context,
                                                                            String ni,
                                                                            String s,
                                                                            String b,
                                                                            String si,
                                                                            @NonNull final Callback<UserInfo.DataBean> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.MOTIFYBASICUSERINFO);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", userid);
        requestParams.addParameter("ni", ni);
        requestParams.addParameter("s", s);
        requestParams.addParameter("b", b);
        requestParams.addParameter("si", si);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("修改成功");
                        ApiResponse<UserInfo.DataBean> apiResponse = JSON.parseObject(result, new TypeReference<ApiResponse<UserInfo.DataBean>>() {
                        });
                        Logger.d(apiResponse.getData().getUsername() + "");
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 更新用户头像  --szx
     *
     * @param context
     * @param uface
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable updateUserFaceImage(Context context,
                                                                            File uface,
                                                                            @NonNull final Callback<String> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.UPDATEUSERFACEIMAGE);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", userid);
        requestParams.addBodyParameter("uface", uface);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMultipart(true);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.e(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("修改成功");
                        callback.onSuccess(obj.getString("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 发说说
     *
     * @param context
     * @param msg
     * @param loabs
     * @param lo
     * @param st
     * @param mmt
     * @param video    ---szx
     * @param pic1
     * @param pic2
     * @param pic3
     * @param pic4
     * @param pic5
     * @param pic6
     * @param pic7
     * @param pic8
     * @param pic9
     * @param callback
     * @return
     */

    public static org.xutils.common.Callback.Cancelable pushCircleMessage(Context context,
                                                                          String msg,
                                                                          String loabs,
                                                                          String lo,
                                                                          int st,
                                                                          String mmt,
                                                                          File video,
                                                                          File pic1,
                                                                          File pic2,
                                                                          File pic3,
                                                                          File pic4,
                                                                          File pic5,
                                                                          File pic6,
                                                                          File pic7,
                                                                          File pic8,
                                                                          File pic9,
                                                                          @NonNull final Callback<String> callback) {


        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("msg", msg));
        postParams.add(new KeyValue("loabs", loabs));
        postParams.add(new KeyValue("lo", lo));
        postParams.add(new KeyValue("st", String.valueOf(st)));
        postParams.add(new KeyValue("mmt", mmt));
        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.PUSHCIRCLEMESSAGE);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("msg", msg);
        requestParams.addBodyParameter("loabs", loabs);
        requestParams.addBodyParameter("lo", lo);
        requestParams.addBodyParameter("st", String.valueOf(st));
        requestParams.addBodyParameter("mmt", mmt);
        requestParams.addBodyParameter("video", video);
        requestParams.addBodyParameter("pic1", pic1);
        requestParams.addBodyParameter("pic2", pic2);
        requestParams.addBodyParameter("pic3", pic3);
        requestParams.addBodyParameter("pic4", pic4);
        requestParams.addBodyParameter("pic5", pic5);
        requestParams.addBodyParameter("pic6", pic6);
        requestParams.addBodyParameter("pic7", pic7);
        requestParams.addBodyParameter("pic8", pic8);
        requestParams.addBodyParameter("pic9", pic9);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        requestParams.setMultipart(true);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.e(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("发送成功");
                        callback.onSuccess(obj.getString("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取广场消息 --szx
     *
     * @param context
     * @param lt
     * @param mid
     * @param pi
     * @param ps
     * @param callback
     * @return
     */

    public static org.xutils.common.Callback.Cancelable getCircleMessageList(Context context,
                                                                             final String lt,
                                                                             final String mid,
                                                                             final int pi,
                                                                             final int ps,
                                                                             final Callback<List<Message.DataBean>> callback) {


        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("lt", lt));
        postParams.add(new KeyValue("mid", mid));
        postParams.add(new KeyValue("pi", String.valueOf(pi)));
        postParams.add(new KeyValue("ps", String.valueOf(ps)));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GETCIRCLEMESSAGELIST);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("mid", mid);
        requestParams.addBodyParameter("lt", lt);
        requestParams.addBodyParameter("pi", String.valueOf(pi));
        requestParams.addBodyParameter("ps", String.valueOf(ps));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("获取成功");
                        ApiResponse<List<Message.DataBean>> apiResponse = JSON.parseObject(result, new TypeReference<ApiResponse<List<Message.DataBean>>>() {
                        });
                        callback.onSuccess(apiResponse.getData());

                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 删除用户自己发的说说 -- szx
     *
     * @param context
     * @param mid
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable delCircleMessage(Context context,
                                                                         final String mid,
                                                                         final Callback<String> callback) {


        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("mid", mid));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.DELCIRCLEMESSAGE);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("mid", mid);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("发送成功");
                        callback.onSuccess(obj.getString("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 点赞 --szx
     *
     * @param context
     * @param mid
     * @param isp
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable praiseCircleMessage(Context context,
                                                                            final String mid,
                                                                            final String isp,
                                                                            final Callback<Integer> callback) {


        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("mid", mid));
        postParams.add(new KeyValue("isp", isp));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.PRAISECIRCLEMESSAGE);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("mid", mid);
        requestParams.addBodyParameter("isp", isp);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("点赞/取消点赞成功");
                        callback.onSuccess(obj.getInt("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 添加评论  --szx
     *
     * @param context
     * @param mid
     * @param c
     * @param topid
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable addCircleMessageComment(Context context,
                                                                                final int mid,
                                                                                final String c,
                                                                                final int topid,
                                                                                final Callback<Integer> callback) {


        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("mid", String.valueOf(mid)));
        postParams.add(new KeyValue("c", c));
        postParams.add(new KeyValue("topid", String.valueOf(topid)));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.ADDCIRCLEMESSAGECOMMENT);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("mid", String.valueOf(mid));
        requestParams.addBodyParameter("c", c);
        requestParams.addBodyParameter("topid", String.valueOf(topid));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.e(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("评论成功");
                        callback.onSuccess(obj.getInt("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 获取鸽友圈信息  --szx
     *
     * @param context
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserCircleInfo(Context context,
                                                                          final Callback<CpigeonGroupUserInfo.DataBean> callback) {


        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GETUSERCIRCLEINFO);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.e("获取鸽友圈成功");
                        Gson gson = new Gson();
                        CpigeonGroupUserInfo json = gson.fromJson(result, new TypeToken<CpigeonGroupUserInfo>() {
                        }.getType());

                        callback.onSuccess(json.getData());
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    /**
     * 获取关注列表  --szx
     *
     * @param context
     * @param pi
     * @param ps
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getAttentionCircleUserList(Context context,
                                                                                   final int pi,
                                                                                   final int ps,
                                                                                   final String t,
                                                                                   final Callback<List<MyFoucs.DataBean>> callback) {

        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("pi", String.valueOf(pi)));
        postParams.add(new KeyValue("ps", String.valueOf(ps)));
        postParams.add(new KeyValue("t", String.valueOf(t)));


        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GETATTENTIONCIRCLEUSERLIST);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("pi", String.valueOf(pi));
        requestParams.addBodyParameter("ps", String.valueOf(ps));
        requestParams.addBodyParameter("t", String.valueOf(t));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("获取关注列表成功");
                        Gson gson = new Gson();
                        MyFoucs json = gson.fromJson(result, new TypeToken<MyFoucs>() {
                        }.getType());
                        callback.onSuccess(json.getData());

                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 关注/取消关注  --szx
     *
     * @param context
     * @param auid
     * @param isa
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable attentionCircleUser(Context context,
                                                                            final String auid,
                                                                            final int isa,
                                                                            final Callback<String> callback) {

        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("auid", auid));
        postParams.add(new KeyValue("isa", isa));


        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.ATTENTIONCIRCLEUSER);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("auid", auid);
        requestParams.addBodyParameter("isa", String.valueOf(isa));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("关注/取消关注成功");
                        callback.onSuccess(obj.getString("data"));

                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    /**
     * 获取黑名单
     *
     * @param context
     * @param pi
     * @param ps
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserCircleBlackList(Context context,
                                                                               final int pi,
                                                                               final int ps,
                                                                               final Callback<List<BadGuy.DataBean>> callback) {

        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("pi", String.valueOf(pi)));
        postParams.add(new KeyValue("ps", String.valueOf(ps)));


        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GETUSERCIRCLEBLACKLIST);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("pi", String.valueOf(pi));
        requestParams.addBodyParameter("ps", String.valueOf(ps));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("获取黑名单用户的列表成功");
                        Gson gson = new Gson();
                        BadGuy json = gson.fromJson(result, new TypeToken<BadGuy>() {
                        }.getType());
                        callback.onSuccess(json.getData());

                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 拉黑/取消拉黑
     *
     * @param context
     * @param duid
     * @param isd
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable defriendCircleUser(Context context,
                                                                           final int duid,
                                                                           final int isd,
                                                                           final Callback<Boolean> callback) {

        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("duid", String.valueOf(duid)));
        postParams.add(new KeyValue("isd", String.valueOf(isd)));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.DEFRIENDCIRCLEUSER);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("duid", String.valueOf(duid));
        requestParams.addBodyParameter("isd", String.valueOf(isd));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("拉黑/取消拉黑成功");
                        callback.onSuccess(obj.getBoolean("data"));

                    } else {

                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 屏蔽某个用户
     *
     * @param context
     * @param suid
     * @param iss
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable shieldCircleUser(Context context,
                                                                         final int suid,
                                                                         final int iss,
                                                                         final Callback<Boolean> callback) {

        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("suid", String.valueOf(suid)));
        postParams.add(new KeyValue("iss", String.valueOf(iss)));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.SHIELDCIRCLEUSER);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("suid", String.valueOf(suid));
        requestParams.addBodyParameter("iss", String.valueOf(iss));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("屏蔽/取消屏蔽成功");
                        callback.onSuccess(obj.getBoolean("data"));

                    } else {

                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 屏蔽某一条消息
     *
     * @param context
     * @param mid
     * @param iss
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable shieldCircleMessage(Context context,
                                                                            final int mid,
                                                                            final int iss,
                                                                            final Callback<Boolean> callback) {

        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("mid", String.valueOf(mid)));
        postParams.add(new KeyValue("iss", String.valueOf(iss)));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.SHIELDCIRCLEMESSAGE);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("mid", String.valueOf(mid));
        requestParams.addBodyParameter("iss", String.valueOf(iss));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("屏蔽消息/取消屏蔽消息成功");
                        callback.onSuccess(obj.getBoolean("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    /**
     * 清除屏蔽用户
     *
     * @param context
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable clearShieldCircleUser(Context context,
                                                                              final Callback<Boolean> callback) {

        final int userid = CpigeonData.getInstance().getUserId(context);
        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.CLEARSHIELDCIRCLEMESSAGE);
        pretreatmentParams(requestParams);
        requestParams.addParameter("u", userid);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("清除屏蔽的用户成功了");
                        Gson gson = new Gson();
                        UserInfo json = gson.fromJson(result, new TypeToken<UserInfo>() {
                        }.getType());
                        Logger.d(json.getData().getUsername() + "");
                        callback.onSuccess(obj.getBoolean("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 获取屏蔽过的消息列表
     *
     * @param context
     * @param pi
     * @param ps
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserShieldCircleMessageList(Context context,
                                                                                       final int pi,
                                                                                       final int ps,
                                                                                       final Callback<List<ShieldMessage.DataBean>> callback) {

        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("pi", String.valueOf(pi)));
        postParams.add(new KeyValue("ps", String.valueOf(ps)));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GETUSERSHIELDCIRCLEMESSAGELIST);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("pi", String.valueOf(pi));
        requestParams.addBodyParameter("ps", String.valueOf(ps));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("获取屏蔽消息的列表成功");
                        Gson gson = new Gson();
                        ShieldMessage json = gson.fromJson(result, new TypeToken<ShieldMessage>() {
                        }.getType());

                        callback.onSuccess(json.getData());

                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    /**
     * 获取自己屏蔽了的用户
     *
     * @param context
     * @param pi
     * @param ps
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable getUserShieldCircleUserList(Context context,
                                                                                    final int pi,
                                                                                    final int ps,
                                                                                    final Callback<List<BadGuy.DataBean>> callback) {

        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("pi", String.valueOf(pi)));
        postParams.add(new KeyValue("ps", String.valueOf(ps)));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GETUSERSHIELDCIRCLEUSERLIST);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("pi", String.valueOf(pi));
        requestParams.addBodyParameter("ps", String.valueOf(ps));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("获取屏蔽用户的列表成功");
                        Gson gson = new Gson();
                        BadGuy json = gson.fromJson(result, new TypeToken<BadGuy>() {
                        }.getType());
                        callback.onSuccess(json.getData());

                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 删除已经发布过的消息  -szx
     *
     * @param context
     * @param cid
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable delCircleComment(Context context,
                                                                         final int cid,
                                                                         final Callback<Boolean> callback) {


        int userid = CpigeonData.getInstance().getUserId(context);

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<KeyValue> urlParams = new ArrayList<>();
        urlParams.add(new KeyValue("timestamp", timestamp));
        List<KeyValue> postParams = new ArrayList<>();
        postParams.add(new KeyValue("u", String.valueOf(userid)));
        postParams.add(new KeyValue("cid", String.valueOf(cid)));

        String sign = getApiSign(urlParams, postParams);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.DELCIRCLECOMMENT);
        pretreatmentParams(requestParams);
        requestParams.addQueryStringParameter("timestamp", timestamp);
        requestParams.addQueryStringParameter("sign", sign);
        requestParams.addBodyParameter("u", String.valueOf(userid));
        requestParams.addBodyParameter("cid", String.valueOf(cid));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        requestParams.setMethod(HttpMethod.POST);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.json(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("删除消息成功");
                        callback.onSuccess(obj.getBoolean("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 鸽币支付
     *
     * @param context
     * @param orderId
     * @param payPwd
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable orderPayByScore(Context context,
                                                                        final long orderId,
                                                                        final String payPwd,
                                                                        @NonNull final Callback<Boolean> callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.ORDER_PAY_BY_SCORE_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("oid", orderId);
        requestParams.addParameter("u", CpigeonData.getInstance().getUserId(context));
        requestParams.addParameter("p", EncryptionTool.encryptAES(payPwd));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("订单支付完成");
                        callback.onSuccess(true);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 余额支付
     *
     * @param context
     * @param orderId
     * @param payPwd
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable orderPayByBalance(Context context,
                                                                          final long orderId,
                                                                          final String payPwd,
                                                                          @NonNull final Callback<Boolean> callback) {

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.ORDER_PAY_BY_BALANCE_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("oid", orderId);
        requestParams.addParameter("u", CpigeonData.getInstance().getUserId(context));
        requestParams.addParameter("p", EncryptionTool.encryptAES(payPwd));
        requestParams.addHeader("u", CommonTool.getUserToken(context));
        addApiSign(requestParams);
        return x.http().get(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("订单支付完成");
                        callback.onSuccess(true);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 添加评论
     *
     * @param subjectId
     * @param typeName
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable addComment(Context context,
                                                                   final long subjectId,
                                                                   final String typeName,
                                                                   final String content,
                                                                   @NonNull final Callback<Boolean> callback) {


        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.ADD_COMMENTS_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("i", subjectId);
        requestParams.addParameter("t", typeName);
        requestParams.addBodyParameter("c", content);
        requestParams.addHeader("u", CommonTool.getUserToken(context));
//      requestParams.setConnectTimeout(CpigeonConfig.CONNECT_TIMEOUT);

        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Logger.i("添加评论完成");
                        callback.onSuccess(true);
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }


    /**
     * 添加反馈(数据未保存到SQLite)
     *
     * @param context
     * @param content
     * @param phoneNum
     * @param callback
     * @return
     */
    public static org.xutils.common.Callback.Cancelable addFeedback(final Context context,
                                                                    final String content,
                                                                    final String phoneNum,
                                                                    @NonNull final Callback<Boolean> callback) {


        //计算TOKEN
        String userToken = CommonTool.getUserToken(context);

        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.FEEDBACK_URL);
        pretreatmentParams(requestParams);
        requestParams.addParameter("uid", CpigeonData.getInstance().getUserId(context));
        requestParams.addParameter("t", "android");
        requestParams.addParameter("pnum", phoneNum);
        requestParams.addBodyParameter("c", content);
        requestParams.addHeader("u", userToken);

//        requestParams.setConnectTimeout(CpigeonConfig.CONNECT_TIMEOUT);
        return x.http().post(requestParams, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (result != null) dealData(result);
            }

            private void dealData(String result) {
                Logger.i(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status") && !obj.isNull("data")) {
                        callback.onSuccess(obj.getBoolean("data"));
                    } else {
                        callback.onError(Callback.ERROR_TYPE_API_RETURN, obj.getInt("errorCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(Callback.ERROR_TYPE_PARSING_EXCEPTION, 0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(Callback.ERROR_TYPE_REQUST_EXCEPTION, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    /**
     * 预处理处理请求配置
     *
     * @param requestParams
     * @return
     */
    public static void pretreatmentParams(RequestParams requestParams) {
        requestParams.setConnectTimeout(8000);
        requestParams.setMaxRetryCount(1);
    }

    /**
     * 自动计算并添加API签名信息
     *
     * @param requestParams
     * @return
     */
    public static String addApiSign(RequestParams requestParams) {
        Map<String, String> map = new TreeMap<String, String>();
        boolean hasTimestamp = false;
        for (KeyValue para : requestParams.getQueryStringParams()) {
            if (para.key != null && ("sign".equals(para.key.toLowerCase()) || TextUtils.isEmpty(para.value.toString())))
                continue;
            if (!map.containsKey("get_" + para.key)) {
                map.put("get_" + para.key, para.value.toString());
            }
            if ("timestamp".equals(para.key)) hasTimestamp = true;
        }
        if (!hasTimestamp) {
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            requestParams.addQueryStringParameter("timestamp", timestamp);
            map.put("get_timestamp", timestamp);
        }
        for (KeyValue para : requestParams.getBodyParams()) {
            if (!map.containsKey("post_" + para.key) && para.value != null &&
                    para.value instanceof String && !TextUtils.isEmpty(para.value.toString())) {
                map.put("post_" + para.key, para.value.toString());
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            stringBuilder.append(key + "=" + map.get(key) + "&");
        }
        stringBuilder.append("key=" + Const.KEY_API_SIGN);
        String result = stringBuilder.toString();
        Logger.d(result);
        result = EncryptionTool.MD5(result);
        requestParams.addQueryStringParameter("sign", result);
        return result;
    }

    /**
     * 计算签名
     *
     * @param urlParams  url参数集合
     * @param postParams post参数集合
     * @return
     */
    public static String getApiSign(List<KeyValue> urlParams, List<KeyValue> postParams) {
        Map<String, String> map = new TreeMap<>();

        for (KeyValue para : urlParams) {
            if (para.key != null && ("sign".equals(para.key.toLowerCase()) || TextUtils.isEmpty(para.value.toString())))
                continue;
            if (!map.containsKey("get_" + para.key)) {
                map.put("get_" + para.key, para.value.toString());
            }
        }
        for (KeyValue para : postParams) {
            if (!map.containsKey("post_" + para.key) && para.value != null &&
                    para.value instanceof String && !TextUtils.isEmpty(para.value.toString())) {
                map.put("post_" + para.key, para.value.toString());
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            stringBuilder.append(key + "=" + map.get(key) + "&");
        }
        stringBuilder.append("key=" + Const.KEY_API_SIGN);
        String result = stringBuilder.toString();
        Logger.d(result);
        result = EncryptionTool.MD5(result);
        return result;
    }

    public static String getCacheKey(String apiName, RequestParams requestParams) {
        StringBuilder builder = new StringBuilder();
        builder.append(apiName);
        for (KeyValue para : requestParams.getQueryStringParams()) {
            builder.append(String.format("&get_%s=%s", para.key, para.value.toString()));
        }
        for (KeyValue para : requestParams.getBodyParams()) {
            builder.append(String.format("&post_%s=%s", para.key, para.value.toString()));
        }
        Logger.d(builder.toString());
        return builder.toString();
    }


    public interface Callback<E> {
        int NO_ERROR = -1;
        int ERROR_TYPE_OTHER = 0;//其他
        int ERROR_TYPE_NOT_NETWORK = 1;//没有网络
        int ERROR_TYPE_TIMEOUT = 2;//超时
        int ERROR_TYPE_NOT_LOGIN = 3;//没有登录
        int ERROR_TYPE_PARSING_EXCEPTION = 4;//解析异常
        int ERROR_TYPE_API_RETURN = 5;//API返回代码
        int ERROR_TYPE_REQUST_EXCEPTION = 6;//请求异常
        int ERROR_TYPE_SAVE_TO_DB_EXCEPTION = 7;//数据保存异常

        void onSuccess(E data);

        void onError(int errorType, Object data);
    }

    /**
     * 数据类型
     */
    public static class DATATYPE {
        /**
         * 验证码类型
         */
        public enum YZM {
            NONE(0),
            REGIST(1),//验证码类型-注册
            FIND_PASSWORD(2),//验证码类型-找回密码
            RESET_PAY_PWD(3);//验证码类型-重置支付密码

            final int value;

            YZM(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }

            public static YZM valueOf(int val) {
                switch (val) {
                    case 1:
                        return REGIST;
                    case 2:
                        return FIND_PASSWORD;
                    case 3:
                        return RESET_PAY_PWD;
                    default:
                        return NONE;
                }
            }
        }

        /**
         * 比赛类型
         */
        public enum MATCH {
            ALL(3),//所有
            GP(1),//公棚
            XH(2);//协会

            final int value;

            MATCH(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }
    }
}