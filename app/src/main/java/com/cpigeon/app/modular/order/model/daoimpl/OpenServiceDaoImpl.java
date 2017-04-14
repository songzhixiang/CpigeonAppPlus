package com.cpigeon.app.modular.order.model.daoimpl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.bean.CpigeonServicesInfo;
import com.cpigeon.app.modular.order.model.dao.IOpenServiceDao;
import com.cpigeon.app.utils.CallAPI;
import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by chenshuai on 2017/4/14.
 */

public class OpenServiceDaoImpl implements IOpenServiceDao {
    @Override
    public void loadAllServicesInfo(String serviceName, @NonNull final OnCompleteListener<List<CpigeonServicesInfo>> onCompleteListener) {
        CallAPI.getServicesInfo(MyApp.getInstance(), serviceName, "1", new CallAPI.Callback<List<CpigeonServicesInfo>>() {
            @Override
            public void onSuccess(final List<CpigeonServicesInfo> data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                onCompleteListener.onFail("加载失败，请稍后再试");
            }
        });

    }

    @Override
    public void createServiceOrder(int serviecId, @NonNull final OnCompleteListener<CpigeonOrderInfo> onCompleteListener) {
        CallAPI.createServiceOrder(MyApp.getInstance(), serviecId, new CallAPI.Callback<CpigeonOrderInfo>() {
            @Override
            public void onSuccess(CpigeonOrderInfo data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                String msg = "创建订单失败,请稍后再试";
                switch (errorType) {
                    case ERROR_TYPE_API_RETURN:
                        switch ((int) data) {
                            case 20010:
                                msg = "服务尚未上架";
                                break;
                            case 20011:
                                msg = "服务已下架";
                                break;
                            case 20022:
                                msg = "服务已暂停销售";
                                break;
                            case 20023:
                                msg = "服务已停止销售";
                                break;
                        }
                        break;
                }
                onCompleteListener.onFail(msg);
            }
        });
    }
}
