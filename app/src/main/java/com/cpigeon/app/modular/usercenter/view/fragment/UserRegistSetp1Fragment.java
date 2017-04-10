package com.cpigeon.app.modular.usercenter.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.usercenter.view.activity.RegisterActivity;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IRegisterView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by chenshuai on 2017/4/8.
 */

public class UserRegistSetp1Fragment extends BaseFragment implements IRegisterView.IRegisterSetp1View {

    @BindView(R.id.et_regist_phone)
    EditText etRegistPhone;
    @BindView(R.id.btn_regist_next)
    AppCompatButton btnRegistNext;
    @BindView(R.id.cb_regist_user_protocol)
    AppCompatCheckBox cbRegistUserProtocol;
    @BindView(R.id.tv_regist_user_protocol)
    TextView tvRegistUserProtocol;
    @BindView(R.id.layout_regist_protocol)
    LinearLayout layoutRegistProtocol;
    Unbinder unbinder;
    private View rootView;
    private String phoneNumber;
    private boolean isAgreeprotocol = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_regist_step_1, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    @Override
    public void initViews() {
        etRegistPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnRegistNext.setEnabled(getPhoneNumber().length() > 0);
            }
        });
        if (((RegisterActivity) getActivity()).getRunModel() == RegisterActivity.START_TYPE_FINDPASSWORD)
            layoutRegistProtocol.setVisibility(View.GONE);

    }

    @Override
    public String getPhoneNumber() {
        if (etRegistPhone != null)
            phoneNumber = etRegistPhone.getText().toString();
        return phoneNumber;
    }

    @Override
    public boolean isAgreeProtocol() {
        if (cbRegistUserProtocol != null)
            isAgreeprotocol = cbRegistUserProtocol.isChecked();
        return isAgreeprotocol;
    }

    @Override
    public void focusInputPhoneNumber() {
        etRegistPhone.requestFocus();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_regist_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_regist_next:
                if (!((RegisterActivity) getActivity()).getRegisterPresenter().checkPhoneNubmer()) {
                    return;
                }
                if (((RegisterActivity) getActivity()).getRunModel() == RegisterActivity.START_TYPE_REGIST && !((RegisterActivity) getActivity()).getRegisterPresenter().checkRegistProtocol()) {
                    return;
                }
                //发送验证码界面
                ((RegisterActivity) getActivity()).getRegisterPresenter().sendYZM(true);
                break;
        }
    }
}
