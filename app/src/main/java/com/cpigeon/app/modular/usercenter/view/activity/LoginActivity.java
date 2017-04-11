package com.cpigeon.app.modular.usercenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.ILoginView;
import com.cpigeon.app.modular.usercenter.presenter.LoginPresenter;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/4/5.
 */

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.civ_user_head_img)
    CircleImageView civUserHeadImg;
    @BindView(R.id.iv_icon_user)
    AppCompatImageView ivIconUser;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.activity_login_layout_01)
    RelativeLayout activityLoginLayout01;
    @BindView(R.id.iv_pass_show)
    AppCompatImageView ivPassShow;
    @BindView(R.id.iv_pass)
    AppCompatImageView ivPass;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.activity_login_layout_02)
    RelativeLayout activityLoginLayout02;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_no_account)
    TextView tvNoAccount;
    @BindView(R.id.tv_forget_pass)
    TextView tvForgetPass;
    @BindView(R.id.activity_login)
    RelativeLayout activityLogin;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        mPresenter = new LoginPresenter(this);
    }

    @Override
    public void initView() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((LoginPresenter) mPresenter).loadUserHeadImgURL();
            }
        });
        etUsername.setText(SharedPreferencesTool.Get(this, "loginname", "", SharedPreferencesTool.SP_FILE_LOGIN).toString());
        String username = getLoginName();
        etUsername.setSelection(username.length() > 0 ? username.length() : 0);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public String getLoginName() {
        return etUsername.getText().toString();
    }

    @Override
    public String getLoginPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public void focusEditTextLoginName() {
        etUsername.requestFocus();
    }

    @Override
    public void focusEditTextLoginPassword() {
        etPassword.requestFocus();
    }

    @Override
    public void loginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        SharedPreferencesTool.Save(this, "loginname", getLoginName(), SharedPreferencesTool.SP_FILE_LOGIN);
        finish();
    }

    @Override
    public void showUserHeadImg(String imgurl) {
        try {
            Picasso.with(this).load(imgurl).error(R.mipmap.head_image_default_2).into(civUserHeadImg);
        } catch (Exception e) {
            civUserHeadImg.setImageResource(R.mipmap.head_image_default_2);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R.id.iv_pass_show, R.id.btn_login, R.id.tv_no_account, R.id.tv_forget_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pass_show:
                if (view.getTag() == null || (boolean) view.getTag()) {
                    view.setTag(false);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().toString().length());//将光标移至文字末尾
                    ((AppCompatImageView) view).setImageResource(R.drawable.svg_ic_eye_enable);
                } else {
                    view.setTag(true);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().toString().length());//将光标移至文字末尾
                    ((AppCompatImageView) view).setImageResource(R.drawable.svg_ic_eye_disable);
                }
                break;
            case R.id.btn_login:
                ((LoginPresenter) mPresenter).login();
                break;
            case R.id.tv_no_account:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_forget_pass:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra(RegisterActivity.INTENT_KEY_START_TYPE, RegisterActivity.START_TYPE_FINDPASSWORD);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
