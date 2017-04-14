package com.cpigeon.app.modular.home.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.home.model.bean.HomeAd;
import com.cpigeon.app.modular.home.model.dao.IHomeFragmentDao;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.utils.CpigeonConfig;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class HomeFragmentDaoImpl implements IHomeFragmentDao {

    @Override
    public void loadHomeAd(OnCompleteListener<List<HomeAd>> listener) {
        String adJsonStr = (String) SharedPreferencesTool.Get(MyApp.getInstance(), "ad", "");
        try {
            JSONArray array = new JSONArray(adJsonStr);
            if (array.length()>0)
            {
                List<HomeAd> adList = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date _begin, _end;
                for (int i = 0; i < array.length(); i++) {
                    try {
                        _begin = sdf.parse(array.getJSONObject(i).getString("start"));
                        _end = sdf.parse(array.getJSONObject(i).getString("end"));
                        if (array.getJSONObject(i).getInt("type") == 2 &&
                                array.getJSONObject(i).getBoolean("enable") == true &&
                                _begin.getTime() < new Date().getTime() && _end.getTime() > new Date().getTime()) {
                            final String imageUrl = array.getJSONObject(i).getString("adImageUrl");
                            final String adurl = array.getJSONObject(i).getString("adUrl");
                            Logger.i("第" + (i + 1) + "个下方广告;URL=" + imageUrl);
                            adList.add(new HomeAd(imageUrl, adurl));
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                if (listener!=null)
                {
                    listener.onSuccess(adList);
                }
            }
        } catch (JSONException e) {
            listener.onFail(e.getMessage());
        }
    }

    @Override
    public void loadMatchInfo(final int loadType, final OnCompleteListener<List<MatchInfo>> listenr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbManager db = x.getDb(CpigeonConfig.getDataDb());
                try {
                    if (loadType == 1)
                    {
                        final List<MatchInfo> listXH = db.selector(MatchInfo.class)
                                .where("lx", "=", "xh")
                                .and("st", ">", DateTool.getDayBeginTimeStr(new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * CpigeonConfig.LIVE_DAYS_XH)))
                                .and("dt", "=", "bs")
                                .orderBy("dt", true)
                                .orderBy("st", true)
                                .findAll();
                        listenr.onSuccess(listXH);
                    }else if (loadType == 0)
                    {
                        final List<MatchInfo> listGP = db.selector(MatchInfo.class)
                                .where("lx", "=", "gp")
                                .and("st", ">", DateTool.getDayBeginTimeStr(new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * CpigeonConfig.LIVE_DAYS_GP)))
                                .and("dt", "=", "bs")
                                .orderBy("dt", true)
                                .orderBy("st", true)
                                .findAll();
                        listenr.onSuccess(listGP);
                    }


                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
