package com.cpigeon.app.modular.order.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.dao.OrderDao;
import com.cpigeon.app.utils.CallAPI;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class OrderDaoImpl implements OrderDao {


    @Override
    public void getUserAllOrder(final int ps,final int pi,final String query, final OnCompleteListener<List<CpigeonOrderInfo>> listener) {

        CallAPI.getUserOrderList(MyApp.getInstance(), ps, pi, query, new CallAPI.Callback<List<CpigeonOrderInfo>>() {
            @Override
            public void onSuccess(List<CpigeonOrderInfo> data) {
                listener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                listener.onFail(errorType + "");
            }
        });
    }
}
