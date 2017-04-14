package com.cpigeon.app.modular.footsearch.model.daoimpl;

import android.content.Context;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.footsearch.model.dao.ICpigeonServicesInfo;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonUserServiceInfo;
import com.cpigeon.app.utils.CallAPI;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/8.
 */

public class CpigeonServicesInfoImpl implements ICpigeonServicesInfo {
    private Context mContext = MyApp.getInstance();

    @Override
    public void getFootSearchService(String query, final OnCompleteListener<CpigeonUserServiceInfo> listener) {
        CallAPI.getUserService(mContext, query, new CallAPI.Callback<CpigeonUserServiceInfo>() {
            @Override
            public void onSuccess(CpigeonUserServiceInfo data) {
                listener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                listener.onFail(""+errorType);
            }
        });
    }

    @Override
    public void queryFoot(String key, final OnCompleteListener<Map<String, Object>> listener) {
        CallAPI.footQuery(mContext, key, new CallAPI.Callback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                listener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                listener.onFail(errorType+"");
            }
        });
    }
}
