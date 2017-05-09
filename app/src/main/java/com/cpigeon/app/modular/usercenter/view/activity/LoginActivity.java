package com.cpigeon.app.modular.usercenter.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.usercenter.presenter.LoginPresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.ILoginView;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.PermissionTool;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2017/4/5.
 */
@RuntimePermissions
public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {

    private static boolean isExit = false;
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

    @NeedsPermission({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE})
    void AlertWindow() {

    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE})
    void AlertWindowOnShowRationale(final PermissionRequest request) {
        showRequest(request);
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE})
    void AlertWindowOnPermissionDenied() {
        ToastUtil.showToast(MyApp.getInstance(), "权限已被拒绝，程序即将退出", Toast.LENGTH_SHORT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppManager.getAppManager().AppExit();
            }
        }, 1000);
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE})
    void AlertWindowOnNeverAskAgain() {
        SweetAlertDialog dialog = new SweetAlertDialog(this)
                .setCancelText("退出程序")
                .setTitleText("权限未开启")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        AppManager.getAppManager().AppExit();
                    }
                }).setConfirmText("去开启").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        PermissionTool.gotoAppPermissionSetting(LoginActivity.this);
                        sweetAlertDialog.dismiss();
                        AppManager.getAppManager().AppExit();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LoginActivityPermissionsDispatcher.AlertWindowWithCheck(this);
        super.onCreate(savedInstanceState);
    }

    private void showRequest(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        request.cancel();
                    }
                })
                .setMessage("我们需要一些权限，以便您更好的体验,若您拒绝，将无法使用本产品")
                .show();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void initView() {
        clearLoginInfo();
        AppManager.getAppManager().killAllToLoginActivity(LoginActivity.class);
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.loadUserHeadImgURL();
            }
        });
        etUsername.setText(SharedPreferencesTool.Get(this, "loginname", "", SharedPreferencesTool.SP_FILE_LOGIN).toString());
        String username = getLoginName();
        etUsername.setSelection(username.length() > 0 ? username.length() : 0);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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

    @OnClick({R.id.iv_pass_show, R.id.btn_login, R.id.tv_no_account, R.id.tv_forget_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pass_show:
                if (view.getTag() == null || (boolean) view.getTag()) {
                    view.setTag(false);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().toString().length());//将光标移至文字末尾
                    ((AppCompatImageView) view).setImageResource(R.drawable.ic_visibility_black);
                } else {
                    view.setTag(true);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().toString().length());//将光标移至文字末尾
                    ((AppCompatImageView) view).setImageResource(R.drawable.ic_visibility_off_black);
                }
                break;
            case R.id.btn_login:
                mPresenter.login();
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


    public void onBackPressed() {

        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Then_click_one_exit_procedure),
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            AppManager.getAppManager().AppExit();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
