package com.cpigeon.app.modular.home.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.home.view.fragment.IHomeView;
import com.cpigeon.app.modular.home.model.daoimpl.AdDaoImpl;

/**
 * Created by Administrator on 2017/4/6.
 */

public class HomePresenter extends BasePresenter<IHomeView,IBaseDao>{
    public HomePresenter(IHomeView mView) {
        super(mView);
//        mModel = new AdDaoImpl();
    }


}
