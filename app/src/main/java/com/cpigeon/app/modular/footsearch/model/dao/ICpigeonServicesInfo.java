package com.cpigeon.app.modular.footsearch.model.dao;

import com.cpigeon.app.modular.usercenter.model.bean.CpigeonUserServiceInfo;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/8.
 */

public interface ICpigeonServicesInfo {
    void getFootSearchService(String query,OnLoadCompleteListener listener);
    interface OnLoadCompleteListener{
        void loadSuccess(CpigeonUserServiceInfo serviceInfo);
        void laodFailed(String msg);
    }
    void queryFoot(String key,OnQueryCompleteListener listener);
    interface OnQueryCompleteListener{
        void querySuccess(Map<String, Object> map);
        void laodFailed(String msg);
    }
}
