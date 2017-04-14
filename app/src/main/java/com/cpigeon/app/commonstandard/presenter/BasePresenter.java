package com.cpigeon.app.commonstandard.presenter;

import android.os.Handler;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;

/**
 * Created by Administrator on 2017/4/6.
 */

public abstract class BasePresenter<TView extends IView, TDao extends IBaseDao> {

    protected TView mView;

    protected TDao mDao;

    protected Handler mHandler;

    public BasePresenter(TView mView) {
        this.mView = mView;
        this.mHandler=new Handler();
        mDao=initDao();
    }

    protected abstract TDao initDao();

    public void dettach() {
        this.mView = null;
        this.mHandler=null;
    }


}