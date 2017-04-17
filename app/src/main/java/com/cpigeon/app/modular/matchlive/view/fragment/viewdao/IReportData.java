package com.cpigeon.app.modular.matchlive.view.fragment.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public interface IReportData extends IView{
    ///////////////////////////////////////////////////////////////////////////
    // 
    ///////////////////////////////////////////////////////////////////////////
    //显示报道数据View
    void showData(List list);
    //显示集鸽数据View
    void showJiGeData(List list);


    ///////////////////////////////////////////////////////////////////////////
    // 
    ///////////////////////////////////////////////////////////////////////////
    //获取比赛类型：1.公棚；2.协会
    String getMatchType();
    //获取比赛的SSID
    String getSsid();
    //足环号码（不加密，可选）
    String getFoot();
    //鸽主姓名（与足环号码结合，定位记录，可选）
    String getName();
    //是否包含插组信息（1：包含，默认不包含，可选）
    boolean hascz();
    //页码 -1获取所有
    int getPager();
    //获取条数 0不获取
    int getPagerSize();
    //插组索引（1,2,3…，用于查询某个组的信息，可选）
    int czIndex();
    //查询关键字（主要用于根据足环来查询的功能）
    String sKey();
}
