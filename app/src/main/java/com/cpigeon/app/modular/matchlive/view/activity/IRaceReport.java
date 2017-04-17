package com.cpigeon.app.modular.matchlive.view.activity;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface IRaceReport extends IView{
    /**
     * 获取传过来的赛事信息
     * @return
     */
    MatchInfo getMatchInfo();

    /**
     * 显示报道数据
     * @param list
     */
    void showReportData(List list);

    /**
     * 显示集鸽数据
     * @param list
     */
    void showPigeonData(List list);


    /**
     * 添加赛鸽的观看量
     * @param data
     */
    void addRaceClickCount(Boolean data);


}
