package com.cpigeon.app.modular.settings.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.settings.model.dao.ISettingDao;
import com.cpigeon.app.modular.settings.model.daoimpl.SettingDaoImpl;
import com.cpigeon.app.modular.settings.view.activity.dao.ISettingView;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/18.
 */

public class SettingPresenter extends BasePresenter<ISettingView, ISettingDao> {
    public SettingPresenter(ISettingView mView) {
        super(mView);
    }

    @Override
    protected ISettingDao initDao() {
        return new SettingDaoImpl();
    }

}
