package com.cpigeon.app.modular.matchlive.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.dao.IChaZuDao;
import com.cpigeon.app.modular.matchlive.model.daoimpl.IChaZuDaoImpl;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IChaZuReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class ChaZuReportPre extends BasePresenter<IChaZuReport, IChaZuDao> {

    private List<HashMap<String, Object>> data_CZTJ = null;//显示数据缓存-插组统计数据

    public ChaZuReportPre(IChaZuReport mView) {
        super(mView);
    }

    @Override
    protected IChaZuDao initDao() {
        return new IChaZuDaoImpl();
    }

    public void loadChaZuReport() {
        if (isAttached()) {
            if (data_CZTJ == null) {
                data_CZTJ = new ArrayList<HashMap<String, Object>>();
            }

            mDao.loadChaZuTongJi(mView.getLx(), mView.getSsid(), new IBaseDao.OnCompleteListener<List<HashMap<String, Object>>>() {
                @Override
                public void onSuccess(final List<HashMap<String, Object>> data) {
                    post(new CheckAttachRunnable() {
                        @Override
                        protected void runAttached() {
                            if (data.size() == 2) {
                                HashMap<String, Object> map;
                                HashMap<String, Object> map_jg = "jg".equals((data.get(0)).get("dt")) ? data.get(0) : data.get(1);
                                HashMap<String, Object> map_bd = "bd".equals((data.get(0)).get("dt")) ? data.get(0) : data.get(1);
                                for (char z = 'A'; z <= 'X'; z++) {
                                    map = new HashMap<String, Object>();
                                    map.put("showtype", 2);
                                    map.put("group", z);
                                    map.put("gcys", map_bd.get("c" + (z - 'A' + 1)));
                                    map.put("sfys", map_jg.get("c" + (z - 'A' + 1)));
                                    data_CZTJ.add(map);
                                }
                            }
                            mView.showChaZuBaoDaoView(data_CZTJ);
                        }
                    });
                }

                @Override
                public void onFail(String msg) {
                    post(new CheckAttachRunnable() {
                        @Override
                        protected void runAttached() {
                            mView.showTips("获取失败", IView.TipType.View);
                        }
                    });
                }
            });
        }
    }
}
