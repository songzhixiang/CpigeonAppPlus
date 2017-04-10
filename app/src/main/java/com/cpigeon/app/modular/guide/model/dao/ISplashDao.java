package com.cpigeon.app.modular.guide.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;

/**
 * Created by chenshuai on 2017/4/10.
 */

public interface ISplashDao extends IBaseDao {
    interface OnLoadCompleteListener {
        void onLoadComplete(String url);
    }

    /**
     * 获取服务器的广告信息
     */
    void getSplashADFromServer();

    /**
     * 自动登录
     */
    void autoiLogin();

    /**
     * 从本地加载Splash广告数据
     */
    void loadSplashAdUrl(OnLoadCompleteListener onLoadCompleteListener);
}
