package com.cpigeon.app.modular.usercenter.presenter;

import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.usercenter.model.dao.IFeedBackDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.FeedBackDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IFeedBackView;

/**
 * Created by chenshuai on 2017/4/11.
 */

public class FeedBackPresenter extends BasePresenter<IFeedBackView, IFeedBackDao> {
    public FeedBackPresenter(IFeedBackView mView) {
        super(mView);
        mModel = new FeedBackDaoImpl();
    }

}
