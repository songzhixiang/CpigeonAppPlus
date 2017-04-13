package com.cpigeon.app.commonstandard.model.daoimpl;


import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IGetUserBandPhone;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonData;

import java.util.Map;

/**
 * Created by chenshuai on 2017/4/11.
 */

public class GetUserBandPhoneImpl implements IGetUserBandPhone {
    @Override
    public void getUserBandPhone(final OnCompleteListener onCompleteListener) {
        CallAPI.getUserBandPhone(MyApp.getInstance(), new CallAPI.Callback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                String userPhone = (String) data.get("phone");
                if ((int) data.get("band") == 1) {
                    CpigeonData.getInstance().setUserBindPhone(userPhone);
                }
                if (onCompleteListener != null)
                    onCompleteListener.onSuccess(userPhone, (int) data.get("band") == 1);
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onCompleteListener != null)
                    onCompleteListener.onFail();
            }
        });
    }
}
