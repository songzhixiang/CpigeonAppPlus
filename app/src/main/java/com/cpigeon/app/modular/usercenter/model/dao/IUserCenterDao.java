package com.cpigeon.app.modular.usercenter.model.dao;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface IUserCenterDao {
    void loadUserBalance(OnLoadCompleteListener listener);
    interface OnLoadCompleteListener{
        void loadSuccess(Map<String,Object> data);
        void laodFailed();
    }
    void getUserSignStatus(OnGetCompleteListener listener);
    interface OnGetCompleteListener{
        void loadSuccess(Boolean isSign);
        void loadFailed();
    }
}
