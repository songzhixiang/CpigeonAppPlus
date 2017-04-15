package com.cpigeon.app.modular.guide.model.daoimpl;

import android.text.TextUtils;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.modular.guide.model.dao.ISplashDao;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonConfig;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenshuai on 2017/4/10.
 */

public class SplashDaoImpl implements ISplashDao {
    @Override
    public void getSplashADFromServer() {
        RequestParams httpparams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.AD_URL);
        CallAPI.pretreatmentParams(httpparams);
        httpparams.setCacheMaxAge(CpigeonConfig.CACHE_COMMON_30MIN_TIME);
        x.http().get(httpparams, new Callback.CommonCallback<String>() {
            String result = "";

            @Override
            public void onSuccess(String result) {
                Logger.i(result);
                this.result = result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (!TextUtils.isEmpty(result))
                    SharedPreferencesTool.Save(MyApp.getInstance(), "ad", result);
            }
        });
    }


    @Override
    public void autoiLogin() {
        CallAPI.autoLogin(MyApp.getInstance(), new CallAPI.Callback<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                if (data > 0)
                    SharedPreferencesTool.Save(MyApp.getInstance(), "lastAutoLoginTime", System.currentTimeMillis());
            }

            @Override
            public void onError(int errorType, Object data) {

            }
        });
    }

    @Override
    public void loadSplashAdUrl(OnLoadCompleteListener onLoadCompleteListener) {
        if (onLoadCompleteListener == null) return;
        String adJsonStr = (String) SharedPreferencesTool.Get(MyApp.getInstance(), "ad", "");
        if (TextUtils.isEmpty(adJsonStr))
            return;
        try {
            JSONArray array = new JSONArray(adJsonStr);
            if (array.length() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date _begin, _end;
                for (int i = 0; i < array.length(); i++) {
                    try {
                        _begin = sdf.parse(array.getJSONObject(i).getString("start"));
                        _end = sdf.parse(array.getJSONObject(i).getString("end"));
                        if (array.getJSONObject(i).getInt("type") == 1 &&
                                array.getJSONObject(i).getBoolean("enable") == true &&
                                _begin.getTime() < new Date().getTime() && _end.getTime() > new Date().getTime()) {
                            final String imageUrl = array.getJSONObject(i).getString("adImageUrl");
                            onLoadCompleteListener.onLoadComplete(imageUrl);
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
}
