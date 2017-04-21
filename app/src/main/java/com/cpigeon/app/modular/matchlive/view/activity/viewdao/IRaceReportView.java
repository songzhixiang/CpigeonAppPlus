package com.cpigeon.app.modular.matchlive.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

/**
 * Created by Administrator on 2017/4/18.
 */

public interface IRaceReportView extends IView{

    MatchInfo getMatchInfo();
    String getSsid();
    String getLx();
    void showBulletin(Bulletin bulletin);
}
