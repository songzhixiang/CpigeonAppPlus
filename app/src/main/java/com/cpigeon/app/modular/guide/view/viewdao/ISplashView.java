package com.cpigeon.app.modular.guide.view.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;

/**
 * Created by chenshuai on 2017/4/10.
 */

public interface ISplashView extends IView {

    /**
     * 倒计时
     */
    void countDown();

    /**
     * 进入APP
     */
    void entryApp();

    /**
     * 显示广告
     *
     * @param url
     */
    void showAd(String url);
}
