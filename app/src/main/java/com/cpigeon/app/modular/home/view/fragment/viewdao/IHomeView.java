package com.cpigeon.app.modular.home.view.fragment.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.home.model.bean.AD;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface IHomeView extends IView{
    void showAd(List<AD> adList);
    void showMatchLiveData(List list);
}
