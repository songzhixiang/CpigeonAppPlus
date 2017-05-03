package com.cpigeon.app.modular.usercenter.model.daoimpl;

import android.text.TextUtils;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.cpigeon.app.modular.usercenter.model.dao.IUserInfoDao;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.SharedPreferencesTool;

import java.io.File;

/**
 * Created by chenshuai on 2017/4/11.
 */

public class UserInfoDaoImpl implements IUserInfoDao {
    @Override
    public void modifyUserInfo(UserInfo.DataBean userinfo, final OnUserinfoMotifyCompleteListener onUserinfoMotifyCompleteListener) {
        CallAPI.motifyBasicUserInfo(MyApp.getInstance(), userinfo.getNickname(),
                userinfo.getSex(), userinfo.getBrithday(),
                userinfo.getSigns(), new CallAPI.Callback<UserInfo.DataBean>() {
                    @Override
                    public void onSuccess(UserInfo.DataBean data) {
                        if (data != null) {
                            CpigeonData.getInstance().setUserInfo(data);
                            //同步登陆信息
                            SharedPreferencesTool.Get(MyApp.getInstance(), "nicheng", data.getNickname(), SharedPreferencesTool.SP_FILE_LOGIN);
                        }
                        onUserinfoMotifyCompleteListener.onSuccess();
                    }

                    @Override
                    public void onError(int errorType, Object data) {
                        onUserinfoMotifyCompleteListener.onError("修改失败");
                    }
                });
    }

    @Override
    public void updateUserFaceImage(final File file, final OnUpdateUserFaceImageCompleteListener onUpdateUserFaceImageCompleteListener) {

        CallAPI.updateUserFaceImage(MyApp.getInstance(), file, new CallAPI.Callback<String>() {
            @Override
            public void onSuccess(String data) {
                if (!TextUtils.isEmpty(data)) {
                    SharedPreferencesTool.Save(MyApp.getInstance(), "touxiangurl", data, SharedPreferencesTool.SP_FILE_LOGIN);
                    UserInfo.DataBean userInfo = CpigeonData.getInstance().getUserInfo();
                    if (userInfo != null) {
                        userInfo.setHeadimg(data);
                        CpigeonData.getInstance().setUserInfo(userInfo);
                    }
                }
                if (file.exists())
                    file.delete();
                onUpdateUserFaceImageCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                onUpdateUserFaceImageCompleteListener.onError("上传失败");
                if (file.exists()) file.delete();
            }
        });
    }
}
