package com.cpigeon.app.modular.matchlive.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.matchlive.model.dao.IRaceReportDao;
import com.cpigeon.app.modular.matchlive.model.daoimpl.RaceReportDaoImpl;
import com.cpigeon.app.modular.matchlive.view.fragment.IReportData;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class RaceReportPre extends BasePresenter<IReportData,IRaceReportDao> {
    public RaceReportPre(IReportData mView) {
        super(mView);
    }

    @Override
    protected IRaceReportDao initDao() {
        return new RaceReportDaoImpl();
    }
    public void loadRaceData(){
        mDao.showReprotData(mView.getMatchType(), mView.getSsid(), mView.getFoot(), mView.getName(),
                mView.hascz(), mView.getPager(), mView.getPagerSize(), mView.czIndex(), mView.sKey(), new IBaseDao.OnCompleteListener<List>() {
                    @Override
                    public void onSuccess(List data) {
                        mView.showData(data);
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
    }
}
