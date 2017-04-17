package com.cpigeon.app.modular.matchlive.model.daoimpl;

import android.content.Context;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsXH;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.modular.matchlive.model.dao.IRaceReportDao;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonConfig;
import com.orhanobut.logger.Logger;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Administrator on 2017/4/15.
 */

public class RaceReportDaoImpl implements IRaceReportDao{
    private final static int DATA_TYPE_BDSJ = 1;//数据类型-报道数据
    private int CURRENT_DATA_TYPE;//当前数据类型
    private List<HashMap<String, Object>> data_BDSJ = null;//显示数据缓存-报道数据
    private MatchInfo matchInfo;//赛事信息
    private final static int mLoadMoreSize = 100;
    private Context mContext = MyApp.getInstance();
    /**
     * 获取报道数据
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
    public void showReprotData(String matchType, String ssid, String foot, String name, boolean hascz, int pager, int pagesize, int czIndex, String sKey, final OnCompleteListener<List> onCompleteListener) {
        CallAPI.getReportData(mContext, matchType, ssid, "", "", hascz, pager, pagesize, czIndex, sKey, new CallAPI.Callback<List>() {
            @Override
            public void onSuccess(List data) {
                Logger.e(data+"");
                onCompleteListener.onSuccess(data);

            }

            @Override
            public void onError(int errorType, Object data) {

            }
        });
    }

    /**
     * 获取集鸽数据
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
    public void showPigeonData(String matchType, String ssid, String foot, String name, boolean hascz, int pager, int pagesize, int czIndex, String sKey, final OnCompleteListener<List> onCompleteListener) {
        CallAPI.getPigeonsData(mContext, matchType, ssid, "", "", hascz, pager, pagesize, czIndex, sKey, new CallAPI.Callback<List>() {
            @Override
            public void onSuccess(List data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {

            }
        });
    }


    private List<HashMap<String, Object>> loadData(final int offset, final int limit, final int czIndex) {
        List list = null;
        list = getLocalReportDataPage(offset, limit, czIndex);
        HashMap<String, Object> map;
        if (list !=null){
            if ("xh".equals(matchInfo.getLx())) {
                MatchReportXH matchReport;
                for (int i = data_BDSJ.size(); i < list.size(); i++) {
                    matchReport = (MatchReportXH) list.get(i);
                    map = new HashMap<String, Object>();
                    //必须项
                    map.put("showtype", 1);
                    map.put("mc", matchReport.getMc());
                    map.put("name", matchReport.getName());
                    map.put("foot", matchReport.getFoot());
                    //详细项
                    map.put("pn", matchReport.getPn());//棚号
                    map.put("kj", matchReport.getSp() + "KM");//空距
                    map.put("speed", matchReport.getSpeed() + "M");//分速
                    map.put("arrive", matchReport.getArrive());//归巢时间
                    map.put("dj", matchReport.getZx() + "/" + matchReport.getZy());//登记经纬度
                    map.put("dc", matchReport.getDczx() + "/" + matchReport.getDczy());//当次经纬度
                    map.put("cz", matchReport.CZtoString());//插组信息
                    data_BDSJ.add(map);
                }

            }
        }
        return data_BDSJ;
    }

    private List getLocalReportDataPage(int offset, int limit, int czIndex) {
        List list = null;
        DbManager db = x.getDb(CpigeonConfig.getDataDb());
        try {
            String key = "%%";
            Selector selector;
            if (CURRENT_DATA_TYPE == DATA_TYPE_BDSJ) {
                //报道数据和插组报道需要的数据
                if ("xh".equals(matchInfo.getLx())) {
                    selector = db.selector(MatchReportXH.class);
                    selector.orderBy("speed", true);
                } else {
                    selector = db.selector(MatchReportGP.class);
                    selector.orderBy("mc");
                }
            } else {
                //集鸽数据（上笼清单）和插组指定需要的数据
                selector = db.selector("xh".equals(matchInfo.getLx()) ? MatchPigeonsXH.class : MatchPigeonsGP.class).orderBy("jgtime");
            }

            selector.where("ssid", "=", matchInfo.getSsid());
            selector.groupBy("foot");
            if (!"%%".equals(key))
                selector.and(WhereBuilder.b().or("name", "like", key));
            if (czIndex > 0 && czIndex <= 24)
                selector.and("c" + czIndex, "=", true);
            selector.offset(offset);
            selector.limit(limit);
            //Logger.i("selector=" + selector.toString());
            list = selector.findAll();
            //Logger.i("本地记录大小：" + (list != null ? list.size() : 0) + ";ssid:" + matchInfo.getSsid());
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
