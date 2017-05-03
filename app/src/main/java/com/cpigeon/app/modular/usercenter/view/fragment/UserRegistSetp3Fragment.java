package com.cpigeon.app.modular.usercenter.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class UserRegistSetp3Fragment extends BaseFragment implements IRegisterView.IRegisterSetp3View {

    @BindView(R.id.et_regist_pass)
    EditText etRegistPass;
    @BindView(R.id.et_regist_repass)
    EditText etRegistRepass;
    @BindView(R.id.btn_regist_submit)
    AppCompatButton btnRegistSubmit;


    @Override
    protected void initView(View view) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnRegistSubmit.setEnabled(etRegistPass.getText().toString().length() > 0 && etRegistRepass.getText().toString().length() > 0);
            }
        };
        etRegistPass.addTextChangedListener(textWatcher);
        etRegistRepass.addTextChangedListener(textWatcher);
        btnRegistSubmit.setText(((RegisterActivity)getActivity()).getRunModel()==RegisterActivity.START_TYPE_FINDPASSWORD?"确认重置":"确认注册");
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_regist_step_3;
    }


    @Override
    public String getPassword() {
        return etRegistPass.getText().toString();
    }

    @Override
    public String getConfirmPassword() {
        return etRegistRepass.getText().toString();
    }

    @Override
    public void focusInputPassword() {
        etRegistPass.requestFocus();
    }

    @Override
    public void focusInputConfirmPassword() {
        etRegistRepass.requestFocus();
    }


    @OnClick(R.id.btn_regist_submit)
    public void onViewClicked() {
        if (((RegisterActivity) getActivity()).getRunModel() == RegisterActivity.START_TYPE_REGIST)
            ((RegisterActivity) getActivity()).getRegisterPresenter().registUser();
        else
            ((RegisterActivity) getActivity()).getRegisterPresenter().findUserPass();
    }
}
