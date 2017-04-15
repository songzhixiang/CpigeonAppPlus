package com.cpigeon.app.modular.usercenter.view.activity;

import android.os.CountDownTimer;
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
import com.cpigeon.app.modular.usercenter.presenter.SetPayPwdPresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.ISetPayPwdView;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class SetPayPwdActivity extends BaseActivity<SetPayPwdPresenter> implements ISetPayPwdView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_phone_prompt)
    TextView tvPhonePrompt;
    @BindView(R.id.tv_get_yzm)
    TextView tvGetYzm;
    @BindView(R.id.v_split_line)
    View vSplitLine;
    @BindView(R.id.et_yzm)
    EditText etYzm;
    @BindView(R.id.tv_pay_pwd_show)
    TextView tvPayPwdShow;
    @BindView(R.id.v_split_line_pwd)
    View vSplitLinePwd;
    @BindView(R.id.et_paypwd)
    EditText etPaypwd;
    @BindView(R.id.btn_ok)
    Button btnOk;

    String payPwd, inputYZM;
    CountDownTimer sendCountDownTimer;
    private TextWatcher mCanResetTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btnOk.setEnabled(etYzm.getText().toString().length() > 0 && etPaypwd.getText().length() >= 6);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_paypwd;
    }

    @Override
    public SetPayPwdPresenter initPresenter() {
        return new SetPayPwdPresenter(this);
    }

    @Override
    public void initView() {
        toolbar.setTitle("设置支付密码");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvPhonePrompt.setText("");
        mPresenter.autoGetUserBandPhone();
        etYzm.addTextChangedListener(mCanResetTextWatcher);
        etPaypwd.addTextChangedListener(mCanResetTextWatcher);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    /**
     * 切换密码显示
     */
    private void checkShowPwd() {

        if ("显示密码".equals(tvPayPwdShow.getText().toString())) {
            tvPayPwdShow.setText("隐藏密码");
            tvPayPwdShow.setTextColor(tvGetYzm.getTextColors().getDefaultColor());
            etPaypwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else if ("隐藏密码".equals(tvPayPwdShow.getText().toString())) {
            tvPayPwdShow.setText("显示密码");
            tvPayPwdShow.setTextColor(getResources().getColor(R.color.textColor_gray_dark));
            etPaypwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        etPaypwd.setSelection(etPaypwd.getText().toString().length());//将光标移至文字末尾
    }

    @OnClick({R.id.tv_get_yzm, R.id.btn_ok, R.id.tv_pay_pwd_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_yzm:
                mPresenter.sendYZM();
                break;
            case R.id.btn_ok:
                mPresenter.setPayPwd();
                break;
            case R.id.tv_pay_pwd_show:
                checkShowPwd();
                break;
        }
    }


    public void runSendTimer() {
        if (sendCountDownTimer != null) {
            sendCountDownTimer.cancel();
        }
        tvGetYzm.setEnabled(false);

        sendCountDownTimer = new CountDownTimer(Const.YZM_WAIT_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (tvGetYzm != null) {
                    tvGetYzm.setText(millisUntilFinished / 1000 + "秒后重新获取");
                }
            }

            @Override
            public void onFinish() {
                if (tvGetYzm != null) {
                    tvGetYzm.setText("重新获取");
                    tvGetYzm.setEnabled(true);
                }
            }
        };
        sendCountDownTimer.start();
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        if (tag == TAG_UnBandPhone && TipType.DialogError == tipType) {
            SweetAlertDialog dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            dialogPrompt.setCancelable(false);
            dialogPrompt.setTitleText("提示")
                    .setContentText(tip)
                    .setConfirmText(getString(R.string.confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    })
                    .show();
            return true;
        }
        if (tag == TAG_SetPayPwdSuccess && TipType.DialogSuccess == tipType) {
            SweetAlertDialog dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            dialogPrompt.setCancelable(false);
            dialogPrompt.setTitleText("提示")
                    .setContentText(tip)
                    .setConfirmText(getString(R.string.confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    })
                    .show();
            return true;
        }
        if (tag == TAG_YZMError && TipType.DialogError == tipType) {
            SweetAlertDialog dialogPrompt = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            dialogPrompt.setCancelable(false);
            dialogPrompt.setTitleText("提示")
                    .setContentText(tip)
                    .setConfirmText(getString(R.string.confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            etYzm.requestFocus();
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
            return true;
        }
        return super.showTips(tip, tipType, tag);
    }

    @Override
    public int getBandPhoneRetryTimes() {
        return 3;
    }

    @Override
    public String getPayPwd() {
        if (etPaypwd != null)
            payPwd = etPaypwd.getText().toString();
        return payPwd;
    }

    @Override
    public String getInputYZM() {
        if (etYzm != null)
            inputYZM = etYzm.getText().toString();
        return inputYZM;
    }

    @Override
    public void sendYzmSuccess(String yzmMd5) {
        String userPhone = CpigeonData.getInstance().getUserBindPhone();
        String phone = userPhone.length() > 0 ? String.format("%s******%s", userPhone.substring(0, 2), userPhone.substring(8, userPhone.length())) : userPhone;
        tvPhonePrompt.setText(String.format(getString(R.string.please_enter_the_phone_received_SMS_verification_code), phone));
        runSendTimer();
    }
}
