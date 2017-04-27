package com.cpigeon.app.modular.matchlive.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.dao.IJiGeDao;
import com.cpigeon.app.utils.CallAPI;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class IJiGeDaoImpl implements IJiGeDao {
    @Override
    public void laodJiGeData(String matchType, String ssid, String foot, String name, boolean hascz, int pager, int pagesize, int czIndex, String sKey, final OnCompleteListener<List> listOnCompleteListener) {
        CallAPI.getPigeonsData(MyApp.getInstance(), matchType, ssid, foot, name, hascz, pager, pagesize, czIndex, sKey, new CallAPI.Callback<List>() {
            @Override
            public void onSuccess(List data) {
                Logger.d(data == null ? "null" : data.size() + "");

                listOnCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                listOnCompleteListener.onFail("加载失败");
            }
        });
    }
}
