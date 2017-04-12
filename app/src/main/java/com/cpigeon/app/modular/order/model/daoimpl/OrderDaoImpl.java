package com.cpigeon.app.modular.order.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.dao.OrderDao;
import com.cpigeon.app.utils.CallAPI;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class OrderDaoImpl implements OrderDao {
    @Override
    public void getUserAllOrder(int ps, int pi, String query, final OnLoadCompleteListener listener) {
        CallAPI.getUserOrderList(MyApp.getInstance(), ps, pi, query, new CallAPI.Callback<List<CpigeonOrderInfo>>() {
            @Override
            public void onSuccess(List<CpigeonOrderInfo> data) {
                listener.loadSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                listener.loadFailed();
            }
        });
    }
}
