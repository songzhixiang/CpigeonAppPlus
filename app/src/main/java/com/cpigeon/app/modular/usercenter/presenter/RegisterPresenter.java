package com.cpigeon.app.modular.usercenter.presenter;

import android.text.TextUtils;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.commonstandard.model.dao.ISendVerificationCode;


import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.usercenter.view.activity.RegisterActivity;

import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IRegisterView;
import com.cpigeon.app.modular.usercenter.model.dao.IRegisterDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.RegisterDaoImpl;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.EncryptionTool;

/**
 * Created by chenshuai on 2017/4/8.
 */

public class RegisterPresenter extends BasePresenter<IRegisterView, IRegisterDao> {

    IRegisterView.IRegisterSetp1View setp1View;
    IRegisterView.IRegisterSetp2View setp2View;
    IRegisterView.IRegisterSetp3View setp3View;
    private boolean isInSetpFrist = false;

    public RegisterPresenter(IRegisterView mView) {
        super(mView);
    }

    @Override
    protected IRegisterDao initDao() {
        return new RegisterDaoImpl();
    }

    public void attachSetp1View(IRegisterView.IRegisterSetp1View setpView) {
        this.setp1View = setpView;
    }

    public void attachSetp2View(IRegisterView.IRegisterSetp2View setpView) {
        this.setp2View = setpView;
    }

    public void attachSetp3View(IRegisterView.IRegisterSetp3View setpView) {
        this.setp3View = setpView;
    }

    IRegisterDao.OnCompleteListener onCompleteListener = new IRegisterDao.OnCompleteListener<String>() {
        @Override
        public void onSuccess(String data) {
            post(new Runnable() {
                @Override
                public void run() {
                    mView.showTips("", IView.TipType.LoadingHide);
                    mView.complete();
                }
            });

        }

        @Override
        public void onFail(final String msg) {
            post(new Runnable() {
                @Override
                public void run() {
                    mView.showTips("", IView.TipType.LoadingHide);
                    mView.showTips(msg, IView.TipType.DialogError);
                }
            });
        }
    };
    ISendVerificationCode.OnSendCompleteListener onSendCompleteListener = new ISendVerificationCode.OnSendCompleteListener() {
        @Override
        public void onSuccess(final String yzmMd5) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (isInSetpFrist)
                        mView.nextStep();
                    setp2View.sendYZMSuccess(setp1View.getPhoneNumber(), yzmMd5);
                    setp2View.runSendTimer();
                }
            });

        }

        @Override
        public void onFail(final int errorCode, final String msg) {
            post(new Runnable() {
                @Override
                public void run() {
                    setp2View.sendYZMFail(msg);
                    if (isInSetpFrist) {
                        if (errorCode == 1000 || errorCode == 1003 || errorCode == 1004 || errorCode == 1005 || errorCode == 1008) {
                            mView.showTips("", IView.TipType.LoadingHide);
                            mView.showTips(msg, IView.TipType.ToastShort);
                        }
                    } else {
                        mView.showTips("", IView.TipType.LoadingHide);
                        mView.showTips(msg, IView.TipType.ToastShort);
                    }
                }
            });
        }

    };

    public boolean checkPhoneNubmer() {
        if (setp1View == null) return false;
        if (TextUtils.isEmpty(setp1View.getPhoneNumber()) || !CommonTool.Compile(setp1View.getPhoneNumber(), CommonTool.PATTERN_PHONE)) {
            mView.showTips("请输入正确的手机号码", IView.TipType.DialogError);
            setp1View.focusInputPhoneNumber();
            return false;
        }
        return true;
    }

    public boolean checkRegistProtocol() {
        if (setp1View == null) return false;
        if (!setp1View.isAgreeProtocol()) {
            mView.showTips("请仔细阅读并同意《中鸽网注册协议》", IView.TipType.DialogError);
            return false;
        }
        return true;
    }

    public boolean checkYZM() {
        if (setp2View == null) return false;
        if (TextUtils.isEmpty(setp2View.getInputYZM())) {
            mView.showTips("请输入验证码", IView.TipType.DialogError);
            setp2View.focusInputYZM();
            return false;
        }
        if (!EncryptionTool.MD5(setp2View.getInputYZM()).equals(setp2View.getYzmMd5())) {
            mView.showTips("请输入正确的验证码", IView.TipType.DialogError);
            setp2View.focusInputYZM();
            return false;
        }
        return true;
    }

    public boolean checkPassword() {
        if (setp3View == null) return false;
        if (TextUtils.isEmpty(setp3View.getPassword())) {
            mView.showTips("请输入密码", IView.TipType.DialogError);
            setp3View.focusInputPassword();
            return false;
        }
        if (setp3View.getPassword().length() < 6 || setp3View.getPassword().length() > 16) {
            mView.showTips("请确定密码位数在6-16位之间", IView.TipType.DialogError);
            setp3View.focusInputPassword();
            return false;
        }
        return true;
    }

    public boolean checkConfirmPassword() {
        if (setp3View == null) return false;
        if (TextUtils.isEmpty(setp3View.getConfirmPassword())) {
            mView.showTips("请输入确认密码", IView.TipType.DialogError);
            setp3View.focusInputConfirmPassword();
            return false;
        }
        if (!setp3View.getConfirmPassword().equals(setp3View.getPassword())) {
            mView.showTips("两次密码输入不一致", IView.TipType.DialogError);
            setp3View.focusInputConfirmPassword();
            return false;
        }
        return true;
    }

    public void registUser() {
        if (!checkPhoneNubmer()) return;
        if (!checkYZM()) return;
        if (!checkPassword()) return;
        if (!checkConfirmPassword()) return;

        mView.showTips("注册中...", IView.TipType.LoadingShow);
        mDao.registUser(setp1View.getPhoneNumber(), setp3View.getPassword(), setp2View.getInputYZM(), onCompleteListener);
    }

    public void findUserPass() {
        if (!checkPhoneNubmer()) return;
        if (!checkYZM()) return;
        if (!checkPassword()) return;
        if (!checkConfirmPassword()) return;

        mView.showTips("处理中...", IView.TipType.LoadingShow);
        mDao.findUserPassword(setp1View.getPhoneNumber(), setp3View.getPassword(), setp2View.getInputYZM(), onCompleteListener);
    }

    /**
     * 发送验证码，自动判断当前的启动模式
     */
    public void sendYZM(boolean isInSetpFrist) {
        if (setp1View == null) {
            onSendCompleteListener.onFail(0, "发送失败，请稍后再试");
            return;
        }
        this.isInSetpFrist = isInSetpFrist;

        mDao.sendYZM(setp1View.getPhoneNumber(), mView.getRunModel() == RegisterActivity.START_TYPE_REGIST ? CallAPI.DATATYPE.YZM.REGIST : CallAPI.DATATYPE.YZM.FIND_PASSWORD, onSendCompleteListener);
    }
}
