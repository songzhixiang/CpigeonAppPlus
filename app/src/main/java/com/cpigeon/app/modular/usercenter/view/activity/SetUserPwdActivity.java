package com.cpigeon.app.modular.usercenter.view.activity;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.presenter.SetUserPwdPresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.ISetUserPwdView;
import com.cpigeon.app.utils.NetUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class SetUserPwdActivity extends BaseActivity<SetUserPwdPresenter> implements ISetUserPwdView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_error_prompt)
    TextView tvErrorPrompt;
    @BindView(R.id.tv_oldpwd_show)
    TextView tvOldpwdShow;
    @BindView(R.id.v_split_line_pwd)
    View vSplitLinePwd;
    @BindView(R.id.et_oldpwd)
    EditText etOldpwd;
    @BindView(R.id.et_newpwd)
    EditText etNewpwd;
    @BindView(R.id.et_confirmnewpwd)
    EditText etConfirmnewpwd;
    @BindView(R.id.btn_ok)
    Button btnOk;

    String oldPwd, newPwd;

    private TextWatcher canSubmitWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btnOk.setEnabled(false);
            tvErrorPrompt.setVisibility(View.INVISIBLE);
            if (etOldpwd.getText().toString().length() == 0) {
                return;
            }
            if (etNewpwd.getText().toString().length() == 0) {
                return;
            } else if (etNewpwd.getText().toString().length() == 0) {
                return;
            } else if (etNewpwd.getText().toString().length() < 6) {
                tvErrorPrompt.setVisibility(View.VISIBLE);
                tvErrorPrompt.setText("新密码至少6位");
                return;
            }
            if (etConfirmnewpwd.getText().toString().length() == 0) {
                tvErrorPrompt.setText("");
                return;
            } else if (!etConfirmnewpwd.getText().toString().equals(etNewpwd.getText().toString())) {
                tvErrorPrompt.setVisibility(View.VISIBLE);
                tvErrorPrompt.setText("两次新密码不一致");
                return;
            }
            oldPwd = etOldpwd.getText().toString();
            newPwd = etNewpwd.getText().toString();
            btnOk.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_user_pwd;
    }

    @Override
    public SetUserPwdPresenter initPresenter() {
        return new SetUserPwdPresenter(this);
    }

    @Override
    public void initView() {
        toolbar.setTitle("修改密码");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etOldpwd.addTextChangedListener(canSubmitWatcher);
        etNewpwd.addTextChangedListener(canSubmitWatcher);
        etConfirmnewpwd.addTextChangedListener(canSubmitWatcher);
    }


    @OnClick({R.id.tv_oldpwd_show, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_oldpwd_show:
                if (view.getTag() == null || (boolean) view.getTag()) {
                    view.setTag(false);
                    etOldpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    tvOldpwdShow.setText("隐藏密码");
                } else {
                    view.setTag(true);
                    etOldpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    tvOldpwdShow.setText("显示密码");
                }
                etOldpwd.setSelection(etOldpwd.getText().toString().length());//将光标移至文字末尾
                break;
            case R.id.btn_ok:
                mPresenter.setUserPwd();
                break;
        }
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        if (tag == TAG_SetUserPwdSuccessAndRunLogin && tipType == TipType.DialogSuccess) {
            SweetAlertDialog dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            dialogPrompt.setCancelable(false);
            dialogPrompt.setTitleText("成功")
                    .setContentText(tip)
                    .setConfirmText(getString(R.string.confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(LoginActivity.class);
                        }
                    })
                    .show();
            return true;
        }
        return super.showTips(tip, tipType, tag);
    }

    @Override
    public String getOldPwd() {
        return oldPwd;
    }

    @Override
    public String getNewPwd() {
        return newPwd;
    }
}
