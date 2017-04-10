package com.cpigeon.app.modular.usercenter.model.daoimpl;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.modular.usercenter.model.dao.ILoginDao;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.EncryptionTool;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenshuai on 2017/4/7.
 */

public class LoginDaoImpl implements ILoginDao {
    OnLoginListener onLoginListener;
    OnLoadUserHeadImageListener onLoadUserHeadImageListener;
    private int mGetUserHeadImg = 0;
    private Handler mGetUserHeadImgHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == mGetUserHeadImg) {
                Logger.d(mGetUserHeadImg);
                if (msg.obj == null || TextUtils.isEmpty(msg.obj.toString())) {
                    if (onLoadUserHeadImageListener != null)
                        onLoadUserHeadImageListener.onError(null);
                    return;
                }

                CallAPI.getUserHeadImg(MyApp.getInstance(), msg.obj.toString(), new CallAPI.Callback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        if (data != null && !TextUtils.isEmpty(data)) {
                            Logger.d("data=" + data);
                            if (onLoadUserHeadImageListener != null)
                                onLoadUserHeadImageListener.onSuccess(data);
                        }
                    }

                    @Override
                    public void onError(int errorType, Object data) {
                        if (onLoadUserHeadImageListener != null)
                            onLoadUserHeadImageListener.onError(null);
                    }
                });
            }
        }
    };

    @Override
    public void login(String username, String password, final OnLoginListener onLoginListener) {
        //检查数据是否正常
        if (username == null || username.equals(""))
            if (onLoginListener != null) {
                onLoginListener.loginPreError(OperateCheck.UsernameIsEmpty);
                return;
            }

        if (password == null || password.equals(""))
            if (onLoginListener != null) {
                onLoginListener.loginPreError(OperateCheck.PasswordIsEmpty);
                return;
            }

        this.onLoginListener = onLoginListener;
        RequestParams params = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.LOGIN_URL);
        CallAPI.pretreatmentParams(params);
        params.addBodyParameter("u", username);
        params.addBodyParameter("t", "1");
        params.addBodyParameter("p", EncryptionTool.encryptAES(password));
        CallAPI.addApiSign(params);
        params.setCacheMaxAge(0);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {

                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("status")) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        JSONObject data = obj.getJSONObject("data");
                        map.put("username", data.getString("yonghuming"));
                        map.put("token", data.getString("token"));
                        map.put("touxiang", data.getString("touxiang"));
                        map.put("touxiangurl", data.getString("touxiangurl"));
                        map.put("nicheng", data.getString("nicheng"));
                        map.put("logined", true);
                        map.put("userid", Integer.valueOf(EncryptionTool.decryptAES(data.getString("token")).split("\\|")[0]));
                        CpigeonData.getInstance().setUserId((int) map.get("userid"));
                        SharedPreferencesTool.Save(MyApp.getInstance(), map, SharedPreferencesTool.SP_FILE_LOGIN);
//                        Intent intent = new Intent(mContext, HomeActivity.class);
//                        startActivity(intent);
//                        finish();
                        if (onLoginListener != null)
                            onLoginListener.loginSuccess();
                    } else {
                        int errorcode = obj.getInt("errorCode");
                        String msg = "登录失败";
                        switch (errorcode) {
                            case 1000:
                                msg = "用户名或密码不能为空";
                                break;
                            case 1001:
                                msg = "用户名或密码错误";
                                break;
                        }
//                        showShortToast(msg);
                        SharedPreferencesTool.Save(MyApp.getInstance(), "logined", false, SharedPreferencesTool.SP_FILE_LOGIN);
                        if (onLoginListener != null)
                            onLoginListener.loginFailed(msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                String msg = "登录失败";
                if (ex instanceof ConnectException)
                    msg = "网络无法连接，请检查您的网络";
                else
                    msg = "登录发生错误，请稍候再试";
                if (onLoginListener != null)
                    onLoginListener.loginFailed(msg);
                SharedPreferencesTool.Save(MyApp.getInstance(), "logined", false, SharedPreferencesTool.SP_FILE_LOGIN);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });

    }

    @Override
    public void loadUserHeadImg(String username, final OnLoadUserHeadImageListener onLoadUserHeadImageListener) {
        this.onLoadUserHeadImageListener = onLoadUserHeadImageListener;
        mGetUserHeadImgHandler.sendMessageDelayed(mGetUserHeadImgHandler.obtainMessage(++mGetUserHeadImg, username), 1000);
    }
}
