package com.cpigeon.app.modular.matchlive.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.dao.IRaceReport;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonConfig;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class IRaceReportImpl implements IRaceReport {
    DbManager db = x.getDb(CpigeonConfig.getDataDb());
    private Bulletin bull = null;//公告信息

    @Override
    public void updateBulletin(String lx, String ssid, final IBaseDao.OnCompleteListener<List<Bulletin>> listOnCompleteListener) {
        CallAPI.getBulletin(MyApp.getInstance(), lx, ssid, new CallAPI.Callback<List<Bulletin>>() {
            @Override
            public void onSuccess(List<Bulletin> data) {
                if (data != null && data.size() > 0) {
                    bull = data.get(0);
                    try {
                        db.saveOrUpdate(bull);
                        db.close();
                    } catch (DbException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                listOnCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {

            }
        });
    }

    @Override
    public void queryBulletin(String ssid, IBaseDao.OnCompleteListener<Bulletin> onCompleteListener) {
        try {
            bull = db.selector(Bulletin.class).where("ssid", "=", ssid).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        onCompleteListener.onSuccess(bull);

    }

    @Override
    public void addRaceClickCount(String lx, String ssid) {
        CallAPI.addRaceClickCount(MyApp.getInstance(), "xh".equals(lx) ? CallAPI.DATATYPE.MATCH.XH : CallAPI.DATATYPE.MATCH.GP, ssid, new CallAPI.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {

            }

            @Override
            public void onError(int errorType, Object data) {

            }
        });
    }
}
