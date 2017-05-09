package com.cpigeon.app.modular.usercenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.usercenter.view.fragment.UserRegistSetp2Fragment;
import com.cpigeon.app.modular.usercenter.presenter.RegisterPresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IRegisterView;
import com.cpigeon.app.modular.usercenter.view.fragment.UserRegistSetp1Fragment;
import com.cpigeon.app.modular.usercenter.view.fragment.UserRegistSetp3Fragment;
import com.cpigeon.app.utils.NetUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/4/5.
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegisterView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment)
    FrameLayout fragment;
    @BindView(R.id.activity_regist)
    LinearLayout activityRegist;

    public final static int YZM_WAIT_TIMES = 60 * 3;

    public final static int START_TYPE_REGIST = 0;
    public final static int START_TYPE_FINDPASSWORD = 1;

    public final static String INTENT_KEY_START_TYPE = "type";
    private int type = 0;
    private SweetAlertDialog dialogFinishPrompt;

    private int currSetpIndex = 0;
    private List<Fragment> mSteps;

    private UserRegistSetp1Fragment mUserRegistSetp1Fragment;
    private UserRegistSetp2Fragment mUserRegistSetp2Fragment;
    private UserRegistSetp3Fragment mUserRegistSetp3Fragment;


    public RegisterPresenter getRegisterPresenter() {
        return  this.mPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    public RegisterPresenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra(INTENT_KEY_START_TYPE, START_TYPE_REGIST);
        if (type == START_TYPE_FINDPASSWORD) {
            toolbar.setTitle(R.string.reset_password);
        } else {
            toolbar.setTitle(R.string.regist_new_user);
        }
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mUserRegistSetp1Fragment = new UserRegistSetp1Fragment();
        mUserRegistSetp2Fragment = new UserRegistSetp2Fragment();
        mUserRegistSetp3Fragment = new UserRegistSetp3Fragment();
        getRegisterPresenter().attachSetp1View(mUserRegistSetp1Fragment);
        getRegisterPresenter().attachSetp2View(mUserRegistSetp2Fragment);
        getRegisterPresenter().attachSetp3View(mUserRegistSetp3Fragment);
        mSteps = new ArrayList<>();
        mSteps.add(mUserRegistSetp1Fragment);
        mSteps.add(mUserRegistSetp2Fragment);
        mSteps.add(mUserRegistSetp3Fragment);

        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.fragment, mUserRegistSetp1Fragment);
        tran.commitAllowingStateLoss();
    }

    /**
     * 下一步
     */
    @Override
    public boolean nextStep() {
        if (currSetpIndex >= mSteps.size() - 1)
            return false;

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mSteps.get(++currSetpIndex)).commitAllowingStateLoss();
        Logger.d("currSetpIndex=" + currSetpIndex);
        return true;
    }

    @Override
    public void complete() {
        SweetAlertDialog dialogPrompt = new SweetAlertDialog(this);
        dialogPrompt.setCancelable(false);
        dialogPrompt.setTitleText(getString(R.string.prompt))
                .setContentText(type == START_TYPE_REGIST ? "注册成功" : "重置成功")
                .setConfirmText(getString(R.string.confirm))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .show();
    }

    /**
     * 上一步
     */
    private boolean prevStep() {
        if (currSetpIndex <= 0)
            return false;
        synchronized (this) {
            if (currSetpIndex <= 0)
                return false;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mSteps.get(--currSetpIndex)).commitAllowingStateLoss();
            Logger.d("currSetpIndex=" + currSetpIndex);
            return true;
        }
    }


    @Override
    public int getRunModel() {
        return type;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && currSetpIndex > 0) {

            dialogFinishPrompt = new SweetAlertDialog(this);
            dialogFinishPrompt.setCancelable(false);
            dialogFinishPrompt.setTitleText(getString(R.string.prompt))
                    .setContentText("确认返回？")
                    .setConfirmText(getString(R.string.confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    }).setCancelText("取消");
            dialogFinishPrompt.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
