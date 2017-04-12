package com.cpigeon.app.modular.order.model.dao;

import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public interface OrderDao {
    void getUserAllOrder(int ps,int pi,String query,OnLoadCompleteListener listener);
    interface OnLoadCompleteListener{
        void loadSuccess(List<CpigeonOrderInfo> orderInfos);
        void loadFailed();
    }
}
