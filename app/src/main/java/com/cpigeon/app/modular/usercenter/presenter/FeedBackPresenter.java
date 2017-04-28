package com.cpigeon.app.modular.usercenter.presenter;

import com.cpigeon.app.commonstandard.model.dao.IGetUserBandPhone;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.dao.IFeedBackDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.FeedBackDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IFeedBackView;
import com.cpigeon.app.utils.CommonTool;

/**
 * Created by chenshuai on 2017/4/11.
 */

public class FeedBackPresenter extends BasePresenter<IFeedBackView, IFeedBackDao> {
    private IFeedBackDao.OnCompleteListener onCompleteListener = new IFeedBackDao.OnCompleteListener() {
        @Override
        public void onSuccess() {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips("", IView.TipType.LoadingHide);
                    mView.showTips("您的反馈我们会认真查看并尽快完善.\n感谢您对中鸽网的支持", IView.TipType.DialogSuccess);
                    mView.clearFeedbackContent();
                }
            }, 800);
        }

        @Override
        public void onFail(final String msg) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips("", IView.TipType.LoadingHide);
                    mView.showTips(msg, IView.TipType.DialogError);
                }
            }, 800);
        }
    };

    public FeedBackPresenter(IFeedBackView mView) {
        super(mView);

    }

    @Override
    protected IFeedBackDao initDao() {
        return new FeedBackDaoImpl();
    }

    public void readUserPhoneNumber() {
        mDao.getUserBandPhone(new IGetUserBandPhone.OnCompleteListener() {
            @Override
            public void onSuccess(final String phoneNumber, boolean isBand) {
                if (isBand)
                    postDelayed(new CheckAttachRunnable() {
                        @Override
                        public void runAttached() {
                            mView.setFeedbackUserPhone(phoneNumber);
                        }
                    }, 100);
            }

            @Override
            public void onFail() {

            }
        });
    }

    public void feedback() {
        String content = mView.getFeedbackContent().trim();
        String phoneNmuber = mView.getFeedbackUserPhone().trim();
        if (content.length() == 0) {
            mView.showTips("请填写您遇到的问题或建议", IView.TipType.DialogError);
            mView.focusInputContent();
            return;
        }
        if (phoneNmuber.length() != 0 && !CommonTool.Compile(phoneNmuber, CommonTool.PATTERN_PHONE)) {
            mView.showTips("请填写正确的手机号码", IView.TipType.DialogError);
            mView.focusInputPhone();
            return;
        }
        mView.showTips("提交中...", IView.TipType.LoadingShow);
        mDao.feedback(content, phoneNmuber, onCompleteListener);
    }
}
