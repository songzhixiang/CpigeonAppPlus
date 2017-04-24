package com.cpigeon.app.modular.matchlive.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.dao.IChaZuDao;
import com.cpigeon.app.utils.CallAPI;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class IChaZuDaoImpl implements IChaZuDao {
    @Override
    public void loadChaZuTongJi(String lx, String ssid, final IBaseDao.OnCompleteListener<List<HashMap<String, Object>>> onCompleteListener) {
        CallAPI.getRaceGroupsCount(MyApp.getInstance(), lx, ssid, new CallAPI.Callback<List<HashMap<String, Object>>>() {
            @Override
            public void onSuccess(List<HashMap<String, Object>> data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                onCompleteListener.onFail(errorType+"");
            }
        });
    }

    /**
     * 获取插组数据的详情
     * @param matchType
     * @param ssid
     * @param foot
     * @param name
     * @param hascz
     * @param pager
     * @param pagesize
     * @param czIndex
     * @param sKey
     * @param onCompleteListener
     */
    @Override
    public void loadChaZuBaoDaoDetails(String matchType, String ssid, String foot, String name, boolean hascz, int pager, int pagesize, int czIndex, String sKey, final OnCompleteListener<List> onCompleteListener) {
        CallAPI.getReportData(MyApp.getInstance(), matchType, ssid, foot, name, hascz, pager, pagesize, czIndex, sKey, new CallAPI.Callback<List>() {
            @Override
            public void onSuccess(List data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                onCompleteListener.onFail(errorType+"");
            }
        });
    }

    @Override
    public void loadChaZhiDingDaoDetails(String matchType, String ssid, String foot, String name, boolean hascz, int pager, int pagesize, int czIndex, String sKey, final OnCompleteListener<List> onCompleteListener) {
        CallAPI.getPigeonsData(MyApp.getInstance(), matchType, ssid, foot, name, hascz, pager, pagesize, czIndex, sKey, new CallAPI.Callback<List>() {
            @Override
            public void onSuccess(List data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                onCompleteListener.onFail(errorType+"");
            }
        });
    }


}
