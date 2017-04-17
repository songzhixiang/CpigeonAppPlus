package com.cpigeon.app.modular.matchlive.view.fragment;

import com.cpigeon.app.commonstandard.view.activity.IView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public interface IReportData extends IView{
    void showData(List list);
    String getMatchType();
    String getSsid();
    String getFoot();
    String getName();
    boolean hascz();
    int getPager();
    int getPagerSize();
    int czIndex();
    String sKey();
}
