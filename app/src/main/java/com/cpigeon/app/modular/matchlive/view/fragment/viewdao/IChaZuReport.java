package com.cpigeon.app.modular.matchlive.view.fragment.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public interface IChaZuReport extends IView {
    void showChaZuBaoDaoView(List list);
    String getLx();
    String getSsid();

}
