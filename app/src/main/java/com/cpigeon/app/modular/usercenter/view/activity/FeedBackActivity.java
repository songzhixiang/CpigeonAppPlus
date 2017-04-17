package com.cpigeon.app.modular.usercenter.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.presenter.FeedBackPresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IFeedBackView;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/4/10.
 */

public class FeedBackActivity extends BaseActivity<FeedBackPresenter> implements IFeedBackView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.activity_feed_back)
    LinearLayout activityFeedBack;

    private String phoneNum = "", feedbackContent;


    @Override
    public int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    public FeedBackPresenter initPresenter() {
        return new FeedBackPresenter(this);
    }

    @Override
    public void initView() {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSubmit.setEnabled(s.length() > 0);
                int length = s.length();
                tvTip.setText(length + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toolbar.setTitle("意见反馈");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etPhone.setText(phoneNum);
//        mPresenter.readUserPhoneNumber();

        CpigeonData.DataHelper.getInstance().updateUserBandPhone(new CpigeonData.DataHelper.OnDataHelperUpdateLisenter<String>() {
            @Override
            public void onUpdated(String data) {
                setFeedbackUserPhone(data);
            }

            @Override
            public void onError(int errortype, String msg) {
                if (errortype == ERR_NOT_NEED_UPDATE)
                    setFeedbackUserPhone(CpigeonData.getInstance().getUserBindPhone());
            }
        });
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public String getFeedbackContent() {
        if (etContent != null)
            feedbackContent = etContent.getText().toString();
        return feedbackContent;
    }

    @Override
    public String getFeedbackUserPhone() {
        if (etPhone != null)
            phoneNum = etPhone.getText().toString();
        return phoneNum;
    }

    @Override
    public void setFeedbackUserPhone(String phoneNum) {
        this.phoneNum = phoneNum;
        if (etPhone != null)
            etPhone.setText(phoneNum);
    }

    @Override
    public void focusInputContent() {
        if (etContent != null)
            etContent.requestFocus();
    }

    @Override
    public void focusInputPhone() {
        if (etPhone != null)
            etPhone.requestFocus();
    }

    @Override
    public void clearFeedbackContent() {
        if (etContent != null)
            etContent.setText("");
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        mPresenter.feedback();
    }
}
