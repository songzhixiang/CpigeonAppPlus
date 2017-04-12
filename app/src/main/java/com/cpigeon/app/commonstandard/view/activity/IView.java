package com.cpigeon.app.commonstandard.view.activity;

import android.support.design.widget.Snackbar;

import com.cpigeon.app.commonstandard.presenter.BasePresenter;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface IView {
    enum TipType {
        View,
        ViewSuccess,
        ViewError,
        Dialog,
        DialogSuccess,
        DialogError,
        ToastLong,
        ToastShort,
        SnackbarShort,
        SnackbarLong
    }

    void showLoading();

    void hideLoading();

    /**
     * 检查是否登录
     *
     * @return true:已登录；false：未登录
     */
    boolean checkLogin();

    /**
     * 显示提示(错误与正确)
     *
     * @param tip     提示内容
     * @param tipType 提示类型
     */
    boolean showTips(String tip, TipType tipType);


}