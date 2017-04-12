package com.cpigeon.app.modular.usercenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.utils.NetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by chenshuai on 2017/4/12.
 */

public class EditActivity extends BaseActivity {
    public static final String INTENT_KEY_OLD_VALUE = "oldvalue";//未修改的值
    public static final String INTENT_KEY_NEW_VALUE = "newvalue";//修改的值
    public static final String INTENT_KEY_EDITTEXT_HINT = "editHint";//
    public static final String INTENT_KEY_SHOW_TIPS = "showtips";//
    public static final String INTENT_KEY_TIPS_TEXT = "tipstext";//
    public static final String INTENT_KEY_NEW_TITLE = "nametitle";//修改的标题

    private boolean mIsSaved = false;
    private boolean mIsCanceled = false;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_tips)
    TextView mTips;
    @BindView(R.id.edit_value)
    EditText mEditText;
    private Intent intent = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        intent = getIntent();
        toolbar.setTitle(intent.getStringExtra(INTENT_KEY_NEW_TITLE));
        mTips.setVisibility(intent.getBooleanExtra(INTENT_KEY_SHOW_TIPS, false) ? View.VISIBLE : View.GONE);
        mEditText.setText(intent.getStringExtra(INTENT_KEY_OLD_VALUE));
        mEditText.setHint(intent.getStringExtra(INTENT_KEY_EDITTEXT_HINT));
        mEditText.setSelection(mEditText.getText().toString().length());//设置光标位置
        mTips.setText(intent.getStringExtra(INTENT_KEY_TIPS_TEXT));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    public void finish() {
        if (mIsSaved || mIsCanceled) {
            super.finish();
            return;
        }
        if (getIntent().getStringExtra(INTENT_KEY_OLD_VALUE).equals(mEditText.getText().toString().trim()) || "".equals(mEditText.getText().toString().trim())) {
            super.finish();
        } else {
            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            dialog.setCancelable(false);
            dialog.setTitleText("提示");
            dialog.setContentText("亲，还没保存呢");
            dialog.setConfirmText("保存");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    intent = new Intent();
                    intent.putExtra(INTENT_KEY_NEW_VALUE, mEditText.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                    mIsSaved = true;
                    finish();
                }
            });
            dialog.setCancelText("取消");
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    mIsCanceled = true;
                    finish();
                }
            });
            dialog.show();
        }
    }

}
