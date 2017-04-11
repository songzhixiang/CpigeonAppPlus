package com.cpigeon.app.modular.usercenter.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;

/**
 * Created by chenshuai on 2017/4/11.
 */

public interface IFeedBackView extends IView {
    /**
     * 获取反馈内容
     *
     * @return
     */
    String getFeedbackContent();

    /**
     * 获取反馈联系电话
     *
     * @return
     */
    String getFeedbackUserPhone();

    /**
     * 设置反馈联系电话
     *
     * @param phoneNum
     * @return
     */
    void setFeedbackUserPhone(String phoneNum);

    /**
     * 聚焦反馈内容输入框
     */
    void focusInputContent();

    /**
     * 聚焦反馈手机号码输入框
     */
    void focusInputPhone();

    /**
     * 清除反馈内容
     */
    void clearFeedbackContent();
}
