package com.cpigeon.app.modular.usercenter.fragment.viewdao;

import com.cpigeon.app.commonstandard.activity.IView;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface IUserCenterView extends IView{
    void showUserInfo(Map<String,Object> data);
    void isSign(Boolean data);
}
