package com.cpigeon.app.modular.usercenter.presenter;

import android.text.TextUtils;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetUserBandPhone;
import com.cpigeon.app.commonstandard.model.dao.ISendVerificationCode;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.dao.ISetPayPwdDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.SetPayPwdDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.ISetPayPwdView;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.EncryptionTool;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class SetPayPwdPresenter extends BasePresenter<ISetPayPwdView, ISetPayPwdDao> {
    public SetPayPwdPresenter(ISetPayPwdView mView) {
        super(mView);
    }

    @Override
    protected ISetPayPwdDao initDao() {
        return new SetPayPwdDaoImpl();
    }

    String mYzmMD5;
    private int retryTimes = 0;

    IGetUserBandPhone.OnCompleteListener onAutoGetUserBandPhoneCompleteListener = new IGetUserBandPhone.OnCompleteListener() {
        @Override
        public void onSuccess(String phoneNumber, boolean isBand) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                        mView.showTips(null, IView.TipType.LoadingHide);
                }
            }, 300);

        }

        @Override
        public void onFail() {
            if (isDetached() || retryTimes < mView.getBandPhoneRetryTimes()) {
                mDao.getUserBandPhone(onAutoGetUserBandPhoneCompleteListener);
                return;
            }
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.showTips("获取绑定手机号失败", IView.TipType.DialogError);
                }
            }, 100);
            retryTimes++;
        }
    };

    IGetUserBandPhone.OnCompleteListener onGetUserBandPhoneCompleteListener = new IGetUserBandPhone.OnCompleteListener() {
        @Override
        public void onSuccess(String phoneNumber, final boolean isBand) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    if (!isBand) {
                        mView.showTips("您未完成手机号绑定,请到中鸽网完成绑定后再试", IView.TipType.DialogError, mView.TAG_UnBandPhone);
                    }
                }
            }, 100);
            mDao.sendYZM(CpigeonData.getInstance().getUserBindPhone(), CallAPI.DATATYPE.YZM.RESET_PAY_PWD, onSendCompleteListener);
        }

        @Override
        public void onFail() {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.showTips("获取绑定手机号失败,请稍后再试", IView.TipType.DialogError);
                }
            }, 100);
        }
    };

    ISendVerificationCode.OnSendCompleteListener onSendCompleteListener = new ISendVerificationCode.OnSendCompleteListener() {
        @Override
        public void onSuccess(final String yzmMd5) {
            mYzmMD5 = yzmMd5;
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips("验证码已发送，请注意查收", IView.TipType.ToastShort);
                    mView.sendYzmSuccess(yzmMd5);
                }
            }, 100);
        }

        @Override
        public void onFail(int errorCode, final String msg) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(msg, IView.TipType.DialogError);
                }
            }, 100);
        }
    };

    public void autoGetUserBandPhone() {
        if (!TextUtils.isEmpty(CpigeonData.getInstance().getUserBindPhone())) return;
        mView.showTips("获取绑定手机号码中...", IView.TipType.LoadingShow);
        retryTimes = 1;
        mDao.getUserBandPhone(onAutoGetUserBandPhoneCompleteListener);
    }

    private void getUserBandPhone() {
        mView.showTips("获取绑定手机号码中...", IView.TipType.LoadingShow);
        mDao.getUserBandPhone(onGetUserBandPhoneCompleteListener);
    }

    public void sendYZM() {
        if (TextUtils.isEmpty(CpigeonData.getInstance().getUserBindPhone())) {
            getUserBandPhone();
            return;
        }
        mDao.sendYZM(CpigeonData.getInstance().getUserBindPhone(), CallAPI.DATATYPE.YZM.RESET_PAY_PWD, onSendCompleteListener);
    }

    IBaseDao.OnCompleteListener onCompleteListener = new IBaseDao.OnCompleteListener<Boolean>() {
        @Override
        public void onSuccess(Boolean data) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.showTips("设置成功", IView.TipType.DialogSuccess, mView.TAG_SetPayPwdSuccess);
                }
            }, 100);
        }

        @Override
        public void onFail(final String msg) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.showTips(msg, IView.TipType.DialogError);
                }
            }, 100);
        }
    };

    public void setPayPwd() {

        if (!EncryptionTool.MD5(mView.getInputYZM()).equals(mYzmMD5)) {
            mView.showTips("验证码错误，请确认验证码是否正确", IView.TipType.DialogError, mView.TAG_YZMError);
            return;
        }
        mView.showTips("设置中...", IView.TipType.LoadingShow);
        mDao.setUserPayPwd(mView.getInputYZM(), mView.getPayPwd(), CpigeonData.getInstance().getUserBindPhone(), onCompleteListener);
    }
}
