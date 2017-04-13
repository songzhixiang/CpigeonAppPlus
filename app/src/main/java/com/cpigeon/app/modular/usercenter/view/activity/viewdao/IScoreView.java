package com.cpigeon.app.modular.usercenter.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/13.
 */

public interface IScoreView extends IView {

    interface IScoreSub2View extends IView {
        int getPageIndex();

        int getPageSize();

        void loadScoreRecord(List<UserScore> data);
        void loadScoreRecordError();
    }
}
