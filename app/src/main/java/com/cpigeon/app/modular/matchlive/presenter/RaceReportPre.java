package com.cpigeon.app.modular.matchlive.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.dao.IRaceReport;
import com.cpigeon.app.modular.matchlive.model.daoimpl.IRaceReportImpl;
import com.cpigeon.app.modular.matchlive.view.activity.viewdao.IRaceReportView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class RaceReportPre extends BasePresenter<IRaceReportView,IRaceReport> {
    public RaceReportPre(IRaceReportView mView) {
        super(mView);
    }


    public void showBulletin()
    {
        if (isAttached())
        {
            mDao.updateBulletin(mView.getLx(), mView.getSsid(), new IBaseDao.OnCompleteListener<List<Bulletin>>() {
                @Override
                public void onSuccess(List<Bulletin> data) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (isAttached())

                                mDao.queryBulletin(mView.getSsid(), new IBaseDao.OnCompleteListener<Bulletin>() {
                                @Override
                                public void onSuccess(Bulletin data) {
                                    mView.showBulletin(data);
                                }

                                @Override
                                public void onFail(String msg) {

                                }
                            });
                        }
                    });

                }

                @Override
                public void onFail(String msg) {

                }
            });
        }
    }

    @Override
    protected IRaceReport initDao() {
        return new IRaceReportImpl();
    }
}
