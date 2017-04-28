package com.cpigeon.app.modular.usercenter.presenter;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.cpigeon.app.modular.usercenter.model.dao.IUserBalanceRechargeDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.UserBalanceRechargeDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IUserBalanceRechargeView;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * Created by chenshuai on 2017/4/15.
 */

public class UserBalanceRechargePresenter extends BasePresenter<IUserBalanceRechargeView, IUserBalanceRechargeDao> {

    public UserBalanceRechargePresenter(IUserBalanceRechargeView mView) {
        super(mView);
    }

    @Override
    protected IUserBalanceRechargeDao initDao() {
        return new UserBalanceRechargeDaoImpl();
    }

    IBaseDao.OnCompleteListener<PayReq> onCompleteListener = new IBaseDao.OnCompleteListener<PayReq>() {
        @Override
        public void onSuccess(final PayReq data) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.onWXPay(data);
                }
            }, 300);
        }

        @Override
        public void onFail(String msg) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.showTips("微信跳转失败", IView.TipType.DialogError);
                }
            }, 300);
        }
    };
    IBaseDao.OnCompleteListener<CpigeonRechargeInfo.DataBean> onCreateRechargeOrderCompleteListener = new IBaseDao.OnCompleteListener<CpigeonRechargeInfo.DataBean>() {
        @Override
        public void onSuccess(final CpigeonRechargeInfo.DataBean data) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    if (data != null) {
                        if (mView.getPayway() == mView.TYPE_PAY_WAY_WXPAY) {
                            mView.showTips("微信跳转中...", IView.TipType.LoadingShow);
                            mDao.getWXPrePayOrderForRecharge(data.getId(), onCompleteListener);
                        } else if (mView.getPayway() == mView.TYPE_PAY_WAY_ALIPAY) {
                            mView.onAliPay(data);
                        }
                    } else {
                        mView.showTips("创建充值订单失败", IView.TipType.DialogError);
                    }
                }
            }, 300);
        }

        @Override
        public void onFail(String msg) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.showTips("创建充值订单失败", IView.TipType.DialogError);
                }
            }, 300);
        }
    };

    public void recharge() {
        if (isDetached()) return;
        if (mView.getInputFee() <= 0) {
            mView.showTips("请输入充值金额", IView.TipType.DialogError);
            return;
        }

        if (mView.getPayway() != mView.TYPE_PAY_WAY_ALIPAY && mView.getPayway() != mView.TYPE_PAY_WAY_WXPAY) {
            mView.showTips("请选择支付方式", IView.TipType.DialogError);
            return;
        }
        if (!mView.isAgreePayProtocol()) {
            mView.showTips(MyApp.getInstance().getString(R.string.sentence_not_watch_pay_agreement_prompt), IView.TipType.DialogError);
            return;
        }

        mView.showTips("创建充值订单中", IView.TipType.LoadingShow);
        mDao.createRechargeOrder(mView.getInputFee(), mView.getPayway(), onCreateRechargeOrderCompleteListener);

    }
}
