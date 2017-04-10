package com.cpigeon.app.modular.usercenter.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;

import com.cpigeon.app.modular.usercenter.view.activity.RegisterActivity;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IRegisterView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by chenshuai on 2017/4/8.
 */

public class UserRegistSetp2Fragment extends BaseFragment implements IRegisterView.IRegisterSetp2View {

    @BindView(R.id.tv_sendPhoneNumber)
    TextView tvSendPhoneNumber;
    @BindView(R.id.et_regist_yzm)
    EditText etRegistYzm;
    @BindView(R.id.btn_get_yzm)
    Button btnGetYzm;
    @BindView(R.id.btn_regist_next)
    AppCompatButton btnRegistNext;
    Unbinder unbinder;
    private View rootView;
    private String tipPhoneNumber;
    private String tipBtnGetYzm = "重新获取";
    private String yzmMd5;
    private String yzmInput;
    Timer sendTimer;
    int times = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_regist_step_2, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    @Override
    public void initViews() {
        etRegistYzm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnRegistNext.setEnabled(getInputYZM().length() > 0);
            }
        });
        tvSendPhoneNumber.setText(tipPhoneNumber);
        btnGetYzm.setText(tipBtnGetYzm);
    }

    @Override
    public String getInputYZM() {
        if (etRegistYzm != null)
            yzmInput = etRegistYzm.getText().toString();
        return yzmInput;
    }

    @Override
    public String getYzmMd5() {
        return this.yzmMd5;
    }

    @Override
    public int getSendTimes() {
        return 0;
    }

    @Override
    public void focusInputYZM() {
        etRegistYzm.requestFocus();
    }

    @Override
    public void sendYZMSuccess(String phone, String yzmMd5) {
        this.yzmMd5 = yzmMd5;
        this.tipPhoneNumber = String.format("验证码已发送到%s", phone);
        if (tvSendPhoneNumber != null)
            tvSendPhoneNumber.setText(tipPhoneNumber);
    }


    @Override
    public void sendYZMFail(String msg) {
        stopSendTimer();
        this.tipPhoneNumber = "";
        if (tvSendPhoneNumber != null)
            tvSendPhoneNumber.setText(tipPhoneNumber);
        if (btnGetYzm != null) {
            tipBtnGetYzm = "重新获取";
            btnGetYzm.setText(tipBtnGetYzm);
            btnGetYzm.setEnabled(true);
        }
    }

    @Override
    public void runSendTimer() {
        if (sendTimer != null) {
            sendTimer.cancel();
            sendTimer.purge();
        }
        times = RegisterActivity.YZM_WAIT_TIMES;
        sendTimer = new Timer();
        sendTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (btnGetYzm != null)
                                btnGetYzm.setEnabled(times <= 0);
                            if (times <= 0) {
                                tipBtnGetYzm = "重新获取";
                                stopSendTimer();
                            } else {
                                tipBtnGetYzm = times + "秒后重试";
                            }
                            if (btnGetYzm != null) btnGetYzm.setText(tipBtnGetYzm);
                        }
                    });
                times--;
            }
        }, 0, 1000);
    }

    @Override
    public void stopSendTimer() {
        if (sendTimer != null) {
            sendTimer.cancel();
            sendTimer.purge();
            sendTimer = null;
        }
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

    @OnClick({R.id.btn_get_yzm, R.id.btn_regist_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_yzm:
                ((RegisterActivity) getActivity()).getRegisterPresenter().sendYZM(false);
                break;
            case R.id.btn_regist_next:
                if (((RegisterActivity) getActivity()).getRegisterPresenter().checkYZM()) {
                    ((RegisterActivity) getActivity()).nextStep();
                }
                break;
        }
    }
}
